package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.domain.Registration;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.service.RegistrationService;
import com.bupt.hospital.mapper.RegistrationMapper;
import com.bupt.hospital.util.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author 86157
* @description 针对表【t_registration】的数据库操作Service实现
* @createDate 2024-06-05 04:25:55
*/
@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration>
    implements RegistrationService{

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Registration> saveIfNotExist(Registration registration, Registration add) {
        Registration r = baseMapper.selectRegistration(add.getDoctorId(), add.getDate(), add.getDaytime());
        if (r != null){
            return Response.fail(registration, ResultEnum.REPEAT_REGISTRATION.getCode(), "您已在该时段发布号源！");
        }
        boolean save = save(add);
        return save ? Response.ok(registration) : Response.fail(registration, "新增号源失败！");
    }

    @Override
    public Response<List<Registration>> getAllAfter(Integer doctorId) {
        List<Registration> registrations = baseMapper.selectFutureRegistration(doctorId);
        return Response.ok(registrations);
    }
}




