package com.hongtai.nat.server.service.model;

import com.hongtai.nat.common.core.constant.CommonConstant;
import com.hongtai.nat.server.dal.entity.LicenseDO;
import com.hongtai.nat.server.dal.entity.LicensePortDO;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class LicenseModel {


    private LicenseDO licenseDO;

    private Map<Integer, LicensePortDO> ports;

    public boolean isValid() {
        if (licenseDO == null || Objects.equals(licenseDO.getStatus(), CommonConstant.DISABLE)) {
            return false;
        }
        return true;
    }

    public boolean hasPortMapping() {
        if (!ports.isEmpty() && ports.values().stream()
                .anyMatch(licensePortDO -> Objects.equals(licensePortDO.getStatus(), CommonConstant.ENABLE))) {
            return true;
        }
        return false;
    }

}
