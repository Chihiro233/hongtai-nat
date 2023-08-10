package com.hongtai.nat.client.core.channel;

import io.netty.channel.Channel;
import lombok.Getter;


@Getter
public class CmdChannelHolder {

    private final Channel cmdChannel;

    public CmdChannelHolder(Channel cmdChannel){
        this.cmdChannel = cmdChannel;
    }



}
