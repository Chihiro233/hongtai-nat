package com.hongtai.nat.client.core.cmd;

import com.hongtai.nat.client.core.channel.ProxyChannelHolder;
import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.fun.ChannelResultResolver;
import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ProxyMessagePayload;
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
        ProxyMessagePayload receiveTransferInfo = proxyMessage.getPayload();
        Channel cmdChannel = ctx.channel();
        clientBootstrap.connect(receiveTransferInfo.getIp(), receiveTransferInfo.getPort())
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            // 操作失败，向服务端的cmdChannel通知断开连接
                            cmdChannel.writeAndFlush(ProxyMessage.Builder.buildDisConnect(receiveTransferInfo.getAccessToken()));
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

                                ProxyMessage proxyConnectMsg = ProxyMessage.Builder.buildProxyConnectMessage(receiveTransferInfo.getAccessToken());
                                ChannelFuture channelFuture = proxyChannel.writeAndFlush(proxyConnectMsg);
                                if(channelFuture.isSuccess()){
                                    log.info("发送连接联立消息成功");
                                }else{
                                    log.info("发送连接消息失败");
                                }
                            }

                            @Override
                            public void error(Channel proxyChannel) {
                                cmdChannel.writeAndFlush(ProxyMessage.Builder.
                                        buildDisConnect(receiveTransferInfo.getAccessToken()));
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
