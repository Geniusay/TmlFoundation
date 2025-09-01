package io.github.timemachinelab.common.resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Result配置管理类
 * 基于Spring Boot配置项风格设计
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
public class ResultConfig {
    
    // 配置项前缀
    public static final String CONFIG_PREFIX = "tml.result";
    
    // 配置项键名（遵循Spring Boot命名规范）
    public static final String TRACE_ENABLED = CONFIG_PREFIX + ".trace.enabled";
    public static final String TRACE_HEADER_NAME = CONFIG_PREFIX + ".trace.header-name";
    public static final String DEFAULT_SUCCESS_CODE = CONFIG_PREFIX + ".default.success-code";
    public static final String DEFAULT_ERROR_CODE = CONFIG_PREFIX + ".default.error-code";
    public static final String EXTENSION_ENABLED = CONFIG_PREFIX + ".extension.enabled";
    public static final String EXTENSION_MAX_SIZE = CONFIG_PREFIX + ".extension.max-size";
    
    // 默认配置值
    private static final Map<String, Object> defaultConfig = new ConcurrentHashMap<>();
    private static final Map<String, Object> userConfig = new ConcurrentHashMap<>();
    
    // 静态初始化默认配置
    static {
        initDefaultConfig();
    }
    
    /**
     * 初始化默认配置
     */
    private static void initDefaultConfig() {
        defaultConfig.put(TRACE_ENABLED, true);
        defaultConfig.put(TRACE_HEADER_NAME, "X-Trace-Id");
        defaultConfig.put(DEFAULT_SUCCESS_CODE, "SUCCESS");
        defaultConfig.put(DEFAULT_ERROR_CODE, "ERROR");
        defaultConfig.put(EXTENSION_ENABLED, true);
        defaultConfig.put(EXTENSION_MAX_SIZE, 100);
    }
    
    /**
     * 设置配置项
     * 
     * @param key 配置键
     * @param value 配置值
     */
    public static void setProperty(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Config key cannot be null");
        }
        userConfig.put(key, value);
    }
    
    /**
     * 获取配置项
     * 
     * @param key 配置键
     * @return 配置值
     */
    public static Object getProperty(String key) {
        if (key == null) {
            return null;
        }
        // 优先返回用户配置，其次返回默认配置
        return userConfig.getOrDefault(key, defaultConfig.get(key));
    }
    
    /**
     * 获取配置项（带类型转换和默认值）
     * 
     * @param key 配置键
     * @param defaultValue 默认值
     * @param <T> 值类型
     * @return 配置值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProperty(String key, T defaultValue) {
        Object value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
    
    /**
     * 获取字符串配置
     * 
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static String getString(String key, String defaultValue) {
        Object value = getProperty(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * 获取布尔配置
     * 
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        Object value = getProperty(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return defaultValue;
    }
    
    /**
     * 获取整数配置
     * 
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static int getInt(String key, int defaultValue) {
        Object value = getProperty(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * 移除配置项
     * 
     * @param key 配置键
     */
    public static void removeProperty(String key) {
        if (key != null) {
            userConfig.remove(key);
        }
    }
    
    /**
     * 批量设置配置
     * 
     * @param properties 配置映射
     */
    public static void setProperties(Map<String, Object> properties) {
        if (properties != null) {
            userConfig.putAll(properties);
        }
    }
    
    /**
     * 获取所有配置
     * 
     * @return 配置映射
     */
    public static Map<String, Object> getAllProperties() {
        Map<String, Object> allConfig = new ConcurrentHashMap<>(defaultConfig);
        allConfig.putAll(userConfig);
        return allConfig;
    }
    
    /**
     * 重置所有配置为默认值
     */
    public static void resetToDefaults() {
        userConfig.clear();
    }
    
    /**
     * 检查链路追踪是否启用
     * 
     * @return 是否启用
     */
    public static boolean isTraceEnabled() {
        return getBoolean(TRACE_ENABLED, true);
    }
    
    /**
     * 获取链路追踪头名称
     * 
     * @return 头名称
     */
    public static String getTraceHeaderName() {
        return getString(TRACE_HEADER_NAME, "X-Trace-Id");
    }
    
    /**
     * 获取默认成功码
     * 
     * @return 成功码
     */
    public static String getDefaultSuccessCode() {
        return getString(DEFAULT_SUCCESS_CODE, "SUCCESS");
    }
    
    /**
     * 获取默认错误码
     * 
     * @return 错误码
     */
    public static String getDefaultErrorCode() {
        return getString(DEFAULT_ERROR_CODE, "ERROR");
    }
    
    /**
     * 检查扩展属性是否启用
     * 
     * @return 是否启用
     */
    public static boolean isExtensionEnabled() {
        return getBoolean(EXTENSION_ENABLED, true);
    }
    
    /**
     * 获取扩展属性最大数量
     * 
     * @return 最大数量
     */
    public static int getExtensionMaxSize() {
        return getInt(EXTENSION_MAX_SIZE, 100);
    }
    
    /**
     * 配置构建器
     * 提供链式调用方式设置配置
     */
    public static class Builder {
        private final Map<String, Object> configs = new ConcurrentHashMap<>();
        
        public Builder traceEnabled(boolean enabled) {
            configs.put(TRACE_ENABLED, enabled);
            return this;
        }
        
        public Builder traceHeaderName(String headerName) {
            configs.put(TRACE_HEADER_NAME, headerName);
            return this;
        }
        
        public Builder defaultSuccessCode(String code) {
            configs.put(DEFAULT_SUCCESS_CODE, code);
            return this;
        }
        
        public Builder defaultErrorCode(String code) {
            configs.put(DEFAULT_ERROR_CODE, code);
            return this;
        }
        
        public Builder extensionEnabled(boolean enabled) {
            configs.put(EXTENSION_ENABLED, enabled);
            return this;
        }
        
        public Builder extensionMaxSize(int maxSize) {
            configs.put(EXTENSION_MAX_SIZE, maxSize);
            return this;
        }
        
        public Builder property(String key, Object value) {
            configs.put(key, value);
            return this;
        }
        
        public void apply() {
            ResultConfig.setProperties(configs);
        }
    }
    
    /**
     * 创建配置构建器
     * 
     * @return 配置构建器
     */
    public static Builder builder() {
        return new Builder();
    }
}