package io.github.timemachinelab.common.resp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.timemachinelab.common.resp.result.Result;
import io.github.timemachinelab.common.resp.result.ResultConfig;
import io.github.timemachinelab.common.resp.result.TraceIdGenerateStrategy;
import io.github.timemachinelab.util.time.TimeUtil.TimePrecision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result配置化功能测试
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@DisplayName("Result配置化功能测试")
class ResultConfigTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @SpringBootTest
    @ActiveProfiles("test-trace-uuid")
    @DisplayName("UUID策略链路追踪测试")
    class UuidTraceTest {
        
        @Autowired
        private ResultConfig resultConfig;
        
        @Test
        @DisplayName("测试UUID策略配置")
        void testUuidTraceConfig() {
            assertTrue(resultConfig.isTraceEnabled(), "应开启链路追踪");
            assertEquals(TraceIdGenerateStrategy.UUID, resultConfig.getTraceIdStrategy(), "策略应为UUID");
            assertEquals(TimePrecision.MILLISECONDS, resultConfig.getTimestampPrecision(), "精度应为毫秒");
        }
        
        @Test
        @DisplayName("测试UUID链路ID生成")
        void testUuidTraceIdGeneration() throws Exception {
            Result<String> result = Result.success("test");
            
            assertNotNull(result.getTraceId(), "链路ID不应为null");
            assertEquals(32, result.getTraceId().length(), "UUID去掉横线后应为32位");
            assertFalse(result.getTraceId().contains("-"), "UUID不应包含横线");
            
            // 测试JSON序列化包含traceId
            String json = new ObjectMapper().writeValueAsString(result);
            assertTrue(json.contains("traceId"), "JSON应包含traceId字段");
        }
        
        @Test
        @DisplayName("测试毫秒级时间戳精度")
        void testMillisecondTimestamp() {
            long before = System.currentTimeMillis();
            Result<String> result = Result.success("test");
            long after = System.currentTimeMillis();
            
            assertNotNull(result.getTimestamp());
            assertTrue(result.getTimestamp() >= before && result.getTimestamp() <= after,
                    "毫秒级时间戳应在合理范围内");
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles("test-trace-snowflake")
    @DisplayName("雪花算法策略链路追踪测试")
    class SnowflakeTraceTest {
        
        @Autowired
        private ResultConfig resultConfig;
        
        @Test
        @DisplayName("测试雪花算法策略配置")
        void testSnowflakeTraceConfig() {
            assertTrue(resultConfig.isTraceEnabled(), "应开启链路追踪");
            assertEquals(TraceIdGenerateStrategy.SNOWFLAKE, resultConfig.getTraceIdStrategy(), "策略应为SNOWFLAKE");
            assertEquals(TimePrecision.NANOSECONDS, resultConfig.getTimestampPrecision(), "精度应为纳秒");
        }
        
        @Test
        @DisplayName("测试雪花算法链路ID生成")
        void testSnowflakeTraceIdGeneration() {
            Result<String> result = Result.success("test");
            System.out.println(result);
            assertNotNull(result.getTraceId(), "链路ID不应为null");
            assertTrue(result.getTraceId().matches("\\d+"), "雪花算法ID应为纯数字");
            assertTrue(result.getTraceId().length() > 10, "雪花算法ID长度应大于10位");
        }
        
        @Test
        @DisplayName("测试纳秒级时间戳精度")
        void testNanosecondTimestamp() {
            long before = System.nanoTime();
            Result<String> result = Result.success("test");
            long after = System.nanoTime();
            
            assertNotNull(result.getTimestamp());
            // 纳秒级时间戳会很大
            assertTrue(result.getTimestamp() > 1000000000000L, "纳秒级时间戳应为大数值");
        }
    }
    
    @SpringBootTest
    @ActiveProfiles("test-trace-timestamp")
    @DisplayName("时间戳随机策略链路追踪测试")
    static class TimestampRandomTraceTest {
        
        @Autowired
        private ResultConfig resultConfig;
        
        @Test
        @DisplayName("测试时间戳随机策略配置")
        void testTimestampRandomTraceConfig() {
            assertTrue(resultConfig.isTraceEnabled(), "应开启链路追踪");
            assertEquals(TraceIdGenerateStrategy.TIMESTAMP_RANDOM, resultConfig.getTraceIdStrategy(), "策略应为TIMESTAMP_RANDOM");
            assertEquals(TimePrecision.SECONDS, resultConfig.getTimestampPrecision(), "精度应为秒");
        }
        
        @Test
        @DisplayName("测试时间戳随机链路ID生成")
        void testTimestampRandomTraceIdGeneration() {
            Result<String> result = Result.success("test");
            
            assertNotNull(result.getTraceId(), "链路ID不应为null");
            assertTrue(result.getTraceId().contains("_"), "时间戳随机ID应包含下划线");
            
            String[] parts = result.getTraceId().split("_");
            assertEquals(2, parts.length, "应包含时间戳和随机数两部分");
            assertTrue(parts[0].matches("\\d+"), "时间戳部分应为数字");
            assertTrue(parts[1].matches("\\d+"), "随机数部分应为数字");
        }
        
        @Test
        @DisplayName("测试秒级时间戳精度")
        void testSecondTimestamp() {
            long before = System.currentTimeMillis() / 1000;
            Result<String> result = Result.success("test");
            long after = System.currentTimeMillis() / 1000;
            
            assertNotNull(result.getTimestamp());
            assertTrue(result.getTimestamp() >= before && result.getTimestamp() <= after + 1,
                    "秒级时间戳应在合理范围内");
        }
    }
    
    @SpringBootTest
    @DisplayName("多次创建一致性测试")
    static class ConsistencyTest {
        
        @Test
        @DisplayName("测试多次创建Result的一致性")
        void testMultipleResultCreation() {
            // 创建多个Result实例
            Result<String> result1 = Result.success("test1");
            Result<String> result2 = Result.success("test2");
            Result<String> result3 = Result.error("error");
            
            // 验证基本属性
            assertNotNull(result1.getTimestamp());
            assertNotNull(result2.getTimestamp());
            assertNotNull(result3.getTimestamp());
            
            // 验证时间戳递增（允许相等，因为创建速度可能很快）
            assertTrue(result2.getTimestamp() >= result1.getTimestamp());
            assertTrue(result3.getTimestamp() >= result2.getTimestamp());
            
            // 验证扩展属性初始化
            assertNotNull(result1.getExtensions());
            assertNotNull(result2.getExtensions());
            assertNotNull(result3.getExtensions());
            assertTrue(result1.getExtensions().isEmpty());
        }
        
        @Test
        @DisplayName("测试配置变更影响")
        void testConfigurationImpact() {
            // 这个测试验证配置对Result创建的影响
            // 由于配置是静态的，这里主要验证配置读取的正确性
            
            Result<String> result = Result.success("config test");
            assertNotNull(result);
            
            // 验证Result的基本功能不受配置影响
            assertTrue(result.isSuccess());
            assertFalse(result.isError());
            assertEquals("config test", result.getData());
        }
    }
}