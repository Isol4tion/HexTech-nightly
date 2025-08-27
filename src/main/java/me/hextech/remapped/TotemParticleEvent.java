package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;

public class TotemParticleEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public double velocityX;
    public double velocityY;
    public double velocityZ;
    public Color color;

    public TotemParticleEvent(double velocityX, double velocityY, double velocityZ) {
        super(Event.Pre);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }
}
