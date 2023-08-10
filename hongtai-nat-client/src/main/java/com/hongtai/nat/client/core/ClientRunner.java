package com.hongtai.nat.client.core;

import com.hongtai.nat.client.core.config.ClientConfig;
import com.hongtai.nat.client.core.config.ClientConfigConstant;
import com.hongtai.nat.common.core.codec.ProxyMessageDecoder;
import com.hongtai.nat.common.core.codec.ProxyMessageEncoder;
import com.hongtai.nat.common.core.config.NettyCoreConfig;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 启动客户端TODO


    }

    private void loadHandler(SocketChannel ch) {
        ch.pipeline().addFirst(new LoggingHandler(ClientRunner.class));
        ch.pipeline().addLast(new ProxyMessageDecoder(NettyCoreConfig.maxFrameLength,
                NettyCoreConfig.lengthFieldOffset, NettyCoreConfig.lengthFieldLength,
                NettyCoreConfig.lengthAdjustment, NettyCoreConfig.initialBytesToStrip));
        ch.pipeline().addLast(new ProxyMessageEncoder());
        ch.pipeline().addLast(new IdleStateHandler(ClientConfig.getInt(ClientConfigConstant.IDLE_READ),
                ClientConfig.getInt(ClientConfigConstant.IDLE_WRITE),
                ClientConfig.getInt(ClientConfigConstant.IDLE_ALL)));
    }
}
