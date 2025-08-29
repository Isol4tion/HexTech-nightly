package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.api.events.impl.TotemParticleEvent;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = {TotemParticle.class})
public abstract class MixinTotemParticle
        extends MixinParticle {
    @Inject(method = {"<init>"}, at = {@At(value = "TAIL")})
    private void hookInit(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        TotemParticleEvent event = new TotemParticleEvent(velocityX, velocityY, velocityZ);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            this.velocityX = event.velocityX;
            this.velocityY = event.velocityY;
            this.velocityZ = event.velocityZ;
            Color color = event.color;
            if (color != null) {
                this.setColor((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f);
                this.setAlpha((float) color.getAlpha() / 255.0f);
            }
        }
    }
}
