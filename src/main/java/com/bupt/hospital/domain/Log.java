package com.bupt.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_log
 */
@TableName(value ="t_log")
@Data
public class Log implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer adminId;

    private Integer userId;

    private String userRole;

    private String operationType;

    private String originalValue;

    private Date modifyTime;

    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}