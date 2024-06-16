package com.bupt.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.annotation.Logged;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.domain.User;
import com.bupt.hospital.enums.AdminOperationTypeEnum;
import com.bupt.hospital.service.PatientService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.util.VoConvertor;
import com.bupt.hospital.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    PatientService patientService;

    @Operation(summary = "获取病人自己的信息", description = "只有本用户可访问")
    @GetMapping("get")
    @Authorized(permits = {RoleEnum.PATIENT})
    public Response<UserVo> getSelf(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        Patient byId = patientService.getById(id);
        if (byId != null){
            byId.setPassword(null);
            return Response.ok(VoConvertor.convertFromPatient(byId));
        }
        String userName = (String) session.getAttribute(SessionAttributeEnum.USER_NAME.name());
        Patient byName = patientService.getOne(new QueryWrapper<Patient>().eq("user_name", userName));
        if (byName != null){
            byName.setPassword(null);
            return Response.ok(VoConvertor.convertFromPatient(byName));
        }
        return Response.fail(null,"获取信息失败，请检查登录状态");
    }

    @Operation(summary = "获取对应id病人的信息", description = "只有管理员或本用户可访问")
    @GetMapping("/get/{id}")
    @Authorized(permits = {RoleEnum.PATIENT, RoleEnum.ADMIN})
    public Response<UserVo> getPatient(@PathVariable("id")Integer id, HttpServletRequest request){
        Patient byId = patientService.getById(id);
        if(byId == null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！");
        }
        return Response.ok(VoConvertor.convertFromPatient(byId));
    }

    @Operation(summary = "获取所有病人的信息", description = "只有管理员可访问")
    @GetMapping("/getAll")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<UserVo>> getAll(HttpServletRequest request){
        Response<List<Patient>> response = patientService.getAllPatient();
        List<UserVo> userVos = response.getData().stream().map(VoConvertor::convertFromPatient).collect(Collectors.toList());
        return new Response<>(userVos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "返回未认证所有病人", description = "只有管理员可访问")
    @GetMapping("/getUnauthorized")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<UserVo>> getUnauthorized(HttpServletRequest request){
        Response<List<Patient>> response = patientService.getUnauthorized();
        List<UserVo> userVos = response.getData().stream().map(VoConvertor::convertFromPatient).collect(Collectors.toList());
        return new Response<>(userVos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "修改病人信息", description = "只有管理员或本用户可访问。在请求体中识别对应id或用户名，id优先级更高，id为空则使用用户名识别。")
    @PostMapping("/update")
    @Authorized(permits = {RoleEnum.PATIENT, RoleEnum.ADMIN})
    @Logged(type = AdminOperationTypeEnum.UPDATE)
    public Response<UserVo> update(@RequestBody Patient patient, HttpServletRequest request){
        Response<Patient> response = patientService.updatePatient(patient);
        return new Response<>(VoConvertor.convertFromPatient(response.getData()),
                response.getCode(), response.getMessage());
    }

}
