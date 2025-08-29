package me.hextech.api.events.eventbus;

import java.util.function.Consumer;

public class ConsumerListener<T>
        implements IListener {
    private final Class<?> target;
    private final int priority;
    private final Consumer<T> executor;

    public ConsumerListener(Class<?> target, int priority, Consumer<T> executor) {
        this.target = target;
        this.priority = priority;
        this.executor = executor;
    }

    public ConsumerListener(Class<?> target, Consumer<T> executor) {
        this(target, 0, executor);
    }

    @Override
    public void call(Object event) {
        this.executor.accept((T) event);
    }

    @Override
    public Class<?> getTarget() {
        return this.target;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}
