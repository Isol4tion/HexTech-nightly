package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.player.PlayerEntity;

public class TotemEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final PlayerEntity player;

    public TotemEvent(PlayerEntity player) {
        super(Event.Post);
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
}
