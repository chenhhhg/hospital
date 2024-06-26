package com.bupt.hospital.aspect;

import com.bupt.hospital.annotation.Logged;
import com.bupt.hospital.domain.Doctor;
import com.bupt.hospital.domain.Log;
import com.bupt.hospital.domain.Patient;
import com.bupt.hospital.domain.User;
import com.bupt.hospital.enums.AdminOperationTypeEnum;
import com.bupt.hospital.enums.ResultEnum;
import com.bupt.hospital.enums.RoleEnum;
import com.bupt.hospital.enums.SessionAttributeEnum;
import com.bupt.hospital.exception.InvalidTargetException;
import com.bupt.hospital.service.LogService;
import com.bupt.hospital.util.Response;
import com.bupt.hospital.vo.UserVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
@Aspect
public class LogAspect {
    @Pointcut("@annotation(com.bupt.hospital.annotation.Logged)")
    public void logPointcut(){}

    @Autowired
    private LogService logService;

    @Around("logPointcut()")
    public Response log(ProceedingJoinPoint joinPoint) throws Throwable {
        Response result = (Response) joinPoint.proceed();
        //操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Logged annotation = signature.getMethod().getAnnotation(Logged.class);
        HttpSession session = getHttpSession(joinPoint);
        String role = (String) session.getAttribute(SessionAttributeEnum.USER_ROLE.name());
        //只记录管理员操作
        if (!role.equalsIgnoreCase(RoleEnum.ADMIN.name())){
            return result;
        }
        Integer adminId = (Integer) session.getAttribute(SessionAttributeEnum.USER_ID.name());
        UserVo user = (UserVo) result.getData();
        ObjectMapper objectMapper = new ObjectMapper();
        String originalValue = objectMapper.writeValueAsString(user);
        Log log = new Log();
        log.setAdminId(adminId);
        log.setUserId(user.getUid());
        log.setUserRole(user.getUserRole());
        log.setOperationType(annotation.type().name());
        log.setOriginalValue(originalValue);
        log.setStatus(result.getCode().equals(ResultEnum.SUCCESS.getCode()) ? 1 : 0);
        log.setModifyTime(new Date());
        logService.save(log);
        return result;
    }

    private static HttpSession getHttpSession(ProceedingJoinPoint joinPoint) {
        //当前会话登录信息
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest){
                request = (HttpServletRequest) arg;
                break;
            }
        }
        if (request == null){
            throw new InvalidTargetException("Shouldn't be used on method without arg 'HttpServletRequest'");
        }
        return request.getSession();
    }
}
