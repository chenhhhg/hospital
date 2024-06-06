package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.domain.RegistrationRelation;
import com.bupt.hospital.service.RegistrationRelationService;
import com.bupt.hospital.mapper.RegistrationRelationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_registration_relation】的数据库操作Service实现
* @createDate 2024-06-05 04:25:55
*/
@Service
public class RegistrationRelationServiceImpl extends ServiceImpl<RegistrationRelationMapper, RegistrationRelation>
    implements RegistrationRelationService{

    @Override
    public List<Integer> getUsedRegistration(Integer doctorId) {
        return baseMapper.selectAllRegistrationIds(doctorId);
    }

    @Override
    public List<RegistrationRelation> getSelfRegistration(Integer patientId) {
        return baseMapper.selectList(new QueryWrapper<RegistrationRelation>()
                .eq("patient_id", patientId));
    }

    @Override
    public List<RegistrationRelation> getRelationBySource(Integer id) {
        return baseMapper.selectList(new QueryWrapper<RegistrationRelation>()
                .eq("registration_source", id));
    }
}




