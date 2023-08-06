package com.hongtai.nat.common.core.handler;

import com.hongtai.nat.common.core.CommandDispatcher;
import com.hongtai.nat.common.core.ProxyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProxyMessageInBoundHandler extends SimpleChannelInboundHandler<ProxyMessage> {
    @Resource
    private CommandDispatcher commandDispatcher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage msg) throws Exception {

        commandDispatcher.dispatch(ctx,msg);
    }


    // TODO 频道阅读
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}


