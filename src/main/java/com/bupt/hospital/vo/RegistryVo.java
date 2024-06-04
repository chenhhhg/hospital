package com.bupt.hospital.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cgx
 */
@Getter
@Setter
public class RegistryVo {
    @Nullable
    private Integer uid;
    @NotBlank(message = "密码不能为空！")
    private String userPwd;
    @NotBlank(message = "账户不能为空！")
    private String userName;
    @NotBlank(message = "角色不能为空！")
    private String userRole;
    @Nullable
    private boolean userChecked;
}
