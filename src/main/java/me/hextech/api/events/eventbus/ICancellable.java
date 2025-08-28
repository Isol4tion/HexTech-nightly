package me.hextech.api.events.eventbus;

public interface ICancellable {
    default void cancel() {
        this.setCancelled(true);
    }

    boolean isCancelled();

    void setCancelled(boolean var1);
}
