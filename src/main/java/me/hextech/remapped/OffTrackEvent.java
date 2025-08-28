package me.hextech.remapped;

import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.util.math.Vec3d;

public class OffTrackEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public float priority = 0.0f;
    private Vec3d target;
    private float yaw;
    private float pitch;
    private boolean rotation;
    private float speed;

    public OffTrackEvent() {
        super(Stage.Pre);
    }

    public Vec3d getTarget() {
        return this.target;
    }

    public float getSpeed() {
        return this.speed;
    }

    public boolean getRotation() {
        return this.rotation;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setTarget(Vec3d target, float speed, float priority) {
        if (priority >= this.priority) {
            this.rotation = false;
            this.priority = priority;
            this.target = target;
            this.speed = speed;
        }
    }

    public void setRotation(float yaw, float pitch, float speed, float priority) {
        if (priority >= this.priority) {
            this.rotation = true;
            this.priority = priority;
            this.yaw = yaw;
            this.pitch = pitch;
            this.speed = speed;
        }
    }
}
