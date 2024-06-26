package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.domain.Admin;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.service.AdminService;
import com.bupt.hospital.service.DoctorService;
import com.bupt.hospital.service.PatientService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.util.VoConvertor;
import com.bupt.hospital.vo.LoginVo;
import com.bupt.hospital.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private AdminService adminService;

    @Operation(summary = "病人登录")
    @PostMapping("/patient")
    public Response<UserVo> patientLogin(@RequestBody @Validated LoginVo loginVo,
                                         HttpServletRequest request){
        Patient patient = patientService.getOne(new QueryWrapper<Patient>().eq(
                "user_name", loginVo.getUserName()
        ));
        if (patient==null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        if (!loginVo.getPassword().equals(patient.getPassword())){
            return Response.fail(null, ResultEnum.WRONG_PASSWORD.getCode(), "密码错误！");
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttributeEnum.USER_ID.name(), patient.getUserId());
        session.setAttribute(SessionAttributeEnum.USER_NAME.name(),patient.getUserName());
        session.setAttribute(SessionAttributeEnum.USER_ROLE.name(), RoleEnum.PATIENT.name());
        patient.setPassword(null);
        return Response.ok(VoConvertor.convertFromPatient(patient));
    }

    @Operation(summary = "医生登录")
    @PostMapping("/doctor")
    public Response<UserVo> doctorLogin(@RequestBody @Validated LoginVo loginVo,
                                        HttpServletRequest request){
        Doctor doctor = doctorService.getOne(new QueryWrapper<Doctor>().eq(
                "user_name", loginVo.getUserName()
        ));
        if (doctor==null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        if (!loginVo.getPassword().equals(doctor.getPassword())){
            return Response.fail(null, ResultEnum.WRONG_PASSWORD.getCode(), "密码错误！");
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttributeEnum.USER_ID.name(), doctor.getUserId());
        session.setAttribute(SessionAttributeEnum.USER_NAME.name(),doctor.getUserName());
        session.setAttribute(SessionAttributeEnum.USER_ROLE.name(), RoleEnum.DOCTOR.name());
        doctor.setPassword(null);
        return Response.ok(VoConvertor.convertFromDoctor(doctor));
    }

    @Operation(summary = "管理员登录")
    @PostMapping("/admin")
    public Response<UserVo> adminLogin(@RequestBody @Validated LoginVo loginVo,
                                      HttpServletRequest request){
        Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq(
                "user_name", loginVo.getUserName()
        ));
        if (admin==null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        if (!loginVo.getPassword().equals(admin.getPassword())){
            return Response.fail(null, ResultEnum.WRONG_PASSWORD.getCode(), "密码错误！");
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttributeEnum.USER_ID.name(), admin.getUserId());
        session.setAttribute(SessionAttributeEnum.USER_NAME.name(),admin.getUserName());
        session.setAttribute(SessionAttributeEnum.USER_ROLE.name(), RoleEnum.ADMIN.name());
        admin.setPassword(null);
        return Response.ok(VoConvertor.convertFromAdmin(admin));
    }

    @Operation(summary = "登出")
    @GetMapping("/logout")
    public Response<Object> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(SessionAttributeEnum.USER_ID.name());
        session.removeAttribute(SessionAttributeEnum.USER_NAME.name());
        session.removeAttribute(SessionAttributeEnum.USER_ROLE.name());
        return Response.ok(null);
    }
}
