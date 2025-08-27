package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

public class ColorComponents extends Component {
   private final ColorSetting colorSetting;
   private final Timer clickTimer = new Timer();
   public double currentWidth = 0.0;
   boolean clicked = false;
   boolean popped = false;
   boolean hover = false;
   private float hue;
   private float saturation;
   private float brightness;
   private int alpha;
   private boolean afocused;
   private boolean hfocused;
   private boolean sbfocused;
   private float spos;
   private float bpos;
   private float hpos;
   private float apos;
   private Color prevColor;
   private boolean firstInit;
   private double lastMouseX;
   private double lastMouseY;

   public ColorComponents(ClickGuiTab parent, ColorSetting setting) {
      this.parent = parent;
      this.colorSetting = setting;
      this.prevColor = this.getColorSetting().getValue();
      this.updatePos();
      this.firstInit = true;
   }

   public ColorSetting getColorSetting() {
      return this.colorSetting;
   }

   @Override
   public boolean isVisible() {
      return this.colorSetting.visibility != null ? this.colorSetting.visibility.test(null) : true;
   }

   private void updatePos() {
      float[] hsb = Color.RGBtoHSB(
         this.getColorSetting().getValue().getRed(), this.getColorSetting().getValue().getGreen(), this.getColorSetting().getValue().getBlue(), null
      );
      this.hue = -1.0F + hsb[0];
      this.saturation = hsb[1];
      this.brightness = hsb[2];
      this.alpha = this.getColorSetting().getValue().getAlpha();
   }

   private void setColor(Color color) {
      this.getColorSetting().setValue(color.getRGB());
      this.prevColor = color;
   }

   @Override
   public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
      int x = this.parent.getX();
      int y = (int)((double)this.parent.getY() + this.currentOffset) - 2;
      int width = this.parent.getWidth();
      double cx = (double)(x + 3);
      double cy = (double)(y + this.defaultHeight);
      double cw = (double)(width - 19);
      double ch = (double)(this.getHeight() - 17);
      this.hover = Render2DUtil.isHovered(
         mouseX, mouseY, (double)((float)x + 1.0F), (double)((float)y + 1.0F), (double)((float)width - 2.0F), (double)((float)this.defaultHeight)
      );
      if (this.hover && GuiManager.currentGrabbed == null && this.isVisible() && ClickGuiScreen.rightClicked) {
         ClickGuiScreen.rightClicked = false;
         this.popped = !this.popped;
      }

      if (this.popped) {
         this.setHeight(45 + this.defaultHeight);
      } else {
         this.setHeight(this.defaultHeight);
      }

      if ((ClickGuiScreen.clicked || ClickGuiScreen.hoverClicked) && this.isVisible()) {
         if (!this.clicked) {
            if (Render2DUtil.isHovered(mouseX, mouseY, cx + cw + 9.0, cy, 4.0, ch)) {
               this.afocused = true;
               ClickGuiScreen.hoverClicked = true;
               ClickGuiScreen.clicked = false;
            }

            if (Render2DUtil.isHovered(mouseX, mouseY, cx + cw + 4.0, cy, 4.0, ch)) {
               this.hfocused = true;
               ClickGuiScreen.hoverClicked = true;
               ClickGuiScreen.clicked = false;
               if (this.colorSetting.isRainbow) {
                  this.colorSetting.setRainbow(false);
                  this.lastMouseX = 0.0;
                  this.lastMouseY = 0.0;
               } else {
                  if (!this.clickTimer.passedMs(400L) && mouseX == this.lastMouseX && mouseY == this.lastMouseY) {
                     this.colorSetting.setRainbow(!this.colorSetting.isRainbow);
                  }

                  this.clickTimer.reset();
                  this.lastMouseX = mouseX;
                  this.lastMouseY = mouseY;
               }
            }

            if (Render2DUtil.isHovered(mouseX, mouseY, cx, cy, cw, ch)) {
               this.sbfocused = true;
               ClickGuiScreen.hoverClicked = true;
               ClickGuiScreen.clicked = false;
            }

            if (GuiManager.currentGrabbed == null && this.isVisible() && this.hover && this.getColorSetting().injectBoolean) {
               this.getColorSetting().booleanValue = !this.getColorSetting().booleanValue;
               ClickGuiScreen.clicked = false;
            }
         }

         this.clicked = true;
      } else {
         this.clicked = false;
         this.sbfocused = false;
         this.afocused = false;
         this.hfocused = false;
      }

