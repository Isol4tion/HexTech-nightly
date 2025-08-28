package me.hextech.api.managers;

import me.hextech.mod.modules.impl.misc.IncreasesTime;

public class TimerManager {
    public float timer = 1.0f;
    public float lastTime;

    public void set(float factor) {
        if (factor < 0.1f) {
            factor = 0.1f;
        }
        this.timer = factor;
    }

    public void reset() {
        this.lastTime = this.timer = this.getDefault();
    }

    public void tryReset() {
        if (this.lastTime != this.getDefault()) {
            this.reset();
        }
    }

    public boolean passedS(double s) {
        return this.passedMs((long)s * 1000L);
    }

    public boolean passedMs(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public boolean passedMs(double ms) {
        return this.passedMs((long)ms);
    }

    public boolean passed(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public boolean passed(double ms) {
        return this.passedMs((long)ms);
    }

    public void setMs(long ms) {
        this.timer = System.nanoTime() - this.convertToNS(ms);
    }

    public boolean passedNS(long ns) {
        return (float)System.nanoTime() - this.timer >= (float)ns;
    }

    public long getPassedTimeMs() {
        return this.getMs((long)((float)System.nanoTime() - this.timer));
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }

    public float get() {
        return this.timer;
    }

    public float getDefault() {
        return IncreasesTime.INSTANCE.isOn() ? (IncreasesTime.INSTANCE.activekey.isPressed() ? IncreasesTime.INSTANCE.active.getValueFloat() : IncreasesTime.INSTANCE.multiplier.getValueFloat()) : 1.0f;
    }
}
