package me.hextech.asm.mixins;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Particle.class})
public abstract class MixinParticle {
    @Shadow
    protected double field_3852;
    @Shadow
    protected double field_3869;
    @Shadow
    protected double field_3850;

    @Shadow
    public abstract void method_3084(float var1, float var2, float var3);

    @Shadow
    protected void method_3083(float alpha) {
    }
}
