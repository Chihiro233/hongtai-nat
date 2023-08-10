package com.hongtai.nat.common.core.model;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class ClientMetaInfo {

    private String ip;

    private Integer port;

    private String visitorId;

    private Channel cmdChannel;

    private Channel proxyChannel;

}
