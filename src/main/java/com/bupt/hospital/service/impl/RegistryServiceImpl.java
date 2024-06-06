package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.service.DoctorService;
import com.bupt.hospital.service.PatientService;
import com.bupt.hospital.service.RegistryService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.vo.RegistryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistryServiceImpl implements RegistryService {
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    @Override
    public Response<Patient> registryPatient(Patient patient) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<Patient>().eq("user_name", patient.getUserName());
        if(patientService.getOne(queryWrapper) != null){
            return Response.fail(null, ResultEnum.USER_NAME_EXIST.getCode(), "用户名已存在！");
        }
        patientService.save(patient);
        patient.setPassword(null);
        return Response.ok(patient);
    }

    @Override
    public Response<Doctor> registryDoctor(Doctor doctor) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<Doctor>().eq("user_name", doctor.getUserName());
        if(doctorService.getOne(queryWrapper) != null){
            return Response.fail(null, ResultEnum.USER_NAME_EXIST.getCode(), "用户名已存在！");
        }
        doctorService.save(doctor);
        doctor.setPassword(null);
        return Response.ok(doctor);
    }
}
