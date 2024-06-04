package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.annotation.Logged;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.enums.AdminOperationTypeEnum;
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

/**
 * @author cgx
 */
@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    PatientService patientService;

    /**
     * 获取自己的信息
     * 只有本用户可访问
     */
    @GetMapping("get")
    @Authorized(permits = {RoleEnum.PATIENT})
    public Response<Patient> getSelf(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        Patient byId = patientService.getById(id);
        if (byId != null){
            byId.setPassword(null);
            return Response.ok(byId);
        }
        String userName = (String) session.getAttribute(SessionAttributeEnum.USER_NAME.name());
        Patient byName = patientService.getOne(new QueryWrapper<Patient>().eq("user_name", userName));
        if (byName != null){
            byName.setPassword(null);
            return Response.ok(byName);
        }
        return Response.fail(null,"获取信息失败，请检查登录状态");
    }

    /**
     * 前端url传id
     * 只有管理员或本用户可访问
     */
    @GetMapping("/get/{id}")
    @Authorized(permits = {RoleEnum.PATIENT, RoleEnum.ADMIN})
    public Response<Patient> getPatient(@PathVariable("id")Integer id, HttpServletRequest request){
        Patient byId = patientService.getById(id);
        if(byId == null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        return Response.ok(byId);
    }

    /**
     * 管理员api
     */
    @GetMapping("/getAll")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<Patient>> getAll(HttpServletRequest request){
        return patientService.getAllPatient();
    }

    /**
     * 管理员api，返回未认证所有病人
     */
    @GetMapping("/getUnauthorized")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<Patient>> getUnauthorized(HttpServletRequest request){
        return patientService.getUnauthorized();
    }

    /**
     * 前端传id或用户名其中一个即可
     * 只有管理员或本用户可访问
     */
    @PostMapping("/update")
    @Authorized(permits = {RoleEnum.PATIENT, RoleEnum.ADMIN})
    @Logged(type = AdminOperationTypeEnum.UPDATE)
    public Response<Patient> update(@RequestBody Patient patient, HttpServletRequest request){
        return patientService.updatePatient(patient);
    }

    /**
     *
     *
     *
     */
}
