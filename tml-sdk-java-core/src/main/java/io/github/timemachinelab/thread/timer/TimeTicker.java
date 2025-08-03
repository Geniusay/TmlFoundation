package io.github.timemachinelab.thread.timer;

import java.util.concurrent.atomic.AtomicLong;

public class TimeTicker {

    private final AtomicLong tick = new AtomicLong(0);

    public long tick() {
        return tick.get();
    }

    private long nextTick(){
        return tick.incrementAndGet();
    }
}
