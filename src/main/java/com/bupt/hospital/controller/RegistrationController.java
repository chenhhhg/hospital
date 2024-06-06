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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private final String REGISTRATION_LOCK_PREFIX = "registration:lock:";

    @Operation(summary = "医生新增号源信息", description = "只有通过审核的医生本人有权访问")
    @PostMapping("/addRegistration")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<Registration> addRegistration(@RequestBody Registration registration,
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
        return registrationService.saveIfNotExist(registration, add);
    }

    @Operation(summary = "医生获取自己今日起三天的号源", description = "只有医生本人有权访问")
    @GetMapping("/getAll")
    @Authorized(permits = RoleEnum.DOCTOR)
    public Response<List<Registration>> getAll(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        return registrationService.getAllAfter(doctorId);
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
    public Response<List<Registration>> getAfterRegistrations(HttpServletRequest request){
        return registrationService.getAfterRegistrations();
    }

    @Operation(summary = "病人获取自己已挂号", description = "只有病人有权访问")
    @GetMapping("/getSelfRegistration")
    @Authorized(permits = RoleEnum.PATIENT)
    public Response<List<Registration>> getSelfRegistration(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer patientId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        List<RegistrationRelation> registrationRelations = registrationRelationService.getSelfRegistration(patientId);
        List<Integer> ids = registrationRelations.stream()
                .map(RegistrationRelation::getRegistrationSource).collect(Collectors.toList());
        List<Registration> registrations = registrationService.getPatientRegistrations(ids);
        return Response.ok(registrations);
    }

    @Operation(summary = "病人挂号", description = "只有病人有权访问")
    @GetMapping("/registration/{id}")
    @Authorized(permits = RoleEnum.PATIENT)
    public Response<List<Registration>> registration(HttpServletRequest request, @PathVariable("id") Integer id){
        HttpSession session = request.getSession();
        Integer patientId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        //接下来是多线程写，开个分布式锁
        //todo 好像不用分布式锁，MySQL事务内更改locked_quantity字段就行
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String lock = REGISTRATION_LOCK_PREFIX + id;
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
        //解锁
        ops.getAndDelete(lock);
        return Response.ok(null);
    }
}
