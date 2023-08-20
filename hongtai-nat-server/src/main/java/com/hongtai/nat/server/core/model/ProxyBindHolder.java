package com.hongtai.nat.server.core.model;

import com.hongtai.nat.common.core.constant.AttrConstant;
import com.hongtai.nat.common.core.constant.CommonConstant;
import com.hongtai.nat.common.core.model.ClientMetaInfo;
import com.hongtai.nat.common.core.util.IdGenerateUtil;
import com.hongtai.nat.common.core.util.SpringUtil;
import com.hongtai.nat.server.dal.entity.LicensePortDO;
import com.hongtai.nat.server.service.model.LicenseModel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

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


    public static void load(LicenseModel licenseModel, Channel cmdChannel) {
        if (!licenseModel.isValid()) {
            return;
        }
        String licenseKey = licenseModel.getLicenseDO().getLicenseKey();

        addCmdChannelMapping(licenseKey, cmdChannel);

        Map<Integer, LicensePortDO> ports = licenseModel.getPorts();
        List<LicensePortDO> enablePortList = ports.values()
                .stream().filter(licensePort -> Objects.equals(licensePort.getStatus(), CommonConstant.ENABLE)).toList();
        for (LicensePortDO licensePortDO : enablePortList) {
            ClientMetaInfo clientMetaInfo = new ClientMetaInfo();
            clientMetaInfo.setIp(licensePortDO.getProxyHost());
            clientMetaInfo.setPort(licensePortDO.getProxyPort());

            addMetaInfo(licensePortDO.getServerPort(),clientMetaInfo);

            addCmdChannel(licensePortDO.getServerPort(), cmdChannel);

            bindServerPort(licensePortDO.getServerPort());
        }
    }

    private static void bindServerPort(Integer port) {
        ServerBootstrap serverBootstrap = getAccessServerBootstrap();
        serverBootstrap.bind(port);
    }

    private static void unbindServerPort(Integer port) {
        ServerBootstrap serverBootstrap = getAccessServerBootstrap();
    }

    public static void clear(Channel cmdChannel) {
        // clear license
        Attribute<String> attr = cmdChannel.attr(AttrConstant.ref_cmd_license);
        String licenseKey = attr.get();
        clientIdToCmdChannelMap.remove(licenseKey);
        // clear port mapping
    }

    public static void clearByProxyPort(Integer port) {
        // clear license
        portCmdChannelMap.remove(port);
        // clear port mapping
        portClientMap.remove(port);


    }

    private static void addCmdChannelMapping(String licenseId, Channel cmdChannel) {
        clientIdToCmdChannelMap.put(licenseId, cmdChannel);
    }

    private static Channel addCmdChannel(Integer port, Channel cmdChannel) {
        return portCmdChannelMap.put(port, cmdChannel);
    }

    private static void addMetaInfo(Integer serverPort, ClientMetaInfo metaInfo) {
        portClientMap.put(serverPort, metaInfo);
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

    private static ServerBootstrap getAccessServerBootstrap() {
        return SpringUtil.getBean("accessServerBootstrap", ServerBootstrap.class);
    }

}
