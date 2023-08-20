package com.hongtai.nat.server.service;

import com.hongtai.nat.server.service.model.LicenseModel;

public interface LicenseService {

    LicenseModel getByLicenseKey(String licenseKey);

}
