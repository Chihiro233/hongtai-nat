package com.hongtai.nat.common.core.handler;

import com.hongtai.nat.common.core.model.ProxyMessage;
import io.netty.channel.ChannelHandlerContext;

public interface CommandHandler {

    void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage);

    default void handleError(ChannelHandlerContext ctx, ProxyMessage proxyMessage, Throwable t){

    }

    byte type();

}
