package com.hongtai.nat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongtai.nat.common.core.exception.AuthException;
import com.hongtai.nat.server.dal.entity.LicenseDO;
import com.hongtai.nat.server.dal.entity.LicensePortDO;
import com.hongtai.nat.server.dal.mapper.LicenseDOMapper;
import com.hongtai.nat.server.dal.mapper.LicensePortDOMapper;
import com.hongtai.nat.server.service.LicenseService;
import com.hongtai.nat.server.service.model.LicenseModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {

    @Resource
    private LicenseDOMapper licenseDOMapper;

    @Resource
    private LicensePortDOMapper licensePortDOMapper;

    public LicenseModel getByLicenseKey(String licenseKey) {
        LicenseModel licenseModel = new LicenseModel();
        LambdaQueryWrapper<LicenseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LicenseDO::getKey, licenseKey);
        LicenseDO licenseDO = licenseDOMapper.selectOne(queryWrapper);
        if (licenseDO == null) {
            throw new AuthException("licenseKey isn't exist");
        }
        List<LicensePortDO> licensePortDOS = licensePortDOMapper.queryLicensePortByKey(licenseKey);
        Map<Integer, LicensePortDO> portMapping = new HashMap<>();
        licensePortDOS.forEach(licensePortDO -> {
            portMapping.put(licensePortDO.getProxyPort(), licensePortDO);
        });

        licenseModel.setLicenseDO(licenseDO);
        licenseModel.setPorts(portMapping);
        return licenseModel;
    }

}
