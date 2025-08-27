package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.remapped.DeathEvent;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.HitLog_STBbGhMCskXcqFYRXyft;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.TotemEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class HitLog
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HitLog INSTANCE;
    static double y;
    private final ArrayList<HitLog_STBbGhMCskXcqFYRXyft> logList = new ArrayList();
    public SliderSetting animationSpeed = this.add(new SliderSetting("AnimationSpeed", 0.2, 0.01, 0.5, 0.01));
    public SliderSetting stayTime = this.add(new SliderSetting("StayTime", 1.0, 0.5, 5.0, 0.1));

    public HitLog() {
        super("HitLog", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    public static double animate(double current, double endPoint, double speed) {
        boolean shouldContinueAnimation = endPoint > current;
        double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        double factor = dif * speed;
        if (Math.abs(factor) <= 0.001) {
            return endPoint;
        }
        return current + (shouldContinueAnimation ? factor : -factor);
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        y = (double)mc.method_22683().method_4507() / 4.0 - 40.0;
        this.logList.removeIf(log -> log.timer.passed(this.stayTime.getValue() * 1000.0) && log.alpha <= 10.0);
        for (HitLog_STBbGhMCskXcqFYRXyft log2 : new ArrayList<HitLog_STBbGhMCskXcqFYRXyft>(this.logList)) {
            double d;
            boolean end = log2.timer.passed(this.stayTime.getValue() * 1000.0);
            log2.alpha = HitLog.animate(log2.alpha, end ? 0.0 : 255.0, this.animationSpeed.getValue());
            double d2 = log2.y;
            if (end) {
                double d3 = (double)mc.method_22683().method_4507() / 4.0 - 30.0;
                Objects.requireNonNull(HitLog.mc.field_1772);
                d = d3 + 9.0;
            } else {
                d = y;
            }
            log2.y = HitLog.animate(d2, d, this.animationSpeed.getValue());
            drawContext.method_25303(HitLog.mc.field_1772, log2.text, (int)log2.x, (int)log2.y, new Color(255, 255, 255, (int)log2.alpha).getRGB());
            if (end) continue;
            Objects.requireNonNull(HitLog.mc.field_1772);
            y -= (double)(9 + 2);
        }
    }

    @EventHandler
    public void onPlayerDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == HitLog.mc.player || player.method_5739((Entity)HitLog.mc.player) > 20.0f) {
            return;
        }
        int popCount = HexTech.POP.popContainer.getOrDefault(player.method_5477().getString(), 0);
        this.addLog("\u00a74\u00a7m" + player.method_5477().getString() + "\u00a7f " + popCount);
    }

    @EventHandler
    public void onTotem(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == HitLog.mc.player || player.method_5739((Entity)HitLog.mc.player) > 20.0f) {
            return;
        }
        int popCount = HexTech.POP.popContainer.getOrDefault(player.method_5477().getString(), 1);
        this.addLog("\u00a7a" + player.method_5477().getString() + "\u00a7f " + popCount);
    }

    public void addLog(String text) {
        this.logList.add(new HitLog_STBbGhMCskXcqFYRXyft(text));
    }
}
