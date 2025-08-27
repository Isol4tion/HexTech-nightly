package me.hextech.remapped;

import net.minecraft.client.util.math.MatrixStack;

public class FreeCam extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static FreeCam INSTANCE;
   final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
   private final SliderSetting speed = this.add(new SliderSetting("HSpeed", 1.0, 0.0, 3.0));
   private final SliderSetting hspeed = this.add(new SliderSetting("VSpeed", 0.42, 0.0, 3.0));
   private float fakeYaw;
   private float fakePitch;
   private float prevFakeYaw;
   private float prevFakePitch;
   private double fakeX;
   private double fakeY;
   private double fakeZ;
   private double prevFakeX;
   private double prevFakeY;
   private double prevFakeZ;
   private float preYaw;
   private float prePitch;

   public FreeCam() {
      super("FreeCam", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   @Override
   public void onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         mc.field_1730 = false;
         this.preYaw = mc.field_1724.method_36454();
         this.prePitch = mc.field_1724.method_36455();
         this.fakePitch = mc.field_1724.method_36455();
         this.fakeYaw = mc.field_1724.method_36454();
         this.prevFakePitch = this.fakePitch;
         this.prevFakeYaw = this.fakeYaw;
         this.fakeX = mc.field_1724.method_23317();
         this.fakeY = mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376());
         this.fakeZ = mc.field_1724.method_23321();
         this.prevFakeX = this.fakeX;
         this.prevFakeY = this.fakeY;
         this.prevFakeZ = this.fakeZ;
      }
   }

   @Override
   public void onDisable() {
      mc.field_1730 = true;
   }

   @Override
   public void onUpdate() {
      if (this.rotate.getValue() && mc.field_1765 != null && mc.field_1765.method_17784() != null) {
         float[] angle = EntityUtil.getLegitRotations(mc.field_1765.method_17784());
         this.preYaw = angle[0];
         this.prePitch = angle[1];
      }
   }

   @EventHandler(
      priority = 200
   )
   public void onRotate(RotateEvent event) {
      event.setYawNoModify(this.preYaw);
      event.setPitchNoModify(this.prePitch);
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      this.prevFakeYaw = this.fakeYaw;
      this.prevFakePitch = this.fakePitch;
      this.fakeYaw = mc.field_1724.method_36454();
      this.fakePitch = mc.field_1724.method_36455();
   }

   @EventHandler
   public void onKeyboardInput(KeyboardInputEvent event) {
      if (mc.field_1724 != null) {
         double[] motion = MovementUtil.directionSpeed(this.speed.getValue());
         this.prevFakeX = this.fakeX;
         this.prevFakeY = this.fakeY;
         this.prevFakeZ = this.fakeZ;
         this.fakeX = this.fakeX + motion[0];
         this.fakeZ = this.fakeZ + motion[1];
         if (mc.field_1690.field_1903.method_1434()) {
            this.fakeY = this.fakeY + this.hspeed.getValue();
         }

         if (mc.field_1690.field_1832.method_1434()) {
            this.fakeY = this.fakeY - this.hspeed.getValue();
         }

         mc.field_1724.field_3913.field_3905 = 0.0F;
         mc.field_1724.field_3913.field_3907 = 0.0F;
         mc.field_1724.field_3913.field_3904 = false;
         mc.field_1724.field_3913.field_3903 = false;
      }
   }

   public float getFakeYaw() {
      return (float)MathUtil.interpolate((double)this.prevFakeYaw, (double)this.fakeYaw, mc.method_1488());
   }

   public float getFakePitch() {
      return (float)MathUtil.interpolate((double)this.prevFakePitch, (double)this.fakePitch, mc.method_1488());
   }

   public double getFakeX() {
      return MathUtil.interpolate(this.prevFakeX, this.fakeX, mc.method_1488());
   }

   public double getFakeY() {
      return MathUtil.interpolate(this.prevFakeY, this.fakeY, mc.method_1488());
   }

   public double getFakeZ() {
      return MathUtil.interpolate(this.prevFakeZ, this.fakeZ, mc.method_1488());
   }
}