      if (this.popped) {
         if (GuiManager.currentGrabbed == null && this.isVisible()) {
            Color value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
            if (this.sbfocused) {
               this.saturation = (float)((double)MathUtil.clamp((float)(mouseX - cx), 0.0F, (float)cw) / cw);
               this.brightness = (float)((ch - (double)MathUtil.clamp((float)(mouseY - cy), 0.0F, (float)ch)) / ch);
               value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
               this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
            }

            if (this.hfocused) {
               this.hue = (float)(-((ch - (double)MathUtil.clamp((float)(mouseY - cy), 0.0F, (float)ch)) / ch));
               value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
               this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
            }

            if (this.afocused) {
               this.alpha = (int)((ch - (double)MathUtil.clamp((float)(mouseY - cy), 0.0F, (float)ch)) / ch * 255.0);
               this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
            }
         }
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
         boolean unShift = !this.hover || !InputUtil.method_15987(mc.method_22683().method_4490(), 340);
         Render2DUtil.drawRect(
            matrixStack,
            (float)x + 1.0F,
            (float)y + 1.0F,
            (float)width - 2.0F,
            (float)this.defaultHeight - 1.0F,
            this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.sbgColor.getValue()
         );
         if (this.colorSetting.injectBoolean) {
            this.currentWidth = animate(
               this.currentWidth, this.colorSetting.booleanValue ? (double)width - 2.0 : 0.0, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.booleanSpeed.getValue()
            );
            switch ((ClickGui_qvrpuTqAQSFqXVuTIwHB)ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.uiType.getValue()) {
               case New:
                  if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.booleanValue) {
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

                  if (unShift) {
                     TextUtil.drawString(drawContext, this.colorSetting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), -1);
                  }
                  break;
               case Old:
                  if (unShift) {
                     TextUtil.drawString(
                        drawContext,
                        this.colorSetting.getName(),
                        (double)(x + 4),
                        (double)y + this.getTextOffsetY(),
                        this.colorSetting.booleanValue
                           ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.enableTextS.getValue()
                           : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.disableText.getValue()
                     );
                  }
            }
         } else if (unShift) {
            TextUtil.drawString(drawContext, this.colorSetting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), -1);
         }

         if (!unShift) {
            TextUtil.drawString(drawContext, "§aL-Copy §cR-Paste", (double)(x + 4), (double)y + this.getTextOffsetY(), -1);
         }

         Render2DUtil.drawRound(
            matrixStack,
            (float)(x + width - 16),
            (float)((double)y + this.getTextOffsetY()),
            12.0F,
            8.0F,
            1.0F,
            ColorUtil.injectAlpha(this.getColorSetting().getValue(), 255)
         );
         if (back) {
            return true;
         } else if (!this.popped) {
            return true;
         } else {
            double cx = (double)x;
            double cy = (double)(y + this.defaultHeight + 1);
            double cw = (double)(width - 15);
            double ch = (double)(this.defaultHeight - 32 + 60);
            if (this.prevColor != this.getColorSetting().getValue()) {
               this.updatePos();
               this.prevColor = this.getColorSetting().getValue();
            }

            if (this.firstInit) {
               this.spos = (float)(cx + cw - (cw - cw * (double)this.saturation));
               this.bpos = (float)(cy + (ch - ch * (double)this.brightness));
               this.hpos = (float)(cy + ch - 3.0 + (ch - 3.0) * (double)this.hue);
               this.apos = (float)(cy + (ch - 3.0 - (ch - 3.0) * (double)((float)this.alpha / 255.0F)));
               this.firstInit = false;
            }

            this.spos = (float)animate((double)this.spos, (double)((float)(cx + cw - (cw - cw * (double)this.saturation))), 0.6F);
            this.bpos = (float)animate((double)this.bpos, (double)((float)(cy + (ch - ch * (double)this.brightness))), 0.6F);
            this.hpos = (float)animate((double)this.hpos, (double)((float)(cy + ch - 3.0 + (ch - 3.0) * (double)this.hue)), 0.6F);
            this.apos = (float)animate((double)this.apos, (double)((float)(cy + (ch - 3.0 - (ch - 3.0) * (double)((float)this.alpha / 255.0F)))), 0.6F);
            Color colorA = Color.getHSBColor(this.hue, 0.0F, 1.0F);
            Color colorB = Color.getHSBColor(this.hue, 1.0F, 1.0F);
            Color colorC = new Color(0, 0, 0, 0);
            Color colorD = new Color(0, 0, 0);
            Render2DUtil.horizontalGradient(matrixStack, (float)cx + 2.0F, (float)cy, (float)(cx + cw), (float)(cy + ch), colorA, colorB);
            Render2DUtil.verticalGradient(matrixStack, (float)(cx + 2.0), (float)cy, (float)(cx + cw), (float)(cy + ch), colorC, colorD);

            for (float i = 1.0F; (double)i < ch - 2.0; i++) {
               float curHue = (float)(1.0 / (ch / (double)i));
               Render2DUtil.drawRect(matrixStack, (float)(cx + cw + 4.0), (float)(cy + (double)i), 4.0F, 1.0F, Color.getHSBColor(curHue, 1.0F, 1.0F));
            }

            Render2DUtil.verticalGradient(
               matrixStack,
               (float)(cx + cw + 9.0),
               (float)(cy + 0.8F),
               (float)(cx + cw + 12.5),
               (float)(cy + ch - 2.0),
               new Color(
                  this.getColorSetting().getValue().getRed(), this.getColorSetting().getValue().getGreen(), this.getColorSetting().getValue().getBlue(), 255
               ),
               new Color(0, 0, 0, 0)
            );
            Render2DUtil.drawRect(matrixStack, (float)(cx + cw + 3.0), this.hpos + 0.5F, 5.0F, 1.0F, Color.WHITE);
            Render2DUtil.drawRect(matrixStack, (float)(cx + cw + 8.0), this.apos + 0.5F, 5.0F, 1.0F, Color.WHITE);
            Render2DUtil.drawRound(matrixStack, this.spos - 1.5F, this.bpos - 1.5F, 3.0F, 3.0F, 1.5F, new Color(-1));
            return true;
         }
      }
   }
}
