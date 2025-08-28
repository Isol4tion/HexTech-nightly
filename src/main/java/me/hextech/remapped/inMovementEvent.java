package me.hextech.remapped;

import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;

public class inMovementEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private float yaw;
    private float pitch;

    public inMovementEvent(float yaw, float pitch) {
        super(Stage.Pre);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRotation(float yaw, float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);
    }
}
