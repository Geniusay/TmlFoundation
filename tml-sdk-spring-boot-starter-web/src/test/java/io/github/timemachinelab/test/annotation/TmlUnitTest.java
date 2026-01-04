package io.github.timemachinelab.test.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.*;

/**
 * 企业级单元测试注解
 * 专为单元测试设计的轻量级注解，自动配置Mockito环境
 * 
 * 使用示例：
 * <pre>
 * {@code
 * @TmlUnitTest
 * class MyServiceUnitTest {
 *     
 *     @Mock
 *     private MyRepository repository;
 *     
 *     @InjectMocks
 *     private MyService service;
 *     
 *     @Test
 *     void testMethod() {
 *         // 测试逻辑
 *     }
 * }
 * }
 * </pre>
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(MockitoExtension.class)
public @interface TmlUnitTest {
    
    /**
     * 是否启用严格的Mock验证
     */
    boolean strictMocks() default true;
    
    /**
     * 是否启用性能监控
     */
    boolean enablePerformanceMonitoring() default false;
}