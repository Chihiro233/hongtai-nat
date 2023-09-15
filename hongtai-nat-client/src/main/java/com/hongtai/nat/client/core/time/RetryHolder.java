package com.hongtai.nat.client.core.time;

import java.util.HashMap;
import java.util.Map;

public class RetryHolder {

    private int maxRetry;

    private int maxTime;


    private static final Map<String, Integer> delayMap = new HashMap<>();


    public RetryHolder(Integer retryMax) {
        this.maxRetry = retryMax;
        this.maxTime = 1 << retryMax;
    }

    public Integer getRetryDelay(String host, Integer port) {
        // 0 means no retry
        if (maxRetry == 0) {
            return -1;
        }
        String address = host + "_" + port;
        Integer delay = delayMap.get(address);
        if (delay == null) {
            final int defaultDelay = 1;
            // put next delay interval
            delayMap.put(address, defaultDelay << 1);
            return defaultDelay;
        }
        if (delay > (maxTime)) {
            delayMap.remove(address);
            return -1;
        }
        delayMap.put(address, delay << 1);
        return delay;
    }


}
