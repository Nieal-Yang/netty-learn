package com.edayqe.example.chapter01.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void ChannelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Msg from client is:" + msg);
    }



}
