package com.edayqe.example.chapter01.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        final EchoServerHandler handler = new EchoServerHandler();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline p = channel.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(handler);
                        }
                    });
            ChannelFuture future = bootstrap.bind(8196).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
