package com.hongtai.nat.server.core.cmd;

import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.exception.AuthException;
import com.hongtai.nat.common.core.handler.CommandHandler;
import com.hongtai.nat.common.core.model.ProxyMessage;
import com.hongtai.nat.server.core.model.ProxyBindHolder;
import com.hongtai.nat.server.service.LicenseService;
import com.hongtai.nat.server.service.model.LicenseModel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServerProxyAuthCommandHandler implements CommandHandler {

    @Resource
    private LicenseService licenseService;

    @Override
    public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel cmdChannel = ctx.channel();
        String licenseId = proxyMessage.getPayload().getLicenseId();
        // TODO 通过 licenseId 找到 对应的配置信息, 放到 holder 中,
        LicenseModel licenseModel = licenseService.getByLicenseKey(licenseId);
        if (!licenseModel.isValid()) {
            throw new AuthException("licenseId已禁用");
        }
        if (!licenseModel.hasPortMapping()) {
            throw new AuthException("端口映射不存在");
        }
        // add port mapping
        ProxyBindHolder.addPortMapping(licenseModel);
        // add licenseId -> cmdChannel mapping
        ProxyBindHolder.addCmdChannelMapping(licenseId, cmdChannel);

    }

    @Override
    public void handleError(ChannelHandlerContext ctx, ProxyMessage proxyMessage, Throwable t) {
        Channel cmdChannel = ctx.channel();
        String licenseId = proxyMessage.getPayload().getLicenseId();
        ProxyMessage failMessage = ProxyMessage.Builder.buildAuthFailMessage(licenseId, t.getMessage());
        cmdChannel.writeAndFlush(failMessage);
    }

    @Override
    public byte type() {
        return CommandConstant.AUTH;
    }
}
