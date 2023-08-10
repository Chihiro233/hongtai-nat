package com.hongtai.nat.common.core.handler;

import com.hongtai.nat.common.core.ProxyMessage;
import io.netty.channel.ChannelHandlerContext;

public interface CommandHandler {

    void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage);

    byte type();

}