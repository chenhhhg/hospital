package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgx
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/get/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Admin> getAdmin(@PathVariable int id,HttpServletRequest request){
        Admin byId = adminService.getById(id);
        if (byId == null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        return Response.ok(byId);
    }

    @GetMapping("/get")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Admin> getSelf(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        Admin byId = adminService.getById(id);
        if (byId != null){
            byId.setPassword(null);
            return Response.ok(byId);
        }
        String userName = (String) session.getAttribute(SessionAttributeEnum.USER_NAME.name());
        Admin byName = adminService.getOne(new QueryWrapper<Admin>().eq("user_name", userName));
        if (byName != null){
            byName.setPassword(null);
            return Response.ok(byName);
        }
        return Response.fail(null,"获取信息失败，请检查登录状态");
    }

    @PostMapping("/check/patient/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Patient> checkPatient(@PathVariable("id")int id,HttpServletRequest request){
        Patient patient = new Patient();
        patient.setUserId(id);
        patient.setAuthorized(1);
        return patientService.updatePatient(patient);
    }

    @PostMapping("/check/doctor/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Doctor> checkDoctor(@PathVariable("id")int id,HttpServletRequest request){
        Doctor doctor = new Doctor();
        doctor.setUserId(id);
        doctor.setAuthorized(1);
        return doctorService.updateDoctor(doctor);
    }

    @PostMapping("/check/doctor")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Object> checkDoctors(@RequestBody List<Integer> ids,HttpServletRequest request){
        List<Doctor> doctors = ids.stream().map(i -> {
            Doctor doctor = new Doctor();
            doctor.setUserId(i);
            doctor.setAuthorized(1);
            return doctor;
        }).collect(Collectors.toList());
        boolean b = doctorService.updateBatchById(doctors);
        return b ? Response.ok(null) : Response.fail(null, "批量通过失败");
    }

    @PostMapping("/check/patient")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Object> checkPatients(@RequestBody List<Integer> ids,HttpServletRequest request){
        List<Patient> patients = ids.stream().map(i -> {
            Patient patient = new Patient();
            patient.setUserId(i);
            patient.setAuthorized(1);
            return patient;
        }).collect(Collectors.toList());
        boolean b = patientService.updateBatchById(patients);
        return b ? Response.ok(null) : Response.fail(null, "批量通过失败");
    }
}
