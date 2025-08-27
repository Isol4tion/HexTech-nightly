package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.ParticleEvent;
import me.hextech.remapped.ParticleEvent_nCuHsVGACULnIozEhCmM;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ParticleManager.class})
public class MixinParticleManager {
    @Inject(at={@At(value="HEAD")}, method={"addParticle(Lnet/minecraft/client/particle/Particle;)V"}, cancellable=true)
    public void onAddParticle(Particle particle, CallbackInfo ci) {
        ParticleEvent event = new ParticleEvent(particle);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;)V"}, cancellable=true)
    public void onAddEmmiter(Entity entity, ParticleEffect particleEffect, CallbackInfo ci) {
        ParticleEvent_nCuHsVGACULnIozEhCmM event = new ParticleEvent_nCuHsVGACULnIozEhCmM(particleEffect);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V"}, cancellable=true)
    public void onAddEmmiterAged(Entity entity, ParticleEffect particleEffect, int maxAge, CallbackInfo ci) {
        ParticleEvent_nCuHsVGACULnIozEhCmM event = new ParticleEvent_nCuHsVGACULnIozEhCmM(particleEffect);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
