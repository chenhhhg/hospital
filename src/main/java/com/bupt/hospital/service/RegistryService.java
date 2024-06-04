package com.bupt.hospital.service;

import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.vo.RegistryVo;

public interface RegistryService {
    Response<RegistryVo> registryPatient(Patient patient);

    Response<RegistryVo> registryDoctor(Doctor doctor);
}
