package test.util.time;

import io.github.timemachinelab.util.time.TimeUtil;
import io.github.timemachinelab.util.time.TimeUtil.TimePrecision;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TimeUtil工具类的单元测试
 * 测试多精度的时间戳获取功能
 * 
 * @author TimeMachineLab
 * @version 1.0
 */
@DisplayName("TimeUtil工具类测试")
public class TimeUtilTest {
    
    @Test
    @DisplayName("测试默认时间戳获取")
    public void testGetCurrentTimestampDefault() {
        long timestamp = TimeUtil.getCurrentTimestamp();
        long currentTime = System.currentTimeMillis();
        
        // 允许1秒的误差
        assertTrue(Math.abs(timestamp - currentTime) < 1000, 
                   "默认时间戳应该接近系统当前时间");
    }
    
    @Test
    @DisplayName("测试毫秒精度时间戳")
    public void testGetCurrentTimestampMilliseconds() {
        long timestamp = TimeUtil.getCurrentTimestamp(TimePrecision.MILLISECONDS);
        long currentTime = System.currentTimeMillis();
        
        // 允许1秒的误差
        assertTrue(Math.abs(timestamp - currentTime) < 1000, 
                   "毫秒精度时间戳应该接近系统当前时间");
    }
    
    @Test
    @DisplayName("测试秒精度时间戳")
    public void testGetCurrentTimestampSeconds() {
        long timestamp = TimeUtil.getCurrentTimestamp(TimePrecision.SECONDS);
        long currentTime = System.currentTimeMillis() / 1000;
        
        // 允许2秒的误差
        assertTrue(Math.abs(timestamp - currentTime) <= 2, 
                   "秒精度时间戳应该接近系统当前时间（秒）");
    }
    
    @Test
    @DisplayName("测试纳秒精度时间戳")
    public void testGetCurrentTimestampNanoseconds() {
        long timestamp = TimeUtil.getCurrentTimestamp(TimePrecision.NANOSECONDS);
        long currentTimeNanos = System.currentTimeMillis() * 1_000_000;
        
        // 纳秒时间戳应该大于毫秒转换的纳秒值
        assertTrue(timestamp > currentTimeNanos - 1_000_000_000L, 
                   "纳秒精度时间戳应该在合理范围内");
        assertTrue(timestamp < currentTimeNanos + 1_000_000_000L, 
                   "纳秒精度时间戳应该在合理范围内");
    }
    

    

    
    @Test
    @DisplayName("测试null参数处理")
    public void testNullParameters() {
        // 测试null精度参数
        assertDoesNotThrow(() -> {
            long timestamp = TimeUtil.getCurrentTimestamp((TimeUtil.TimePrecision) null);
            assertTrue(timestamp > 0, "null精度参数应该返回有效时间戳");
        });
    }
    
    @Test
    @DisplayName("测试性能")
    public void testPerformance() {
        int iterations = 100000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            TimeUtil.getCurrentTimestamp();
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double avgTimePerCall = (double) duration / iterations;
        
        System.out.println(String.format("执行%d次调用，平均每次耗时: %.2f纳秒", iterations, avgTimePerCall));
        
        // 验证性能：平均每次调用应该少于1微秒（1000纳秒）
        assertTrue(avgTimePerCall < 1000, "平均每次调用应该少于1微秒");
    }
    
    @Test
    @DisplayName("测试边界情况")
    public void testEdgeCases() {
        // 测试null参数
        assertDoesNotThrow(() -> TimeUtil.getCurrentTimestamp((TimeUtil.TimePrecision) null));
        
        // 测试所有精度枚举值
        for (TimeUtil.TimePrecision precision : TimeUtil.TimePrecision.values()) {
            assertDoesNotThrow(() -> {
                long timestamp = TimeUtil.getCurrentTimestamp(precision);
                assertTrue(timestamp > 0, "所有精度都应该返回正数时间戳");
            });
        }
    }
}