package me.hextech.remapped;

import me.hextech.remapped.Wrapper;
import net.minecraft.util.math.MathHelper;

public class BetterDynamicAnimation {
    private final int maxTicks;
    private double value;
    private double dstValue;
    private int prevStep;
    private int step;

    public BetterDynamicAnimation(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public BetterDynamicAnimation() {
        this(5);
    }

    public static double createAnimation(double value) {
        return Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
    }

    public void update() {
        this.prevStep = this.step;
        this.step = MathHelper.clamp(this.step + 1, 0, this.maxTicks);
    }

    public void setValue(double value) {
        if (value != this.dstValue) {
            this.prevStep = 0;
            this.step = 0;
            this.value = this.dstValue;
            this.dstValue = value;
        }
    }

    public double getAnimationD() {
        double delta = this.dstValue - this.value;
        double animation = BetterDynamicAnimation.createAnimation((double)((float)this.prevStep + (float)(this.step - this.prevStep) * Wrapper.mc.getTickDelta()) / (double)this.maxTicks);
        return this.value + delta * animation;
    }
}
