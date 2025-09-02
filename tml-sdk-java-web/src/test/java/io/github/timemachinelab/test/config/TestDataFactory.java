package io.github.timemachinelab.test.config;

import io.github.timemachinelab.common.resp.result.Result;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 企业级测试数据工厂
 * 提供标准化、可复用的测试数据生成方法
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
public class TestDataFactory {
    
    private static final String[] SAMPLE_STRINGS = {
        "test", "sample", "demo", "example", "mock", "stub"
    };
    
    private static final Random RANDOM = ThreadLocalRandom.current();
    
    /**
     * 创建成功的Result对象
     */
    public <T> Result<T> createSuccessResult(T data) {
        return Result.success(data);
    }
    
    /**
     * 创建失败的Result对象
     */
    public <T> Result<T> createErrorResult(String message) {
        return Result.error(message);
    }
    
    /**
     * 生成随机字符串
     */
    public String randomString() {
        return SAMPLE_STRINGS[RANDOM.nextInt(SAMPLE_STRINGS.length)] + "_" + RANDOM.nextInt(1000);
    }
    
    /**
     * 生成随机整数
     */
    public Integer randomInt() {
        return RANDOM.nextInt(1000);
    }
    
    /**
     * 生成随机布尔值
     */
    public Boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }
    
    /**
     * 创建测试用的Map数据
     */
    public Map<String, Object> createTestMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("stringField", randomString());
        map.put("intField", randomInt());
        map.put("booleanField", randomBoolean());
        map.put("timestamp", LocalDateTime.now());
        return map;
    }
    
    /**
     * 创建测试用的List数据
     */
    public <T> List<T> createTestList(Class<T> clazz, int size) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (clazz == String.class) {
                list.add(clazz.cast(randomString()));
            } else if (clazz == Integer.class) {
                list.add(clazz.cast(randomInt()));
            } else if (clazz == Boolean.class) {
                list.add(clazz.cast(randomBoolean()));
            }
        }
        return list;
    }
    
    /**
     * 创建性能测试数据
     * 用于大数据量测试场景
     */
    public List<String> createPerformanceTestData(int size) {
        List<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add("performance_test_data_" + i);
        }
        return data;
    }
}