package com.hongtai.nat.client.core;

import com.hongtai.nat.client.core.config.ClientConfig;
import com.hongtai.nat.client.core.config.ClientConfigConstant;
import com.hongtai.nat.common.core.CommandConstant;
import com.hongtai.nat.common.core.ProxyMessage;
import com.hongtai.nat.common.core.codec.ProxyMessageDecoder;
import com.hongtai.nat.common.core.codec.ProxyMessageEncoder;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientStarter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 启动客户端TODO
        // if (args.length == 0) {
        //     throw new IllegalArgumentException("client need to config a licenseKey");
        // }
        // String licenseId = args[0];
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture channelFuture = bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup(2))
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        loadHandler(ch);
                    }
                })
                .connect((String) ClientConfig.getStr(ClientConfigConstant.SERVER_HOST),
                        (Integer) ClientConfig.getInt(ClientConfigConstant.SERVER_PORT)).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            log.info("通道启动完成");
                            ProxyMessage proxyMessage = new ProxyMessage();
                            proxyMessage.setType(CommandConstant.CONNECT);
                            proxyMessage.setMessage("卧槽我就是想测试一下");
                            proxyMessage.setMsgId(10001L);
                            future.channel().writeAndFlush(proxyMessage);
                        } else {
                            log.error("通道启动失败,原因 ", future.cause());
                        }
                    }
                }).sync();

    }

    private void loadHandler(SocketChannel ch) {
        ch.pipeline().addFirst(new LoggingHandler(ClientStarter.class));
        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
        ch.pipeline().addLast(new ProxyMessageEncoder());
        ch.pipeline().addLast(new IdleStateHandler(ClientConfig.getInt(ClientConfigConstant.IDLE_READ),
                ClientConfig.getInt(ClientConfigConstant.IDLE_WRITE),
                ClientConfig.getInt(ClientConfigConstant.IDLE_ALL)));
    }
}
