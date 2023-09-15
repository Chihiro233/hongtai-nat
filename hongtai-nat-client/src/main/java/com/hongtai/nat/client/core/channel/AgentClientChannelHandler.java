package com.hongtai.nat.client.core.channel;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AgentClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Channel agentChannel = ctx.channel();
        Channel proxyChannel = agentChannel.attr(AttrConstant.ref_proxy_channel).get();
        if (proxyChannel == null) {
            log.error("proxy channel isn't exist");
            agentChannel.close();
            return;
        }
        byte[] dataBytes = new byte[msg.readableBytes()];
        msg.readBytes(dataBytes);
        String accessToken = agentChannel.attr(AttrConstant.ref_access_token).get();
        //proxyChannel.attr(AttrConstant.)
        proxyChannel.writeAndFlush(ProxyMessage.Builder.buildTransferMessage(accessToken, dataBytes));

    }
}
