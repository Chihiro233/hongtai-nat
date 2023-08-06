package com.hongtai.nat.common.core.config;

public interface NettyCoreConfig {

    Integer maxFrameLength = Integer.MAX_VALUE;
    Integer lengthFieldOffset = 0;
    Integer lengthFieldLength = 4;

    Integer lengthAdjustment = 0;
    Integer initialBytesToStrip = 0;

    Integer typeLength = 1;

    Integer msgIdLength = 4;


}
