package com.hongtai.nat.client.core.channel;

import com.hongtai.nat.client.core.config.ClientConfig;
import com.hongtai.nat.client.core.config.ClientConfigConstant;
import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.handler.CommandDispatcher;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.util.SpringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandInBoundHandler extends SimpleChannelInboundHandler<ProxyMessage>{
    private final CommandDispatcher commandDispatcher;

    public CommandInBoundHandler() {
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
        // 发送 licenseId，促使服务端绑定licenseId 和 channel
        // licenseId获取方式简单点直接放配置文件里 TODO 是否要移动到ChanneloperationComplete 监听器中
        String licenseId = ClientConfig.getStr(ClientConfigConstant.LICENSE_ID);
        Channel cmdChannel = ctx.channel();
        ProxyMessage proxyMessage = ProxyMessage.Builder.buildAuthMessage(licenseId);
        cmdChannel.writeAndFlush(proxyMessage);
        log.info("启动成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Bootstrap cmdBootstrap = SpringUtil.getBean("cmdBootstrap", Bootstrap.class);

        Channel cmdChannel = ctx.channel();

        cmdBootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    // if reconnect is still fail, fire the inactive event
                    log.info("start to fire the channel inactive");
                    cmdChannel.pipeline().fireChannelInactive();

                }else{
                    log.info("reconnect success!");
                }
            }
        });

        super.channelInactive(ctx);
    }

    // TODO 频道阅读
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel agentChannel = ctx.attr(AttrConstant.ref_agent_channel).get();
        boolean writable = ctx.channel().isWritable();
        if (agentChannel != null) {
            log.info("读写状态改变，{}", writable);
            agentChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }
        super.channelWritabilityChanged(ctx);
    }

}


