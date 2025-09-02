package io.github.timemachinelab.common.resp.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.timemachinelab.util.time.TimeUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.timemachinelab.common.constant.HttpCode.ERROR;
import static io.github.timemachinelab.common.constant.HttpCode.SUCCESS;
import static io.github.timemachinelab.common.constant.HttpStatus.INTERNAL_SERVER_ERROR;
import static io.github.timemachinelab.common.constant.HttpStatus.OK;

/**
 * HTTP响应结果类
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String traceId;

    private Map<String, Object> extensions;

    private Result() {
        this.traceId = generateTraceId();
        this.timestamp = generateTimestamp();
        this.extensions = new HashMap<>();
    }

    public Integer getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public Long getTimestamp() { return timestamp; }
    public String getTraceId() { return traceId; }
    public Map<String, Object> getExtensions() { return extensions; }
    
    // 生成链路追踪ID
    private String generateTraceId() {
        ResultConfig config = ResultConfigHolder.getConfig();
        if (config == null || !config.isTraceEnabled()) {
            return null;
        }
        
        TraceIdGenerateStrategy strategy = config.getTraceIdStrategy();
        switch (strategy) {
            case SNOWFLAKE:
                return String.valueOf(System.currentTimeMillis() << 12 | ThreadLocalRandom.current().nextInt(4096));
            case TIMESTAMP_RANDOM:
                return System.currentTimeMillis() + "_" + ThreadLocalRandom.current().nextInt(100000);
            default:
                return UUID.randomUUID().toString().replace("-", "");
        }
    }
    
    // 生成时间戳
    private Long generateTimestamp() {
        ResultConfig config = ResultConfigHolder.getConfig();
        if (config == null) {
            return TimeUtil.getCurrentTimestamp();
        }
        return TimeUtil.getCurrentTimestamp(config.getTimestampPrecision());
    }
    
    // 成功响应 - 无数据
    public static <T> Result<T> success() {
        return success("success", SUCCESS, null);
    }
    
    // 成功响应
    public static <T> Result<T> success(T data) {
        return success(SUCCESS,"success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return success(SUCCESS, message, data);
    }

    public static <T> Result<T> success(String code, String message, T data) {
        return new Builder<T>()
                .status(OK)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }


    // 错误响应
    public static <T> Result<T> error() {
        return error(INTERNAL_SERVER_ERROR, ERROR, "error", null);
    }

    public static <T> Result<T> error(String message) {
        return error(INTERNAL_SERVER_ERROR, ERROR, message, null);
    }

    public static <T> Result<T> error(String code, String message) {
        return error(INTERNAL_SERVER_ERROR, code, message, null);
    }

    public static <T> Result<T> error(String code, String message, T data) {
        return error(INTERNAL_SERVER_ERROR, code, message, data);
    }

    public static <T> Result<T> error(Integer status, String code, String message, T data) {
        return new Builder<T>()
                .status(status)
                .code(code)
                .message(message)
                .data(data)
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