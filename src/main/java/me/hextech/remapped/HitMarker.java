package me.hextech.remapped;

import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Identifier;

public class HitMarker
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final Identifier marker = new Identifier("nullpoint", "hitmarker.png");
    public SliderSetting time = this.add(new SliderSetting("Show Time", 3, 0, 60));
    public Timer timer = new Timer();
    public int ticks = 114514;

    public HitMarker() {
        super("HitMarker", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    @Override
    public void onEnable() {
        this.ticks = 114514;
        this.timer.reset();
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        if (this.timer.passedMs(0L)) {
            this.timer.reset();
            if ((float)this.ticks <= this.time.getValueFloat()) {
                ++this.ticks;
                drawContext.method_25291(this.marker, mc.method_22683().method_4486() / 2 - 8, mc.method_22683().method_4502() / 2 - 8, 0, 0.0f, 0.0f, 16, 16, 16, 16);
            }
        }
    }

    @EventHandler
    public void onpacket(PacketEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            this.ticks = 0;
        }
    }
}
