package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.vehicle.BoatEntity;

public class BoatMoveEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final BoatEntity boat;

    public BoatMoveEvent(BoatEntity boat) {
        super(Event.Pre);
        this.boat = boat;
    }

    public BoatEntity getBoat() {
        return this.boat;
    }
}
