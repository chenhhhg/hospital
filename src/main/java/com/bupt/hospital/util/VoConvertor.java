package com.bupt.hospital.util;

import com.bupt.hospital.domain.Admin;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.vo.UserVo;

public class VoConvertor {
    public static UserVo convertFromAdmin(Admin admin){
        if (admin == null){
            return null;
        }
        UserVo userVo = new UserVo();

        userVo.setUid(admin.getUserId());
        userVo.setUserPwd(admin.getPassword());
        userVo.setUsername(admin.getUserName());
        userVo.setUserRole(RoleEnum.ADMIN.name());
        //管理员表没有审核与否的状态
        userVo.setUserChecked(null);
        userVo.setIdCard(admin.getIdNumber());
        userVo.setRealName(admin.getName());
        userVo.setAddress(admin.getAddress());
        userVo.setPhone(admin.getContact());

        return userVo;
    }

    public static UserVo convertFromPatient(Patient patient){
        if (patient == null){
            return null;
        }
        UserVo userVo = new UserVo();

        userVo.setUid(patient.getUserId());
        userVo.setUserPwd(patient.getPassword());
        userVo.setUsername(patient.getUserName());
        userVo.setUserRole(RoleEnum.PATIENT.name());
        userVo.setUserChecked(patient.getAuthorized());
        userVo.setIdCard(patient.getIdNumber());
        userVo.setRealName(patient.getName());
        userVo.setAddress(patient.getAddress());
        userVo.setPhone(patient.getContact());
        userVo.setAge(patient.getAge());
        userVo.setGender(patient.getGender());
        userVo.setMedicalHistory(patient.getMedicalRecord());

        return userVo;
    }

    public static UserVo convertFromDoctor(Doctor doctor){
        if (doctor == null){
            return null;
        }
        UserVo userVo = new UserVo();

        // 将doctor对象的字段复制到userVo对象
        userVo.setUid(doctor.getUserId());
        userVo.setUserPwd(doctor.getPassword());
        userVo.setUsername(doctor.getUserName());
        userVo.setUserRole(RoleEnum.DOCTOR.name());
        userVo.setUserChecked(doctor.getAuthorized());
        userVo.setIdCard(doctor.getIdNumber());
        userVo.setRealName(doctor.getName());
        userVo.setAddress(doctor.getAddress());
        userVo.setPhone(doctor.getContact());
        userVo.setAge(doctor.getAge());
        userVo.setGender(doctor.getGender());
        userVo.setHospital(doctor.getHospital());
        userVo.setOffice(doctor.getDepartment());
        userVo.setTitle(doctor.getTitle());
        userVo.setSpeciality(doctor.getSpecialty());

        return userVo;
    }

}
