package com.hongtai.nat.server.core.model;

import com.hongtai.nat.common.core.model.ClientMetaInfo;
import com.hongtai.nat.common.core.util.IdGenerateUtil;
import io.netty.channel.Channel;

import java.util.Map;
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


    private static final Map<String, Channel> tokenAccessChannelMap = new ConcurrentHashMap<>();

    public static Channel getCmdChannel(Integer port) {
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
