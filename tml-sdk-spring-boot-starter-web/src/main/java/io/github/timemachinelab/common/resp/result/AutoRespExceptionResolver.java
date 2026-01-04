package io.github.timemachinelab.common.resp.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.timemachinelab.common.annotation.AutoResp;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AutoRespExceptionResolver implements HandlerExceptionResolver, Ordered {

    private final ObjectMapper objectMapper;

    public AutoRespExceptionResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HandlerMethod handlerMethod = handler instanceof HandlerMethod ? (HandlerMethod) handler : null;
        if (!shouldHandle(handlerMethod)) {
            return null;
        }

        try {
            Result<?> result = Result.error(500, "tml.error", ex.getMessage(), null);
            response.setStatus(200);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.flushBuffer();
            return new ModelAndView();
        } catch (Exception writeEx) {
            return null;
        }
    }

    private boolean shouldHandle(HandlerMethod handlerMethod) {
        if (Objects.isNull(handlerMethod)) {
            return false;
        }
        Method method = handlerMethod.getMethod();
        if (AnnotatedElementUtils.hasAnnotation(method, AutoResp.class)) {
            return true;
        }
        Class<?> beanType = handlerMethod.getBeanType();
        return AnnotatedElementUtils.hasAnnotation(beanType, AutoResp.class);
    }
}
