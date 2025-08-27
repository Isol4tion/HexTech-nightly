package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Crosshair extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Crosshair INSTANCE;
   public final SliderSetting length = this.add(new SliderSetting("Length", 5.0, 0.0, 20.0, 0.1));
   public final SliderSetting thickness = this.add(new SliderSetting("Thickness", 2.0, 0.0, 20.0, 0.1));
   public final SliderSetting interval = this.add(new SliderSetting("Interval", 2.0, 0.0, 20.0, 0.1));
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));

   public Crosshair() {
      super("Crosshair", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   public void draw(DrawContext context) {
      MatrixStack matrixStack = context.method_51448();
      float centerX = (float)mc.method_22683().method_4486() / 2.0F;
      float centerY = (float)mc.method_22683().method_4502() / 2.0F;
      Render2DUtil.drawRect(
         matrixStack,
         centerX - this.thickness.getValueFloat() / 2.0F,
         centerY - this.length.getValueFloat() - this.interval.getValueFloat(),
         this.thickness.getValueFloat(),
         this.length.getValueFloat(),
         this.color.getValue()
      );
      Render2DUtil.drawRect(
         matrixStack,
         centerX - this.thickness.getValueFloat() / 2.0F,
         centerY + this.interval.getValueFloat(),
         this.thickness.getValueFloat(),
         this.length.getValueFloat(),
         this.color.getValue()
      );
      Render2DUtil.drawRect(
         matrixStack,
         centerX + this.interval.getValueFloat(),
         centerY - this.thickness.getValueFloat() / 2.0F,
         this.length.getValueFloat(),
         this.thickness.getValueFloat(),
         this.color.getValue()
      );
      Render2DUtil.drawRect(
         matrixStack,
         centerX - this.interval.getValueFloat() - this.length.getValueFloat(),
         centerY - this.thickness.getValueFloat() / 2.0F,
         this.length.getValueFloat(),
         this.thickness.getValueFloat(),
         this.color.getValue()
      );
   }
}
