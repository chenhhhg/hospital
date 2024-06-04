package com.bupt.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.hospital.domain.Log;
import com.bupt.hospital.util.Response;

import java.util.List;

/**
 * @author 86157
 * @description 针对表【t_log】的数据库操作Service
 * @createDate 2024-06-04 15:15:30
 */
public interface LogService extends IService<Log> {

    Response<List<Log>> getAll();
}
