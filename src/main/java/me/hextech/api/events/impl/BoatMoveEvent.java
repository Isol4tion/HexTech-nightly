package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.vehicle.BoatEntity;

public class BoatMoveEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final BoatEntity boat;

    public BoatMoveEvent(BoatEntity boat) {
        super(Stage.Pre);
        this.boat = boat;
    }

    public BoatEntity getBoat() {
        return this.boat;
    }
}
