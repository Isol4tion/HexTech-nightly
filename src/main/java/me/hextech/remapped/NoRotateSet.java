package me.hextech.remapped;

import me.hextech.asm.accessors.IPlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class NoRotateSet extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static NoRotateSet INSTANCE;
   public final BooleanSetting rotate = this.add(new BooleanSetting("Apply", false));
   public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 50, 0, 100));
   private final Timer lagTimer = new Timer();

   public NoRotateSet() {
      super("NoRotateSet", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   @EventHandler
   public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (!nullCheck()) {
         if (this.lagTimer.passedMs((long)this.lagTime.getValueInt())) {
            if (!this.rotate.getValue() && event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
               if (packet.method_11733().contains(PositionFlag.field_12401)) {
                  ((IPlayerPositionLookS2CPacket)packet).setYaw(0.0F);
               } else {
                  ((IPlayerPositionLookS2CPacket)packet).setYaw(mc.field_1724.method_36454());
               }

               if (packet.method_11733().contains(PositionFlag.field_12397)) {
                  ((IPlayerPositionLookS2CPacket)packet).setPitch(0.0F);
               } else {
                  ((IPlayerPositionLookS2CPacket)packet).setPitch(mc.field_1724.method_36455());
               }

               if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                  this.lagTimer.reset();
               }
            }
         }
      }
   }
}
