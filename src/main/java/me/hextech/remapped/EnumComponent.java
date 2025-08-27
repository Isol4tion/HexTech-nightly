package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class EnumComponent extends Component {
   private final EnumSetting setting;
   boolean isback;
   private boolean hover = false;

   public EnumComponent(ClickGuiTab parent, EnumSetting enumSetting) {
      this.parent = parent;
      this.setting = enumSetting;
   }

   @Override
   public boolean isVisible() {
      return this.setting.visibility != null ? this.setting.visibility.test(null) : true;
   }

   @Override
   public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
      int parentX = this.parent.getX();
      int parentY = this.parent.getY();
      int parentWidth = this.parent.getWidth();
      if (mouseX >= (double)(parentX + 2)
         && mouseX <= (double)(parentX + parentWidth - 2)
         && mouseY >= (double)(parentY + offset)
         && mouseY <= (double)(parentY + offset + this.defaultHeight - 2)) {
         this.hover = true;
         if (GuiManager.currentGrabbed == null && this.isVisible()) {
            if (mouseClicked) {
               ClickGuiScreen.clicked = false;
               this.setting.increaseEnum();
            }

            if (ClickGuiScreen.rightClicked) {
               this.setting.popped = !this.setting.popped;
               ClickGuiScreen.rightClicked = false;
            }
         }
      } else {
         this.hover = false;
      }

      if (GuiManager.currentGrabbed == null && this.isVisible() && mouseClicked) {
         int cy = parentY + offset - 1 + (this.defaultHeight - 2) - 2;
         if (this.setting.popped) {
            for (Object o : (java.lang.Enum[])this.setting.getValue().getClass().getEnumConstants()) {
               if (mouseX >= (double)parentX
                  && mouseX <= (double)(parentX + parentWidth)
                  && mouseY >= (double)(TextUtil.getHeight() / 2.0F + (float)cy)
                  && mouseY < (double)(TextUtil.getHeight() + TextUtil.getHeight() / 2.0F + (float)cy)) {
                  this.setting.setEnumValue(String.valueOf(o));
                  ClickGuiScreen.clicked = false;
                  break;
               }

               cy = (int)((float)cy + TextUtil.getHeight());
            }
         }
      }
   }

   @Override
   public int getHeight() {
      if (!this.isVisible()) {
         return 0;
      } else if (this.setting.popped && !this.isback) {
         int y = 0;

         for (Object ignored : (java.lang.Enum[])this.setting.getValue().getClass().getEnumConstants()) {
            y = (int)((float)y + TextUtil.getHeight());
         }

         return this.defaultHeight + y;
      } else {
         return this.defaultHeight;
      }
   }

   @Override
   public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
      this.isback = back;
      this.currentOffset = animate(this.currentOffset, (double)offset);
      if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
         return false;
      } else {
         int x = this.parent.getX();
         int y = (int)((double)this.parent.getY() + this.currentOffset - 2.0);
         int width = this.parent.getWidth();
         MatrixStack matrixStack = drawContext.method_51448();
         Render2DUtil.drawRect(
            matrixStack,
            (float)x + 1.0F,
            (float)y + 1.0F,
            (float)width - 2.0F,
            (float)this.defaultHeight - 1.0F,
            this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : me.hextech.HexTech.GUI.getColor()
         );
         TextUtil.drawString(
            drawContext,
            this.setting.getName() + ": " + this.setting.getValue().name(),
            (double)(x + 4),
            (double)y + this.getTextOffsetY(),
            new Color(-1).getRGB()
         );
         TextUtil.drawString(
            drawContext, this.setting.popped ? "-" : "+", (double)(x + width - 11), (double)y + this.getTextOffsetY(), new Color(255, 255, 255).getRGB()
         );
         int cy = (int)((double)this.parent.getY() + this.currentOffset - 1.0 + (double)(this.defaultHeight - 2)) - 2;
         if (this.setting.popped && !back) {
            for (Object o : (java.lang.Enum[])this.setting.getValue().getClass().getEnumConstants()) {
               String s = o.toString();
               TextUtil.drawString(
                  drawContext,
                  s,
                  (double)width / 2.0 - (double)(TextUtil.getWidth(s) / 2.0F) + 2.0 + (double)x,
                  (double)(TextUtil.getHeight() / 2.0F + (float)cy),
                  this.setting.getValue().name().equals(s) ? -1 : new Color(120, 120, 120).getRGB()
               );
               cy = (int)((float)cy + TextUtil.getHeight());
            }
         }

         return true;
      }
   }
}
