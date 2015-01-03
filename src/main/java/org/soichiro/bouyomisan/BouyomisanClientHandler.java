package org.soichiro.bouyomisan;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 棒読みちゃんクライアントのハンドラ
 * (テスト用)
 */
public class BouyomisanClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public BouyomisanClientHandler() {
        firstMessage = Unpooled.buffer(BouyomisanClient.SIZE);
        for (int i = 0; i < firstMessage.capacity(); i ++) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        ByteBuf byteBuf = ctx.alloc().buffer(4);
        short command = 0x001;
        byteBuf.writeShort(Short.reverseBytes(command));
        short speed = -1;
        byteBuf.writeShort(Short.reverseBytes(speed));
        short tone = -1;
        byteBuf.writeShort(Short.reverseBytes(tone));
        short volume = -1;
        byteBuf.writeShort(Short.reverseBytes(volume));
        short voice = 0;
        byteBuf.writeShort(Short.reverseBytes(voice));
        byte code = 0;
        byteBuf.writeByte(code);

        String msg = "おはようございます";
        byte[] msgBytes = msg.getBytes(Charset.forName("UTF-8"));
        byteBuf.writeInt(Integer.reverseBytes(msgBytes.length));
        byteBuf.writeBytes(msgBytes);

        ctx.writeAndFlush(byteBuf);
        System.out.println("sended!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
        System.out.println(msg);
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
