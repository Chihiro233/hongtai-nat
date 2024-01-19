package com.hongtai.nat.server.core.bootstrap;

import com.hongtai.nat.common.core.codec.ProxyMessageDecoder;
import com.hongtai.nat.common.core.codec.ProxyMessageEncoder;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import com.hongtai.nat.server.config.ProxyConfig;
import com.hongtai.nat.server.core.channel.CommandChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
 *
 * server channel starter
 *
 * */
//@Component
@Slf4j
public class CmdServerStarter implements Starter {

    @Resource
    private ProxyConfig proxyConfig;


    @Override
    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // load encoder decoder handler ...
                        log.info("haha");
                        loadChannelHandler(ch);
                    }
                });
        try {
            bootstrap.bind(proxyConfig.getPort()).sync();
            log.info("bootstrap start success, port: [{}]", proxyConfig.getPort());
        } catch (Throwable e) {
            log.error("bootstrap start fail, case: ", e);
        }

    }

    // 测试rebase，怎么和我想的不一样

    // 测试rebase，怎么和我想的不一样2

    // 测试rebase，怎么和我想的不一样3


    private void loadChannelHandler(SocketChannel ch) {
        if (proxyConfig.getTransferLogEnable() != null && proxyConfig.getTransferLogEnable()) {
            ch.pipeline().addLast(new LoggingHandler(CmdServerStarter.class));
        }
        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
        ch.pipeline().addLast(new ProxyMessageEncoder());
        ch.pipeline().addLast(new IdleStateHandler(proxyConfig.getReadIdle(), proxyConfig.getWriteIdle(), proxyConfig.getAllIdle()));
        ch.pipeline().addLast(new CommandChannelHandler());
    }

    // 提交1
}
