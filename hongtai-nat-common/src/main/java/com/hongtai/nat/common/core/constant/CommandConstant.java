package com.hongtai.nat.common.core.constant;

/**
 * the command type
 */
public interface CommandConstant {

    /**
     * agent 、proxy channel connect
     */
    byte CONNECT = 0x01;

    byte AUTH = 0x02;

    byte DISCONNECT = 0x03;

    byte TRANSFER = 0x04;


}
