package com.edayqe.example.chapter01.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    ByteBuf firstMessage;

    public EchoClientHandler(){
        firstMessage = Unpooled.wrappedBuffer("I am a first echo message".getBytes());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received:" + byteBuf.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        ctx.flush();
    }
}
