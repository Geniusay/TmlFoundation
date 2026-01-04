package io.github.timemachinelab.test.controller;

import io.github.timemachinelab.common.annotation.AutoResp;
import io.github.timemachinelab.common.resp.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 * 用于验证@AutoResp注解功能
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    /**
     * 测试方法级别的@AutoResp注解
     * 返回字符串，应该被自动包装成Result
     */
    @GetMapping("/string")
    @AutoResp
    public String testString() {
        return "Hello World";
    }
    
    /**
     * 测试返回对象，应该被自动包装成Result
     */
    @GetMapping("/object")
    @AutoResp
    public Map<String, Object> testObject() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "TimeMachineLab");
        data.put("version", "1.0.0");
        data.put("timestamp", System.currentTimeMillis());
        return data;
    }
    
    /**
     * 测试返回已经是Result类型的情况
     * 不应该被重复包装
     */
    @GetMapping("/result")
    @AutoResp
    public Result<String> testResult() {
        return Result.success("Already wrapped");
    }
    
    /**
     * 测试异常情况
     * 应该返回错误的Result
     */
    @GetMapping("/error")
    @AutoResp
    public String testError() {
        throw new RuntimeException("Test exception");
    }
    
    /**
     * 没有@AutoResp注解的方法
     * 不应该被包装
     */
    @GetMapping("/normal")
    public String testNormal() {
        return "Normal response";
    }
}

/**
 * 类级别@AutoResp注解测试
 * 该类下所有方法都会被自动包装
 */
@RestController
@RequestMapping("/test-class")
@AutoResp
class TestClassController {
    
    @GetMapping("/data")
    public String getData() {
        return "Class level annotation";
    }
    
    @GetMapping("/info")
    public Map<String, String> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("controller", "TestClassController");
        info.put("annotation", "Class Level @AutoResp");
        return info;
    }
}