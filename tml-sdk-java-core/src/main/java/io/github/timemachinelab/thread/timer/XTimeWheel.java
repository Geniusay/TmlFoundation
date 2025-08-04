package io.github.timemachinelab.thread.timer;

import java.util.concurrent.TimeUnit;

public class XTimeWheel extends AbstractTimeWheel implements TimeWheel{

    // the time unit of the time wheel
    private TimeUnit timeUnit;

    // the duration of the time wheel
    private int duration;

    // Task slot, used to store the list of tasks that need to be executed at the current timestamp
    private final TimeNode[] slots;

    private final static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private final static int DEFAULT_SLOT_NUM = 60;

    private final static int DEFAULT_DURATION = 1;

    public XTimeWheel(TimeTicker timeTicker) {
        super(timeTicker);
        this.slots = new TimeNode[DEFAULT_SLOT_NUM];
        this.duration = DEFAULT_DURATION;
        this.timeUnit = DEFAULT_TIME_UNIT;
    }

    public XTimeWheel(TimeTicker timeTicker, int slotNum, int duration, TimeUnit timeUnit) {
        super(timeTicker);
        if(slotNum <= 0){
            throw new IllegalArgumentException(String.format("Illegal slotNum: %s <= 0", slotNum));
        }
        if(duration <= 0){
            throw new IllegalArgumentException(String.format("Illegal duration: %s <= 0", duration));
        }
        this.slots = new TimeNode[slotNum];
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public XTimeWheel(TimeTicker timeTicker, int slotNum, int duration, TimeUnit timeUnit, TaskIdGenerator taskIdGenerator) {
        this(timeTicker, slotNum, duration, timeUnit);
        this.idGen = taskIdGenerator;
    }

    @Override
    public boolean addTask0(Runnable task, long delay, TimeUnit timeUnit) {
        if(task == null){
            return false;
        }
        int idx;
        if((idx = computeSlotIndex(delay, timeUnit)) < 0){
            return false;
        }
        TimeNode timeNode = new TimeNode();
        this.slots[idx] = timeNode;
        return true;
    }

    private class TimeNode {

        private Runnable scheduleWork;

        private TimeNode next;
    }

    @Override
    public boolean removeTask(String taskId) {
        return false;
    }


    private int computeSlotIndex(long delay, TimeUnit timeUnit){
        return -1;
    }

}
