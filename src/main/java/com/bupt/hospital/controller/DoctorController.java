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
import com.bupt.hospital.util.VoConvertor;
import com.bupt.hospital.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Operation(summary = "获取医生自己的信息", description = "只有本用户可访问")
    @Authorized(permits = {RoleEnum.DOCTOR})
    @GetMapping("get")
    public Response<UserVo> getSelf(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        Doctor byId = doctorService.getById(id);
        if (byId != null){
            byId.setPassword(null);
            return Response.ok(VoConvertor.convertFromDoctor(byId));
        }
        String userName = (String) session.getAttribute(SessionAttributeEnum.USER_NAME.name());
        Doctor byName = doctorService.getOne(new QueryWrapper<Doctor>().eq("user_name", userName));
        if (byName != null){
            byName.setPassword(null);
            return Response.ok(VoConvertor.convertFromDoctor(byName));
        }
        return Response.fail(null,"获取信息失败，请检查登录状态");
    }

    @Operation(summary = "获取对应id医生的信息", description = "只有管理员或本用户可访问")
    @Authorized(permits = {RoleEnum.ADMIN, RoleEnum.DOCTOR})
    @GetMapping("/get/{id}")
    public Response<UserVo> getDoctor(@PathVariable Integer id, HttpServletRequest request){
        Doctor byId = doctorService.getById(id);
        if(byId == null){
            return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(),  "用户不存在！");
        }
        return Response.ok(VoConvertor.convertFromDoctor(byId));
    }

    @Operation(summary = "获取所有医生的信息", description = "只有管理员可访问")
    @Authorized(permits = {RoleEnum.ADMIN})
    @GetMapping("/getAll")
    public Response<List<UserVo>> getAll(HttpServletRequest request){
        Response<List<Doctor>> response = doctorService.getAllDoctor();
        List<UserVo> userVos = response.getData().stream()
                .map(VoConvertor::convertFromDoctor).collect(Collectors.toList());
        return new Response<>(userVos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "返回未认证所有医生", description = "只有管理员可访问")
    @Authorized(permits = {RoleEnum.ADMIN})
    @GetMapping("/getUnauthorized")
    public Response<List<UserVo>> getUnauthorized(HttpServletRequest request){
        Response<List<Doctor>> response = doctorService.getUnauthorized();
        List<UserVo> userVos = response.getData().stream()
                .map(VoConvertor::convertFromDoctor).collect(Collectors.toList());
        return new Response<>(userVos, response.getCode(), response.getMessage());
    }

    @Operation(summary = "修改医生信息", description = "只有管理员或本用户可访问。在请求体中识别对应id或用户名，id优先级更高，id为空则使用用户名识别。")
    @Authorized(permits = {RoleEnum.ADMIN, RoleEnum.DOCTOR})
    @Logged(type = AdminOperationTypeEnum.UPDATE)
    @PostMapping("/update")
    public Response<UserVo> update(@RequestBody Doctor doctor, HttpServletRequest request){
        Response<Doctor> response = doctorService.updateDoctor(doctor);
        return new Response<>(VoConvertor.convertFromDoctor(response.getData()),
                response.getCode(), response.getMessage());
    }
}
