package io.github.timemachinelab.util.time;

import java.time.Instant;

/**
 * 高性能时间工具类
 * 提供多精度的时间戳获取功能
 * 
 * @author TimeMachineLab
 * @version 1.0
 */
public final class TimeUtil {
    
    /**
     * 时间精度枚举
     */
    public enum TimePrecision {
        /** 秒级精度 */
        SECONDS,
        /** 毫秒级精度 */
        MILLISECONDS,
        /** 纳秒级精度 */
        NANOSECONDS
    }

    
    // 私有构造函数，防止实例化
    private TimeUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * 获取当前时间戳
     * 支持多精度配置
     * 
     * @param precision 时间精度，为null时默认使用毫秒精度
     * @return 指定精度的时间戳
     */
    public static long getCurrentTimestamp(TimePrecision precision) {
        // 参数默认值处理
        final TimePrecision targetPrecision = precision != null ? precision : TimePrecision.MILLISECONDS;
        
        // 获取当前时间
        final Instant instant = Instant.now();
        
        // 根据精度返回相应的时间戳
        switch (targetPrecision) {
            case SECONDS:
                return instant.getEpochSecond();
            case MILLISECONDS:
                return instant.toEpochMilli();
            case NANOSECONDS:
                // 纳秒 = 秒 * 10^9 + 纳秒部分
                return instant.getEpochSecond() * 1_000_000_000L + instant.getNano();
            default:
                throw new IllegalArgumentException("Unsupported precision: " + targetPrecision);
        }
    }
    
    /**
     * 获取当前时间戳（默认毫秒精度）
     * 
     * @return 毫秒级时间戳
     */
    public static long getCurrentTimestamp() {
        return getCurrentTimestamp(null);
    }
}