package me.hextech.remapped;

public interface ICancellable {
    default public void cancel() {
        this.setCancelled(true);
    }

    public boolean isCancelled();

    public void setCancelled(boolean var1);
}
