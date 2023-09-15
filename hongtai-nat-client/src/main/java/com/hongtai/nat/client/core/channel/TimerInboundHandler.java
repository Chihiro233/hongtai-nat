package com.hongtai.nat.client.core.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TimerInboundHandler<T> extends ChannelInboundHandlerAdapter implements TimerTask {

    private final Map<Channel, Integer> channelMap = new HashMap<>();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel parent = ctx.channel().parent();
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        if (!(parent instanceof Bootstrap bootstrap)) {
            super.channelInactive(ctx);
            return;
        }
        bootstrap.connect(socketAddress)
                .addListener((f) -> {
                    if (f.isSuccess()) {
                        log.info("重连成功");
                    } else {
                        log.info("重新触发连接 ");
                        ctx.fireChannelInactive();
                    }
                });
    }

    @Override
    public void run(Timeout timeout) throws Exception {

    }
}
