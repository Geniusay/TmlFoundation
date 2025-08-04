package io.github.timemachinelab.thread.timer;

import java.util.concurrent.TimeUnit;

public class CircleTimeWheel extends AbstractTimeWheel implements TimeWheel{

    public CircleTimeWheel(TimeTicker timeTicker) {
        super(timeTicker);
    }

    public CircleTimeWheel(TimeTicker timeTicker, TaskIdGenerator idGen) {
        super(timeTicker, idGen);
    }

    @Override
    public boolean addTask0(Runnable task, long delay, TimeUnit timeUnit) {
        return false;
    }


    @Override
    public boolean removeTask(String taskId) {
        return false;
    }
}
