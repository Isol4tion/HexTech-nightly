package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;

import java.awt.*;

public class TotemParticleEvent
        extends Event_auduwKaxKOWXRtyJkCPb {
    public double velocityX;
    public double velocityY;
    public double velocityZ;
    public Color color;

    public TotemParticleEvent(double velocityX, double velocityY, double velocityZ) {
        super(Stage.Pre);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }
}
