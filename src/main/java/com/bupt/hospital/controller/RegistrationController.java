package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Registration;
import com.bupt.hospital.domain.RegistrationRelation;
import com.bupt.hospital.enums.AuthorizeEnum;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.service.DoctorService;
import com.bupt.hospital.service.RegistrationRelationService;
import com.bupt.hospital.service.RegistrationService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.vo.RegistrationVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RegistrationRelationService registrationRelationService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String REGISTRATION_LOCK_PREFIX = "hospital:registration:lock:";

    @Operation(summary = "医生新增号源信息", description = "只有通过审核的医生本人有权访问")
    @PostMapping("/addRegistration")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<RegistrationVo> addRegistration(@RequestBody Registration registration,
                                                  HttpServletRequest request){
        HttpSession session = request.getSession();
        Registration add = new Registration();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        if (doctorService.getById(doctorId).getAuthorized() == AuthorizeEnum.UNAUTHORIZED.getCode()){
            return Response.fail(null, ResultEnum.NOT_AUTHORIED.getCode(), "您的审核未通过！");
        }
        BeanUtils.copyProperties(registration,add);
        add.setDoctorId(doctorId);
        add.setId(null);
        add.setDoctorId(doctorId);
        add.setLockedQuantity(0);
        add.setAuthorized(0);
        Response<Registration> response = registrationService.saveIfNotExist(registration, add);
        return new Response<>(null, response.getCode(), response.getMessage());
    }

    @Operation(summary = "医生获取自己今日起三天的号源", description = "只有医生本人有权访问")
    @GetMapping("/getAll")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<List<RegistrationVo>> getAll(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        Response<List<Registration>> response = registrationService.getAllAfter(doctorId);
        List<Registration> registrations = response.getData();
        HashMap<Integer, Doctor> map = new HashMap<>();
        List<RegistrationVo> vos = registrations.stream()
                .map(r -> convertFromRegistration(r, map)).collect(Collectors.toList());
        return new Response<>(vos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "医生删除自己未被挂号的号源", description = "只有医生有权访问")
    @DeleteMapping("/delete/{id}")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<Integer> deleteRegistration(HttpServletRequest request, @PathVariable Integer id){
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        List<Integer> registrationIds = registrationRelationService.getUsedRegistration(doctorId);
        Set<Integer> integerSet = new HashSet<>(registrationIds);
        if (integerSet.contains(id)){
            return Response.fail(id, ResultEnum.REGISTRATION_NOT_DELETABLE.getCode(), "号源已被挂号，不可删除！");
        }
        return registrationService.deleteRegistrations(Collections.singletonList(id));
    }

    @Operation(summary = "医生批量删除自己未被挂号的号源", description = "只有医生有权访问")
    @DeleteMapping("/delete")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<Integer> deleteRegistrations(HttpServletRequest request, @RequestBody List<Integer> ids){
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        List<Integer> registrationIds = registrationRelationService.getUsedRegistration(doctorId);
        Set<Integer> integerSet = new HashSet<>(registrationIds);
        List<Integer> unusedIds = ids.stream().filter(id -> !integerSet.contains(id)).collect(Collectors.toList());
        return registrationService.deleteRegistrations(unusedIds);
    }

    @Operation(summary = "病人获取通过审核的三天内的所有号源", description = "只有病人有权访问")
    @GetMapping("/getAfterRegistration")
    @Authorized(permits = RoleEnum.PATIENT)
    public Response<List<RegistrationVo>> getAfterRegistrations(HttpServletRequest request){
        Response<List<Registration>> response = registrationService.getAfterRegistrations();
        List<Registration> registrations = response.getData();
        HashMap<Integer, Doctor> map = new HashMap<>();
        List<RegistrationVo> vos = registrations.stream()
                .map(r -> convertFromRegistration(r, map)).collect(Collectors.toList());
        return new Response<>(vos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "病人获取自己已挂号", description = "只有病人有权访问")
    @GetMapping("/getSelfRegistration")
    @Authorized(permits = RoleEnum.PATIENT)
    public Response<List<RegistrationVo>> getSelfRegistration(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer patientId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        List<RegistrationRelation> registrationRelations = registrationRelationService.getSelfRegistration(patientId);
        List<Integer> ids = registrationRelations.stream()
                .map(RegistrationRelation::getRegistrationSource).collect(Collectors.toList());
        List<Registration> registrations = registrationService.getPatientRegistrations(ids);
        HashMap<Integer, Doctor> map = new HashMap<>();
        List<RegistrationVo> vos = registrations.stream()
                .map(r -> convertFromRegistration(r, map)).collect(Collectors.toList());
        return Response.ok(vos);
    }

    @Operation(summary = "病人挂号", description = "只有病人有权访问")
    @GetMapping("/registration/{id}")
    @Authorized(permits = RoleEnum.PATIENT)
    public Response<List<RegistrationVo>> registration(HttpServletRequest request, @PathVariable("id") Integer id){
        HttpSession session = request.getSession();
        Integer patientId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        //接下来是多线程写，开个分布式锁
        //todo 好像不用分布式锁，MySQL事务内更改locked_quantity字段就行
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String lock = REGISTRATION_LOCK_PREFIX + id;
        try {
            if (!Boolean.TRUE.equals(ops.setIfAbsent(lock, "locked"))){
                return Response.fail(null, ResultEnum.REGISTRATION_BUSY.getCode(), "其他人正在挂号中，请稍后重试！");
            }
            //该号源是否仍有余号
            Registration byId = registrationService.getById(id);
            if (byId == null){
                return Response.fail(null, ResultEnum.REGISTRATION_NOT_EXIXT.getCode(), "号源不存在，请检查id");
            }
            if (byId.getLockedQuantity() != null && byId.getLockedQuantity() >= byId.getQuantity()){
                return Response.fail(null, ResultEnum.REGISTRATION_NOT_AVAILiBLE.getCode(), "该号源已满！请重新选择日期！");
            }
            //病人是否已挂该号
            List<RegistrationRelation> registrationRelations = registrationRelationService.getRelationBySource(id);
            Set<Integer> patients = registrationRelations.stream().map(RegistrationRelation::getPatientId).collect(Collectors.toSet());
            if (patients.contains(patientId)){
                return Response.fail(null, ResultEnum.REPEAT_REGISTRATION.getCode(), "您已挂该号！请勿重复挂号");
            }
            Registration registration = new Registration();
            registration.setId(id);
            registration.setLockedQuantity(byId.getLockedQuantity() + 1);
            registrationService.updateById(registration);
            RegistrationRelation registrationRelation = new RegistrationRelation();
            registrationRelation.setRegistrationDate(byId.getDate());
            registrationRelation.setRegistrationDaytime(byId.getDaytime());
            registrationRelation.setRegistrationSource(id);
            registrationRelation.setDoctorId(byId.getDoctorId());
            registrationRelation.setPatientId(patientId);
            registrationRelation.setPayStatus(0);
            registrationRelationService.save(registrationRelation);
        }finally {
            //解锁
            redisTemplate.delete(lock);
        }

        return Response.ok(null);
    }

    @Operation(summary = "病人支付", description = "只有病人有权访问")
    @GetMapping("/pay/{id}")
    @Authorized(permits = RoleEnum.PATIENT)
    public Response<RegistrationVo> pay(HttpServletRequest request, @PathVariable("id") Integer id){
        HttpSession session = request.getSession();
        Integer patientId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        List<RegistrationRelation> selfRegistration = registrationRelationService.getSelfRegistration(patientId);
        RegistrationRelation relation = selfRegistration.stream()
                .filter(r -> r.getRegistrationSource().equals(id)).collect(Collectors.toList()).get(0);
        if (relation.getPayStatus().equals(0)){
            relation.setPayStatus(1);
            registrationRelationService.updateById(relation);
            return Response.ok(null);
        }else {
            return Response.fail(null, ResultEnum.REPEATE_PAY.getCode(), "请勿重复支付！");
        }
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
        vo.setTitle(doctor.getTitle());
        return vo;
    }
}
