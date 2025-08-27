package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;

public abstract class Component implements Wrapper {
   public int defaultHeight = 16;
   public double currentOffset = 0.0;
   protected ClickGuiTab parent;
   private int height = this.defaultHeight;

   public static double animate(double current, double endPoint) {
      return animate(current, endPoint, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.animationSpeed.getValue());
   }

   public static double animate(double current, double endPoint, double speed) {
      if (speed >= 1.0) {
         return endPoint;
      } else {
         return speed == 0.0
            ? current
            : AnimateUtil.animate(current, endPoint, speed, (AnimateUtil_AcLZzRdHWZkNeKEYTOwI)ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.animMode.getValue());
      }
   }

   public boolean isVisible() {
      return true;
   }

   public int getHeight() {
      return !this.isVisible() ? 0 : this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public ClickGuiTab getParent() {
      return this.parent;
   }

   public void setParent(ClickGuiTab parent) {
      this.parent = parent;
   }

   public abstract void update(int var1, double var2, double var4, boolean var6);

   public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
      this.currentOffset = (double)offset;
      return false;
   }

   public double getTextOffsetY() {
      return (double)(this.defaultHeight - 9) / 2.0 + 1.0;
   }
}
