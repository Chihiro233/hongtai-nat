package com.hongtai.nat.common.core.handler;

import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CommandDispatcher implements InitializingBean {

    private final Map<Byte, CommandHandler> handlerMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<CommandHandler> handlers = SpringUtil.getBeans(CommandHandler.class);
        for (CommandHandler handler : handlers) {
            handlerMap.put(handler.type(), handler);
        }
    }

    public void dispatch(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        CommandHandler handler = handlerMap.get(proxyMessage.getType());
        handler.handle(ctx, proxyMessage);
    }


}
