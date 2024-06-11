package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.service.AdminService;
import com.bupt.hospital.domain.Admin;
import com.bupt.hospital.mapper.AdminMapper;
import org.springframework.stereotype.Service;

/**
* @author 86157
* @description 针对表【t_admin】的数据库操作Service实现
* @createDate 2024-06-02 18:34:11
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService {

}




