package com.bupt.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.bupt.hospital.util.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author cgx
 * @TableName t_admin
 */
@TableName(value ="t_admin")
@Data
public class Admin implements Serializable, User {
    @Schema(description = "数据库记录id")
    @TableId(type = IdType.AUTO)
    private Integer userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户名")
    private String idNumber;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "联系方式")
    private String contact;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}