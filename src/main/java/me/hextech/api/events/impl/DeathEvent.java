package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.player.PlayerEntity;

public class DeathEvent
        extends Event_auduwKaxKOWXRtyJkCPb {
    private final PlayerEntity player;

    public DeathEvent(PlayerEntity player) {
        super(Stage.Post);
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
}
