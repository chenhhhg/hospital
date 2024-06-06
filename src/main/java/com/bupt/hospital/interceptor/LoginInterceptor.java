package com.bupt.hospital.interceptor;

import com.bupt.hospital.domain.Log;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.enums.ResultEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * @author cgx
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute(SessionAttributeEnum.USER_ID.name());
        if(userId == null){
            //不知道为什么字符集设置失效，所以拦截器里用的是英文
            response.setContentType("application/json;charset=UTF-8");
            Response<Object> fail = Response.fail(null, ResultEnum.NOT_LOGIN.getCode(), "User didn't login!");
            PrintWriter writer = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, fail);
            return false;
        }
        return true;
    }
}
