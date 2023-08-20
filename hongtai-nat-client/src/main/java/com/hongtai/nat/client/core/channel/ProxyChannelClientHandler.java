package com.hongtai.nat.client.core.channel;

import com.hongtai.nat.common.core.handler.CommandDispatcher;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyChannelClientHandler extends SimpleChannelInboundHandler<ProxyMessage> {

    private final CommandDispatcher dispatcher;

    public ProxyChannelClientHandler() {
        this.dispatcher = SpringUtil.getBean(CommandDispatcher.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage msg) throws Exception {
        dispatcher.dispatch(ctx, msg);
    }
}
