package com.hongtai.nat.server.config;

import com.hongtai.nat.common.core.codec.ProxyMessageDecoder;
import com.hongtai.nat.common.core.codec.ProxyMessageEncoder;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.common.core.util.SpringUtil;
import com.hongtai.nat.server.core.channel.AccessChannelHandler;
import com.hongtai.nat.server.core.channel.CommandChannelHandler;
import com.hongtai.nat.server.core.channel.ProxyChannelServerHandler;
import com.hongtai.nat.server.core.enums.ChannelEnum;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
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
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        loadHandler(ch, ChannelEnum.CMD);
                    }
                });
        ProxyConfig proxyConfig = SpringUtil.getBean(ProxyConfig.class);
        bootstrap.bind(proxyConfig.getPort()).syncUninterruptibly();
        return bootstrap;
    }

    @Bean("proxyServerBootstrap")
    public ServerBootstrap proxyServerBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(2), new NioEventLoopGroup(4))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        loadHandler(ch, ChannelEnum.PROXY);
                    }
                });
        bootstrap.bind(NettyCoreConfig.PROXY_BIND_PORT).syncUninterruptibly();
        return bootstrap;
    }

    private void loadHandler(SocketChannel ch, ChannelEnum channelEnum) {
        log.info("加载handler,类型参数:{}", channelEnum);
        ch.pipeline().addLast(new ProxyMessageEncoder());
        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
        ProxyConfig proxyConfig = SpringUtil.getBean(ProxyConfig.class);
        switch (channelEnum) {
            case CMD -> {
                ch.pipeline().addFirst(new LoggingHandler(CommandChannelHandler.class));
                ch.pipeline().addLast(new CommandChannelHandler());
            }
            case PROXY -> {
                ch.pipeline().addFirst(new LoggingHandler(ProxyChannelServerHandler.class));
                ch.pipeline().addLast(new ProxyChannelServerHandler());
            }
        }
        ch.pipeline().addLast(new IdleStateHandler(proxyConfig.getReadIdle(),
                proxyConfig.getWriteIdle(),
                proxyConfig.getAllIdle()));
    }

}
