package com.hongtai.nat.server.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseDO {

    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;



}
