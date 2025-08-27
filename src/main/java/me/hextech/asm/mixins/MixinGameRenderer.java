package me.hextech.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.HexTech;
import me.hextech.remapped.AspectRatio;
import me.hextech.remapped.CustomFov;
import me.hextech.remapped.FreeCam;
import me.hextech.remapped.InteractUtil;
import me.hextech.remapped.MineTweak;
import me.hextech.remapped.NoRender;
import me.hextech.remapped.TextUtil;
import me.hextech.remapped.TotemAnimation;
import me.hextech.remapped.Zoom_qxASoURSmqLSKrnTPdNq;
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
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRenderer.class})
public class MixinGameRenderer {
   @Shadow
   @Final
   MinecraftClient field_4015;
   @Shadow
   private float field_4005;
   @Shadow
   private float field_3988;
   @Shadow
   private float field_4004;
   @Shadow
   private float field_4025;

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
      if (floatingItem.method_7909() == Items.field_8288 && NoRender.INSTANCE.isOn() && NoRender.INSTANCE.totem.getValue()) {
         info.cancel();
      }
   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void showFloatingItemHook(ItemStack floatingItem, CallbackInfo info) {
      if (TotemAnimation.instance.isOn()) {
         TotemAnimation.instance.showFloatingItem(floatingItem);
         info.cancel();
      }
   }

   @Inject(
      method = {"renderFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderFloatingItemHook(int scaledWidth, int scaledHeight, float tickDelta, CallbackInfo ci) {
      if (TotemAnimation.instance.isOn()) {
         TotemAnimation.instance.renderFloatingItem(scaledWidth, scaledHeight, tickDelta);
         ci.cancel();
      }
   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"
      )
   )
   private float applyCameraTransformationsMathHelperLerpProxy(float delta, float first, float second) {
      return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.nausea.getValue() ? 0.0F : MathHelper.method_16439(delta, first, second);
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tiltViewWhenHurtHook(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.hurtCam.getValue()) {
         ci.cancel();
      }
   }

