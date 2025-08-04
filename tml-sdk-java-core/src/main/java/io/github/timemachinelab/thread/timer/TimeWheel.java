package io.github.timemachinelab.thread.timer;

import java.util.concurrent.TimeUnit;

public interface TimeWheel {

    String genTaskId();

    String addTask(Runnable task, long delay, TimeUnit timeUnit);

    boolean removeTask(String taskId);
}
