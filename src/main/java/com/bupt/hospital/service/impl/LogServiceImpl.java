package com.bupt.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.hospital.domain.Log;
import com.bupt.hospital.mapper.LogMapper;
import com.bupt.hospital.service.LogService;
import com.bupt.hospital.service.PatientService;
import com.bupt.hospital.util.Response;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86157
* @description 针对表【t_log】的数据库操作Service实现
* @createDate 2024-06-04 15:15:30
*/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
    implements LogService {
    @Override
    public Response<List<Log>> getAll() {
        List<Log> logs = baseMapper.selectList(null);
        return Response.ok(logs);
    }
}




