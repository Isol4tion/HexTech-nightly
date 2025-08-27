package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.LogoDrawer;
import me.hextech.remapped.TextUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Menu
extends Screen {
    private static final Identifier sky;
    private static final Identifier station;
    private static final Identifier pillar;
    private static final Identifier moon;
    private static final Identifier setting;
    private static final Identifier exit;
    private static final Identifier single;
    private static final Identifier multi;
    float zoomSingle = 1.0f;
    float zoomMulti = 1.0f;
    float zoomSetting = 1.0f;
    float zoomExit = 1.0f;

    public Menu() {
        super((Text)Text.translatable((String)"narrator.screen.title"));
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        LogoDrawer.draw(context, this.width / 4, this.height / 4, 1.0f);
        context.method_25302(moon, 50, 15, 0, 0, this.width, this.height);
        context.method_25291(sky, 0, 0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        context.method_25291(station, (mouseX - this.width) / 48, 30 + (mouseY - this.height / 2) / 80, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        MatrixStack matrices = context.getMatrices();
        RenderSystem.defaultBlendFunc();
        float maxButtonWidth = (float)this.width / 2.0f;
        int buttonWidth = this.height / 12;
        float scaled = 1.2f;
        int startX = this.width / 4 + 25;
        int buttonY = (this.height - buttonWidth) / 2;
        int charc = (int)((maxButtonWidth - (float)(4 * buttonWidth)) / 5.0f);
        double zoomAdd = 0.06;
        float alphaFac = (float)(1 - Math.abs(mouseY - this.height / 2) / this.height) / 255.0f;
        matrices.push();
        if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
            if (this.zoomSingle < scaled) {
                this.zoomSingle = (float)((double)this.zoomSingle + zoomAdd);
            }
        } else if ((double)this.zoomSingle > 1.0) {
            this.zoomSingle = (float)((double)this.zoomSingle - zoomAdd);
        }
        if (this.zoomSingle > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(this.zoomSingle, this.zoomSingle, 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
        }
        RenderSystem.setShaderTexture((int)0, (Identifier)single);
        RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
        context.method_25291(single, startX, buttonY, 0, 0.0f, 0.0f, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
        matrices.pop();
        matrices.push();
        if (this.zoomSingle > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(this.zoomSingle, this.zoomSingle, 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
            TextUtil.drawString(context, "Singleplayer", (float)(startX + buttonWidth / 2) - TextUtil.getWidth("Singleplayer"), (float)buttonY + (float)buttonWidth + 1.0f, new Color(255, 255, 255, 255).getRGB(), true);
        }
        matrices.pop();
        matrices.push();
        if (this.isMouseHoveringRect((float)(startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc)), (float)buttonY, buttonWidth, mouseX, mouseY)) {
            if (this.zoomMulti < scaled) {
                this.zoomMulti = (float)((double)this.zoomMulti + zoomAdd);
            }
        } else if ((double)this.zoomMulti > 1.0) {
            this.zoomMulti = (float)((double)this.zoomMulti - zoomAdd);
        }
        if (this.zoomMulti > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(Math.min(scaled, this.zoomMulti), Math.min(scaled, this.zoomMulti), 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
        }
        RenderSystem.setShaderTexture((int)0, (Identifier)multi);
        RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
        context.method_25291(multi, startX, buttonY, 0, 0.0f, 0.0f, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
        matrices.pop();
        matrices.push();
        if (this.zoomMulti > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(this.zoomMulti, this.zoomMulti, 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
            TextUtil.drawString(context, "Multiplayer", (float)(startX + buttonWidth / 2) - TextUtil.getWidth("Multiplayer"), (float)buttonY + (float)buttonWidth + 1.0f, new Color(255, 255, 255, 255).getRGB(), true);
        }
        matrices.pop();
        matrices.push();
        if (this.isMouseHoveringRect((float)(startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc)), (float)buttonY, buttonWidth, mouseX, mouseY)) {
            if (this.zoomSetting < scaled) {
                this.zoomSetting = (float)((double)this.zoomSetting + zoomAdd);
            }
        } else if ((double)this.zoomSetting > 1.0) {
            this.zoomSetting = (float)((double)this.zoomSetting - zoomAdd);
        }
        if (this.zoomSetting > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(Math.min(scaled, this.zoomSetting), Math.min(scaled, this.zoomSetting), 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
        }
        RenderSystem.setShaderTexture((int)0, (Identifier)setting);
        RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
        context.method_25291(setting, startX, buttonY, 0, 0.0f, 0.0f, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
        matrices.pop();
        matrices.push();
        if (this.zoomSetting > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(this.zoomSetting, this.zoomSetting, 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
            TextUtil.drawString(context, "Singleplayer", (float)(startX + buttonWidth / 2) - TextUtil.getWidth("Singleplayer"), (float)buttonY + (float)buttonWidth + 1.0f, new Color(255, 255, 255, 255).getRGB(), true);
        }
        matrices.pop();
        matrices.push();
        if (this.isMouseHoveringRect((float)(startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc)), (float)buttonY, buttonWidth, mouseX, mouseY)) {
            if (this.zoomExit < scaled) {
                this.zoomExit = (float)((double)this.zoomExit + zoomAdd);
            }
        } else if ((double)this.zoomExit > 1.0) {
            this.zoomExit = (float)((double)this.zoomExit - zoomAdd);
        }
        if (this.zoomExit > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(Math.min(scaled, this.zoomExit), Math.min(scaled, this.zoomExit), 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
        }
        RenderSystem.setShaderTexture((int)0, (Identifier)exit);
        RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
        context.method_25291(exit, startX, buttonY, 0, 0.0f, 0.0f, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
        matrices.pop();
        matrices.push();
        if (this.zoomExit > 1.0f) {
            matrices.translate((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0f);
            matrices.scale(this.zoomExit, this.zoomExit, 1.0f);
            matrices.translate((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0f);
            TextUtil.drawString(context, "Exit", (float)(startX + buttonWidth / 2) - TextUtil.getWidth("Exit") - 2.0f, (float)buttonY + (float)buttonWidth + 1.0f, new Color(255, 255, 255, 255).getRGB(), true);
        }
        matrices.pop();
        RenderSystem.disableBlend();
    }

    public boolean isMouseHoveringRect(float x, float y, int width, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x + (float)width && (float)mouseY <= y + (float)width;
    }

    public boolean isMouseHoveringRect(float x, float y, int width, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseY >= (double)y && mouseX <= (double)(x + (float)width) && mouseY <= (double)(y + (float)width);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        float maxButtonWidth = (float)this.width / 2.0f;
        int buttonWidth = this.height / 12;
        float scaled = 1.2f;
        int startX = this.width / 4 + 15;
        int buttonY = (this.height - buttonWidth) / 2;
        int charc = (int)((maxButtonWidth - (float)(4 * buttonWidth)) / 5.0f);
        if (button == 0) {
            if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
                this.client.setScreen((Screen)new SelectWorldScreen((Screen)this));
            }
            if (this.isMouseHoveringRect((float)(startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc)), (float)buttonY, buttonWidth, mouseX, mouseY)) {
                if (!Wrapper.mc.options.skipMultiplayerWarning) {
                    Wrapper.mc.options.skipMultiplayerWarning = true;
                    Wrapper.mc.options.write();
                }
                MultiplayerScreen screen = new MultiplayerScreen((Screen)this);
                Wrapper.mc.setScreen((Screen)screen);
            }
            if (this.isMouseHoveringRect((float)(startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc)), (float)buttonY, buttonWidth, mouseX, mouseY)) {
                Wrapper.mc.setScreen((Screen)new OptionsScreen((Screen)this, Wrapper.mc.options));
            }
            if (this.isMouseHoveringRect((float)(startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc)), (float)buttonY, buttonWidth, mouseX, mouseY)) {
                Wrapper.mc.stop();
            }
        }
        return super.method_25402(mouseX, mouseY, button);
    }
}
