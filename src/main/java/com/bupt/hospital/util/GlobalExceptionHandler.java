package com.bupt.hospital.util;

import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.exception.InvalidTargetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author cgx
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> argsInvalidHandler(MethodArgumentNotValidException e){
        return Response.fail(null, e.getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidTargetException.class)
    public Response<Object> reflectArgInvalidHandler(InvalidTargetException e){
        return Response.fail(null, ResultEnum.INVALID_ANNOTATION.getCode(), "后端放错了注解或者方法参数忘记加HttpServletRequest了，请联系开发人员。");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Object> frontEndArgInvalidHandler(HttpMessageNotReadableException e){
        return Response.fail(null, ResultEnum.INVALID_BODY_ARG.getCode(), "前端同学请求体传错参了，请联系开发人员");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Object> frontEndArgInvalidHandler(HttpRequestMethodNotSupportedException e){
        return Response.fail(null, ResultEnum.INVALID_METHOD.getCode(), "前端同学用错方法了，请联系开发人员");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<Object> frontEndUrlArgInvalidHandler(MethodArgumentTypeMismatchException e){
        return Response.fail(null, ResultEnum.INVALID_METHOD.getCode(), "前端同学看一下url是不是错了，或者路径传参类型不对。请联系开发人员");
    }

}
