package com.hongtai.nat.common.core.codec;


import com.hongtai.nat.common.core.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

public class ProxyMessageDecoder extends LengthFieldBasedFrameDecoder {


    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                               int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }


    @Override
    protected ProxyMessage decode(ChannelHandlerContext ctx, ByteBuf inEx) throws Exception {
        // 长度
        ByteBuf in = (ByteBuf) super.decode(ctx, inEx);
        if (in == null) {
            return null;
        }
        // TODO 疑惑点
        // if readable bytes length less than
        // if (in.readableBytes() < Constant.HEAD_SIZE) {
        //     return null;
        // }
        int frameLength = in.readInt();
        if (in.readableBytes() < frameLength) {
            return null;
        }
        ProxyMessage proxyMessage = new ProxyMessage();
        byte type = in.readByte();
        long msgId = in.readLong();
        byte[] msgBytes = new byte[frameLength];
        in.readBytes(msgBytes);
        String message = new String(msgBytes, StandardCharsets.UTF_8);

        proxyMessage.setType(type);
        proxyMessage.setMsgId(msgId);
        proxyMessage.setMessage(message);

        in.release();
        return proxyMessage;
    }
}
