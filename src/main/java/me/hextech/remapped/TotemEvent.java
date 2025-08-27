package me.hextech.remapped;

import net.minecraft.entity.player.PlayerEntity;

public class TotemEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private final PlayerEntity player;

   public TotemEvent(PlayerEntity player) {
      super(Event.Post);
      this.player = player;
   }

   public PlayerEntity getPlayer() {
      return this.player;
   }
}
