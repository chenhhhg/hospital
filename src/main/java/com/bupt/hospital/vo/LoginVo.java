package com.bupt.hospital.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author cgx
 */
@Getter
@Setter
public class LoginVo {
    @NotNull(message = "用户名不能为空！")
    private String userName;
    @NotNull(message = "密码不能为空！")
    private String password;
}
