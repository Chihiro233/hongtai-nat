package com.hongtai.nat.common.core.codec;


import com.alibaba.fastjson2.JSON;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.model.ProxyMessagePayload;
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
        //TODO 疑惑点
        // if readable bytes length less than
        if (in.readableBytes() < NettyCoreConfig.headSize) {
            return null;
        }
        int frameLength = in.readInt();
        if (in.readableBytes() < frameLength) {
            return null;
        }
        ProxyMessage proxyMessage = new ProxyMessage();
        byte type = in.readByte();
        long msgId = in.readLong();
        int payloadLength = in.readInt();
        if (payloadLength != 0) {
            byte[] payLoadBytes = new byte[payloadLength];
            in.readBytes(payLoadBytes);
            ProxyMessagePayload payload = JSON.parseObject(payLoadBytes, ProxyMessagePayload.class);
            proxyMessage.setPayload(payload);
        }
        byte[] dataBytes = new byte[frameLength - NettyCoreConfig.msgIdLength - NettyCoreConfig.typeLength - payloadLength];
        in.readBytes(dataBytes);
        proxyMessage.setType(type);
        proxyMessage.setMsgId(msgId);
        proxyMessage.setData(dataBytes);

        in.release();
        return proxyMessage;
    }
}
