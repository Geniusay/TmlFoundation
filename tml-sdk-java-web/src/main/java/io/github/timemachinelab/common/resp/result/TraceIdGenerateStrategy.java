package io.github.timemachinelab.common.resp.result;

/**
 * 链路追踪ID生成策略枚举
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
public enum TraceIdGenerateStrategy {
    
    /**
     * UUID生成策略（默认）
     */
    UUID,
    
    /**
     * 雪花算法生成策略
     */
    SNOWFLAKE,
    
    /**
     * 时间戳+随机数生成策略
     */
    TIMESTAMP_RANDOM
}