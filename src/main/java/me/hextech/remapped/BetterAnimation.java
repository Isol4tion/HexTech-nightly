package me.hextech.remapped;

import me.hextech.remapped.Wrapper;
import net.minecraft.util.math.MathHelper;

public class BetterAnimation {
    private final int maxTick;
    private int prevTick;
    private int tick;

    public BetterAnimation(int maxTick) {
        this.maxTick = maxTick;
    }

    public BetterAnimation() {
        this(10);
    }

    public static double dropAnimation(double value) {
        double c1 = 1.70158;
        double c3 = 2.70158;
        return 1.0 + c3 * Math.pow(value - 1.0, 3.0) + c1 * Math.pow(value - 1.0, 2.0);
    }

    public void update(boolean update) {
        this.prevTick = this.tick;
        this.tick = MathHelper.clamp((int)(this.tick + (update ? 1 : -1)), (int)0, (int)this.maxTick);
    }

    public double getAnimationd() {
        return BetterAnimation.dropAnimation(((float)this.prevTick + (float)(this.tick - this.prevTick) * Wrapper.mc.method_1488()) / (float)this.maxTick);
    }
}
