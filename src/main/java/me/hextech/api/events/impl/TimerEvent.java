package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;

public class TimerEvent
        extends Event_auduwKaxKOWXRtyJkCPb {
    private float timer = 1.0f;
    private boolean modified;

    public TimerEvent() {
        super(Stage.Pre);
    }

    public float get() {
        return this.timer;
    }

    public void set(float timer) {
        this.modified = true;
        this.timer = timer;
    }

    public boolean isModified() {
        return this.modified;
    }
}
