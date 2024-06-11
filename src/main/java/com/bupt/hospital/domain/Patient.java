package com.bupt.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;


/**
 * 
 * @author zyq
 * @TableName t_patient
 */
@TableName(value ="t_patient")
@Data
@NoArgsConstructor
public class Patient implements Serializable, User {
    @TableId(type = IdType.AUTO)
    @Nullable
    private Integer userId;

    @NotNull(message = "用户名不能为空！")
    private String userName;

    @NotNull(message = "密码不能为空！")
    private String password;

    @NotNull(message = "身份证不能为空！")
    private String idNumber;

    @NotNull(message = "姓名不能为空！")
    private String name;

    @NotNull(message = "年龄不能为空！")
    private Integer age;

    @NotNull(message = "性别不能为空！")
    private String gender;

    @NotNull(message = "居住地址不能为空！")
    private String address;

    @NotNull(message = "联系方式不能为空！")
    private String contact;

    @Nullable
    private String medicalRecord;

    @Nullable
    private int authorized;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}