   @Inject(
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
         opcode = 180,
         ordinal = 0
      )},
      method = {"renderWorld"}
   )
   void render3dHook(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
      TextUtil.lastProjMat.set(RenderSystem.getProjectionMatrix());
      TextUtil.lastModMat.set(RenderSystem.getModelViewMatrix());
      TextUtil.lastWorldSpaceMatrix.set(matrix.method_23760().method_23761());
      HexTech.FPS.record();
      HexTech.MODULE.render3D(matrix);
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V",
         shift = Shift.AFTER
      )}
   )
   public void postRender3dHook(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
      HexTech.SHADER.renderShaders();
   }

   @Inject(
      method = {"getFov(Lnet/minecraft/client/render/Camera;FZ)D"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cb) {
      if (CustomFov.INSTANCE.isOn()) {
         if ((Double)cb.getReturnValue() == 70.0 && !CustomFov.INSTANCE.itemFov.getValue()) {
            return;
         }

         if (CustomFov.INSTANCE.itemFov.getValue() && (Double)cb.getReturnValue() == 70.0) {
            cb.setReturnValue(CustomFov.INSTANCE.itemFovModifier.getValue());
            return;
         }

         if (CustomFov.INSTANCE.usefov.getValue()) {
            cb.setReturnValue(CustomFov.INSTANCE.fov.getValue());
         }

         if ((Double)cb.getReturnValue() == 70.0) {
            return;
         }

         if (Zoom_qxASoURSmqLSKrnTPdNq.on) {
            cb.setReturnValue(Math.min(Math.max((Double)cb.getReturnValue() - Zoom_qxASoURSmqLSKrnTPdNq.INSTANCE.currentFov, 1.0), 177.0));
         }
      }
   }

   @Inject(
      method = {"getBasicProjectionMatrix"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void getBasicProjectionMatrixHook(double fov, CallbackInfoReturnable<Matrix4f> cir) {
      if (AspectRatio.INSTANCE.isOn()) {
         MatrixStack matrixStack = new MatrixStack();
         matrixStack.method_23760().method_23761().identity();
         if (this.field_4005 != 1.0F) {
            matrixStack.method_46416(this.field_3988, -this.field_4004, 0.0F);
            matrixStack.method_22905(this.field_4005, this.field_4005, 1.0F);
         }

         matrixStack.method_23760()
            .method_23761()
            .mul(
               new Matrix4f()
                  .setPerspective((float)(fov * (float) (Math.PI / 180.0)), AspectRatio.INSTANCE.ratio.getValueFloat(), 0.05F, this.field_4025 * 4.0F)
            );
         cir.setReturnValue(matrixStack.method_23760().method_23761());
      }
   }

   @Inject(
      method = {"updateTargetedEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void updateTargetedEntityHook(float tickDelta, CallbackInfo ci) {
      ci.cancel();
      this.update(tickDelta);
   }

   @Unique
   public void update(float tickDelta) {
      Entity entity = this.field_4015.method_1560();
      if (entity != null && this.field_4015.field_1687 != null) {
         this.field_4015.method_16011().method_15396("pick");
         this.field_4015.field_1692 = null;
         double d = (double)this.field_4015.field_1761.method_2904();
         MineTweak.INSTANCE.isActive = MineTweak.INSTANCE.ghostHand();
         this.field_4015.field_1765 = entity.method_5745(d, tickDelta, false);
         MineTweak.INSTANCE.isActive = false;
         Vec3d vec3d = entity.method_5836(tickDelta);
         boolean bl = false;
         double e = d;
         if (this.field_4015.field_1761.method_2926()) {
            e = 6.0;
            d = e;
         } else if (d > 3.0) {
            bl = true;
         }

         e *= e;
         if (this.field_4015.field_1765 != null) {
            e = this.field_4015.field_1765.method_17784().method_1025(vec3d);
         }

         Vec3d vec3d2 = entity.method_5828(1.0F);
         Vec3d vec3d3 = vec3d.method_1031(vec3d2.field_1352 * d, vec3d2.field_1351 * d, vec3d2.field_1350 * d);
         Box box = entity.method_5829().method_18804(vec3d2.method_1021(d)).method_1009(1.0, 1.0, 1.0);
         if (FreeCam.INSTANCE.isOn()) {
            this.field_4015.field_1765 = InteractUtil.getRtxTarget(
               FreeCam.INSTANCE.getFakeYaw(),
               FreeCam.INSTANCE.getFakePitch(),
               FreeCam.INSTANCE.getFakeX(),
               FreeCam.INSTANCE.getFakeY(),
               FreeCam.INSTANCE.getFakeZ()
            );
            this.field_4015.method_16011().method_15407();
            return;
         }

         if (!MineTweak.INSTANCE.noEntityTrace()) {
            EntityHitResult entityHitResult = ProjectileUtil.method_18075(
               entity, vec3d, vec3d3, box, entityx -> !entityx.method_7325() && entityx.method_5863(), e
            );
            if (entityHitResult != null) {
               Entity entity2 = entityHitResult.method_17782();
               Vec3d vec3d4 = entityHitResult.method_17784();
               double g = vec3d.method_1025(vec3d4);
               if (bl && g > 9.0) {
                  this.field_4015.field_1765 = BlockHitResult.method_17778(
                     vec3d4, Direction.method_10142(vec3d2.field_1352, vec3d2.field_1351, vec3d2.field_1350), BlockPos.method_49638(vec3d4)
                  );
               } else if (g < e || this.field_4015.field_1765 == null) {
                  this.field_4015.field_1765 = entityHitResult;
                  if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                     this.field_4015.field_1692 = entity2;
                  }
               }
            }
         }

         this.field_4015.method_16011().method_15407();
      }
   }
}
