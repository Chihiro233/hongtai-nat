package com.hongtai.nat.server.core.cmd;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.server.core.model.ProxyBindHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerProxyConnectCommandHandler implements CommandHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage message) {
        Channel proxyChannel = ctx.channel();
        String accessToken = message.getPayload().getAccessToken();
        Channel accessChannel = ProxyBindHolder.getAccessChannel(accessToken);
        accessChannel.attr(AttrConstant.ref_proxy_channel).set(proxyChannel);
        proxyChannel.attr(AttrConstant.ref_access_channel).set(accessChannel);
    }

    @Override
    public byte type() {
        return CommandConstant.CONNECT;
    }
}
