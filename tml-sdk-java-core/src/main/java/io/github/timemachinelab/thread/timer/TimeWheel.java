package io.github.timemachinelab.thread.timer;

import java.util.concurrent.TimeUnit;

/**
 * Time wheel
 */
public class TimeWheel {

    // the time unit of the time wheel
    private TimeUnit timeUnit;

    // the number of slots in the time wheel
    private int slotNum;

    private final TimeNode[] slots;

    private final static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private final static int DEFAULT_SLOT_NUM = 60;

    public TimeWheel() {
        this.timeUnit = DEFAULT_TIME_UNIT;
        this.slotNum = DEFAULT_SLOT_NUM;
        this.slots = new TimeNode[slotNum];
    }

    private class TimeNode {

        private Runnable scheduleWork;

        private TimeNode next;
    }
}
