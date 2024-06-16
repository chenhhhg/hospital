package com.bupt.hospital.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.domain.Doctor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.hospital.util.Response;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_doctor】的数据库操作Service
* @createDate 2024-06-02 18:34:11
*/
public interface DoctorService extends IService<Doctor> {

    Response<Doctor> updateDoctor(Doctor doctor);

    Response<List<Doctor>> getAllDoctor();

    Response<List<Doctor>> getUnauthorized();

    List<Doctor> getBatch(QueryWrapper<Doctor> condition);
}
