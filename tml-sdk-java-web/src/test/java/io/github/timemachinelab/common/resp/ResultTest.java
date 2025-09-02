package io.github.timemachinelab.common.resp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.timemachinelab.common.resp.result.Result;
import io.github.timemachinelab.common.resp.result.ResultConfig;
import io.github.timemachinelab.common.resp.result.TraceIdGenerateStrategy;
import io.github.timemachinelab.util.time.TimeUtil.TimePrecision;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result类功能测试
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@SpringBootTest
@DisplayName("Result类功能测试")
class ResultTest {
    
    @Autowired
    private ResultConfig resultConfig;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("测试默认配置 - 链路追踪关闭")
    void testDefaultConfig() {
        // 验证默认配置
        assertFalse(resultConfig.isTraceEnabled(), "默认应关闭链路追踪");
        assertEquals(TraceIdGenerateStrategy.UUID, resultConfig.getTraceIdStrategy(), "默认策略应为UUID");
        assertEquals(TimePrecision.SECONDS, resultConfig.getTimestampPrecision(), "默认精度应为秒");
        
        // 测试Result创建
        Result<String> result = Result.success("test data");
        assertNotNull(result, "Result不应为null");
        assertNull(result.getTraceId(), "链路追踪关闭时traceId应为null");
        assertNotNull(result.getTimestamp(), "时间戳不应为null");
        assertEquals("test data", result.getData(), "数据应正确设置");
        assertTrue(result.isSuccess(), "应为成功状态");
    }
    
    @Test
    @DisplayName("测试成功响应创建")
    void testSuccessResponse() {
        // 无数据成功响应
        Result<Void> emptyResult = Result.success();
        assertTrue(emptyResult.isSuccess());
        assertNull(emptyResult.getData());
        assertEquals("success", emptyResult.getMessage());
        
        // 带数据成功响应
        Result<String> dataResult = Result.success("hello");
        assertTrue(dataResult.isSuccess());
        assertEquals("hello", dataResult.getData());
        
        // 自定义消息成功响应
        Result<Integer> customResult = Result.success("操作成功", 100);
        assertTrue(customResult.isSuccess());
        assertEquals("操作成功", customResult.getMessage());
        assertEquals(100, customResult.getData());
    }
    
    @Test
    @DisplayName("测试错误响应创建")
    void testErrorResponse() {
        // 默认错误响应
        Result<Void> errorResult = Result.error();
        System.out.println(errorResult);
        assertTrue(errorResult.isError());
        assertFalse(errorResult.isSuccess());
        assertEquals("error", errorResult.getMessage());
        
        // 自定义消息错误响应
        Result<Void> customErrorResult = Result.error("操作失败");
        assertTrue(customErrorResult.isError());
        assertEquals("操作失败", customErrorResult.getMessage());
        
        // 带数据错误响应
        Result<String> dataErrorResult = Result.error("验证失败", "invalid input");
        assertTrue(dataErrorResult.isError());
        assertEquals("验证失败", dataErrorResult.getMessage());
        assertEquals("invalid input", dataErrorResult.getData());
    }
    
    @Test
    @DisplayName("测试Builder模式")
    void testBuilderPattern() {
        Result<String> result = new Result.Builder<String>()
                .status(200)
                .code("SUCCESS")
                .message("操作成功")
                .data("test")
                .extension("extra", "value")
                .build();
        
        assertEquals(200, result.getStatus());
        assertEquals("SUCCESS", result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals("test", result.getData());
        assertEquals("value", result.getExtensions().get("extra"));
    }
    
    @Test
    @DisplayName("测试链式调用")
    void testChainedCalls() {
        Result<String> result = Result.success("data")
                .addExtension("key1", "value1")
                .addExtension("key2", "value2")
                .withTraceId("custom-trace-id");
        
        assertEquals("value1", result.getExtensions().get("key1"));
        assertEquals("value2", result.getExtensions().get("key2"));
        // 注意：withTraceId可能被配置覆盖
    }

    
    @Test
    @DisplayName("测试时间戳生成")
    void testTimestampGeneration() {
        long before = System.currentTimeMillis() / 1000; // 秒级时间戳
        Result<String> result = Result.success("test");
        long after = System.currentTimeMillis() / 1000;
        
        assertNotNull(result.getTimestamp());
        // 由于默认配置是秒级精度，时间戳应该在合理范围内
        assertTrue(result.getTimestamp() >= before && result.getTimestamp() <= after + 1,
                "时间戳应在合理范围内");
    }
    
    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        Result<String> result = Result.success("test data");
        String str = result.toString();
        System.out.println(str);
        assertNotNull(str);
        assertTrue(str.contains("Result{"));
        assertTrue(str.contains("test data"));
        assertTrue(str.contains("timestamp="));
    }
}