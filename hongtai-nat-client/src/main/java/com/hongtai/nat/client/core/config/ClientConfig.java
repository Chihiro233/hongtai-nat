package com.hongtai.nat.client.core.config;


import com.hongtai.nat.client.core.channel.*;
import com.hongtai.nat.client.core.exception.ConnectFailException;
import com.hongtai.nat.common.core.codec.ProxyMessageDecoder;
import com.hongtai.nat.common.core.codec.ProxyMessageEncoder;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
@Configuration
public class ClientConfig {

    private static final Properties properties;


    static {
        properties = ClientConfigInitializer.initializeConfig();
    }

    public static String getStr(String key) {
        return properties.getProperty(key);
    }

    public static Integer getInt(String key) {
        return Integer.valueOf(properties.getProperty(key));
    }


    @Bean("clientBootstrap")
    public Bootstrap clientBootstrap() {
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap
                .group(new NioEventLoopGroup(2))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addFirst(new LoggingHandler(AgentClientChannelHandler.class));
                        ch.pipeline().addLast(new AgentClientChannelHandler());
                    }
                })
                .channel(NioSocketChannel.class);

        return clientBootstrap;
    }

    @Bean("proxyBootstrap")
    public Bootstrap proxyBootstrap() {
        Bootstrap proxyBootstrap = new Bootstrap();
        proxyBootstrap.group(new NioEventLoopGroup(2))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addFirst(new LoggingHandler(ProxyChannelClientHandler.class));
                        ch.pipeline().addLast(new ProxyMessageEncoder());
                        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
                        ch.pipeline().addLast(new ProxyChannelClientHandler());
                    }
                })
                .remoteAddress(ClientConfig.getStr(ClientConfigConstant.SERVER_HOST),
                        NettyCoreConfig.PROXY_BIND_PORT);
        return proxyBootstrap;
    }

    @Bean("cmdBootstrap")
    public Bootstrap cmdBootstrap(ClientConfig clientConfig) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap.group(new NioEventLoopGroup(1))
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            loadHandler(ch, bootstrap);
                        }
                    })
                    .remoteAddress(ClientConfig.getStr(ClientConfigConstant.SERVER_HOST), ClientConfig.getInt(ClientConfigConstant.SERVER_PORT))
                    .connect().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                throw new ConnectFailException("connect server fail");
                            }
                            //log.info("connect server success,server host:[{}],server port: [{}]", ClientConfig.getStr(ClientConfigConstant.SERVER_HOST), ClientConfig.getInt(ClientConfigConstant.SERVER_PORT));
                            log.info("operate fail! case: ", future.cause());
                        }
                    }).sync();
            if (!channelFuture.isSuccess()) {
                throw new ConnectFailException("connect server fail");
            }
            return bootstrap;
        } catch (Exception e) {
            log.error("connect server fail, case: ", e);
            throw new ConnectFailException("connect server fail");
        }
    }

    private void loadHandler(SocketChannel ch, Bootstrap bootstrap) {

        ch.pipeline().addFirst(new LoggingHandler(CmdChannelHolder.class));
        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
        ch.pipeline().addLast(new ProxyMessageEncoder());
        ch.pipeline().addLast(new ReconnectChannelHandler(bootstrap, 3));
        ch.pipeline().addLast(new CommandInBoundHandler());
        ch.pipeline().addLast(new IdleStateHandler(ClientConfig.getInt(ClientConfigConstant.IDLE_READ),
                ClientConfig.getInt(ClientConfigConstant.IDLE_WRITE),
                ClientConfig.getInt(ClientConfigConstant.IDLE_ALL)));
    }


}
