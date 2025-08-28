package me.hextech.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.HexTech;
import me.hextech.remapped.mod.modules.impl.render.AspectRatio;
import me.hextech.remapped.CustomFov;
import me.hextech.remapped.FreeCam;
import me.hextech.remapped.InteractUtil;
import me.hextech.remapped.MineTweak;
import me.hextech.remapped.NoRender;
import me.hextech.remapped.TextUtil;
import me.hextech.remapped.TotemAnimation;
import me.hextech.remapped.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GameRenderer.class})
public class MixinGameRenderer {
    @Shadow
    @Final
    MinecraftClient client;
    @Shadow
    private float zoom;
    @Shadow
    private float zoomX;
    @Shadow
    private float zoomY;
    @Shadow
    private float viewDistance;

    @Inject(method={"showFloatingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && NoRender.INSTANCE.isOn() && NoRender.INSTANCE.totem.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"showFloatingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void showFloatingItemHook(ItemStack floatingItem, CallbackInfo info) {
        if (TotemAnimation.instance.isOn()) {
            TotemAnimation.instance.showFloatingItem(floatingItem);
            info.cancel();
        }
    }

    @Inject(method={"renderFloatingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderFloatingItemHook(int scaledWidth, int scaledHeight, float tickDelta, CallbackInfo ci) {
        if (TotemAnimation.instance.isOn()) {
            TotemAnimation.instance.renderFloatingItem(scaledWidth, scaledHeight, tickDelta);
            ci.cancel();
        }
    }

    @Redirect(method={"renderWorld"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float applyCameraTransformationsMathHelperLerpProxy(float delta, float first, float second) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.nausea.getValue()) {
            return 0.0f;
        }
        return MathHelper.lerp(delta, first, second);
    }

    @Inject(method={"tiltViewWhenHurt"}, at={@At(value="HEAD")}, cancellable=true)
    private void tiltViewWhenHurtHook(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.hurtCam.getValue()) {
            ci.cancel();
        }
    }

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode=180, ordinal=0)}, method={"renderWorld"})
    void render3dHook(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        TextUtil.lastProjMat.set(RenderSystem.getProjectionMatrix());
        TextUtil.lastModMat.set(RenderSystem.getModelViewMatrix());
        TextUtil.lastWorldSpaceMatrix.set(matrix.peek().getPositionMatrix());
        HexTech.FPS.record();
        HexTech.MODULE.render3D(matrix);
    }

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V", shift=At.Shift.AFTER)})
    public void postRender3dHook(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        HexTech.SHADER.renderShaders();
    }

    @Inject(method={"getFov(Lnet/minecraft/client/render/Camera;FZ)D"}, at={@At(value="TAIL")}, cancellable=true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cb) {
        if (CustomFov.INSTANCE.isOn()) {
            if (cb.getReturnValue() == 70.0 && !CustomFov.INSTANCE.itemFov.getValue()) {
                return;
            }
            if (CustomFov.INSTANCE.itemFov.getValue() && cb.getReturnValue() == 70.0) {
                cb.setReturnValue(CustomFov.INSTANCE.itemFovModifier.getValue());
                return;
            }
            if (CustomFov.INSTANCE.usefov.getValue()) {
                cb.setReturnValue(CustomFov.INSTANCE.fov.getValue());
            }
            if (cb.getReturnValue() == 70.0) {
                return;
            }
            if (Zoom.on) {
                cb.setReturnValue(Math.min(Math.max(cb.getReturnValue() - Zoom.INSTANCE.currentFov, 1.0), 177.0));
            }
        }
    }

    @Inject(method={"getBasicProjectionMatrix"}, at={@At(value="TAIL")}, cancellable=true)
    public void getBasicProjectionMatrixHook(double fov, CallbackInfoReturnable<Matrix4f> cir) {
        if (AspectRatio.INSTANCE.isOn()) {
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            if (this.zoom != 1.0f) {
                matrixStack.translate(this.zoomX, -this.zoomY, 0.0f);
                matrixStack.scale(this.zoom, this.zoom, 1.0f);
            }
            matrixStack.peek().getPositionMatrix().mul(new Matrix4f().setPerspective((float)(fov * 0.01745329238474369), AspectRatio.INSTANCE.ratio.getValueFloat(), 0.05f, this.viewDistance * 4.0f));
            cir.setReturnValue(matrixStack.peek().getPositionMatrix());
        }
    }

    @Inject(method={"updateTargetedEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateTargetedEntityHook(float tickDelta, CallbackInfo ci) {
        ci.cancel();
        this.update(tickDelta);
    }

    @Unique
    public void update(float tickDelta) {
        Entity entity = this.client.getCameraEntity();
        if (entity != null && this.client.world != null) {
            EntityHitResult entityHitResult;
            this.client.getProfiler().push("pick");
            this.client.targetedEntity = null;
            double d = this.client.interactionManager.getReachDistance();
            MineTweak.INSTANCE.isActive = MineTweak.INSTANCE.ghostHand();
            this.client.crosshairTarget = entity.raycast(d, tickDelta, false);
            MineTweak.INSTANCE.isActive = false;
            Vec3d vec3d = entity.getCameraPosVec(tickDelta);
            boolean bl = false;
            double e = d;
            if (this.client.interactionManager.hasExtendedReach()) {
                d = e = 6.0;
            } else if (d > 3.0) {
                bl = true;
            }
            e *= e;
            if (this.client.crosshairTarget != null) {
                e = this.client.crosshairTarget.getPos().squaredDistanceTo(vec3d);
            }
            Vec3d vec3d2 = entity.getRotationVec(1.0f);
            Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
            Box box = entity.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
            if (FreeCam.INSTANCE.isOn()) {
                this.client.crosshairTarget = InteractUtil.getRtxTarget(FreeCam.INSTANCE.getFakeYaw(), FreeCam.INSTANCE.getFakePitch(), FreeCam.INSTANCE.getFakeX(), FreeCam.INSTANCE.getFakeY(), FreeCam.INSTANCE.getFakeZ());
                this.client.getProfiler().pop();
                return;
            }
            if (!MineTweak.INSTANCE.noEntityTrace() && (entityHitResult = ProjectileUtil.raycast(entity, vec3d, vec3d3, box, entityx -> !entityx.isSpectator() && entityx.canHit(), e)) != null) {
                Entity entity2 = entityHitResult.getEntity();
                Vec3d vec3d4 = entityHitResult.getPos();
                double g = vec3d.squaredDistanceTo(vec3d4);
                if (bl && g > 9.0) {
                    this.client.crosshairTarget = BlockHitResult.createMissed(vec3d4, Direction.getFacing(vec3d2.x, vec3d2.y, vec3d2.z), BlockPos.ofFloored(vec3d4));
                } else if (g < e || this.client.crosshairTarget == null) {
                    this.client.crosshairTarget = entityHitResult;
                    if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                        this.client.targetedEntity = entity2;
                    }
                }
            }
            this.client.getProfiler().pop();
        }
    }
}
