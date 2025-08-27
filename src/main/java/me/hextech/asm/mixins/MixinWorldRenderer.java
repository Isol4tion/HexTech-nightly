package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.FreeCam;
import me.hextech.remapped.NoInterp;
import me.hextech.remapped.ShaderManager;
import me.hextech.remapped.Shader_CLqIXXaHSdAoBoxRSgjR;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={WorldRenderer.class})
public abstract class MixinWorldRenderer {
    @Final
    @Shadow
    private MinecraftClient field_4088;
    @Shadow
    @Final
    private EntityRenderDispatcher field_4109;

    @Shadow
    protected abstract void method_3250(MatrixStack var1);

    @Inject(method={"renderEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderEntityHook(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (NoInterp.INSTANCE.isOn() && entity != MinecraftClient.getInstance().player && entity instanceof PlayerEntity) {
            ci.cancel();
            double d = entity.getX();
            double e = entity.getY();
            double f = entity.getZ();
            float g = entity.getYaw();
            this.field_4109.method_3954(entity, d - cameraX, e - cameraY, f - cameraZ, g, NoInterp.INSTANCE.tickDelta.getValueFloat(), matrices, vertexConsumers, this.field_4109.getLight(entity, tickDelta));
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal=0))
    void replaceShaderHook(PostEffectProcessor instance, float tickDelta) {
        ShaderManager.Mode shaders = Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.mode.getValue();
        if (Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.isOn() && Wrapper.mc.world != null) {
            HexTech.SHADER.setupShader(shaders, HexTech.SHADER.getShaderOutline(shaders));
        } else {
            instance.method_1258(tickDelta);
        }
    }

    @Inject(method={"renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderSkyHead(MatrixStack matrices, Matrix4f matrix4f, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo info) {
        if (Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.isOn() && Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.sky.getValue()) {
            HexTech.SHADER.applyShader(() -> this.method_3250(matrices), Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.skyMode.getValue());
            info.cancel();
        }
    }

    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"), index=3)
    private boolean renderSetupTerrainModifyArg(boolean spectator) {
        return FreeCam.INSTANCE.isOn() || spectator;
    }
}
