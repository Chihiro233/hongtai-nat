package com.hongtai.nat.server.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LicenseDO extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String key;

    private Integer status;

    private String creator;



}
