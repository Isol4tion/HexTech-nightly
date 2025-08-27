package me.hextech.remapped;

import me.hextech.remapped.LambdaListener;

/*
 * Exception performing whole class analysis ignored.
 */
private static class EventBus {
    public final LambdaListener factory;

    public EventBus(LambdaListener factory) {
        this.factory = factory;
    }
}
