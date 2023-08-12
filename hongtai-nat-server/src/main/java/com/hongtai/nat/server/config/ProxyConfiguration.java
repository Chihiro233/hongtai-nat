package com.hongtai.nat.server.config;

import com.hongtai.nat.server.core.channel.AccessChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfiguration {


    @Bean("accessServerBootstrap")
    public ServerBootstrap serverBootstrap() {
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

}
