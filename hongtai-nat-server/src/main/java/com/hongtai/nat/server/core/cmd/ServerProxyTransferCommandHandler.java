package com.hongtai.nat.server.core.cmd;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ProxyMessagePayload;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        ProxyMessagePayload payload = message.getPayload();
        byte[] data = payload.getData();
        accessChannel.writeAndFlush(data);
    }

    @Override
    public byte type() {
        return CommandConstant.TRANSFER;
    }
}
