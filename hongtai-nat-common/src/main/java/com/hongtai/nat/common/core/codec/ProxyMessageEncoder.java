package com.hongtai.nat.common.core.codec;

import com.alibaba.fastjson2.JSON;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProxyMessageEncoder extends MessageToByteEncoder<ProxyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProxyMessage msg, ByteBuf out) throws Exception {
        int messageLength = NettyCoreConfig.typeLength + NettyCoreConfig.msgIdLength + NettyCoreConfig.payLoadLength;
        byte[] dataBytes = null;
        byte[] payLoadBytes = null;
        if (msg.getPayload() != null) {
            payLoadBytes = JSON.toJSONBytes(msg.getPayload());
            messageLength += payLoadBytes.length;
        }
        if (msg.getData() != null) {
            dataBytes = msg.getData();
            messageLength += dataBytes.length;
        }

        byte type = msg.getType();
        out.writeInt(messageLength);
        out.writeByte(type);
        out.writeLong(msg.getMsgId());

        if (payLoadBytes != null) {
            out.writeInt(payLoadBytes.length);
            out.writeBytes(payLoadBytes);
        } else {
            out.writeInt(0);
        }
        if (dataBytes != null) {
            out.writeBytes(dataBytes);
        }

    }
}
