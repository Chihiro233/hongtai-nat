package com.hongtai.nat.server.dal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongtai.nat.common.core.constant.CommonConstant;
import com.hongtai.nat.server.dal.entity.LicensePortDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LicensePortDOMapper extends BaseMapper<LicensePortDO> {

    default List<LicensePortDO> queryLicensePortByKey(String key) {
        LambdaQueryWrapper<LicensePortDO> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(LicensePortDO::getLicenseKey, key)
                .eq(LicensePortDO::getStatus, CommonConstant.ENABLE);
        return this.selectList(queryWrapper);
    }

}
