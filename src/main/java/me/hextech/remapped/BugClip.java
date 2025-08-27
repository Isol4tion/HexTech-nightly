package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class BugClip extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static BugClip INSTANCE;
   final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500));
   final Timer timer = new Timer();
   private final BooleanSetting clipIn = this.add(new BooleanSetting("ClipIn", true));
   boolean cancelPacket = true;

   public BugClip() {
      super("BugClip", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   @Override
   public void onEnable() {
      this.cancelPacket = false;
      if (this.clipIn.getValue()) {
         Direction f = mc.field_1724.method_5735();
         mc.field_1724
            .method_5814(
               mc.field_1724.method_23317() + (double)f.method_10148() * 0.5,
               mc.field_1724.method_23318(),
               mc.field_1724.method_23321() + (double)f.method_10165() * 0.5
            );
         mc.field_1724
            .field_3944
            .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
      } else {
         mc.field_1724
            .field_3944
            .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
         mc.field_1724
            .method_5814(
               this.roundToClosest(
                  mc.field_1724.method_23317(), Math.floor(mc.field_1724.method_23317()) + 0.23, Math.floor(mc.field_1724.method_23317()) + 0.77
               ),
               mc.field_1724.method_23318(),
               this.roundToClosest(
                  mc.field_1724.method_23321(), Math.floor(mc.field_1724.method_23321()) + 0.23, Math.floor(mc.field_1724.method_23321()) + 0.77
               )
            );
         mc.field_1724
            .field_3944
            .method_52787(
               new PositionAndOnGround(
                  this.roundToClosest(
                     mc.field_1724.method_23317(), Math.floor(mc.field_1724.method_23317()) + 0.23, Math.floor(mc.field_1724.method_23317()) + 0.77
                  ),
                  mc.field_1724.method_23318(),
                  this.roundToClosest(
                     mc.field_1724.method_23321(), Math.floor(mc.field_1724.method_23321()) + 0.23, Math.floor(mc.field_1724.method_23321()) + 0.77
                  ),
                  true
               )
            );
      }

      this.cancelPacket = true;
   }

   private double roundToClosest(double num, double low, double high) {
      double d1 = num - low;
      double d2 = high - num;
      return d2 > d1 ? low : high;
   }

   @Override
   public void onUpdate() {
      if (!this.insideBurrow()) {
         this.disable();
      }
   }

   @EventHandler
   public void onPacket(PacketEvent event) {
      if (!nullCheck()) {
         if (this.cancelPacket && event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (!this.insideBurrow()) {
               this.disable();
               return;
            }

            if (packet.method_36172()) {
               float packetYaw = packet.method_12271(0.0F);
               float packetPitch = packet.method_12270(0.0F);
               if (this.timer.passedMs(this.delay.getValue())) {
                  this.cancelPacket = false;
                  mc.field_1724
                     .field_3944
                     .method_52787(
                        new Full(
                           mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1337.0, mc.field_1724.method_23321(), packetYaw, packetPitch, false
                        )
                     );
                  this.cancelPacket = true;
                  this.timer.reset();
               }
            }

            event.cancel();
         }
      }
   }

   public boolean insideBurrow() {
      BlockPos playerBlockPos = EntityUtil.getPlayerPos(true);

      for (int xOffset = -1; xOffset <= 1; xOffset++) {
         for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int zOffset = -1; zOffset <= 1; zOffset++) {
               BlockPos offsetPos = playerBlockPos.method_10069(xOffset, yOffset, zOffset);
               if (mc.field_1687.method_8320(offsetPos).method_26204() == Blocks.field_9987 && mc.field_1724.method_5829().method_994(new Box(offsetPos))) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
