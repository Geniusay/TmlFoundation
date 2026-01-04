package io.github.timemachinelab.test.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.timemachinelab.TestApplication;
import io.github.timemachinelab.common.resp.result.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AutoResp注解功能集成测试
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("@AutoResp注解功能测试")
class AutoRespAspectTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("测试字符串返回值自动包装")
    void testStringAutoWrap() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/string"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("tml.success"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value("Hello World"))
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(responseBody, Result.class);
        
        assertTrue(response.isSuccess());
        assertEquals("Hello World", response.getData());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    @DisplayName("测试对象返回值自动包装")
    void testObjectAutoWrap() throws Exception {
        mockMvc.perform(get("/test/object"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("tml.success"))
                .andExpect(jsonPath("$.data.name").value("TimeMachineLab"))
                .andExpect(jsonPath("$.data.version").value("1.0.0"))
                .andExpect(jsonPath("$.data.timestamp").exists());
    }
    
    @Test
    @DisplayName("测试Result类型不重复包装")
    void testResultNotDoubleWrap() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/result"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("Already wrapped"))
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(responseBody, Result.class);
        
        // 验证没有被重复包装
        assertEquals("Already wrapped", response.getData());
        assertNotEquals("Result", response.getData().getClass().getSimpleName());
    }
    
    @Test
    @DisplayName("测试异常自动包装为错误响应")
    void testExceptionAutoWrap() throws Exception {
        mockMvc.perform(get("/test/error"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("tml.error"))
                .andExpect(jsonPath("$.message").value("Test exception"));
    }
    
    @Test
    @DisplayName("测试无注解方法不被包装")
    void testNormalMethodNotWrap() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/normal"))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        
        // 验证返回的是原始字符串，不是JSON格式的Result
        assertEquals("Normal response", responseBody);
        
        // 验证不是JSON格式
        assertThrows(Exception.class, () -> {
            objectMapper.readValue(responseBody, Result.class);
        });
    }
    
    @Test
    @DisplayName("测试类级别注解功能")
    void testClassLevelAnnotation() throws Exception {
        mockMvc.perform(get("/test-class/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("Class level annotation"));
        
        mockMvc.perform(get("/test-class/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.controller").value("TestClassController"))
                .andExpect(jsonPath("$.data.annotation").value("Class Level @AutoResp"));
    }
}