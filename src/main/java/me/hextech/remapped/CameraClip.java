package me.hextech.remapped;

import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;

public class CameraClip extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static CameraClip INSTANCE;
   public final SliderSetting distance = this.add(new SliderSetting("Distance", 4.0, 1.0, 20.0));
   public final SliderSetting animateTime = this.add(new SliderSetting("AnimationTime", 200, 0, 1000));
   private final BooleanSetting noFront = this.add(new BooleanSetting("NoFront", false));
   private final FadeUtils_DPfHthPqEJdfXfNYhDbG animation = new FadeUtils_DPfHthPqEJdfXfNYhDbG(300L);
   boolean first = false;

   public CameraClip() {
      super("CameraClip", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (mc.field_1690.method_31044() == Perspective.field_26666 && this.noFront.getValue()) {
         mc.field_1690.method_31043(Perspective.field_26664);
      }

      this.animation.setLength((long)this.animateTime.getValueInt());
      if (mc.field_1690.method_31044() == Perspective.field_26664) {
         if (!this.first) {
            this.first = true;
            this.animation.reset();
         }
      } else if (this.first) {
         this.first = false;
         this.animation.reset();
      }
   }

   public double getDistance() {
      double quad = mc.field_1690.method_31044() == Perspective.field_26664 ? 1.0 - this.animation.easeOutQuad() : this.animation.easeOutQuad();
      return 1.0 + (this.distance.getValue() - 1.0) * quad;
   }
}
