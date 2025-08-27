package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.player.PlayerEntity;

public class TravelEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final PlayerEntity entity;

    public TravelEvent(Event stage, PlayerEntity entity) {
        super(stage);
        this.entity = entity;
    }

    public PlayerEntity getEntity() {
        return this.entity;
    }
}
