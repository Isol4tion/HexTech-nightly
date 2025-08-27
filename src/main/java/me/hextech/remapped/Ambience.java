package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

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
        super("Ambience", "Custom ambience", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.fullBright.getValue()) {
            Ambience.mc.player.method_6092(new StatusEffectInstance(StatusEffects.field_5925, 100000, 0));
        }
        if (this.customTime.getValue()) {
            Ambience.mc.world.method_8435((long)this.time.getValue());
        }
    }

    @Override
    public void onEnable() {
        if (Ambience.nullCheck()) {
            return;
        }
        this.oldTime = Ambience.mc.world.method_8532();
    }

    @Override
    public void onDisable() {
        if (Ambience.nullCheck()) {
            return;
        }
        Ambience.mc.player.method_6016(StatusEffects.field_5925);
        Ambience.mc.world.method_8435(this.oldTime);
    }

    @EventHandler
    public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            this.oldTime = ((WorldTimeUpdateS2CPacket)event.getPacket()).method_11871();
            event.cancel();
        }
    }
}
