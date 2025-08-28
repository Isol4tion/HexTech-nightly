package me.hextech.remapped.api.events;

public class Event_auduwKaxKOWXRtyJkCPb {
    private final Stage stage;
    private boolean cancel = false;

    public Event_auduwKaxKOWXRtyJkCPb(Stage stage) {
        this.stage = stage;
    }

    public void cancel() {
        this.setCancelled(true);
    }

    public boolean isCancel() {
        return this.cancel;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Stage getStage() {
        return this.stage;
    }

    public boolean isPost() {
        return this.stage == Stage.Post;
    }

    public boolean isPre() {
        return this.stage == Stage.Pre;
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Stage {
        Pre,
        Post

    }
}
