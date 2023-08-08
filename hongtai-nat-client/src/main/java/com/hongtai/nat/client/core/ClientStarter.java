package com.hongtai.nat.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ClientStarter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 启动客户端TODO
        if (args.length == 0) {
            throw new IllegalArgumentException("client need to config a licenseKey");
        }
        String licenseId = args[0];
        Bootstrap bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup(2))
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addFirst(new LoggingHandler(ClientStarter.class));
                        ch.pipeline().addLast();
                    }
                });


    }
}
