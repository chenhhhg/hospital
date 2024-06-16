package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.service.DoctorService;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.mapper.DoctorMapper;
import com.bupt.hospital.enums.AuthorizeEnum;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_doctor】的数据库操作Service实现
* @createDate 2024-06-02 18:34:11
*/
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor>
    implements DoctorService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Doctor> updateDoctor(Doctor doctor) {
        Doctor byId = baseMapper.selectById(doctor.getUserId());
        if (byId != null){
            baseMapper.updateById(doctor);
            byId.setPassword(null);
            return Response.ok(byId);
        }
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<Doctor>()
                .eq("user_name", doctor.getUserName());
        Doctor byName = baseMapper.selectOne(queryWrapper);
        if (byName!=null){
            baseMapper.update(doctor, queryWrapper);
            byName.setPassword(null);
            return Response.ok(byName);
        }
        return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！请检查id或用户名！");
    }

    @Override
    public Response<List<Doctor>> getAllDoctor() {
        List<Doctor> doctors = baseMapper.selectList(null);
        return Response.ok(doctors);
    }

    @Override
    public Response<List<Doctor>> getUnauthorized() {
        List<Doctor> doctors = baseMapper.selectList(new QueryWrapper<Doctor>()
                .eq("authorized", AuthorizeEnum.UNAUTHORIZED.getCode()));
        return Response.ok(doctors);
    }

    @Override
    public List<Doctor> getBatch(QueryWrapper<Doctor> condition) {
        return baseMapper.selectList(condition);
    }
}




