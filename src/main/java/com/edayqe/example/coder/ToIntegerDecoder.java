package com.edayqe.example.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        //标记读的Index
        in.markReaderIndex();

        //读取头部
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        out.add(in.readBytes(length).toString(CharsetUtil.UTF_8));
    }
}
