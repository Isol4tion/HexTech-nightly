package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.KeyboardInputEvent;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;

public class FreeCam
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static FreeCam INSTANCE;
    final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
    private final SliderSetting speed = this.add(new SliderSetting("HSpeed", 1.0, 0.0, 3.0));
    private final SliderSetting hspeed = this.add(new SliderSetting("VSpeed", 0.42, 0.0, 3.0));
    private float fakeYaw;
    private float fakePitch;
    private float prevFakeYaw;
    private float prevFakePitch;
    private double fakeX;
    private double fakeY;
    private double fakeZ;
    private double prevFakeX;
    private double prevFakeY;
    private double prevFakeZ;
    private float preYaw;
    private float prePitch;

    public FreeCam() {
        super("FreeCam", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (FreeCam.nullCheck()) {
            this.disable();
            return;
        }
        FreeCam.mc.field_1730 = false;
        this.preYaw = FreeCam.mc.player.method_36454();
        this.prePitch = FreeCam.mc.player.method_36455();
        this.fakePitch = FreeCam.mc.player.method_36455();
        this.fakeYaw = FreeCam.mc.player.method_36454();
        this.prevFakePitch = this.fakePitch;
        this.prevFakeYaw = this.fakeYaw;
        this.fakeX = FreeCam.mc.player.getX();
        this.fakeY = FreeCam.mc.player.getY() + (double)FreeCam.mc.player.method_18381(FreeCam.mc.player.method_18376());
        this.fakeZ = FreeCam.mc.player.getZ();
        this.prevFakeX = this.fakeX;
        this.prevFakeY = this.fakeY;
        this.prevFakeZ = this.fakeZ;
    }

    @Override
    public void onDisable() {
        FreeCam.mc.field_1730 = true;
    }

    @Override
    public void onUpdate() {
        if (this.rotate.getValue() && FreeCam.mc.field_1765 != null && FreeCam.mc.field_1765.method_17784() != null) {
            float[] angle = EntityUtil.getLegitRotations(FreeCam.mc.field_1765.method_17784());
            this.preYaw = angle[0];
            this.prePitch = angle[1];
        }
    }

    @EventHandler(priority=200)
    public void onRotate(RotateEvent event) {
        event.setYawNoModify(this.preYaw);
        event.setPitchNoModify(this.prePitch);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        this.prevFakeYaw = this.fakeYaw;
        this.prevFakePitch = this.fakePitch;
        this.fakeYaw = FreeCam.mc.player.method_36454();
        this.fakePitch = FreeCam.mc.player.method_36455();
    }

    @EventHandler
    public void onKeyboardInput(KeyboardInputEvent event) {
        if (FreeCam.mc.player == null) {
            return;
        }
        double[] motion = MovementUtil.directionSpeed(this.speed.getValue());
        this.prevFakeX = this.fakeX;
        this.prevFakeY = this.fakeY;
        this.prevFakeZ = this.fakeZ;
        this.fakeX += motion[0];
        this.fakeZ += motion[1];
        if (FreeCam.mc.field_1690.field_1903.method_1434()) {
            this.fakeY += this.hspeed.getValue();
        }
        if (FreeCam.mc.field_1690.field_1832.method_1434()) {
            this.fakeY -= this.hspeed.getValue();
        }
        FreeCam.mc.player.field_3913.field_3905 = 0.0f;
        FreeCam.mc.player.field_3913.field_3907 = 0.0f;
        FreeCam.mc.player.field_3913.field_3904 = false;
        FreeCam.mc.player.field_3913.field_3903 = false;
    }

    public float getFakeYaw() {
        return (float)MathUtil.interpolate(this.prevFakeYaw, this.fakeYaw, mc.getTickDelta());
    }

    public float getFakePitch() {
        return (float)MathUtil.interpolate(this.prevFakePitch, this.fakePitch, mc.getTickDelta());
    }

    public double getFakeX() {
        return MathUtil.interpolate(this.prevFakeX, this.fakeX, mc.getTickDelta());
    }

    public double getFakeY() {
        return MathUtil.interpolate(this.prevFakeY, this.fakeY, mc.getTickDelta());
    }

    public double getFakeZ() {
        return MathUtil.interpolate(this.prevFakeZ, this.fakeZ, mc.getTickDelta());
    }
}
