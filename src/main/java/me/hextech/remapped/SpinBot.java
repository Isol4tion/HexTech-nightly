package me.hextech.remapped;

import net.minecraft.item.BowItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;

public class SpinBot extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public final SliderSetting yawDelta = this.add(new SliderSetting("YawDelta", 60, -360, 360));
   public final SliderSetting pitchDelta = this.add(new SliderSetting("PitchDelta", 10, -90, 90));
   public final BooleanSetting allowInteract = this.add(new BooleanSetting("AllowInteract", true));
   private final EnumSetting<SpinBot_YiToqkCkUTOMQxneHmRR> pitchMode = this.add(new EnumSetting("PitchMode", SpinBot_YiToqkCkUTOMQxneHmRR.None));
   private final EnumSetting<SpinBot_YiToqkCkUTOMQxneHmRR> yawMode = this.add(new EnumSetting("YawMode", SpinBot_YiToqkCkUTOMQxneHmRR.None));
   private float rotationYaw;
   private float rotationPitch;

   public SpinBot() {
      super("SpinBot", "fun", Module_JlagirAibYQgkHtbRnhw.Player);
   }

   @EventHandler
   public void onPacket(PacketEvent event) {
      if (event.getPacket() instanceof PlayerActionC2SPacket packet
         && packet.method_12363() == Action.field_12974
         && mc.field_1724.method_6030().method_7909() instanceof BowItem) {
         EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), mc.field_1724.method_36455());
      }
   }

   @EventHandler(
      priority = 200
   )
   public void onUpdateWalkingPlayerPre(RotateEvent event) {
      if (this.pitchMode.getValue() == SpinBot_YiToqkCkUTOMQxneHmRR.RandomAngle) {
         this.rotationPitch = MathUtil.random(90.0F, -90.0F);
      }

      if (this.yawMode.getValue() == SpinBot_YiToqkCkUTOMQxneHmRR.RandomAngle) {
         this.rotationYaw = MathUtil.random(0.0F, 360.0F);
      }

      if (this.yawMode.getValue() == SpinBot_YiToqkCkUTOMQxneHmRR.Spin) {
         this.rotationYaw = (float)((double)this.rotationYaw + this.yawDelta.getValue());
      }

      if (this.rotationYaw > 360.0F) {
         this.rotationYaw = 0.0F;
      }

      if (this.rotationYaw < 0.0F) {
         this.rotationYaw = 360.0F;
      }

      if (this.pitchMode.getValue() == SpinBot_YiToqkCkUTOMQxneHmRR.Spin) {
         this.rotationPitch = (float)((double)this.rotationPitch + this.pitchDelta.getValue());
      }

      if (this.rotationPitch > 90.0F) {
         this.rotationPitch = -90.0F;
      }

      if (this.rotationPitch < -90.0F) {
         this.rotationPitch = 90.0F;
      }

      if (this.pitchMode.getValue() == SpinBot_YiToqkCkUTOMQxneHmRR.Static) {
         this.rotationPitch = mc.field_1724.method_36455() + this.pitchDelta.getValueFloat();
         this.rotationPitch = MathUtil.clamp(this.rotationPitch, -90.0F, 90.0F);
      }

      if (this.yawMode.getValue() == SpinBot_YiToqkCkUTOMQxneHmRR.Static) {
         this.rotationYaw = mc.field_1724.method_36454() % 360.0F + this.yawDelta.getValueFloat();
      }

      if (!this.allowInteract.getValue() || (!mc.field_1690.field_1904.method_1434() || EntityUtil.isUsing()) && !mc.field_1690.field_1886.method_1434()) {
         if (this.yawMode.getValue() != SpinBot_YiToqkCkUTOMQxneHmRR.None) {
            event.setYaw(this.rotationYaw);
         }

         if (this.pitchMode.getValue() != SpinBot_YiToqkCkUTOMQxneHmRR.None) {
            event.setPitch(this.rotationPitch);
         }
      }
   }
}
