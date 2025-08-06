package io.github.timemachinelab.thread.timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class XTimeWheel extends AbstractTimeWheel implements TimeWheel{

    // the time unit of the time wheel
    private TimeUnit timeUnit;

    // the duration of the time wheel
    private int duration;

    // Task slot, used to store the list of tasks that need to be executed at the current timestamp
    private final TimeNode[] slots;

    private final AtomicInteger ptr = new AtomicInteger(0);

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

        private int remainNanos;

    }

    @Override
    public boolean removeTask(String taskId) {
        return false;
    }


    private int computeSlotIndex(long delay, TimeUnit timeUnit){
        return -1;
    }

    protected long[] calculateSlotDistributionAndIndex(long delay, TimeUnit delayUnit){
        // 1. 直接比较时间单位层级
        int delayOrdinal = delayUnit.ordinal();
        int durationOrdinal = timeUnit.ordinal();

        // 2. 根据单位层级关系选择最优计算路径
        if (durationOrdinal < delayOrdinal) {
            // 情况1：duration单位比delay单位更精细
            return calculateWhenDurationFiner(delay, delayUnit, duration, timeUnit);
        } else {
            // 情况2：duration单位比delay单位更粗或相同
            return calculateWhenDurationCoarser(delay, delayUnit, duration, timeUnit);
        }
    }

    // 当duration单位比delay单位更精细时的计算
    private static long[] calculateWhenDurationFiner(long delay, TimeUnit delayUnit,
                                                     long duration, TimeUnit durationUnit) {
        // 直接转换到duration单位计算
        long convertedDelay = durationUnit.convert(delay, delayUnit);
        long fullSlots = convertedDelay / duration;
        long remainder = convertedDelay % duration;

        // 将余数转换回纳秒
        long remainderNanos = (remainder > 0) ? durationUnit.toNanos(remainder) : 0;

        return new long[]{fullSlots, remainderNanos};
    }

    // 当duration单位比delay单位更粗或相同时的计算
    private static long[] calculateWhenDurationCoarser(long delay, TimeUnit delayUnit,
                                                       long duration, TimeUnit durationUnit) {
        // 1. 先将delay转换到duration单位
        long convertedDelay = durationUnit.convert(delay, delayUnit);

        // 2. 计算完整槽数
        long fullSlots = convertedDelay / duration;

        // 3. 计算剩余时间
        long remainderInDelayUnit = delay - delayUnit.convert(
                fullSlots * duration, durationUnit);

        // 4. 将剩余时间转为纳秒
        long remainderNanos = (remainderInDelayUnit > 0) ? remainderInDelayUnit : 0;

        return new long[]{fullSlots, remainderNanos};
    }

}
