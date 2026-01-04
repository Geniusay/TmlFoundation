package io.github.timemachinelab.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

/**
 * 企业级测试配置类
 * 提供统一的测试环境配置和Bean管理
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@TestConfiguration
@ActiveProfiles("test")
public class TMLTestConfiguration {
    
    /**
     * 测试专用的ObjectMapper配置
     * 优化JSON序列化/反序列化性能
     */
    @Bean
    @Primary
    public ObjectMapper testObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 配置测试环境下的JSON处理策略
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }
    
    /**
     * 测试数据工厂
     * 提供标准化的测试数据创建方法
     */
    @Bean
    public TestDataFactory testDataFactory() {
        return new TestDataFactory();
    }
    
    /**
     * 测试性能监控器
     * 用于监控测试执行性能
     */
    @Bean
    public TestPerformanceMonitor testPerformanceMonitor() {
        return new TestPerformanceMonitor();
    }
}