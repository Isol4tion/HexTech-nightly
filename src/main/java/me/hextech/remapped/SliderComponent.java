package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class SliderComponent extends Component {
   final SliderSetting setting;
   private final ClickGuiTab parent;
   private final Timer timer = new Timer();
   public double renderSliderPosition = 0.0;
   boolean b;
   private double currentSliderPosition;
   private boolean clicked = false;
   private boolean hover = false;
   private boolean firstUpdate = true;

   public SliderComponent(ClickGuiTab parent, SliderSetting setting) {
      this.parent = parent;
      this.setting = setting;
   }

   @Override
   public boolean isVisible() {
      return this.setting.visibility != null ? this.setting.visibility.test(null) : true;
   }

   @Override
   public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
      if (this.firstUpdate) {
         this.currentSliderPosition = (double)((float)((this.setting.getValue() - this.setting.getMinimum()) / this.setting.getRange()));
         this.firstUpdate = false;
      }

      int parentX = this.parent.getX();
      int parentY = this.parent.getY();
      int parentWidth = this.parent.getWidth();
      if (mouseX >= (double)parentX
         && mouseX <= (double)(parentX + parentWidth - 2)
         && mouseY >= (double)(parentY + offset)
         && mouseY <= (double)(parentY + offset + this.defaultHeight - 2)) {
         this.hover = true;
         if (GuiManager.currentGrabbed == null && this.isVisible()) {
            if (ClickGuiScreen.clicked) {
            }

            if (ClickGuiScreen.clicked || ClickGuiScreen.hoverClicked && this.clicked) {
               if (this.setting.isListening()) {
                  this.setting.setListening(false);
                  ClickGuiScreen.clicked = false;
               } else {
                  this.clicked = true;
                  ClickGuiScreen.hoverClicked = true;
                  ClickGuiScreen.clicked = false;
                  this.currentSliderPosition = (double)((float)Math.min((mouseX - (double)parentX) / (double)(parentWidth - 4), 1.0));
                  this.currentSliderPosition = Math.max(0.0, this.currentSliderPosition);
                  this.setting.setValue(this.currentSliderPosition * this.setting.getRange() + this.setting.getMinimum());
               }
            }

            if (ClickGuiScreen.rightClicked) {
               this.setting.setListening(!this.setting.isListening());
               ClickGuiScreen.rightClicked = false;
            }
         }
      } else {
         this.clicked = false;
         this.hover = false;
      }
   }

   @Override
   public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
      if (back) {
         this.setting.setListening(false);
      }

      int parentX = this.parent.getX();
      int parentY = this.parent.getY();
      int parentWidth = this.parent.getWidth();
      MatrixStack matrixStack = drawContext.method_51448();
      this.currentOffset = animate(this.currentOffset, (double)offset);
      if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
         this.renderSliderPosition = 0.0;
         return false;
      } else {
         this.renderSliderPosition = animate(
            this.renderSliderPosition,
            Math.floor((double)(parentWidth - 2) * this.currentSliderPosition),
            ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.sliderSpeed.getValue()
         );
         float height = ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.uiType.getValue() == ClickGui_qvrpuTqAQSFqXVuTIwHB.New
            ? 1.0F
            : (float)(this.defaultHeight - (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 0 : 1));
         float y = ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.uiType.getValue() == ClickGui_qvrpuTqAQSFqXVuTIwHB.New
            ? (float)(parentY + offset + this.defaultHeight - 3)
            : (float)(parentY + offset - 1);
         if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.booleanValue) {
            Render2DUtil.drawRectHorizontal(
               matrixStack,
               (float)(parentX + 1),
               y,
               (float)((int)this.renderSliderPosition),
               height,
               this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color,
               ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.getValue()
            );
         } else {
            Render2DUtil.drawRect(
               matrixStack,
               (float)(parentX + 1),
               y,
               (float)((int)this.renderSliderPosition),
               height,
               this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color
            );
         }

         if (this.setting == null) {
            return true;
         } else {
            if (this.setting.isListening()) {
               if (this.timer.passed(1000L)) {
                  this.b = !this.b;
                  this.timer.reset();
               }

               TextUtil.drawString(
                  drawContext,
                  this.setting.temp + (this.b ? "_" : ""),
                  (double)(parentX + 4),
                  (double)((float)((double)parentY + this.getTextOffsetY() + (double)offset - 2.0)),
                  16777215
               );
            } else {
               String value;
               if ((double)this.setting.getValueInt() == this.setting.getValue()) {
                  value = String.valueOf(this.setting.getValueInt());
               } else {
                  value = String.valueOf(this.setting.getValueFloat());
               }

               value = value + this.setting.getSuffix();
               TextUtil.drawString(
                  drawContext,
                  this.setting.getName(),
                  (double)((float)(parentX + 4)),
                  (double)((float)((double)parentY + this.getTextOffsetY() + (double)offset - 2.0)),
                  16777215
               );
               TextUtil.drawString(
                  drawContext,
                  value,
                  (double)((float)(parentX + parentWidth) - TextUtil.getWidth(value) - 5.0F),
                  (double)((float)((double)parentY + this.getTextOffsetY() + (double)offset - 2.0)),
                  16777215
               );
            }

            return true;
         }
      }
   }
}
