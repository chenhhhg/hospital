package com.bupt.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @author cgx
 * @TableName t_registration_relation
 */
@TableName(value ="t_registration_relation")
@Data
public class RegistrationRelation implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer patientId;

    private Integer doctorId;

    private Integer registrationSource;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registrationDate;

    private Integer registrationDaytime;

    private Integer payStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}