package com.bupt.hospital.mapper;

import com.bupt.hospital.domain.Registration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author 86157
* @description 针对表【t_registration】的数据库操作Mapper
* @createDate 2024-06-05 04:25:55
* @Entity com.bupt.hospital.domain.Registration
*/
public interface RegistrationMapper extends BaseMapper<Registration> {

    List<Registration> selectFutureRegistration(@Param("id") Integer doctorId);

    Registration selectRegistration(@Param("id") Integer doctorId,@Param("date") Date date,@Param("daytime") Integer daytime);
}




