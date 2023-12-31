package com.hongtai.nat.client.core.cmd;

import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientAuthCommandHandler implements CommandHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {

    }

    @Override
    public byte type() {
        return CommandConstant.AUTH;
    }
}
