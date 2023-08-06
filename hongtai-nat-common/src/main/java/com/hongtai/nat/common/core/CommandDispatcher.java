package com.hongtai.nat.common.core;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandDispatcher {


    public void dispatch(ChannelHandlerContext ctx,ProxyMessage proxyMessage){
        switch (proxyMessage.getType()){
            case CommandConstant.AUTH -> {
                log.info("授权触发，消息id:[{}]",proxyMessage.getMsgId());
            }
            case CommandConstant.CONNECT -> {
                log.info("连接触发，消息id:[{}]",proxyMessage.getMsgId());
            }
        }
    }


}
