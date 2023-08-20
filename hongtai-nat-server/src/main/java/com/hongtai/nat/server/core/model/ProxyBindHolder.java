package com.hongtai.nat.server.core.model;

import com.hongtai.nat.common.core.constant.CommonConstant;
import com.hongtai.nat.common.core.model.ClientMetaInfo;
import com.hongtai.nat.common.core.util.IdGenerateUtil;
import com.hongtai.nat.server.dal.entity.LicensePortDO;
import com.hongtai.nat.server.service.model.LicenseModel;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyBindHolder {

    /**
     * server port -> client info
     */

    private final static Map<Integer, ClientMetaInfo> portClientMap = new ConcurrentHashMap<>();

    /**
     * server port -> command channel
     */
    private final static Map<Integer, Channel> portCmdChannelMap = new ConcurrentHashMap<>();

    /**
     * accessToken -> access channel
     */
    private static final Map<String, Channel> tokenAccessChannelMap = new ConcurrentHashMap<>();

    /**
     * clientId -> command channel
     */
    private static final Map<String, Channel> clientIdToCmdChannelMap = new ConcurrentHashMap<>();

    public static Channel getCmdChannel(Integer port) {
        return portCmdChannelMap.get(port);
    }


    public static void addPortMapping(LicenseModel licenseModel) {
        if (!licenseModel.isValid()) {
            return;
        }
        Map<Integer, LicensePortDO> ports = licenseModel.getPorts();
        List<LicensePortDO> enablePortList = ports.values()
                .stream().filter(licensePort -> Objects.equals(licensePort.getStatus(), CommonConstant.ENABLE)).toList();
        for (LicensePortDO licensePortDO : enablePortList) {
            ClientMetaInfo clientMetaInfo = new ClientMetaInfo();
            clientMetaInfo.setIp(licensePortDO.getProxyHost());
            clientMetaInfo.setPort(licensePortDO.getProxyPort());
            portClientMap.put(licensePortDO.getProxyPort(), clientMetaInfo);
        }
    }

    public static void addCmdChannelMapping(String licenseId, Channel cmdChannel) {
        clientIdToCmdChannelMap.put(licenseId, cmdChannel);
    }

    public static Channel addCmdChannel(Integer port) {
        return portCmdChannelMap.get(port);
    }

    public static ClientMetaInfo getMetaInfo(Integer port) {
        return portClientMap.get(port);
    }


    public static Channel getAccessChannel(String accessToken) {
        return tokenAccessChannelMap.get(accessToken);
    }

    public static String addTokenAccessChannel(Channel accessChannel) {
        String accessToken = IdGenerateUtil.generateAccessToken();
        tokenAccessChannelMap.put(accessToken, accessChannel);
        return accessToken;
    }

    public static void addMetaInfo(Integer port, ClientMetaInfo metaInfo) {
        if (portClientMap.containsKey(port)) {
            throw new IllegalArgumentException("server port has been assigned");
        }
        portClientMap.put(port, metaInfo);
    }

}
