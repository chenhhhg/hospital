package com.bupt.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author zyq
 * @TableName t_doctor
 */
@TableName(value ="t_doctor")
@Data
public class Doctor implements Serializable, User {
    @Schema(description = "用户 ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer userId;

    @Schema(description = "用户名", example = "johndoe")
    @NotNull(message = "用户名不能为空！")
    private String userName;

    @Schema(description = "密码", example = "password123")
    @NotNull(message = "密码不能为空！")
    private String password;

    @Schema(description = "身份证号", example = "123456789012345")
    @NotNull(message = "身份证不能为空！")
    private String idNumber;

    @Schema(description = "姓名", example = "John Doe")
    @NotNull(message = "姓名不能为空！")
    private String name;

    @Schema(description = "年龄", example = "35")
    @NotNull(message = "年龄不能为空！")
    private Integer age;

    @Schema(description = "性别", example = "男")
    @NotNull(message = "性别不能为空！")
    private String gender;

    @Schema(description = "居住地址", example = "123 Main Street, Anytown USA")
    @NotNull(message = "居住地址不能为空！")
    private String address;

    @Schema(description = "联系方式", example = "123-456-7890")
    @NotNull(message = "联系方式不能为空！")
    private String contact;

    @Schema(description = "医院单位", example = "ABC Hospital")
    @NotNull(message = "医院单位不能为空！")
    private String hospital;

    @Schema(description = "部门", example = "Internal Medicine")
    @NotNull(message = "部门不能为空！")
    private String department;

    @Schema(description = "职衔", example = "Chief of Staff")
    @NotNull(message = "职衔不能为空！")
    private String title;

    @Schema(description = "专业特长", example = "Cardiology")
    @Nullable
    private String specialty;

    @Schema(description = "是否授权", example = "1")
    @Nullable
    private int authorized;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}