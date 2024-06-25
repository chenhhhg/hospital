package com.bupt.hospital.service;

import com.bupt.hospital.domain.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.hospital.util.Response;

/**
* @author 86157
* @description 针对表【t_admin】的数据库操作Service
* @createDate 2024-06-02 18:34:11
*/
public interface AdminService extends IService<Admin> {

    Response<Admin> updateAdmin(Admin admin);
}
