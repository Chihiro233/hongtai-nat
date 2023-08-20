package com.hongtai.nat.server.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("license_port")
public class LicensePortDO extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * licenseKey
     */
    private String licenseKey;

    /**
     * server port
     */
    private Integer serverPort;
    /**
     * client port
     */
    private Integer proxyPort;
    /**
     * client proxy
     */
    private String proxyHost;
    /**
     * the port mapping info status
     */
    private Integer status;

}
