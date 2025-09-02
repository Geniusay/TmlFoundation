package io.github.timemachinelab.thread.timer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Time wheel
 */
public abstract class AbstractTimeWheel implements TimeWheel{

    protected final TimeTicker timeTicker;

    protected TaskIdGenerator idGen;

    private final static TaskIdGenerator UUID_TASK_ID_GENERATOR = ()->(UUID.randomUUID().toString());

    private final static String INVALID_TASK_ID = "-1";

    public AbstractTimeWheel(TimeTicker timeTicker) {
        if(timeTicker == null){
            throw new IllegalArgumentException("Illegal timeTicker: null");
        }
        this.idGen = UUID_TASK_ID_GENERATOR;
        this.timeTicker = timeTicker;
    }

    public AbstractTimeWheel(TimeTicker timeTicker, TaskIdGenerator idGen) {
        if(timeTicker == null){
            throw new IllegalArgumentException("Illegal timeTicker: null");
        }
        if(idGen == null){
            idGen = UUID_TASK_ID_GENERATOR;
        }
        this.timeTicker = timeTicker;
        this.idGen = idGen;
    }

    /**
     * add schedule task in time wheel
     * @param task schedule task
     * @param delay delay time
     * @param timeUnit time unit
     */
    public abstract boolean addTask0(Runnable task, long delay, TimeUnit timeUnit);

    @Override
    public String addTask(Runnable task, long delay, TimeUnit timeUnit) {
        if (addTask0(task, delay, timeUnit)) {
            return genTaskId();
        }
        return INVALID_TASK_ID;
    }

    @Override
    public String genTaskId() {
        return idGen.generateId();
    }

    /**
     * task id generator
     */
    @FunctionalInterface
    public interface TaskIdGenerator {
        String generateId();
    }
}
