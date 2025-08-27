package me.hextech.remapped;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class PacketControl extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static PacketControl INSTANCE;
   private final SliderSetting fullPackets = this.add(new SliderSetting("FullPackets", 15, 1, 50));
   private final SliderSetting positionPackets = this.add(new SliderSetting("PositionPackets", 15, 1, 50));
   public boolean full;
   private int fullPacket;
   private int positionPacket;

   public PacketControl() {
      super("PacketControl", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   @EventHandler
   public final void onPacketSend(PacketEvent event) {
      if (event.getPacket() instanceof PositionAndOnGround && !this.full) {
         this.positionPacket++;
         if ((double)this.positionPacket >= this.positionPackets.getValue()) {
            this.positionPacket = 0;
            this.full = true;
         }
      } else if (event.getPacket() instanceof Full && this.full) {
         this.fullPacket++;
         if ((double)this.fullPacket > this.fullPackets.getValue()) {
            this.fullPacket = 0;
            this.full = false;
         }
      }
   }
}
