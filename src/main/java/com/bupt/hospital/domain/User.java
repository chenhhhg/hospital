package com.bupt.hospital.domain;

import com.bupt.hospital.enums.RoleEnum;

public interface User {
    Integer getUserId();
    String getUserName();

    default String getUserRole(){
        Class<? extends User> aClass = this.getClass();
        if (aClass.equals(Admin.class)) {
            return RoleEnum.ADMIN.name();
        } else if (aClass.equals(Doctor.class)) {
            return RoleEnum.DOCTOR.name();
        }else if (aClass.equals(Patient.class)){
            return RoleEnum.PATIENT.name();
        }else {
            return "UnknownRole";
        }
    }

}
