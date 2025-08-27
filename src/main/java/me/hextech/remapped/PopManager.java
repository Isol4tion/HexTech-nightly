package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class PopManager implements Wrapper {
   public final HashMap<String, Integer> popContainer = new HashMap();
   public final ArrayList<PlayerEntity> deadPlayer = new ArrayList();

   public PopManager() {
      me.hextech.HexTech.EVENT_BUS.subscribe(this);
   }

   public Integer getPop(String s) {
      return (Integer)this.popContainer.getOrDefault(s, 0);
   }

   public void update() {
      if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
         for (PlayerEntity player : mc.field_1687.method_18456()) {
            if (player == null || player.method_5805()) {
               this.deadPlayer.remove(player);
            } else if (!this.deadPlayer.contains(player)) {
               me.hextech.HexTech.EVENT_BUS.post(new DeathEvent(player));
               this.onDeath(player);
               this.deadPlayer.add(player);
            }
         }
      }
   }

   @EventHandler
   public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
         if (event.getPacket() instanceof EntityStatusS2CPacket packet
            && packet.method_11470() == 35
            && packet.method_11469(mc.field_1687) instanceof PlayerEntity player) {
            this.onTotemPop(player);
         }
      }
   }

   public void onDeath(PlayerEntity player) {
      this.popContainer.remove(player.method_5477().getString());
   }

   public void onTotemPop(PlayerEntity player) {
      int l_Count = 1;
      if (this.popContainer.containsKey(player.method_5477().getString())) {
         l_Count = (Integer)this.popContainer.get(player.method_5477().getString());
         this.popContainer.put(player.method_5477().getString(), ++l_Count);
      } else {
         this.popContainer.put(player.method_5477().getString(), l_Count);
      }

      me.hextech.HexTech.EVENT_BUS.post(new TotemEvent(player));
   }
}
