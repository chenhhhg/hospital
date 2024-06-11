package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.domain.Admin;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.domain.Registration;
import com.bupt.hospital.service.AdminService;
import com.bupt.hospital.service.DoctorService;
import com.bupt.hospital.service.PatientService;
import com.bupt.hospital.service.RegistrationService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.vo.RegistrationVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private RegistrationService registrationService;

    @Operation(summary = "获得对应id的管理员", description = "所有管理员有权访问")
    @GetMapping("/get/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Admin> getAdmin(@PathVariable int id,HttpServletRequest request){
        Admin byId = adminService.getById(id);
        if (byId == null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        return Response.ok(byId);
    }

    @Operation(summary = "获得管理员本人信息", description = "所有管理员有权访问")
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

    @Operation(summary = "对应id的病人账户审核通过", description = "所有管理员有权访问")
    @PostMapping("/check/patient/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Patient> checkPatient(@PathVariable("id")int id,HttpServletRequest request){
        Patient patient = new Patient();
        patient.setUserId(id);
        patient.setAuthorized(1);
        return patientService.updatePatient(patient);
    }

    @Operation(summary = "对应id的医生账户审核通过", description = "所有管理员有权访问")
    @PostMapping("/check/doctor/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Doctor> checkDoctor(@PathVariable("id")int id,HttpServletRequest request){
        Doctor doctor = new Doctor();
        doctor.setUserId(id);
        doctor.setAuthorized(1);
        return doctorService.updateDoctor(doctor);
    }

    @Operation(summary = "所有对应id的医生账户审核通过", description = "所有管理员有权访问")
    @PostMapping("/check/doctor")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Object> checkDoctors(@RequestBody @Parameter(description = "审核通过的医生id列表") List<Integer> ids, HttpServletRequest request){
        List<Doctor> doctors = ids.stream().map(i -> {
            Doctor doctor = new Doctor();
            doctor.setUserId(i);
            doctor.setAuthorized(1);
            return doctor;
        }).collect(Collectors.toList());
        boolean b = doctorService.updateBatchById(doctors);
        return b ? Response.ok(null) : Response.fail(null, "批量通过失败");
    }

    @Operation(summary = "所有对应id的病人账户审核通过", description = "所有管理员有权访问")
    @PostMapping("/check/patient")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<Object> checkPatients(@RequestBody @Parameter(description = "审核通过的病人id列表") List<Integer> ids,HttpServletRequest request){
        List<Patient> patients = ids.stream().map(i -> {
            Patient patient = new Patient();
            patient.setUserId(i);
            patient.setAuthorized(1);
            return patient;
        }).collect(Collectors.toList());
        boolean b = patientService.updateBatchById(patients);
        return b ? Response.ok(null) : Response.fail(null, "批量通过失败");
    }

    @Operation(summary = "管理员获取所有医生所有号源", description = "管理员有权限访问")
    @GetMapping("/getRegistration")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<RegistrationVo>> getRegistration(HttpServletRequest request){
        Response<List<Registration>> response = registrationService.getAllRegistration();
        List<Registration> registrations = response.getData();
        HashMap<Integer, Doctor> map = new HashMap<>();
        List<RegistrationVo> vos = registrations.stream()
                .map(r -> convertFromRegistration(r, map)).collect(Collectors.toList());
        return new Response<>(vos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "管理员允许某号源发布", description = "管理员有权访问")
    @PostMapping("/check/registration/{id}")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<RegistrationVo>> checkRegistration(HttpServletRequest request, @PathVariable int id){
        Response<List<Registration>> response = registrationService.checkRegistration(id);
        List<Registration> registrations = response.getData();
        HashMap<Integer, Doctor> map = new HashMap<>();
        List<RegistrationVo> vos = registrations.stream()
                .map(r -> convertFromRegistration(r, map)).collect(Collectors.toList());
        return new Response<>(vos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "管理员允许一些号源发布", description = "管理员有权访问")
    @PostMapping("/check/registration")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<RegistrationVo>> checkRegistrations(HttpServletRequest request, @RequestBody List<Integer> ids){
        List<Registration> collect = ids.stream().map(id -> {
            Registration registration = new Registration();
            registration.setId(id);
            registration.setAuthorized(1);
            return registration;
        }).collect(Collectors.toList());
        boolean b = registrationService.updateBatchById(collect);
        return b ? Response.ok(null) : Response.fail(null, "批量通过失败");
    }

    private RegistrationVo convertFromRegistration(Registration r, Map<Integer, Doctor> map){
        RegistrationVo vo = new RegistrationVo();
        Integer doctorId = r.getDoctorId();
        //然而不可能npe
        vo.setUid(doctorId);
        vo.setDate(r.getDate());
        vo.setTime(r.getDaytime());
        vo.setCount(r.getQuantity());
        vo.setId(r.getId());
        vo.setChecked(r.getAuthorized());
        vo.setAvailableCount(r.getQuantity() - r.getLockedQuantity());
        Doctor doctor = null;
        if (!map.containsKey(doctorId)) {
            doctor = doctorService.getById(doctorId);
            map.put(doctorId, doctor);
        } else {
            doctor = map.get(doctorId);
        }
        vo.setDoctorName(doctor.getName());
        vo.setPhone(doctor.getContact());
        vo.setHospital(doctor.getHospital());
        vo.setOffice(doctor.getDepartment());
        return vo;
    }
}
