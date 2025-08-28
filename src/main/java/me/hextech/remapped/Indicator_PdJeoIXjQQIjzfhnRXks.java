package me.hextech.remapped;

import java.awt.Color;

import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.impl.combat.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.mod.modules.impl.combat.AutoTrap;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Indicator_PdJeoIXjQQIjzfhnRXks
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Indicator_PdJeoIXjQQIjzfhnRXks INSTANCE;
    MatrixStack matrixStack;
    float offset;
    float height;

    public Indicator_PdJeoIXjQQIjzfhnRXks() {
        super("Indicator", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        if (!Indicator_PdJeoIXjQQIjzfhnRXks.nullCheck()) {
            this.matrixStack = drawContext.getMatrices();
            this.height = FontRenderers.Calibri.getFontHeight();
            this.offset = 0.0f;
            if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                this.draw("BURROW", HoleKickTest.isInWeb(Indicator_PdJeoIXjQQIjzfhnRXks.mc.player) ? Indicator.Red : Indicator.Green);
            }
            if (BlockUtil.isHole(EntityUtil.getPlayerPos(true))) {
                this.draw("SAFE", Indicator.Green);
            } else {
                this.draw("UNSAFE", Indicator.Red);
            }
            if (Speed.INSTANCE.isOn()) {
                this.draw("BHOP", Indicator.White);
            }
            if (HoleKickTest.INSTANCE.isOn()) {
                this.draw("PUSH", Indicator.White);
            }
            if (AutoTrap.INSTANCE.isOn()) {
                this.draw("TRAP", Indicator.White);
            }
            if (Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn()) {
                this.draw("DEF", Indicator.White);
            }
            if (Step_EShajbhvQeYkCdreEeNY.INSTANCE.isOn()) {
                this.draw("ST", Indicator.White);
            }
            if (WebAuraTick.INSTANCE.isOn()) {
                this.draw("AW", Indicator.White);
            }
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOn()) {
                this.draw("AC", AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget != null && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastDamage > 0.0f ? Indicator.Green : Indicator.Red);
            }
            if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.isOn()) {
                this.draw("AN", AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.displayTarget != null && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null ? Indicator.Green : Indicator.Red);
            }
        }
    }

    private void draw(String s, Indicator type) {
        int color = -1;
        if (type == Indicator.Red) {
            color = new Color(255, 0, 0).getRGB();
        }
        if (type == Indicator.Green) {
            color = new Color(47, 173, 26).getRGB();
        }
        double width = FontRenderers.Calibri.getWidth(s) + 8.0f;
        Render2DUtil.horizontalGradient(this.matrixStack, 10.0f, (float)(mc.getWindow().getScaledHeight() - 200) + this.offset, (float)(10.0 + width / 2.0), (float)(mc.getWindow().getScaledHeight() - 200) + this.offset + this.height, new Color(0, 0, 0, 0), new Color(0, 0, 0, 100));
        Render2DUtil.horizontalGradient(this.matrixStack, (float)(10.0 + width / 2.0), (float)(mc.getWindow().getScaledHeight() - 200) + this.offset, (float)(10.0 + width), (float)(mc.getWindow().getScaledHeight() - 200) + this.offset + this.height, new Color(0, 0, 0, 100), new Color(0, 0, 0, 0));
        FontRenderers.Calibri.drawString(this.matrixStack, s, 14.0f, (float)(mc.getWindow().getScaledHeight() - 195) + this.offset, color);
        this.offset -= this.height + 3.0f;
    }
}
