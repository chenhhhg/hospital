package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.service.AdminService;
import com.bupt.hospital.domain.Admin;
import com.bupt.hospital.mapper.AdminMapper;
import com.bupt.hospital.util.Response;
import org.springframework.stereotype.Service;

/**
* @author 86157
* @description 针对表【t_admin】的数据库操作Service实现
* @createDate 2024-06-02 18:34:11
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService {

    @Override
    public Response<Admin> updateAdmin(Admin admin) {
        Admin ori = baseMapper.selectById(admin.getUserId());
        int i = baseMapper.updateById(admin);
        if(i < 1){
            return Response.fail(null,"更新失败！");
        }
        return Response.ok(ori);
    }
}




