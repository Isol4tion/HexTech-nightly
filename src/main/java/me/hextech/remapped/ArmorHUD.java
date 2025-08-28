package me.hextech.remapped;

import java.awt.Color;
import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.GuiManager;
import me.hextech.remapped.HUD_ssNtBhEveKlCmIccBvAN;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.Tab;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ArmorHUD
extends Tab {
    public ArmorHUD() {
        this.width = 80;
        this.height = 34;
        this.x = (int)HexTech.CONFIG.getFloat("armor_x", 0.0f);
        this.y = (int)HexTech.CONFIG.getFloat("armor_y", 200.0f);
    }

    @Override
    public void update(double mouseX, double mouseY, boolean mouseClicked) {
        if (GuiManager.currentGrabbed == null && HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.armor.getValue() && mouseX >= (double)this.x && mouseX <= (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY <= (double)(this.y + this.height) && mouseClicked) {
            GuiManager.currentGrabbed = this;
        }
    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks, Color color) {
        if (HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.armor.getValue() && this.mc.player != null) {
            if (HexTech.GUI.isClickGuiOpen()) {
                Render2DUtil.drawRect(drawContext.getMatrices(), (float)this.x, (float)this.y, (float)this.width, (float)this.height, new Color(0, 0, 0, 70));
            }
            int xOff = 0;
            for (ItemStack armor : this.mc.player.getInventory().armor) {
                xOff += 20;
                if (armor.isEmpty()) continue;
                MatrixStack matrixStack = drawContext.getMatrices();
                matrixStack.push();
                int damage = EntityUtil.getDamagePercent(armor);
                int yOffset = this.height / 2;
                drawContext.drawItem(armor, this.x + this.width - xOff, this.y + yOffset);
                drawContext.drawItemInSlot(this.mc.textRenderer, armor, this.x + this.width - xOff, this.y + yOffset);
                TextRenderer textRenderer = this.mc.textRenderer;
                String string = String.valueOf(damage);
                int n = this.x + this.width + 8 - xOff - this.mc.textRenderer.getWidth(String.valueOf(damage)) / 2;
                Objects.requireNonNull(this.mc.textRenderer);
                drawContext.drawText(textRenderer, string, n, this.y + yOffset - 9 - 2, new Color((int)(255.0f * (1.0f - (float)damage / 100.0f)), (int)(255.0f * ((float)damage / 100.0f)), 0).getRGB(), true);
                matrixStack.pop();
            }
        }
    }
}
