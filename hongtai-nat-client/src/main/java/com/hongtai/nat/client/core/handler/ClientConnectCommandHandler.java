package com.hongtai.nat.client.core.handler;

import com.alibaba.fastjson2.JSON;
import com.hongtai.nat.client.core.channel.ProxyChannelHolder;
import com.hongtai.nat.common.core.AttrConstant;
import com.hongtai.nat.common.core.ChannelResultResolver;
import com.hongtai.nat.common.core.CommandConstant;
import com.hongtai.nat.common.core.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ClientMetaInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientConnectCommandHandler implements CommandHandler {

    @Resource(name = "clientBootstrap")
    private Bootstrap clientBootstrap;



    @Resource
    private ProxyChannelHolder proxyChannelHolder;


    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {

        log.info("receive cmd message, the command type: {}", proxyMessage.getType());
        // 对被代理服务建立链接
        String message = proxyMessage.getMessage();
        ClientMetaInfo metaInfo = JSON.parseObject(message, ClientMetaInfo.class);
        Channel cmdChannel = ctx.channel();
        clientBootstrap.connect(metaInfo.getIp(), metaInfo.getPort())
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            // 操作失败，向服务端的cmdChannel通知断开连接
                            ClientMetaInfo cmdContentDTO = new ClientMetaInfo()
                                    .setVisitorId(metaInfo.getVisitorId());
                            cmdChannel.writeAndFlush(ProxyMessage.Builder.buildCmdMessage(CommandConstant.DISCONNECT, cmdContentDTO));
                            cmdChannel.close();
                            return;
                        }
                        // set no read to agent channel,wait for proxy channel get complete
                        Channel agentChannel = future.channel();
                        agentChannel.config().setOption(ChannelOption.AUTO_READ, false);
                        // 创建proxyChannel
                        proxyChannelHolder.connect(new ChannelResultResolver() {
                            @Override
                            public void success(Channel proxyChannel) {
                                proxyChannel.attr(AttrConstant.ref_agent_channel).set(agentChannel);
                                agentChannel.attr(AttrConstant.ref_proxy_channel).set(proxyChannel);
                            }

                            @Override
                            public void error(Channel proxyChannel) {
                                ClientMetaInfo metaInfo2 = new ClientMetaInfo();
                                metaInfo2.setVisitorId(metaInfo.getVisitorId());
                                cmdChannel.writeAndFlush(ProxyMessage.Builder.buildCmdMessage(CommandConstant.DISCONNECT, metaInfo2));
                            }
                        });


                    }
                });

    }


    @Override
    public byte type() {
        return CommandConstant.CONNECT;
    }
}
