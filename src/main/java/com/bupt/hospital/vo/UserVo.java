package com.bupt.hospital.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 86157
 */
@Data
public class UserVo {
    private Integer uid;
    private String userPwd;
    private String username;
    private String userRole;
    @Schema(description = "0-未审核；1-审核通过")
    private Integer userChecked;
    private String idCard;
    private String realName;
    private String address;
    private String phone;
    private Integer age;
    private String gender;
    private String hospital;
    private String office;
    private String title;
    private String speciality;
    private String medicalHistory;
}
