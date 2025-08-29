package me.hextech.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.mod.modules.impl.render.Ambience;
import me.hextech.mod.modules.impl.render.NoRender;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {BackgroundRenderer.class})
public class MixinBackgroundRenderer {
    @Inject(method = {"applyFog"}, at = {@At(value = "TAIL")})
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        if (Ambience.INSTANCE.isOn()) {
            if (Ambience.INSTANCE.fog.booleanValue) {
                RenderSystem.setShaderFogColor((float) Ambience.INSTANCE.fog.getValue().getRed() / 255.0f, (float) Ambience.INSTANCE.fog.getValue().getGreen() / 255.0f, (float) Ambience.INSTANCE.fog.getValue().getBlue() / 255.0f, (float) Ambience.INSTANCE.fog.getValue().getAlpha() / 255.0f);
            }
            if (Ambience.INSTANCE.fogDistance.getValue()) {
                RenderSystem.setShaderFogStart(Ambience.INSTANCE.fogStart.getValueFloat());
                RenderSystem.setShaderFogEnd(Ambience.INSTANCE.fogEnd.getValueFloat());
            }
        }
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fog.getValue() && fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            RenderSystem.setShaderFogStart(viewDistance * 4.0f);
            RenderSystem.setShaderFogEnd(viewDistance * 4.25f);
        }
    }

    @Inject(method = {"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"}, at = {@At(value = "HEAD")}, cancellable = true)
    private static void onGetFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Object> info) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.blindness.getValue()) {
            info.setReturnValue(null);
        }
    }
}
