package org.soichiro.bouyomisan;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.nio.charset.Charset;

/**
 * 棒読みちゃんの読み上げサーバーを模したサーバーのハンドラ
 */
@Sharable
public class BouyomisanServerHandler extends ChannelInboundHandlerAdapter {

    final public SayCommandExecutor sayCommandExecutor;

    final private PublishSubject<String> readingTextSubject;

    /**
     * コンストラクタ
     * @param sayCommandExecutor
     */
    public BouyomisanServerHandler(SayCommandExecutor sayCommandExecutor) {
        this.sayCommandExecutor = sayCommandExecutor;
        this.readingTextSubject = PublishSubject.create();
    }

    /**
     * 読み上げられたテキストのObservableを取得する
     * @return
     */
    public Observable<String> getSayOptionObservable() {
        return this.readingTextSubject.asObservable();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        short command = Short.reverseBytes(byteBuf.getShort(0));
        if (command != 0x001) throw new IllegalArgumentException("不正なコマンドを検知しました. command:" + command);
        short speed = Short.reverseBytes(byteBuf.getShort(2));
        short volume = Short.reverseBytes(byteBuf.getShort(6));
        short voice = Short.reverseBytes(byteBuf.getShort(8));
        byte encode = byteBuf.getByte(10);
        int textLength = Integer.reverseBytes(byteBuf.getInt(11));

        String charset;
        if (encode == 0) {
            charset = "UTF-8";
        } else if (encode == 1) {
            charset = "UTF-16LE";
        } else if (encode == 2) {
            charset = "Shift_JIS";
        } else {
            throw new IllegalArgumentException("不正な文字コードを検知しました. encode:" + encode);
        }

        byte[] textBytes = new byte[textLength];
        byteBuf.getBytes(15, textBytes);
        String text = new String(textBytes, Charset.forName(charset));

        String stringVoice;
        if (voice == 0 ) {
            stringVoice = null;
        } else if ( 1 <= voice  && voice <= 2) {
            stringVoice = "aq_f1b";
        } else if ( 3 <= voice  && voice <= 4) {
            stringVoice = "aq_m3";
        } else {
            stringVoice = "aq_rm";
        }
        String stringVolume = volume < 0 ? null : Short.valueOf(volume).toString();
        String stringSpeed = speed < 0 ? null : Short.valueOf(speed).toString();

        SayOption option = new SayOption(
                text,
                stringVolume,
                stringVoice,
                stringSpeed
        );

        String readingText = sayCommandExecutor.execute(option);
        readingTextSubject.onNext(readingText);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
