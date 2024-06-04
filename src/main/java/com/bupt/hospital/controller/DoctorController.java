package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.annotation.Logged;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.enums.AdminOperationTypeEnum;
import com.bupt.hospital.service.DoctorService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    /**
     * 获取自己的信息
     * 只有本用户可访问
     */
    @Authorized(permits = {RoleEnum.DOCTOR})
    @GetMapping("get")
    public Response<Doctor> getSelf(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        Doctor byId = doctorService.getById(id);
        if (byId != null){
            byId.setPassword(null);
            return Response.ok(byId);
        }
        String userName = (String) session.getAttribute(SessionAttributeEnum.USER_NAME.name());
        Doctor byName = doctorService.getOne(new QueryWrapper<Doctor>().eq("user_name", userName));
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
    @Authorized(permits = {RoleEnum.ADMIN, RoleEnum.DOCTOR})
    @GetMapping("/get/{id}")
    public Response<Doctor> getDoctor(@PathVariable Integer id, HttpServletRequest request){
        Doctor byId = doctorService.getById(id);
        if(byId == null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(),  "用户不存在！");
        }
        return Response.ok(byId);
    }

    /**
     * 管理员api
     */
    @Authorized(permits = {RoleEnum.ADMIN})
    @GetMapping("/getAll")
    public Response<List<Doctor>> getAll(HttpServletRequest request){
        return doctorService.getAllDoctor();
    }

    /**
     * 管理员api，返回未认证所有医生
     */
    @Authorized(permits = {RoleEnum.ADMIN})
    @GetMapping("/getUnauthorized")
    public Response<List<Doctor>> getUnauthorized(HttpServletRequest request){
        return doctorService.getUnauthorized();
    }
    /**
     * 前端传id或用户名其中一个即可
     * 只有管理员或本用户可访问
     */
    @Authorized(permits = {RoleEnum.ADMIN, RoleEnum.DOCTOR})
    @Logged(type = AdminOperationTypeEnum.UPDATE)
    @PostMapping("/update")
    public Response<Doctor> update(@RequestBody Doctor doctor, HttpServletRequest request){
        return doctorService.updateDoctor(doctor);
    }
}
