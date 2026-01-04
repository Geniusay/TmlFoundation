package io.github.timemachinelab.common.resp.result;

import io.github.timemachinelab.common.annotation.AutoResp;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * 自动响应包装增强器
 * 在响应序列化前自动将返回值包装成Result对象
 * 解决Controller返回类型与Result包装的冲突问题
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@RestControllerAdvice
public class AutoRespAdvice implements ResponseBodyAdvice<Object> {
    
    /**
     * 判断是否需要处理响应体
     * 只处理标记了@AutoResp注解的Controller方法
     * 优先使用JSON转换器，确保Result对象能正确序列化
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        Method method = returnType.getMethod();

        if(Objects.nonNull(method)){
            // 检查方法级别的@AutoResp注解
            if (AnnotatedElementUtils.hasAnnotation(method, AutoResp.class)) {
                return true;
            }
            // 检查类级别的@AutoResp注解
            if (AnnotatedElementUtils.hasAnnotation(returnType.getDeclaringClass(), AutoResp.class)) {
                return true;
            }
        }

        return false;
    }
    
    private final ObjectMapper objectMapper;

    public AutoRespAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * 在响应体写入前进行处理
     * 将原始返回值包装成Result对象
     * 特殊处理String类型以确保正确序列化
     */
    @Override
    public Object beforeBodyWrite(Object body, 
                                MethodParameter returnType, 
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType, 
                                ServerHttpRequest request,
                                ServerHttpResponse response) {

        if (body instanceof Result) {
            return body;
        }

        Result<?> result = Result.success(body);
        
        // 如果是StringHttpMessageConverter，需要将Result序列化为JSON字符串
        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                try {
                    return objectMapper.writeValueAsString(Result.error("tml-foundation serialize error: " + e.getMessage()));
                } catch (JsonProcessingException ex) {
                    return "{\"code\":500,\"message\":\"tml-foundation serialize error!\",\"data\":null}";
                }
            }
        }

        return result;
    }
}
