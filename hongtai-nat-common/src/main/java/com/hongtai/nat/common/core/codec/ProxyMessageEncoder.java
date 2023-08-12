package com.hongtai.nat.common.core.codec;

import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class ProxyMessageEncoder extends MessageToByteEncoder<ProxyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProxyMessage msg, ByteBuf out) throws Exception {
        int messageLength = NettyCoreConfig.typeLength + NettyCoreConfig.msgIdLength;
        byte[] msgBytes = null;
        if (msg.getMessage() != null) {
            msgBytes = msg.getMessage().getBytes(StandardCharsets.UTF_8);
            messageLength += msgBytes.length;

        }
        byte type = msg.getType();
        out.writeInt(messageLength);
        out.writeByte(type);
        out.writeLong(msg.getMsgId());

        if (msgBytes != null) {
            out.writeBytes(msgBytes);
        }

    }
}
