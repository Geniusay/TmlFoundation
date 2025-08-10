package io.github.timemachinelab.util.rmq;

public interface RMQ<V> {

    V max(int l, int r);

    V min(int l, int r);

    @FunctionalInterface
    interface ValueCalculation<T, V>{
        V calculate(T t);
    }
}
