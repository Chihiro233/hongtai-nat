package com.hongtai.nat.client.core.channel;

import com.hongtai.nat.client.core.time.RetryHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@ChannelHandler.Sharable
public class ReconnectChannelHandler extends ChannelInboundHandlerAdapter {


    private Bootstrap bootstrap;

    private RetryHolder retryHolder;


    public ReconnectChannelHandler(Bootstrap bootstrap,int maxRetry) {
        this.bootstrap = bootstrap;
        retryHolder = new RetryHolder(maxRetry);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = socketAddress.getHostName();
        int port = socketAddress.getPort();
        long delay;
        if ((delay = retryHolder.getRetryDelay(host, port)) != -1) {
            Channel channel = ctx.channel();
            channel.eventLoop().schedule(() -> {
                bootstrap.connect().addListener((future) -> {
                    if (future.isSuccess()) {
                        log.info("重连成功，host:[{}],port:[{}]", host, port);
                    } else {
                        log.info("重连失败,host:[{}],port:[{}]", host, port);
                        ChannelFuture f = (ChannelFuture) future;
                        f.channel().pipeline().fireChannelInactive();
                    }
                });
            }, delay, TimeUnit.SECONDS);
        }
        super.channelInactive(ctx);
    }


}
