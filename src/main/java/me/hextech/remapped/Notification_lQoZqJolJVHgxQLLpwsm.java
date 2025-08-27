package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.HUD_ssNtBhEveKlCmIccBvAN;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Notification;
import me.hextech.remapped.Notification_PrCPUuiCqVywIosJdRuW;
import me.hextech.remapped.Notification_UtrzOkjpnGqedJBDTCjT;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.gui.DrawContext;

public class Notification_lQoZqJolJVHgxQLLpwsm
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final ArrayList<Notification_UtrzOkjpnGqedJBDTCjT> notifyList = new ArrayList();
    public static Notification_lQoZqJolJVHgxQLLpwsm INSTANCE;
    public final EnumSetting<Notification_PrCPUuiCqVywIosJdRuW> type = this.add(new EnumSetting<Notification_PrCPUuiCqVywIosJdRuW>("Type", Notification_PrCPUuiCqVywIosJdRuW.Chat));
    public final EnumSetting<Notification> mode = this.add(new EnumSetting<Notification>("Type", Notification.Fill));
    public final SliderSetting notifyX = this.add(new SliderSetting("notifyX", 256, 18, 500));
    private final SliderSetting notifyY = this.add(new SliderSetting("Y", 18, -50, 500));
    private final ColorSetting typeColor = this.add(new ColorSetting("TypeColor", new Color(20, 20, 20, 100)));
    private final ColorSetting fillcolor = this.add(new ColorSetting("FillColor", new Color(20, 20, 20, 100)));
    private final ColorSetting linecolor = this.add(new ColorSetting("LineColor", new Color(140, 140, 250, 225)));

    public Notification_lQoZqJolJVHgxQLLpwsm() {
        super("Notification", "Notification", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        boolean bl = true;
        int n = (int)(379.0 - this.notifyY.getValue());
        int n2 = this.notifyX.getValueInt() + 500;
        for (Notification_UtrzOkjpnGqedJBDTCjT notifys : notifyList) {
            if (notifys == null || notifys.first == null || notifys.firstFade == null || notifys.delayed < 1) continue;
            bl = false;
            if (notifys.delayed < 5 && !notifys.end) {
                notifys.end = true;
                notifys.endFade.reset();
            }
            n = (int)((double)n - 18.0 * notifys.yFade.easeOutQuad());
            String string = notifys.first;
            double d = notifys.delayed < 5 ? (double)n2 - (double)(Notification_lQoZqJolJVHgxQLLpwsm.mc.textRenderer.getWidth(string) + 10) * (1.0 - notifys.endFade.easeOutQuad()) : (double)n2 - (double)(Notification_lQoZqJolJVHgxQLLpwsm.mc.textRenderer.getWidth(string) + 10) * notifys.firstFade.easeOutQuad();
            Render2DUtil.drawRound(drawContext.getMatrices(), (int)d, n, 5.0f, 50.0f, 0.0f, this.typeColor.getValue());
            Render2DUtil.drawRound(drawContext.getMatrices(), (int)d + 5, n, 5 + Notification_lQoZqJolJVHgxQLLpwsm.mc.textRenderer.getWidth(string), 50.0f, 0.0f, this.fillcolor.getValue());
            drawContext.drawText(Notification_lQoZqJolJVHgxQLLpwsm.mc.textRenderer, string, 10 + (int)d, 4 + n, new Color(255, 255, 255, 255).getRGB(), true);
            if (notifys.delayed < 5) {
                n = (int)((double)n + 18.0 * notifys.yFade.easeOutQuad() - 18.0 * (1.0 - notifys.endFade.easeOutQuad()));
                continue;
            }
            Render2DUtil.drawRect(drawContext.getMatrices(), (float)((int)d) + 2.0f, (float)(n + 14), (float)((10 + Notification_lQoZqJolJVHgxQLLpwsm.mc.textRenderer.getWidth(string)) * (notifys.delayed - 4) - 2) / 62.0f, 1.0f, this.linecolor.getValue());
        }
        if (bl) {
            notifyList.clear();
        }
    }

    @Override
    public void onUpdate() {
        if (HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.state) {
            return;
        }
        for (Notification_UtrzOkjpnGqedJBDTCjT notifys : notifyList) {
            if (notifys == null || notifys.first == null || notifys.firstFade == null) continue;
            --notifys.delayed;
        }
    }

    @Override
    public void onDisable() {
        notifyList.clear();
    }
}
