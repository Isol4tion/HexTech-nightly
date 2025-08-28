package me.hextech.mod.modules.impl.movement;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.events.impl.RotateEvent;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.MovementUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.player.Blink;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

public class TickShift
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static TickShift INSTANCE;
    static DecimalFormat df;
    public final BooleanSetting usestep = this.add(new BooleanSetting("UseStep", true));
    private final BooleanSetting onlyBlink = this.add(new BooleanSetting("OnlyBlink", true));
    private final SliderSetting multiplier = this.add(new SliderSetting("Speed", 2.0, 1.0, 10.0, 0.1));
    private final SliderSetting accumulate = this.add(new SliderSetting("Charge", 2000.0, 1.0, 10000.0, 50.0).setSuffix("ms"));
    private final SliderSetting minAccumulate = this.add(new SliderSetting("MinCharge", 500.0, 1.0, 10000.0, 50.0).setSuffix("ms"));
    private final BooleanSetting smooth = this.add(new BooleanSetting("Smooth", true).setParent());
    private final EnumSetting<FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad> quad = this.add(new EnumSetting<FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad>("Quad", FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad.In, v -> this.smooth.isOpen()));
    private final BooleanSetting reset = this.add(new BooleanSetting("Reset", true));
    private final BooleanSetting indicator = this.add(new BooleanSetting("Indicator", true).setParent());
    private final ColorSetting work = this.add(new ColorSetting("Completed", new Color(0, 255, 0), v -> this.indicator.isOpen()));
    private final ColorSetting charging = this.add(new ColorSetting("Charging", new Color(255, 0, 0), v -> this.indicator.isOpen()));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0.0, -200.0, 200.0, 1.0, v -> this.indicator.isOpen()));
    private final Timer timer = new Timer();
    private final Timer timer2 = new Timer();
    private final FadeUtils_DPfHthPqEJdfXfNYhDbG end = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
    long lastMs = 0L;
    boolean moving = false;
    private int normalLookPos;
    private int rotationMode;
    private int normalPos;

    public TickShift() {
        super("TickShift", Category.Movement);
        INSTANCE = this;
    }

    public static float nextFloat(float startInclusive, float endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f ? startInclusive : (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random());
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        if (TickShift.nullCheck()) {
            return;
        }
        if (this.onlyBlink.getValue() && Blink.INSTANCE.isOff()) {
            return;
        }
        this.timer.setMs(Math.min(Math.max(0L, this.timer.getPassedTimeMs()), this.accumulate.getValueInt()));
        if (MovementUtil.isMoving() && !EntityUtil.isInsideBlock()) {
            if (!this.moving) {
                if (this.timer.passedMs(this.minAccumulate.getValue())) {
                    this.timer2.reset();
                    this.lastMs = this.timer.getPassedTimeMs();
                } else {
                    this.lastMs = 0L;
                }
                this.moving = true;
            }
            this.timer.reset();
            if (this.timer2.passed(this.lastMs)) {
                HexTech.TIMER.reset();
            } else if (this.smooth.getValue()) {
                double timer = (double)HexTech.TIMER.getDefault() + (1.0 - this.end.getQuad(this.quad.getValue())) * (double)(this.multiplier.getValueFloat() - 1.0f) * ((double)this.lastMs / this.accumulate.getValue());
                HexTech.TIMER.set((float)Math.max(HexTech.TIMER.getDefault(), timer));
            } else {
                HexTech.TIMER.set(this.multiplier.getValueFloat());
            }
        } else {
            if (this.moving) {
                HexTech.TIMER.reset();
                if (this.reset.getValue()) {
                    this.timer.reset();
                } else {
                    this.timer.setMs(Math.max(this.lastMs - this.timer2.getPassedTimeMs(), 0L));
                }
                this.moving = false;
            }
            this.end.setLength(this.timer.getPassedTimeMs());
            this.end.reset();
        }
        if (this.indicator.getValue()) {
            double current = this.moving ? Math.max(this.lastMs - this.timer2.getPassedTimeMs(), 0L) : this.timer.getPassedTimeMs();
            boolean completed = this.moving && current > 0.0 || current >= (double)this.minAccumulate.getValueInt();
            double max = this.accumulate.getValue();
            String text = df.format(current / max * 100.0) + "%";
            TextRenderer textRenderer = TickShift.mc.textRenderer;
            int n = mc.getWindow().getScaledWidth() / 2 - TickShift.mc.textRenderer.getWidth(text) / 2;
            int n2 = mc.getWindow().getScaledHeight() / 2;
            Objects.requireNonNull(TickShift.mc.textRenderer);
            drawContext.drawText(textRenderer, text, n, n2 + 9 - this.yOffset.getValueInt(), completed ? this.work.getValue().getRGB() : this.charging.getValue().getRGB(), true);
        }
    }

    @Override
    public String getInfo() {
        double current = this.moving ? Math.max(this.lastMs - this.timer2.getPassedTimeMs(), 0L) : this.timer.getPassedTimeMs();
        double max = this.accumulate.getValue();
        double value = Math.min(current / max * 100.0, 100.0);
        return df.format(value) + "%";
    }

    @EventHandler
    public void onReceivePacket(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.lastMs = 0L;
        }
    }

    @Override
    public void onDisable() {
        if (Blink.INSTANCE.isOff()) {
            Step_EShajbhvQeYkCdreEeNY.INSTANCE.disable();
        }
        HexTech.TIMER.reset();
    }

    @Override
    public void onEnable() {
        if (this.usestep.getValue() && Blink.INSTANCE.isOn()) {
            Step_EShajbhvQeYkCdreEeNY.INSTANCE.enable();
        }
        HexTech.TIMER.reset();
    }

    @EventHandler
    public final void onPacketSend(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        if (TickShift.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket.PositionAndOnGround && this.rotationMode == 1) {
            ++this.normalPos;
            if (this.normalPos > 20) {
                this.rotationMode = 2;
            }
        } else if (event.getPacket() instanceof PlayerMoveC2SPacket.Full && this.rotationMode == 2) {
            ++this.normalLookPos;
            if (this.normalLookPos > 20) {
                this.rotationMode = 1;
            }
        }
    }

    @EventHandler
    public final void RotateEvent(RotateEvent event) {
        if (this.rotationMode == 2) {
            event.setRotation(event.getYaw() + TickShift.nextFloat(1.0f, 3.0f), event.getPitch() + TickShift.nextFloat(1.0f, 3.0f));
        }
    }

    static {
        df = new DecimalFormat("0.0");
    }
}
