package io.github.timemachinelab.common.resp.result;

import io.github.timemachinelab.util.time.TimeUtil.TimePrecision;

public class ResultConfig {

    private boolean traceEnabled = false;

    private TraceIdGenerateStrategy traceIdStrategy = TraceIdGenerateStrategy.UUID;

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
