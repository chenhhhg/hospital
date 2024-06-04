package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Registration;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.service.RegistrationRelationService;
import com.bupt.hospital.service.RegistrationService;
import com.bupt.hospital.util.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RegistrationRelationService registrationRelationService;
    @PostMapping("/addRegistration")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<Registration> addRegistration(@RequestBody Registration registration,
                                                  HttpServletRequest request){
        HttpSession session = request.getSession();
        Registration add = new Registration();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        BeanUtils.copyProperties(registration,add);
        add.setDoctorId(doctorId);
        add.setId(null);
        add.setDoctorId(doctorId);
        add.setLockedQuantity(0);
        add.setAuthorized(0);
        return registrationService.saveIfNotExist(registration, add);
    }

    @GetMapping("/getAll")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<List<Registration>> getAll(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        return registrationService.getAllAfter(doctorId);
    }


}
