package io.github.timemachinelab.thread.timer;

import java.util.concurrent.TimeUnit;

/**
 * Time wheel
 */
public class TimeWheel {

    private TimeTicker timeTicker;

    // the time unit of the time wheel
    private TimeUnit timeUnit;

    // the duration of the time wheel
    private int duration;

    // the number of slots in the time wheel
    private int slotNum;

    // Task slot, used to store the list of tasks that need to be executed at the current timestamp
    private final TimeNode[] slots;

    private final static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private final static int DEFAULT_SLOT_NUM = 60;

    private final static int DEFAULT_DURATION = 1;

    public TimeWheel() {
        this.timeUnit = DEFAULT_TIME_UNIT;
        this.slotNum = DEFAULT_SLOT_NUM;
        this.duration = DEFAULT_DURATION;
        this.slots = new TimeNode[slotNum];
    }

    private class TimeNode {

        private Runnable scheduleWork;

        private TimeNode next;
    }
}
