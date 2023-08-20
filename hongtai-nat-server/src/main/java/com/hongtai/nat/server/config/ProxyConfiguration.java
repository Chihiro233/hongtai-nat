package com.hongtai.nat.server.config;

import com.hongtai.nat.common.core.codec.ProxyMessageDecoder;
import com.hongtai.nat.common.core.codec.ProxyMessageEncoder;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.common.core.util.SpringUtil;
import com.hongtai.nat.server.core.channel.AccessChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfiguration {


    @Bean("accessServerBootstrap")
    public ServerBootstrap accessServerBootstrap() {
        return new ServerBootstrap()
                .group(new NioEventLoopGroup(4), new NioEventLoopGroup(20))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(AccessChannelHandler.class));
                        ch.pipeline().addLast(new AccessChannelHandler());
                    }
                });
    }

    @Bean("cmdServerBootstrap")
    public ServerBootstrap cmdServerBootstrap() {
        return new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        loadHandler(ch);
                    }
                });
    }

    private void loadHandler(SocketChannel ch) {
        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
        ch.pipeline().addLast(new ProxyMessageEncoder());
        ProxyConfig proxyConfig = SpringUtil.getBean(ProxyConfig.class);
        ch.pipeline().addLast(new IdleStateHandler(proxyConfig.getReadIdle(),
                proxyConfig.getWriteIdle(),
                proxyConfig.getAllIdle()));
    }

}
