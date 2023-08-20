package com.hongtai.nat.server.core.channel;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.exception.OfflineException;
import com.hongtai.nat.common.core.model.ClientMetaInfo;
import com.hongtai.nat.common.core.util.IdGenerateUtil;
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
        Channel accessChannel = ctx.channel();
        Channel proxyChannel = accessChannel.attr(AttrConstant.ref_proxy_channel).get();
        if (proxyChannel == null) {
            accessChannel.close();
            return;
        }
        // 向 proxyChannel传输原始数据 ？
        byte[] proxyData = new byte[msg.readableBytes()];
        // 生成 accessToken 的时机可能要斟酌一下 TODO
        String accessToken = IdGenerateUtil.generateAccessToken();
        proxyChannel.writeAndFlush(ProxyMessage.Builder.buildTransferMessage(accessToken, proxyData));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel accessChannel = ctx.channel();
        // look for the associated proxyChannel
        Channel proxyChannel = ctx.channel().attr(AttrConstant.ref_proxy_channel).get();
        if (proxyChannel == null) {

            InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
            Channel cmdChannel = ProxyBindHolder.getCmdChannel(sa.getPort());
            if (cmdChannel == null || !cmdChannel.isActive()) {
                accessChannel.close();
                ProxyBindHolder.clearByProxyPort(sa.getPort());
                throw new OfflineException("client is off-line");
            }
            ClientMetaInfo metaInfo = ProxyBindHolder.getMetaInfo(sa.getPort());

            String accessToken = ProxyBindHolder.addTokenAccessChannel(accessChannel);

            cmdChannel.writeAndFlush(ProxyMessage.Builder.buildCmdMessage(accessToken, metaInfo));
            accessChannel.config().setOption(ChannelOption.AUTO_READ, false);
        }
        super.channelActive(ctx);
    }
}
