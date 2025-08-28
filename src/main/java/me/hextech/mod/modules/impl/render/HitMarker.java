package me.hextech.mod.modules.impl.render;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.math.Timer;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
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
        super("HitMarker", Category.Render);
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
                drawContext.drawTexture(this.marker, mc.getWindow().getScaledWidth() / 2 - 8, mc.getWindow().getScaledHeight() / 2 - 8, 0, 0.0f, 0.0f, 16, 16, 16, 16);
            }
        }
    }

    @EventHandler
    public void onpacket(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            this.ticks = 0;
        }
    }
}
