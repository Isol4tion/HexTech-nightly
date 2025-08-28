package me.hextech.mod.modules.impl.client;

import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.render.Render2DUtil;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.gui.font.FontRenderers;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.*;
import me.hextech.mod.modules.impl.movement.Speed;
import me.hextech.mod.modules.impl.movement.Step_EShajbhvQeYkCdreEeNY;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Indicator_PdJeoIXjQQIjzfhnRXks
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Indicator_PdJeoIXjQQIjzfhnRXks INSTANCE;
    MatrixStack matrixStack;
    float offset;
    float height;

    public Indicator_PdJeoIXjQQIjzfhnRXks() {
        super("Indicator", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        if (!Indicator_PdJeoIXjQQIjzfhnRXks.nullCheck()) {
            this.matrixStack = drawContext.getMatrices();
            this.height = FontRenderers.Calibri.getFontHeight();
            this.offset = 0.0f;
            if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                this.draw("BURROW", HoleKickTest.isInWeb(Indicator_PdJeoIXjQQIjzfhnRXks.mc.player) ? Type.Red : Type.Green);
            }
            if (BlockUtil.isHole(EntityUtil.getPlayerPos(true))) {
                this.draw("SAFE", Type.Green);
            } else {
                this.draw("UNSAFE", Type.Red);
            }
            if (Speed.INSTANCE.isOn()) {
                this.draw("BHOP", Type.White);
            }
            if (HoleKickTest.INSTANCE.isOn()) {
                this.draw("PUSH", Type.White);
            }
            if (AutoTrap.INSTANCE.isOn()) {
                this.draw("TRAP", Type.White);
            }
            if (Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn()) {
                this.draw("DEF", Type.White);
            }
            if (Step_EShajbhvQeYkCdreEeNY.INSTANCE.isOn()) {
                this.draw("ST", Type.White);
            }
            if (WebAuraTick.INSTANCE.isOn()) {
                this.draw("AW", Type.White);
            }
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOn()) {
                this.draw("AC", AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget != null && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastDamage > 0.0f ? Type.Green : Type.Red);
            }
            if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.isOn()) {
                this.draw("AN", AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.displayTarget != null && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null ? Type.Green : Type.Red);
            }
        }
    }

    private void draw(String s, Type type) {
        int color = -1;
        if (type == Type.Red) {
            color = new Color(255, 0, 0).getRGB();
        }
        if (type == Type.Green) {
            color = new Color(47, 173, 26).getRGB();
        }
        double width = FontRenderers.Calibri.getWidth(s) + 8.0f;
        Render2DUtil.horizontalGradient(this.matrixStack, 10.0f, (float)(mc.getWindow().getScaledHeight() - 200) + this.offset, (float)(10.0 + width / 2.0), (float)(mc.getWindow().getScaledHeight() - 200) + this.offset + this.height, new Color(0, 0, 0, 0), new Color(0, 0, 0, 100));
        Render2DUtil.horizontalGradient(this.matrixStack, (float)(10.0 + width / 2.0), (float)(mc.getWindow().getScaledHeight() - 200) + this.offset, (float)(10.0 + width), (float)(mc.getWindow().getScaledHeight() - 200) + this.offset + this.height, new Color(0, 0, 0, 100), new Color(0, 0, 0, 0));
        FontRenderers.Calibri.drawString(this.matrixStack, s, 14.0f, (float)(mc.getWindow().getScaledHeight() - 195) + this.offset, color);
        this.offset -= this.height + 3.0f;
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Type {
        Red,
        Green,
        White
    }
}
