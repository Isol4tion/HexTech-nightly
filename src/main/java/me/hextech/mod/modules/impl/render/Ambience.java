package me.hextech.mod.modules.impl.render;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.awt.*;

public class Ambience
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Ambience INSTANCE;
    public final ColorSetting worldColor = this.add(new ColorSetting("WorldColor", new Color(-1, true)).injectBoolean(true));
    public final BooleanSetting customTime = this.add(new BooleanSetting("CustomTime", false).setParent());
    public final ColorSetting fog = this.add(new ColorSetting("FogColor", new Color(13401557)).injectBoolean(false));
    public final ColorSetting sky = this.add(new ColorSetting("SkyColor", new Color(0)).injectBoolean(false));
    public final ColorSetting cloud = this.add(new ColorSetting("CloudColor", new Color(0)).injectBoolean(false));
    public final ColorSetting dimensionColor = this.add(new ColorSetting("DimensionColor", new Color(0)).injectBoolean(false));
    public final BooleanSetting fogDistance = this.add(new BooleanSetting("FogDistance", false).setParent());
    public final SliderSetting fogStart = this.add(new SliderSetting("FogStart", 50, 0, 1000, v -> this.fogDistance.isOpen()));
    public final SliderSetting fogEnd = this.add(new SliderSetting("FogEnd", 100, 0, 1000, v -> this.fogDistance.isOpen()));
    public final BooleanSetting fullBright = this.add(new BooleanSetting("FullBright", false));
    public final BooleanSetting forceOverworld = this.add(new BooleanSetting("ForceOverworld", false));
    public final BooleanSetting customLuminance = this.add(new BooleanSetting("CustomLuminance", false).setParent().injectTask(() -> {
        if (!Ambience.nullCheck()) {
            Ambience.mc.worldRenderer.reload();
        }
    }));
    public final SliderSetting luminance = this.add(new SliderSetting("Luminance", 15, 0, 15, v -> this.customLuminance.isOpen()).injectTask(() -> {
        if (!Ambience.nullCheck() && this.customLuminance.getValue()) {
            Ambience.mc.worldRenderer.reload();
        }
    }));
    private final SliderSetting time = this.add(new SliderSetting("Time", 0, 0, 24000, v -> this.customTime.isOpen()));
    long oldTime;

    public Ambience() {
        super("Ambience", "Custom ambience", Category.Render);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.fullBright.getValue()) {
            Ambience.mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 100000, 0));
        }
        if (this.customTime.getValue()) {
            Ambience.mc.world.setTimeOfDay((long) this.time.getValue());
        }
    }

    @Override
    public void onEnable() {
        if (Ambience.nullCheck()) {
            return;
        }
        this.oldTime = Ambience.mc.world.getTimeOfDay();
    }

    @Override
    public void onDisable() {
        if (Ambience.nullCheck()) {
            return;
        }
        Ambience.mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        Ambience.mc.world.setTimeOfDay(this.oldTime);
    }

    @EventHandler
    public void onReceivePacket(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            this.oldTime = ((WorldTimeUpdateS2CPacket) event.getPacket()).getTime();
            event.cancel();
        }
    }
}
