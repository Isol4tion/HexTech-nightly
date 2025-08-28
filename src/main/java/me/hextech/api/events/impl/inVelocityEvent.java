package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.util.math.Vec3d;

public class inVelocityEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    Vec3d movementInput;
    float speed;
    float yaw;
    Vec3d velocity;

    public inVelocityEvent(Vec3d movementInput, float speed, float yaw, Vec3d velocity) {
        super(Stage.Pre);
        this.movementInput = movementInput;
        this.speed = speed;
        this.yaw = yaw;
        this.velocity = velocity;
    }

    public Vec3d getMovementInput() {
        return this.movementInput;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Vec3d getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }
}
