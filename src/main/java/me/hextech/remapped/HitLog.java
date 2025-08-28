package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.client.gui.DrawContext;
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
        y = (double)mc.getWindow().getHeight() / 4.0 - 40.0;
        this.logList.removeIf(log -> log.timer.passed(this.stayTime.getValue() * 1000.0) && log.alpha <= 10.0);
        for (HitLog_STBbGhMCskXcqFYRXyft log2 : new ArrayList<HitLog_STBbGhMCskXcqFYRXyft>(this.logList)) {
            double d;
            boolean end = log2.timer.passed(this.stayTime.getValue() * 1000.0);
            log2.alpha = HitLog.animate(log2.alpha, end ? 0.0 : 255.0, this.animationSpeed.getValue());
            double d2 = log2.y;
            if (end) {
                double d3 = (double)mc.getWindow().getHeight() / 4.0 - 30.0;
                Objects.requireNonNull(HitLog.mc.textRenderer);
                d = d3 + 9.0;
            } else {
                d = y;
            }
            log2.y = HitLog.animate(d2, d, this.animationSpeed.getValue());
            drawContext.drawTextWithShadow(HitLog.mc.textRenderer, log2.text, (int)log2.x, (int)log2.y, new Color(255, 255, 255, (int)log2.alpha).getRGB());
            if (end) continue;
            Objects.requireNonNull(HitLog.mc.textRenderer);
            y -= 9 + 2;
        }
    }

    @EventHandler
    public void onPlayerDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == HitLog.mc.player || player.distanceTo(HitLog.mc.player) > 20.0f) {
            return;
        }
        int popCount = HexTech.POP.popContainer.getOrDefault(player.getName().getString(), 0);
        this.addLog("\u00a74\u00a7m" + player.getName().getString() + "\u00a7f " + popCount);
    }

    @EventHandler
    public void onTotem(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == HitLog.mc.player || player.distanceTo(HitLog.mc.player) > 20.0f) {
            return;
        }
        int popCount = HexTech.POP.popContainer.getOrDefault(player.getName().getString(), 1);
        this.addLog("\u00a7a" + player.getName().getString() + "\u00a7f " + popCount);
    }

    public void addLog(String text) {
        this.logList.add(new HitLog_STBbGhMCskXcqFYRXyft(text));
    }

    static class HitLog_STBbGhMCskXcqFYRXyft {
        final Timer timer = new Timer();
        final String text;
        double x;
        double y;
        double alpha;

        public HitLog_STBbGhMCskXcqFYRXyft(String text) {
            this.text = text;
            this.x = (double) Wrapper.mc.getWindow().getWidth() / 4.0;
            this.y = HitLog.y - 20.0;
            this.alpha = 0.0;
        }
    }
}
