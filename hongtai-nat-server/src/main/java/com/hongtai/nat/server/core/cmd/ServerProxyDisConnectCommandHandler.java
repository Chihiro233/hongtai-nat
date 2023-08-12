package com.hongtai.nat.server.core.cmd;

import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.server.core.model.ProxyBindHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServerProxyDisConnectCommandHandler implements CommandHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        log.info("receive the disconnect message, message info: {}", proxyMessage.getPayload());
        // 断开 accessToken,
        String accessToken = proxyMessage.getPayload().getAccessToken();
        Channel accessChannel = ProxyBindHolder.getAccessChannel(accessToken);
        if (accessChannel != null && accessChannel.isActive()) {
            accessChannel.close();
            log.info("access channel is closed success, the accessToken is: {}", accessToken);
        } else {
            log.info("access channel has inactive, the accessToken is: {}", accessToken);
        }

    }

    @Override
    public byte type() {
        return CommandConstant.DISCONNECT;
    }
}
