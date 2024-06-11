package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.service.PatientService;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.mapper.PatientMapper;
import com.bupt.hospital.enums.AuthorizeEnum;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_patient】的数据库操作Service实现
* @createDate 2024-06-02 18:34:11
*/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>
    implements PatientService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Patient> updatePatient(Patient patient) {
        Patient byId = baseMapper.selectById(patient.getUserId());
        if (byId != null){
            baseMapper.updateById(patient);
            byId.setPassword(null);
            return Response.ok(byId);
        }
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<Patient>()
                .eq("user_name", patient.getUserName());
        Patient byName = baseMapper.selectOne(queryWrapper);
        if (byName!=null){
            baseMapper.update(patient, queryWrapper);
            byName.setPassword(null);
            return Response.ok(byName);
        }
        return Response.fail(null, ResultEnum.USER_NOT_EXIXT.getCode(), "用户不存在！请检查id或用户名！");
    }

    @Override
    public Response<List<Patient>> getAllPatient() {
        List<Patient> patients = baseMapper.selectList(null);
        return Response.ok(patients);
    }

    @Override
    public Response<List<Patient>> getUnauthorized() {
        List<Patient> patients = baseMapper.selectList(new QueryWrapper<Patient>()
                .eq("authorized", AuthorizeEnum.UNAUTHORIZED.getCode()));
        return Response.ok(patients);
    }
}




