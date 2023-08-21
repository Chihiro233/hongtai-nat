package com.hongtai.nat.client.core.channel;

import com.hongtai.nat.client.core.config.ClientConfig;
import com.hongtai.nat.client.core.config.ClientConfigConstant;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.common.core.fun.ChannelResultResolver;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Slf4j
public class ProxyChannelHolder {

    @Resource(name = "proxyBootstrap")
    private Bootstrap bootstrap;

    private final ConcurrentLinkedQueue<Channel> proxyChannelPool = new ConcurrentLinkedQueue<>();

    public void connect(ChannelResultResolver resultResolver) {

        Channel proxyChannel = proxyChannelPool.poll();
        if (proxyChannel == null) {
            bootstrap.connect(ClientConfig.getStr(ClientConfigConstant.SERVER_HOST), NettyCoreConfig.PROXY_BIND_PORT).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        resultResolver.error(future.channel());
                        return;
                    }
                    resultResolver.success(future.channel());
                }
            });
        } else {
            resultResolver.success(proxyChannel);
        }

    }

}
