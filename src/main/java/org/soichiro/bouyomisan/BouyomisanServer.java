package org.soichiro.bouyomisan;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 棒読みちゃんの読み上げサーバーを模したサーバー
 */
public class BouyomisanServer {

    private final boolean SSL = System.getProperty("ssl") != null;
    private final int PORT = Integer.parseInt(
            System.getProperty("port", Config.getSingleton().serverPort));
    private final ExecutorService es = Executors.newSingleThreadExecutor();

    private final BouyomisanServerHandler bouyomisanServerHandler;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * コンストラクタ
     * @param bouyomisanServerHandler
     */
    public BouyomisanServer(BouyomisanServerHandler bouyomisanServerHandler) {
        this.bouyomisanServerHandler = bouyomisanServerHandler;
    }

    /**
     * サーバーの開始を非同期に行う
     * @throws Exception
     */
    public void start() throws Exception {
        es.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BouyomisanServer.this.runServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * サーバーをシャットダウンする
     */
    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        es.shutdown();
    }

    /**
     * サーバーの開始
     */
    public void runServer() throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }

        // Configure the server.
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(bouyomisanServerHandler);
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * テスト用のメインメソッド
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BouyomisanServer bouyomisanServer =
                new BouyomisanServer(
                        new BouyomisanServerHandler(
                                new SayCommandExecutor()
                        ));
        bouyomisanServer.start();
    }
}
