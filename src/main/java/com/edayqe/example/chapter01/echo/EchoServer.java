package com.edayqe.example.chapter01.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class EchoServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //主线程，处理客户端连接，不用设定线程数，bind是时候start
        EventLoopGroup workGroup = new NioEventLoopGroup(20); //从线程，处理IO、编码解码、业务逻辑，默认为cpu核心数*2
        final EchoServerHandler handler = new EchoServerHandler();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置半连接队列的大小
                    .option(ChannelOption.SO_BACKLOG, 500)
                    //长链接支持
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //显式指定使用内存池（netty默认使用内存池，减少垃圾回收）
                    //PooledByteBufAllocator 构造方法传入参数为true，则使用堆外内存
                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline p = channel.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(handler);
                            p.addLast("idleStateHandler", new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS));
                        }
                    });
            ChannelFuture future = bootstrap.bind(8196).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
