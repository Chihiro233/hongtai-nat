package com.hongtai.nat.client.core.cmd;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientTransferCommandHandler implements CommandHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel proxyChannel = ctx.channel();
        Channel agentChannel = proxyChannel.attr(AttrConstant.ref_agent_channel).get();
        if(agentChannel  == null || !agentChannel.isActive()){
            // TODO 要断开连接吗
            proxyChannel.close();
            return;
        }
        byte[] data = proxyMessage.getData();
        agentChannel.writeAndFlush(data);

    }

    @Override
    public byte type() {
        return CommandConstant.TRANSFER;
    }
}
