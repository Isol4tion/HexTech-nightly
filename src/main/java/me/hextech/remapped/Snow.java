package me.hextech.remapped;

import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Snow {
   private int x;
   private int y;
   private int fallingSpeed;
   private int size;

   public Snow(int x, int y, int fallingSpeed, int size) {
      this.x = x;
      this.y = y;
      this.fallingSpeed = fallingSpeed;
      this.size = size;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int _y) {
      this.y = _y;
   }

   public void drawSnow(DrawContext drawContext) {
      Render2DUtil.drawRect(drawContext.method_51448(), (float)this.getX(), (float)this.getY(), (float)this.size, (float)this.size, -1714829883);
      this.setY(this.getY() + this.fallingSpeed);
      if (this.getY() > MinecraftClient.method_1551().method_22683().method_4502() + 10 || this.getY() < -10) {
         this.setY(-10);
         Random rand = new Random();
         this.fallingSpeed = rand.nextInt(10) + 1;
         this.size = rand.nextInt(4) + 1;
      }
   }
}
