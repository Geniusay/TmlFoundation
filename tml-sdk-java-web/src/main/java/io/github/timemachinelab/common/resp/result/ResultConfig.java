package io.github.timemachinelab.common.resp.result;

import io.github.timemachinelab.util.time.TimeUtil.TimePrecision;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Result响应配置类
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "tml.web.result")
public class ResultConfig {
    
    /**
     * 是否开启链路追踪（默认：false）
     */
    private boolean traceEnabled = false;
    
    /**
     * 链路ID生成策略（默认：UUID）
     */
    private TraceIdGenerateStrategy traceIdStrategy = TraceIdGenerateStrategy.UUID;
    
    /**
     * 时间戳精度（默认：秒）
     */
    private TimePrecision timestampPrecision = TimePrecision.SECONDS;
    
    public boolean isTraceEnabled() {
        return traceEnabled;
    }
    
    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }
    
    public TraceIdGenerateStrategy getTraceIdStrategy() {
        return traceIdStrategy;
    }
    
    public void setTraceIdStrategy(TraceIdGenerateStrategy traceIdStrategy) {
        this.traceIdStrategy = traceIdStrategy;
    }
    
    public TimePrecision getTimestampPrecision() {
        return timestampPrecision;
    }
    
    public void setTimestampPrecision(TimePrecision timestampPrecision) {
        this.timestampPrecision = timestampPrecision;
    }
}