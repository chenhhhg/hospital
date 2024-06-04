package com.bupt.hospital.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum AuthorizeEnum {
    UNAUTHORIZED(0),
    AUTHORIZED(1);
    int code;
}
