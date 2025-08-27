package me.hextech.remapped;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import me.hextech.HexTech;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.FadeUtils;
import me.hextech.remapped.MineManager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class BreakESP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final ConcurrentHashMap<String, MineManager> BREAK;
    public static BreakESP INSTANCE;
    public final BooleanSetting noSelf = this.add(new BooleanSetting("noSelf", true));
    public final SliderSetting namescale = this.add(new SliderSetting("Name", 0.5, 0.0, 0.8, 0.1));
    public final SliderSetting nameY = this.add(new SliderSetting("Name-Y", 0.15, -0.1, 1.0, 0.1));
    public final SliderSetting minescale = this.add(new SliderSetting("Mine", 0.5, 0.0, 0.8, 0.1));
    public final SliderSetting mineY = this.add(new SliderSetting("Mine-Y", -0.15, 0.0, 1.0, 0.01));
    public final SliderSetting animationTime = this.add(new SliderSetting("AnimationTime", 500, 0, 2000).setSuffix("ms"));
    public final SliderSetting breakTime = this.add(new SliderSetting("BreakTime", 2.5, 0.0, 5.0, 0.1).setSuffix("s"));
    private final BooleanSetting name = this.add(new BooleanSetting("BreakName", true));
    private final BooleanSetting breakName = this.add(new BooleanSetting("BreakText", true));
    private final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final EnumSetting<FadeUtils> quad = this.add(new EnumSetting<FadeUtils>("Quad", FadeUtils.In));
    private final DecimalFormat df = new DecimalFormat("0.0");

    public BreakESP() {
        super("BreakESP", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        HexTech.BREAK.breakMap.forEach((key, breakData) -> {
            if (breakData == null || breakData.getEntity() == null) {
                return;
            }
            if (this.noSelf.getValue() && breakData.getEntity() == BreakESP.mc.player) {
                return;
            }
            if (breakData.fade == null || breakData.timer == null) {
                return;
            }
            double size = 0.5 * (1.0 - breakData.fade.getQuad(this.quad.getValue()));
            Box cbox = new Box(breakData.pos).shrink(size, size, size).shrink(-size, -size, -size);
            if (this.fill.booleanValue) {
                Render3DUtil.drawFill(matrixStack, cbox, this.fill.getValue());
            }
            if (this.box.booleanValue) {
                Render3DUtil.drawBox(matrixStack, cbox, this.box.getValue());
            }
            if (this.name.getValue()) {
                Render3DUtil.drawText3DBreak(breakData.getEntity().getName().getString(), breakData.pos.toCenterPos().add(0.0, (double)this.nameY.getValueFloat(), 0.0), -1);
            }
            double breakTimeMs = this.breakTime.getValue() * 1000.0;
            if (this.breakName.getValue()) {
                Render3DUtil.drawText3DBreakMine(Text.of((String)this.df.format(Math.min(1.0, (double)breakData.timer.getPassedTimeMs() / breakTimeMs) * 100.0)), breakData.pos.toCenterPos().add(0.0, (double)(-this.mineY.getValueFloat()), 0.0), 0.0, 0.0, this.minescale.getValueFloat(), ColorUtil.fadeColor(new Color(235, 235, 235), new Color(235, 235, 235), (double)breakData.timer.getPassedTimeMs() / breakTimeMs));
            }
        });
    }
}
