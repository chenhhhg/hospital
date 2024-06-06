package com.bupt.hospital.mapper;

import com.bupt.hospital.domain.RegistrationRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_registration_relation】的数据库操作Mapper
* @createDate 2024-06-05 04:25:55
* @Entity com.bupt.hospital.domain.RegistrationRelation
*/
public interface RegistrationRelationMapper extends BaseMapper<RegistrationRelation> {

    List<Integer> selectAllRegistrationIds(@Param("doctorIc") Integer doctorId);
}




