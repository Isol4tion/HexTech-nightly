package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Tab {
   public ArmorHUD() {
      this.width = 80;
      this.height = 34;
      this.x = (int)me.hextech.HexTech.CONFIG.getFloat("armor_x", 0.0F);
      this.y = (int)me.hextech.HexTech.CONFIG.getFloat("armor_y", 200.0F);
   }

   @Override
   public void update(double mouseX, double mouseY, boolean mouseClicked) {
      if (GuiManager.currentGrabbed == null
         && HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.armor.getValue()
         && mouseX >= (double)this.x
         && mouseX <= (double)(this.x + this.width)
         && mouseY >= (double)this.y
         && mouseY <= (double)(this.y + this.height)
         && mouseClicked) {
         GuiManager.currentGrabbed = this;
      }
   }

   @Override
   public void draw(DrawContext drawContext, float partialTicks, Color color) {
      if (HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.armor.getValue() && this.mc.field_1724 != null) {
         if (me.hextech.HexTech.GUI.isClickGuiOpen()) {
            Render2DUtil.drawRect(drawContext.method_51448(), (float)this.x, (float)this.y, (float)this.width, (float)this.height, new Color(0, 0, 0, 70));
         }

         int xOff = 0;

         for (ItemStack armor : this.mc.field_1724.method_31548().field_7548) {
            xOff += 20;
            if (!armor.method_7960()) {
               MatrixStack matrixStack = drawContext.method_51448();
               matrixStack.method_22903();
               int damage = EntityUtil.getDamagePercent(armor);
               int yOffset = this.height / 2;
               drawContext.method_51427(armor, this.x + this.width - xOff, this.y + yOffset);
               drawContext.method_51431(this.mc.field_1772, armor, this.x + this.width - xOff, this.y + yOffset);
               drawContext.method_51433(
                  this.mc.field_1772,
                  String.valueOf(damage),
                  this.x + this.width + 8 - xOff - this.mc.field_1772.method_1727(String.valueOf(damage)) / 2,
                  this.y + yOffset - 9 - 2,
                  new Color((int)(255.0F * (1.0F - (float)damage / 100.0F)), (int)(255.0F * ((float)damage / 100.0F)), 0).getRGB(),
                  true
               );
               matrixStack.method_22909();
            }
         }
      }
   }
}
