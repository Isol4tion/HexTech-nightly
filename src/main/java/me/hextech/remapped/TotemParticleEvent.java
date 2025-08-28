package me.hextech.remapped;

import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;

import java.awt.Color;

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
