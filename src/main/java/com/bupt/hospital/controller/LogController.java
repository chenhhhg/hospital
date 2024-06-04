package com.bupt.hospital.controller;

import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Log;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.service.LogService;
import com.bupt.hospital.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/getAll")
    @Authorized(permits = {RoleEnum.ADMIN})
    public Response<List<Log>> getAll(HttpServletRequest request){
        return logService.getAll();
    }


}
