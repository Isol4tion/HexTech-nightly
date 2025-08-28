package me.hextech.mod.modules.impl.render;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.TotemParticleEvent;
import me.hextech.api.utils.render.ColorUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;

import java.awt.*;
import java.util.Random;

public class TotemParticle
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static TotemParticle INSTANCE;
    public final SliderSetting velocityXZ = this.add(new SliderSetting("VelocityXZ", 100.0, 0.0, 500.0, 1.0).setSuffix("%"));
    public final SliderSetting velocityY = this.add(new SliderSetting("VelocityY", 100.0, 0.0, 500.0, 1.0).setSuffix("%"));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));
    private final ColorSetting color2 = this.add(new ColorSetting("Color2", new Color(0, 0, 0, 255)));
    Random random = new Random();

    public TotemParticle() {
        super("TotemParticle", Category.Render);
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
