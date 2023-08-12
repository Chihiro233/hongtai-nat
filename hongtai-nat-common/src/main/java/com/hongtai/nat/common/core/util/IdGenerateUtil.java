package com.hongtai.nat.common.core.util;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerateUtil {

    private static final AtomicInteger ati = new AtomicInteger(0);

    private static final Integer INT_LIMIT = 800000;

    public static Long generate() {
        long epochSecond = Instant.now().getEpochSecond();
        int tail = ati.incrementAndGet();
        if (tail >= INT_LIMIT) {
            ati.set(0);
        }
        String idStr = epochSecond + tail + "";
        return Long.valueOf(idStr);
    }

    public static String generateAccessToken() {
        return UUID.randomUUID().toString();
    }

}
