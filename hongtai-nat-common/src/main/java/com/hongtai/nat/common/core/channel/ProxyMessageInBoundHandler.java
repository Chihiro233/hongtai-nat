package com.hongtai.nat.common.core.channel;

import com.hongtai.nat.common.core.handler.CommandDispatcher;
import com.hongtai.nat.common.core.ProxyMessage;
import com.hongtai.nat.common.core.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyMessageInBoundHandler extends SimpleChannelInboundHandler<ProxyMessage> {
    private final CommandDispatcher commandDispatcher;

    public ProxyMessageInBoundHandler() {
        super();
        commandDispatcher = SpringUtil.getBean(CommandDispatcher.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage msg) throws Exception {

        commandDispatcher.dispatch(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("启动成功");
    }

    // TODO 频道阅读
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}


