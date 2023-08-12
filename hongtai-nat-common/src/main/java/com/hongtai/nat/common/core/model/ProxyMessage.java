package com.hongtai.nat.common.core.model;

import com.hongtai.nat.common.core.constant.CommandConstant;
import com.hongtai.nat.common.core.util.IdGenerateUtil;
import lombok.Data;

@Data
public class ProxyMessage {

    /**
     * @see CommandConstant
     */
    private byte type;

    private Long msgId;

    private ProxyMessagePayload payload;


    public static class Builder {

        public static ProxyMessage buildCmdMessage(String accessToken, ClientMetaInfo metaInfo) {

            ProxyMessage proxyMessage = new ProxyMessage();
            ProxyMessagePayload transferInfo = new ProxyMessagePayload();
            transferInfo.setIp(metaInfo.getIp());
            transferInfo.setPort(metaInfo.getPort());
            transferInfo.setAccessToken(accessToken);
            proxyMessage.setMsgId(IdGenerateUtil.generate());
            proxyMessage.setType(CommandConstant.CONNECT);
            proxyMessage.setPayload(transferInfo);
            return proxyMessage;
        }

        public static ProxyMessage buildDisConnect(String accessToken) {

            ProxyMessage proxyMessage = new ProxyMessage();
            ProxyMessagePayload transferInfo = new ProxyMessagePayload();
            transferInfo.setAccessToken(accessToken);
            proxyMessage.setMsgId(IdGenerateUtil.generate());
            proxyMessage.setType(CommandConstant.DISCONNECT);
            proxyMessage.setPayload(transferInfo);
            return proxyMessage;
        }

        public static ProxyMessage buildProxyConnectMessage(String accessToken) {

            ProxyMessage proxyMessage = new ProxyMessage();
            ProxyMessagePayload transferInfo = new ProxyMessagePayload();
            transferInfo.setAccessToken(accessToken);
            proxyMessage.setMsgId(IdGenerateUtil.generate());
            proxyMessage.setType(CommandConstant.CONNECT);
            proxyMessage.setPayload(transferInfo);
            return proxyMessage;
        }

        public static ProxyMessage buildTransferMessage(String accessToken, byte[] transferData) {
            ProxyMessage proxyMessage = new ProxyMessage();
            ProxyMessagePayload transferInfo = new ProxyMessagePayload();
            transferInfo.setData(transferData);
            transferInfo.setAccessToken(accessToken);

            proxyMessage.setMsgId(IdGenerateUtil.generate());
            proxyMessage.setType(CommandConstant.TRANSFER);
            proxyMessage.setPayload(transferInfo);
            return proxyMessage;
        }
    }

}
