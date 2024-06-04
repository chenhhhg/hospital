package com.bupt.hospital.aspect;

import com.bupt.hospital.annotation.Authorized;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.domain.User;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.exception.InvalidTargetException;
import com.bupt.hospital.util.Response;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {
    @Pointcut(value = "@annotation(com.bupt.hospital.annotation.Authorized)")
    public void authorizedPointcut(){
    }

    @Around("authorizedPointcut()")
    public Response checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        //当前会话登录信息
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        User user = null;
        Integer id = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest){
                request = (HttpServletRequest) arg;
            }else if (arg instanceof User){
                user = (User) arg;
            }else if (arg instanceof Integer){
                id = (Integer) arg;
            }
        }
        if (request == null){
            throw new InvalidTargetException("Shouldn't be used on method without arg 'HttpServletRequest'");
        }
        HttpSession session = request.getSession();

        //允许访问当前方法的角色
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Authorized annotation = signature.getMethod().getAnnotation(Authorized.class);
        RoleEnum[] permits = annotation.permits();
        //当前请求的角色是否允许访问
        String role = (String) session.getAttribute(SessionAttributeEnum.USER_ROLE.name());
        boolean contain = false;
        for (RoleEnum permit : permits) {
            if (contain = permit.name().equalsIgnoreCase(role)){
                break;
            }
        }
        if (!contain){
            return Response.fail(null, ResultEnum.NOT_AUTHORIED.getCode(), "您无权访问！");
        }
        //当管理员可访问，所有管理员可访问
        if (role.equalsIgnoreCase(RoleEnum.ADMIN.name())){
            return (Response) joinPoint.proceed();
        }
        //当病人/医生可访问，只可访问自己的
        Integer userId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        String userName = (String) session.getAttribute(SessionAttributeEnum.USER_NAME.name());
        //使用参数进行校验，无参鉴权默认允许
        boolean intoMethodWithArg = false;
        //get/{id}，只允许访问自己的
        if (id != null){
            if(id.equals(userId)){
                return (Response) joinPoint.proceed();
            }else {
                intoMethodWithArg = true;
            }
        }
        //update，只允许更新自己的
        if (user != null){
            if (userId.equals(user.getUserId()) || user.getUserId()==null && userName.equals(user.getUserName())){
                return (Response) joinPoint.proceed();
            }else {
                intoMethodWithArg = true;
            }
        }
        //留待其余方法扩展
        return intoMethodWithArg ? Response.fail(null, ResultEnum.NOT_AUTHORIED.getCode(), "您无权访问！") : (Response) joinPoint.proceed();
    }
}
