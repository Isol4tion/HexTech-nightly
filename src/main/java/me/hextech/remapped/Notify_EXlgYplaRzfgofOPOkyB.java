package me.hextech.remapped;

import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Notify;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.gui.DrawContext;

public class Notify_EXlgYplaRzfgofOPOkyB
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final CopyOnWriteArrayList<Notify> notifyList = new CopyOnWriteArrayList();
    public static Notify_EXlgYplaRzfgofOPOkyB INSTANCE;
    public final EnumSetting<Notify._hvcAdwcUFPZabyUezEMv> type = this.add(new EnumSetting<Notify._hvcAdwcUFPZabyUezEMv>("Type", Notify._hvcAdwcUFPZabyUezEMv.Chat));
    public final EnumSetting<Notify._eNVZNRNonauDhUZRxBAg> mode = this.add(new EnumSetting<Notify._eNVZNRNonauDhUZRxBAg>("Type", Notify._eNVZNRNonauDhUZRxBAg.Fill));
    public final SliderSetting notifyX = this.add(new SliderSetting("notifyX", 256, -500, 500));
    private final SliderSetting notifyY = this.add(new SliderSetting("Y", -120, -500, 500));
    private final SliderSetting startdelay = this.add(new SliderSetting("StartDelay", 1, 0, 15));
    private final SliderSetting delay = this.add(new SliderSetting("EndDelay", 5, 0, 15));
    private final ColorSetting fillcolor = this.add(new ColorSetting("FillColor", new Color(20, 20, 20, 100)));
    private final ColorSetting linecolor = this.add(new ColorSetting("LineColor", new Color(140, 140, 250, 225)));
    private final SliderSetting height = this.add(new SliderSetting("Height", 15, 0, 30));
    private final SliderSetting radius = this.add(new SliderSetting("Radius", 4.0, 0.0, 10.0));

    public Notify_EXlgYplaRzfgofOPOkyB() {
        super("Notify", "Notify Test", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        boolean bl = true;
        int n = (int)(379.0 - this.notifyY.getValue());
        int n2 = this.notifyX.getValueInt() + 500;
        for (Notify notifys : notifyList) {
            if (notifys == null || notifys.first == null || notifys.firstFade == null || notifys.delayed < 1) continue;
            bl = false;
            if (notifys.delayed < 5 && !notifys.end) {
                notifys.end = true;
                notifys.endFade.reset();
            }
            n = (int)((double)n - 18.0 * notifys.yFade.easeOutQuad());
            String string = notifys.first;
            double d = (float)notifys.delayed < this.delay.getValueFloat() ? (double)n2 - (double)(Notify_EXlgYplaRzfgofOPOkyB.mc.textRenderer.getWidth(string) + 10) * (1.0 - notifys.endFade.easeOutQuad()) : (double)n2 - (double)(Notify_EXlgYplaRzfgofOPOkyB.mc.textRenderer.getWidth(string) + 10) * notifys.firstFade.easeOutQuad();
            Render2DUtil.drawRound(drawContext.getMatrices(), (int)d, n, 10 + Notify_EXlgYplaRzfgofOPOkyB.mc.textRenderer.getWidth(string), this.height.getValueInt(), this.radius.getValueFloat(), this.fillcolor.getValue());
            drawContext.drawText(Notify_EXlgYplaRzfgofOPOkyB.mc.textRenderer, string, 5 + (int)d, 4 + n, new Color(255, 255, 255, 255).getRGB(), true);
            if (notifys.delayed < 5) {
                n = (int)((double)n + 18.0 * notifys.yFade.easeOutQuad() - 18.0 * (1.0 - notifys.endFade.easeOutQuad()));
                continue;
            }
            Render2DUtil.drawRect(drawContext.getMatrices(), (float)((int)d) + 2.0f, (float)(n + 14), (float)((10 + Notify_EXlgYplaRzfgofOPOkyB.mc.textRenderer.getWidth(string)) * (notifys.delayed - 4) - 2) / 62.0f, 1.0f, this.linecolor.getValue());
        }
        if (bl) {
            notifyList.clear();
        }
    }

    @Override
    public void onUpdate() {
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.state) {
            return;
        }
        for (Notify notifys : notifyList) {
            if (notifys == null || notifys.first == null || notifys.firstFade == null) continue;
            --notifys.delayed;
        }
    }

    @Override
    public void onDisable() {
        notifyList.clear();
    }
}
