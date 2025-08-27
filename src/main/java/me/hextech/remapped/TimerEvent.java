package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;

public class TimerEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private float timer = 1.0f;
    private boolean modified;

    public TimerEvent() {
        super(Event.Pre);
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
