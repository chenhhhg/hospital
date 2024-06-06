package com.bupt.hospital.controller;

import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.service.RegistryService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.vo.RegistryVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/registry")
public class RegistryController {
    @Autowired
    private RegistryService registryService;

    @Operation(summary = "使用userRole去选择性注册")
    @PostMapping("/registry")
    public Response<RegistryVo> registry(@RequestBody @Validated RegistryVo registryUser){
        if(registryUser.getUserRole().equalsIgnoreCase(RoleEnum.ADMIN.name())){
            return Response.fail(null, ResultEnum.NOT_ALLOW_ADMIN.getCode(), "无法创建管理员角色，请联系所在单位。");
        }
        if(registryUser.getUserRole().equalsIgnoreCase(RoleEnum.PATIENT.name())){
            Patient patient = new Patient();
            patient.setUserName(registryUser.getUserName());
            patient.setPassword(registryUser.getUserPwd());
            //some default value
            patient.setAddress("");
            patient.setAge(0);
            patient.setGender("F");
            patient.setContact("");
            patient.setName("");
            patient.setIdNumber(UUID.randomUUID().toString().substring(0,18));
            Patient data = registryService.registryPatient(patient).getData();
            if (data == null){
                return Response.fail(null, ResultEnum.USER_NAME_EXIST.getCode(), "用户名已存在");
            }
            registryUser.setUid(data.getUserId());
            return Response.ok(registryUser);
        }else if(registryUser.getUserRole().equalsIgnoreCase(RoleEnum.DOCTOR.name())){
            Doctor doctor = new Doctor();
            doctor.setUserName(registryUser.getUserName());
            doctor.setPassword(registryUser.getUserPwd());
            //some default value
            doctor.setAge(0);
            doctor.setContact("");
            doctor.setGender("F");
            doctor.setDepartment("");
            doctor.setIdNumber(UUID.randomUUID().toString().substring(0,18));
            doctor.setName("");
            doctor.setHospital("");
            doctor.setSpecialty("");
            doctor.setAddress("");
            Doctor data = registryService.registryDoctor(doctor).getData();
            doctor.setTitle("");if (data == null){
                return Response.fail(null, ResultEnum.USER_NAME_EXIST.getCode(), "用户名已存在");
            }
            registryUser.setUid(data.getUserId());
            return Response.ok(registryUser);
        }else {
            return Response.fail(null, ResultEnum.ROLE_ERROR.getCode(), "角色信息错误！");
        }
    }

    @Operation(summary = "使用url去选择性注册病人")
    @PostMapping("/patient")
    public Response<Patient> registryPatient(@RequestBody @Validated Patient patient){
        //id is set in db
        patient.setUserId(null);
        return registryService.registryPatient(patient);
    }

    @Operation(summary = "使用url去选择性注册医生")
    @PostMapping("/doctor")
    public Response<Doctor> registryDoctor(@RequestBody @Validated Doctor doctor){
        doctor.setUserId(null);
        return registryService.registryDoctor(doctor);
    }
}
