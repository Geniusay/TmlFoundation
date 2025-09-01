package io.github.timemachinelab.common.resp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 企业级HTTP响应结果类
 * 支持泛型、可扩展等功能
 * 
 * @param <T> 返回数据类型
 * @author TimeMachineLab
 * @version 1.0.0
 */
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 核心属性
    private Integer status;     // HTTP状态码
    private String code;        // 业务返回码
    private String message;     // 返回消息
    private T data;             // 返回数据
    private Long timestamp;     // 时间戳
    
    // 扩展属性
    private String traceId;
    private Map<String, Object> extensions;
    
    // 私有构造函数，强制使用建造者模式
    private Result() {
        this.traceId = generateTraceId();
        this.timestamp = System.currentTimeMillis();
        this.extensions = new HashMap<>();
    }
    
    // Getters
    public Integer getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public Long getTimestamp() { return timestamp; }
    public String getTraceId() { return traceId; }
    public Map<String, Object> getExtensions() { return extensions; }
    
    // 生成链路追踪ID
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    // 成功响应 - 无数据
    public static <T> Result<T> success() {
        return new Builder<T>()
                .status(200)
                .code("SUCCESS")
                .message("操作成功")
                .build();
    }
    
    // 成功响应 - 带数据
    public static <T> Result<T> success(T data) {
        return new Builder<T>()
                .status(200)
                .code("SUCCESS")
                .message("操作成功")
                .data(data)
                .build();
    }
    
    // 成功响应 - 自定义消息
    public static <T> Result<T> success(String message, T data) {
        return new Builder<T>()
                .status(200)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }
    
    // 错误响应 - 默认
    public static <T> Result<T> error() {
        return new Builder<T>()
                .status(500)
                .code("ERROR")
                .message("操作失败")
                .build();
    }
    
    // 错误响应 - 自定义消息
    public static <T> Result<T> error(String message) {
        return new Builder<T>()
                .status(500)
                .code("ERROR")
                .message(message)
                .build();
    }
    
    // 错误响应 - 自定义状态码和消息
    public static <T> Result<T> error(Integer status, String code, String message) {
        return new Builder<T>()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }
    
    // 验证错误
    public static <T> Result<T> validationError(String message) {
        return new Builder<T>()
                .status(400)
                .code("VALIDATION_ERROR")
                .message(message)
                .build();
    }
    
    // 未授权
    public static <T> Result<T> unauthorized(String message) {
        return new Builder<T>()
                .status(401)
                .code("UNAUTHORIZED")
                .message(message)
                .build();
    }
    
    // 禁止访问
    public static <T> Result<T> forbidden(String message) {
        return new Builder<T>()
                .status(403)
                .code("FORBIDDEN")
                .message(message)
                .build();
    }
    
    // 资源未找到
    public static <T> Result<T> notFound(String message) {
        return new Builder<T>()
                .status(404)
                .code("NOT_FOUND")
                .message(message)
                .build();
    }
    
    // 建造者模式
    public static class Builder<T> {
        private final Result<T> result;
        
        public Builder() {
            this.result = new Result<>();
        }
        
        public Builder<T> status(Integer status) {
            result.status = status;
            return this;
        }
        
        public Builder<T> code(String code) {
            result.code = code;
            return this;
        }
        
        public Builder<T> message(String message) {
            result.message = message;
            return this;
        }
        
        public Builder<T> data(T data) {
            result.data = data;
            return this;
        }
        
        public Builder<T> traceId(String traceId) {
            result.traceId = traceId;
            return this;
        }
        
        public Builder<T> extension(String key, Object value) {
            result.extensions.put(key, value);
            return this;
        }
        
        public Builder<T> extensions(Map<String, Object> extensions) {
            result.extensions.putAll(extensions);
            return this;
        }
        
        public Result<T> build() {
            return result;
        }
    }
    
    // 判断是否成功
    public boolean isSuccess() {
        return this.status != null && this.status == 200;
    }
    
    // 判断是否失败
    public boolean isError() {
        return !isSuccess();
    }
    
    // 链式调用 - 添加扩展属性
    public Result<T> addExtension(String key, Object value) {
        this.extensions.put(key, value);
        return this;
    }
    
    // 链式调用 - 设置链路追踪ID
    public Result<T> withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("Result{status=%d, code='%s', message='%s', data=%s, traceId='%s', timestamp=%d}",
                status, code, message, data, traceId, timestamp);
    }
}