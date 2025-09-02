package io.github.timemachinelab.test.annotation;

import io.github.timemachinelab.test.config.TMLTestConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

/**
 * 企业级集成测试注解
 * 整合常用的测试注解，提供统一的测试环境配置
 * 
 * 使用示例：
 * <pre>
 * {@code
 * @TmlIntegrationTest
 * class MyServiceIntegrationTest {
 *     // 测试方法
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
@SpringBootTest(
    classes = TMLTestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public @interface TmlIntegrationTest {
    
    /**
     * 指定额外的配置类
     */
    Class<?>[] classes() default {};
    
    /**
     * 指定测试属性
     */
    String[] properties() default {};
    
    /**
     * 是否启用性能监控
     */
    boolean enablePerformanceMonitoring() default true;
    
    /**
     * 是否启用事务回滚
     */
    boolean transactional() default true;
}