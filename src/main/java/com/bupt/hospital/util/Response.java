package com.bupt.hospital.util;

import com.bupt.hospital.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cgx
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Response<T> {
    private T data;
    private Integer code;
    private String message;
    public static <T> Response<T> ok(T data){
        return new Response<>(data, ResultEnum.SUCCESS.getCode(), "success");
    }

    public static <T> Response<T> fail(T data, String message){
        return new Response<>(data, ResultEnum.UNKNOWN_FAIL.getCode(), message);
    }

    public static <T> Response<T> fail(T data, int code, String message){
        return new Response<>(data, code, message);
    }
}
