package com.bupt.hospital.service;

import com.bupt.hospital.domain.RegistrationRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_registration_relation】的数据库操作Service
* @createDate 2024-06-05 04:25:55
*/
public interface RegistrationRelationService extends IService<RegistrationRelation> {

    List<Integer> getUsedRegistration(Integer doctorId);

    List<RegistrationRelation> getSelfRegistration(Integer patientId);

    List<RegistrationRelation> getRelationBySource(Integer id);
}
