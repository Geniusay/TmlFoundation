package io.github.timemachinelab.aspect;

import io.github.timemachinelab.common.resp.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 自动响应包装切面
 * 拦截被@AutoResp注解标记的Controller方法，自动将返回值包装成Result对象
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@Aspect
@Component
@Order(1)
public class AutoRespAspect {
    
    /**
     * 拦截被@AutoResp注解标记的方法
     * 支持类级别和方法级别的注解
     */
    @Around("@annotation(io.github.timemachinelab.common.annotation.AutoResp) || " +
            "@within(io.github.timemachinelab.common.annotation.AutoResp)")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();

            if (result instanceof Result) {
                return result;
            }

            return Result.success(result);
            
        } catch (Exception e) {
            // 异常情况下返回错误Result
            return Result.error(e.getMessage());
        }
    }
}