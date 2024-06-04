package com.bupt.hospital.enums;

import javafx.scene.input.KeyCodeCombination;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cgx
 */

@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(200),
    NOT_LOGIN(300),
    USER_NAME_EXIST(400),
    NOT_ALLOW_ADMIN(401),
    ROLE_ERROR(402),
    USER_NOT_EXIXT(403),
    WRONG_PASSWORD(404),
    NOT_AUTHORIED(405),
    INVALID_METHOD(406),
    REPEAT_REGISTRATION(407),
    UNKNOWN_FAIL(500),
    INVALID_ANNOTATION(501),
    INVALID_BODY_ARG(502),

    ;
    Integer code;
}
