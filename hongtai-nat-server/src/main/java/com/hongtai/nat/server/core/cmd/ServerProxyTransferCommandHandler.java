package com.hongtai.nat.server.core.cmd;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerProxyTransferCommandHandler implements CommandHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage message) {
        Channel proxyChannel = ctx.channel();

        Channel accessChannel = proxyChannel.attr(AttrConstant.ref_access_channel).get();

        if (accessChannel == null) {
            proxyChannel.close();
            log.info("the access channel associated with the  proxy channel doesn't exist");
            return;
        }
        byte[] data = message.getData();
        String msg = new String(data);
        log.info("返回给调用方的消息:{}", msg);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(data);
        accessChannel.writeAndFlush(byteBuf);
    }

    @Override
    public byte type() {
        return CommandConstant.TRANSFER;
    }
}
