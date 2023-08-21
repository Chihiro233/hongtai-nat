package com.hongtai.nat.server.core.channel;

import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandDispatcher;
import com.hongtai.nat.common.core.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyChannelServerHandler extends SimpleChannelInboundHandler<ProxyMessage> {

    private final CommandDispatcher dispatcher;

    public ProxyChannelServerHandler() {
        this.dispatcher = SpringUtil.getBean(CommandDispatcher.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage message) throws Exception {
        dispatcher.dispatch(ctx, message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("proxy连接建立");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
