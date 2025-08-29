package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.player.PlayerEntity;

public class TravelEvent
        extends Event_auduwKaxKOWXRtyJkCPb {
    private final PlayerEntity entity;

    public TravelEvent(Stage stage, PlayerEntity entity) {
        super(stage);
        this.entity = entity;
    }

    public PlayerEntity getEntity() {
        return this.entity;
    }
}
