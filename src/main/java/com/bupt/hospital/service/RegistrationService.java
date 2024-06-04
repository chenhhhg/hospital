package com.bupt.hospital.service;

import com.bupt.hospital.domain.Registration;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.hospital.util.Response;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_registration】的数据库操作Service
* @createDate 2024-06-05 04:25:55
*/
public interface RegistrationService extends IService<Registration> {

    Response<Registration> saveIfNotExist(Registration registration, Registration add);

    Response<List<Registration>> getAllAfter(Integer doctorId);
}
