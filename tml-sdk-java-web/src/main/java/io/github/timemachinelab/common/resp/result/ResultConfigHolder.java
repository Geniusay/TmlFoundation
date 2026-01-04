package io.github.timemachinelab.common.resp.result;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class ResultConfigHolder {

    private static final AtomicReference<ResultConfig> CONFIG = new AtomicReference<>(new ResultConfig());

    private ResultConfigHolder() {
    }

    public static ResultConfig getConfig() {
        return CONFIG.get();
    }

    public static void setConfig(ResultConfig config) {
        if (Objects.nonNull(config)) {
            CONFIG.set(config);
        }
    }
}
