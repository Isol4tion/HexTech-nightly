package me.hextech.remapped;

import java.awt.Color;
import java.util.Random;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.TotemParticleEvent;

public class TotemParticle
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static TotemParticle INSTANCE;
    public final SliderSetting velocityXZ = this.add(new SliderSetting("VelocityXZ", 100.0, 0.0, 500.0, 1.0).setSuffix("%"));
    public final SliderSetting velocityY = this.add(new SliderSetting("VelocityY", 100.0, 0.0, 500.0, 1.0).setSuffix("%"));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));
    private final ColorSetting color2 = this.add(new ColorSetting("Color2", new Color(0, 0, 0, 255)));
    Random random = new Random();

    public TotemParticle() {
        super("TotemParticle", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    @EventHandler
    public void idk(TotemParticleEvent event) {
        event.cancel();
        event.velocityZ *= this.velocityXZ.getValue() / 100.0;
        event.velocityX *= this.velocityXZ.getValue() / 100.0;
        event.velocityY *= this.velocityY.getValue() / 100.0;
        event.color = ColorUtil.fadeColor(this.color.getValue(), this.color2.getValue(), this.random.nextDouble());
    }
}
