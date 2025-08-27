package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class StringComponent extends Component {
   private final StringSetting setting;
   private final Timer timer = new Timer();
   boolean hover = false;
   boolean b;

   public StringComponent(ClickGuiTab parent, StringSetting setting) {
      this.setting = setting;
      this.parent = parent;
   }

   @Override
   public boolean isVisible() {
      return this.setting.visibility != null ? this.setting.visibility.test(null) : true;
   }

   @Override
   public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
      if (GuiManager.currentGrabbed == null && this.isVisible()) {
         int parentX = this.parent.getX();
         int parentY = this.parent.getY();
         int parentWidth = this.parent.getWidth();
         if (mouseX >= (double)(parentX + 1)
            && mouseX <= (double)(parentX + parentWidth - 1)
            && mouseY >= (double)(parentY + offset)
            && mouseY <= (double)(parentY + offset + this.defaultHeight - 2)) {
            this.hover = true;
            if (mouseClicked) {
               ClickGuiScreen.clicked = false;
               this.setting.setListening(!this.setting.isListening());
            }
         } else {
            if (mouseClicked && this.setting.isListening()) {
               this.setting.setListening(false);
            }

            this.hover = false;
         }
      } else {
         if (this.setting.isListening()) {
            this.setting.setListening(false);
         }

         this.hover = false;
      }
   }

   @Override
   public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
      if (this.timer.passed(1000L)) {
         this.b = !this.b;
         this.timer.reset();
      }

      if (back) {
         this.setting.setListening(false);
      }

      int parentX = this.parent.getX();
      int parentY = this.parent.getY();
      this.currentOffset = animate(this.currentOffset, (double)offset);
      if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
         return false;
      } else {
         int y = (int)((double)this.parent.getY() + this.currentOffset - 2.0);
         int width = this.parent.getWidth();
         MatrixStack matrixStack = drawContext.method_51448();
         String text = this.setting.getValue();
         if (this.setting.isListening() && this.b) {
            text = text + "_";
         }

         String name = this.setting.isListening() ? "[E]" : this.setting.getName();
         if (this.hover) {
            Render2DUtil.drawRect(
               matrixStack,
               (float)parentX + 1.0F,
               (float)y + 1.0F,
               (float)width - 3.0F,
               (float)this.defaultHeight - 1.0F,
               ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue()
            );
         }

         TextUtil.drawString(
            drawContext,
            text,
            (double)((float)(parentX + 4) + TextUtil.getWidth(name) / 2.0F),
            (double)((float)((double)parentY + this.getTextOffsetY() + this.currentOffset) - 2.0F),
            16777215
         );
         TextUtil.drawStringWithScale(
            drawContext, name, (float)(parentX + 4), (float)((double)parentY + this.getTextOffsetY() + this.currentOffset - 2.0), -1, 0.5F
         );
         return true;
      }
   }
}
