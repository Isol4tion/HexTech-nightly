package me.hextech.remapped;

import java.awt.Color;
import java.util.Objects;
import me.hextech.remapped.AltScreen;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.FontRenderers;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HexTech
extends Screen
implements Wrapper {
    private final Identifier logo = new Identifier("nullpoint", "icon.png");
    private final Identifier bg = new Identifier("nullpoint", "menu/bg2.png");
    private final Identifier single = new Identifier("nullpoint", "menu/single.png");
    private final Identifier multi = new Identifier("nullpoint", "menu/multi.png");
    private final Identifier setting = new Identifier("nullpoint", "menu/setting.png");
    private final Identifier clickGui = new Identifier("nullpoint", "menu/clickgui.png");
    private final Identifier alt = new Identifier("nullpoint", "menu/alt.png");
    private final Identifier exit = new Identifier("nullpoint", "menu/exit.png");
    private final float buttonHeight = 30.0f;
    private final float buttonWidth = 200.0f;
    private final float[] zoom = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

    public HexTech() {
        super(Text.of("HexTech"));
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        if (mc.getWindow() == null) {
            return;
        }
        float offsetX = ((float)mouseX - (float)this.width / 2.0f) / 48.0f;
        float offsetY = ((float)mouseY - (float)this.height / 2.0f) / 80.0f;
        drawContext.drawTexture(this.bg, (int)offsetX, (int)offsetY, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        float buttonSpacing = 8.0f;
        float baseX = (float)(this.width / 2) - this.buttonWidth / 2.0f;
        float baseY = (float)(this.height / 2) - 2.0f * this.buttonHeight;
        for (int i = 0; i < 6; ++i) {
            float y = baseY + (float)i * (30.0f + buttonSpacing);
            boolean hover = this.isMouseHoveringRect(baseX, y, 200.0, 30.0, mouseX, mouseY);
            this.zoom[i] = hover ? Math.min(1.12f, this.zoom[i] + 0.04f) : Math.max(1.0f, this.zoom[i] - 0.04f);
            MatrixStack matrices = drawContext.getMatrices();
            matrices.push();
            float cx = baseX + 100.0f;
            float cy = y + 15.0f;
            matrices.translate(cx, cy, 0.0f);
            matrices.scale(this.zoom[i], this.zoom[i], 1.0f);
            matrices.translate(-cx, -cy, 0.0f);
            Render2DUtil.drawRound(matrices, baseX, y, 200.0f, 30.0f, 8.0f, hover ? new Color(40, 40, 40, 150) : new Color(40, 40, 40, 80), true, 1000);
            int iconSize = 25;
            Identifier iconTex = switch (i) {
                case 0 -> this.single;
                case 1 -> this.multi;
                case 2 -> this.setting;
                case 3 -> this.clickGui;
                case 4 -> this.alt;
                case 5 -> this.exit;
                default -> this.single;
            };
            drawContext.drawTexture(iconTex, (int)(baseX + (float)iconSize + 8.0f), (int)(y + (30.0f - (float)iconSize) / 2.0f), 0.0f, 0.0f, iconSize, iconSize, iconSize, iconSize);
            String label = switch (i) {
                case 0 -> "Singleplayer";
                case 1 -> "Multiplayer";
                case 2 -> "Setting";
                case 3 -> "ClickGui";
                case 4 -> "AltManager";
                case 5 -> "Exit";
                default -> "";
            };
            float fh = FontRenderers.Arial.getFontHeight();
            FontRenderers.Arial.drawCenteredString(matrices, label, (double)this.width / 2.0, (double)y + (double)(30.0f - fh) / 2.0, Color.WHITE.getRGB());
            matrices.pop();
        }
        int logoSize = this.height / 8;
        drawContext.drawTexture(this.logo, this.width / 2 - logoSize / 2, (int)(baseY - (float)logoSize - 30.0f), 0, 0.0f, 0.0f, logoSize, logoSize, logoSize, logoSize);
        FontRenderers.ui.drawString(drawContext.getMatrices(), "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c-8", 5.0f, (float)this.height - FontRenderers.ui.getFontHeight(), Color.WHITE.getRGB());
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float buttonSpacing = 8.0f;
        float x = (float)(this.width / 2) - this.buttonWidth / 2.0f;
        float y = (float)(this.height / 2) - 2.0f * this.buttonHeight;
        if (button == 0) {
            double d = x;
            double d2 = y;
            Objects.requireNonNull(this);
            Objects.requireNonNull(this);
            if (this.isMouseHoveringRect(d, d2, 200.0, 30.0, mouseX, mouseY)) {
                mc.setScreen(new SelectWorldScreen(this));
            }
            float y2 = y + this.buttonHeight + buttonSpacing;
            double d3 = x;
            double d4 = y2;
            Objects.requireNonNull(this);
            Objects.requireNonNull(this);
            if (this.isMouseHoveringRect(d3, d4, 200.0, 30.0, mouseX, mouseY)) {
                if (!HexTech.mc.options.skipMultiplayerWarning) {
                    HexTech.mc.options.skipMultiplayerWarning = true;
                    HexTech.mc.options.write();
                }
                mc.setScreen(new MultiplayerScreen(this));
            }
            float y3 = y2 + this.buttonHeight + buttonSpacing;
            double d5 = x;
            double d6 = y3;
            Objects.requireNonNull(this);
            Objects.requireNonNull(this);
            if (this.isMouseHoveringRect(d5, d6, 200.0, 30.0, mouseX, mouseY)) {
                mc.setScreen(new OptionsScreen(this, HexTech.mc.options));
            }
            float y4 = y3 + this.buttonHeight + buttonSpacing;
            double d7 = x;
            double d8 = y4;
            Objects.requireNonNull(this);
            Objects.requireNonNull(this);
            if (this.isMouseHoveringRect(d7, d8, 200.0, 30.0, mouseX, mouseY)) {
                mc.setScreen(new ClickGuiScreen());
            }
            float y5 = y4 + this.buttonHeight + buttonSpacing;
            double d9 = x;
            double d10 = y5;
            Objects.requireNonNull(this);
            Objects.requireNonNull(this);
            if (this.isMouseHoveringRect(d9, d10, 200.0, 30.0, mouseX, mouseY)) {
                mc.setScreen(new AltScreen(new HexTech()));
            }
            float y6 = y5 + this.buttonHeight + buttonSpacing;
            double d11 = x;
            double d12 = y6;
            Objects.requireNonNull(this);
            Objects.requireNonNull(this);
            if (this.isMouseHoveringRect(d11, d12, 200.0, 30.0, mouseX, mouseY)) {
                mc.stop();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isMouseHoveringRect(double x, double y, double w, double h, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + h;
    }
}
