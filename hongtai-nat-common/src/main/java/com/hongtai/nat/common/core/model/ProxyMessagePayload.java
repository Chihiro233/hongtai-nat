package com.hongtai.nat.common.core.model;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class ProxyMessagePayload {

    private String ip;

    private Integer port;

    private String licenseId;

    private String accessToken;

    private String errMsg;


}
