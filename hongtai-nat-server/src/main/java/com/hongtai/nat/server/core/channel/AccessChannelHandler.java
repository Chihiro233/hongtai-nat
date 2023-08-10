package com.hongtai.nat.server.core.channel;

import com.hongtai.nat.common.core.CommandConstant;
import com.hongtai.nat.common.core.ProxyMessage;
import com.hongtai.nat.common.core.exception.OfflineException;
import com.hongtai.nat.common.core.model.ClientMetaInfo;
import com.hongtai.nat.server.core.model.ProxyBindHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class AccessChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // look for the associated proxyChannel
        Channel proxyChannel = ctx.channel().attr(AttrConstant.ref_proxy_channel).get();
        if (proxyChannel == null) {

            InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
            ClientMetaInfo clientMetaInfo = ProxyBindHolder.getClientMetaInfo(sa.getPort());
            Channel cmdChannel = clientMetaInfo.getCmdChannel();
            if (cmdChannel == null) {
                channel.close();
                throw new OfflineException("client is off-line");
            }
            cmdChannel.writeAndFlush(ProxyMessage.Builder.buildCmdMessage(CommandConstant.CONNECT, clientMetaInfo));
            channel.config().setOption(ChannelOption.AUTO_READ, false);
        }
        super.channelActive(ctx);
    }
}
