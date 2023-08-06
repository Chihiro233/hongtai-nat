package com.hongtai.nat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "proxy.config")
@Component
@Data
public class ProxyConfig {

    /**
     * 指令通道的端口
     */
    private Integer port;

    /**
     * the switch for transfer log
     */
    private Boolean transferLogEnable;



}
