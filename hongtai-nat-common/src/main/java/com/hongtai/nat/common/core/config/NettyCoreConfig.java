package com.hongtai.nat.common.core.config;

public interface NettyCoreConfig {

    Integer maxFrameLength = Integer.MAX_VALUE;
    Integer lengthFieldOffset = 0;
    Integer lengthFieldLength = 4;

    Integer lengthAdjustment = 0;
    Integer initialBytesToStrip = 0;

    Integer headSize = 4;

    Integer typeLength = 1;

    Integer payLoadLength = 4;

    Integer msgIdLength = 8;


}
