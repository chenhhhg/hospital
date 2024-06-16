package com.bupt.hospital.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.domain.Patient;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.hospital.util.Response;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_patient】的数据库操作Service
* @createDate 2024-06-02 18:34:11
*/
public interface PatientService extends IService<Patient> {

    Response<Patient> updatePatient(Patient patient);

    Response<List<Patient>> getAllPatient();

    Response<List<Patient>> getUnauthorized();


    List<Patient> getBatch(QueryWrapper<Patient> condition);
}
