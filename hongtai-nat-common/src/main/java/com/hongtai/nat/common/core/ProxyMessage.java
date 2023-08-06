package com.hongtai.nat.common.core;

import lombok.Data;

@Data
public class ProxyMessage {

    /**
     * @see com.hongtai.nat.common.core.CommandConstant
     */
    private byte type;

    private Long msgId;

    private String message;

}
