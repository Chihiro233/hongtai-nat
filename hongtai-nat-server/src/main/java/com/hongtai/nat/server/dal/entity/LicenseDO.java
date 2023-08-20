package com.hongtai.nat.server.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("license_entity")
public class LicenseDO extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String licenseKey;

    private Integer status;

    private String creator;



}
