package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class BooleanComponent extends Component {
   final BooleanSetting setting;
   public double currentWidth = 0.0;
   boolean hover = false;

   public BooleanComponent(ClickGuiTab parent, BooleanSetting setting) {
      this.parent = parent;
      this.setting = setting;
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
      if (GuiManager.currentGrabbed == null
         && this.isVisible()
         && mouseX >= (double)(parentX + 1)
         && mouseX <= (double)(parentX + parentWidth - 1)
         && mouseY >= (double)(parentY + offset)
         && mouseY <= (double)(parentY + offset + this.defaultHeight - 2)) {
         this.hover = true;
         if (mouseClicked) {
            ClickGuiScreen.clicked = false;
            this.setting.toggleValue();
         }

         if (ClickGuiScreen.rightClicked) {
            ClickGuiScreen.rightClicked = false;
            this.setting.popped = !this.setting.popped;
         }
      } else {
         this.hover = false;
      }
   }

   @Override
   public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
      this.currentOffset = animate(this.currentOffset, (double)offset);
      if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
         this.currentWidth = 0.0;
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
            this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.sbgColor.getValue()
         );
         this.currentWidth = animate(
            this.currentWidth, this.setting.getValue() ? (double)width - 2.0 : 0.0, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.booleanSpeed.getValue()
         );
         switch ((ClickGui_qvrpuTqAQSFqXVuTIwHB)ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.uiType.getValue()) {
            case New:
               TextUtil.drawString(
                  drawContext,
                  this.setting.getName(),
                  (double)(x + 4),
                  (double)y + this.getTextOffsetY(),
                  this.setting.getValue()
                     ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.enableTextS.getValue()
                     : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.disableText.getValue()
               );
               break;
            case Old:
               if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.booleanValue) {
                  Render2DUtil.drawRectHorizontal(
                     matrixStack,
                     (float)x + 1.0F,
                     (float)y + 1.0F,
                     (float)this.currentWidth,
                     (float)this.defaultHeight - (float)(ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 0 : 1),
                     this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color,
                     ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.getValue()
                  );
               } else {
                  Render2DUtil.drawRect(
                     matrixStack,
                     (float)x + 1.0F,
                     (float)y + 1.0F,
                     (float)this.currentWidth,
                     (float)this.defaultHeight - (float)(ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 0 : 1),
                     this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color
                  );
               }

               TextUtil.drawString(drawContext, this.setting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), new Color(-1).getRGB());
         }

         if (this.setting.parent) {
            TextUtil.drawString(
               drawContext, this.setting.popped ? "-" : "+", (double)(x + width - 11), (double)y + this.getTextOffsetY(), new Color(255, 255, 255).getRGB()
            );
         }

         return true;
      }
   }
}
