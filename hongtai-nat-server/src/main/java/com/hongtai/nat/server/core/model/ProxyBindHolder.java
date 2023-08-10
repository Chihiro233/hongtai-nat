package com.hongtai.nat.server.core.model;

import com.hongtai.nat.common.core.model.ClientMetaInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyBindHolder {

    /**
     * server port -> client info
     */
    private final static Map<Integer, ClientMetaInfo> portClientMap = new ConcurrentHashMap<>();

    public static ClientMetaInfo getClientMetaInfo(Integer port) {
        return portClientMap.get(port);
    }

    public static void add(Integer port, ClientMetaInfo metaInfo) {
        if (portClientMap.containsKey(port)) {
            throw new IllegalArgumentException("server port has been assigned");
        }
        portClientMap.put(port, metaInfo);
    }

}
