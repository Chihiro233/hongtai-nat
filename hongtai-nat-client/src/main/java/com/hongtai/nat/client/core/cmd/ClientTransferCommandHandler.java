package com.hongtai.nat.client.core.cmd;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientTransferCommandHandler implements CommandHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel proxyChannel = ctx.channel();
        Channel agentChannel = proxyChannel.attr(AttrConstant.ref_agent_channel).get();
        if (agentChannel == null || !agentChannel.isActive()) {
            // TODO 要断开连接吗
            proxyChannel.close();
            return;
        }
        byte[] data = proxyMessage.getData();
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(data);
        ChannelFuture channelFuture = agentChannel.writeAndFlush(byteBuf).syncUninterruptibly();
        if (channelFuture.isSuccess()) {
            log.info("发送给真实服务器成功");
        } else {
            log.info("发送给真实服务器失败");
        }

    }

    @Override
    public byte type() {
        return CommandConstant.TRANSFER;
    }
}
