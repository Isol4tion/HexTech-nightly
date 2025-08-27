package me.hextech.asm.mixins;

import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.remapped.HeldItemRendererEvent;
import me.hextech.remapped.ViewModel;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemRenderer.class})
public abstract class MixinHeldItemRenderer {
   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
      )},
      cancellable = true
   )
   private void onRenderItem(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
      HexTech.EVENT_BUS.post(event);
   }

   @Shadow
   public abstract void method_3233(
      LivingEntity var1, ItemStack var2, ModelTransformationMode var3, boolean var4, MatrixStack var5, VertexConsumerProvider var6, int var7
   );

   @Shadow
   protected abstract void method_3218(MatrixStack var1, float var2, Arm var3, ItemStack var4);

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderItemHook(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.swingAnimation.getValue() && !item.method_7960() && !(item.method_7909() instanceof FilledMapItem)) {
         ci.cancel();
         this.renderFirstPersonItemCustom(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
      }
   }

   private void renderFirstPersonItemCustom(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light
   ) {
      if (!player.method_31550()) {
         boolean bl = hand == Hand.field_5808;
         Arm arm = bl ? player.method_6068() : player.method_6068().method_5928();
         matrices.method_22903();
         if (item.method_31574(Items.field_8399)) {
            boolean bl2 = CrossbowItem.method_7781(item);
            boolean bl3 = arm == Arm.field_6183;
            int i = bl3 ? 1 : -1;
            if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               matrices.method_46416((float)i * -0.4785682F, -0.094387F, 0.05731531F);
               matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-11.935F));
               matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * 65.3F));
               matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)i * -9.785F));
               float f = (float)item.method_7935() - ((float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0F);
               float g = f / (float)CrossbowItem.method_7775(item);
               if (g > 1.0F) {
                  g = 1.0F;
               }

               if (g > 0.1F) {
                  float h = MathHelper.method_15374((f - 0.1F) * 1.3F);
                  float j = g - 0.1F;
                  float k = h * j;
                  matrices.method_46416(k * 0.0F, k * 0.004F, k * 0.0F);
               }

               matrices.method_46416(g * 0.0F, g * 0.0F, g * 0.04F);
               matrices.method_22905(1.0F, 1.0F, 1.0F + g * 0.2F);
               matrices.method_22907(RotationAxis.field_40715.rotationDegrees((float)i * 45.0F));
            } else {
               float fx = -0.4F * MathHelper.method_15374(MathHelper.method_15355(swingProgress) * (float) Math.PI);
               float gx = 0.2F * MathHelper.method_15374(MathHelper.method_15355(swingProgress) * (float) (Math.PI * 2));
               float h = -0.2F * MathHelper.method_15374(swingProgress * (float) Math.PI);
               matrices.method_46416((float)i * fx, gx, h);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
               if (bl2 && swingProgress < 0.001F && bl) {
                  matrices.method_46416((float)i * -0.641864F, 0.0F, 0.0F);
                  matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * 10.0F));
               }
            }

            HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
            HexTech.EVENT_BUS.post(event);
            this.method_3233(
               player, item, bl3 ? ModelTransformationMode.field_4322 : ModelTransformationMode.field_4321, !bl3, matrices, vertexConsumers, light
            );
         } else {
            boolean bl2 = arm == Arm.field_6183;
            if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
               int l = bl2 ? 1 : -1;
               UseAction useAction = item.method_7976();
               if (Objects.requireNonNull(useAction) == UseAction.field_8952 || useAction == UseAction.field_8949) {
                  this.applyEquipOffset(matrices, arm, equipProgress);
               } else if (useAction != UseAction.field_8950 && useAction != UseAction.field_8946) {
                  if (useAction == UseAction.field_8953) {
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     matrices.method_46416((float)l * -0.2785682F, 0.18344387F, 0.15731531F);
                     matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-13.935F));
                     matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)l * 35.3F));
                     matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)l * -9.785F));
                     float m = (float)item.method_7935() - ((float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0F);
                     float fx = m / 20.0F;
                     fx = (fx * fx + fx * 2.0F) / 3.0F;
                     if (fx > 1.0F) {
                        fx = 1.0F;
                     }

                     if (fx > 0.1F) {
                        float gx = MathHelper.method_15374((m - 0.1F) * 1.3F);
                        float h = fx - 0.1F;
                        float j = gx * h;
                        matrices.method_46416(j * 0.0F, j * 0.004F, j * 0.0F);
                     }

                     matrices.method_46416(fx * 0.0F, fx * 0.0F, fx * 0.04F);
                     matrices.method_22905(1.0F, 1.0F, 1.0F + fx * 0.2F);
                     matrices.method_22907(RotationAxis.field_40715.rotationDegrees((float)l * 45.0F));
                  } else if (useAction == UseAction.field_8951) {
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     matrices.method_46416((float)l * -0.5F, 0.7F, 0.1F);
                     matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-55.0F));
                     matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)l * 35.3F));
                     matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)l * -9.785F));
                     float mx = (float)item.method_7935() - ((float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0F);
                     float fxx = mx / 10.0F;
                     if (fxx > 1.0F) {
                        fxx = 1.0F;
                     }

                     if (fxx > 0.1F) {
                        float gx = MathHelper.method_15374((mx - 0.1F) * 1.3F);
                        float h = fxx - 0.1F;
                        float j = gx * h;
                        matrices.method_46416(j * 0.0F, j * 0.004F, j * 0.0F);
                     }

                     matrices.method_46416(0.0F, 0.0F, fxx * 0.2F);
                     matrices.method_22905(1.0F, 1.0F, 1.0F + fxx * 0.2F);
                     matrices.method_22907(RotationAxis.field_40715.rotationDegrees((float)l * 45.0F));
                  } else if (useAction == UseAction.field_42717) {
                     this.applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
                  }
               } else {
                  if (ViewModel.INSTANCE.eatAnimation.getValue()) {
                     this.applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, item);
                  } else {
                     this.method_3218(matrices, tickDelta, arm, item);
                  }

                  this.applyEquipOffset(matrices, arm, equipProgress);
               }
            } else if (player.method_6123()) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               int l = bl2 ? 1 : -1;
               matrices.method_46416((float)l * -0.4F, 0.8F, 0.3F);
               matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)l * 65.0F));
               matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)l * -85.0F));
            } else {
               matrices.method_46416(0.0F, 0.0F, 0.0F);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
            }

            HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
            HexTech.EVENT_BUS.post(event);
            this.method_3233(
               player, item, bl2 ? ModelTransformationMode.field_4322 : ModelTransformationMode.field_4321, !bl2, matrices, vertexConsumers, light
            );
         }

         matrices.method_22909();
      }
   }

   private void applyEquipOffset(@NotNull MatrixStack matrices, Arm arm, float equipProgress) {
      int i = arm == Arm.field_6183 ? 1 : -1;
      matrices.method_46416((float)i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
   }

   private void applySwingOffset(@NotNull MatrixStack matrices, Arm arm, float swingProgress) {
      int i = arm == Arm.field_6183 ? 1 : -1;
      float f = MathHelper.method_15374(swingProgress * swingProgress * (float) Math.PI);
      matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * (45.0F + f * -20.0F)));
      float g = MathHelper.method_15374(MathHelper.method_15355(swingProgress) * (float) Math.PI);
      matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)i * g * -20.0F));
      matrices.method_22907(RotationAxis.field_40714.rotationDegrees(g * -80.0F));
      matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * -45.0F));
   }

   private void applyEatOrDrinkTransformationCustom(MatrixStack matrices, float tickDelta, Arm arm, @NotNull ItemStack stack) {
      float f = (float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0F;
      float g = f / (float)stack.method_7935();
      if (g < 0.8F) {
         float h = MathHelper.method_15379(MathHelper.method_15362(f / 4.0F * (float) Math.PI) * 0.005F);
         matrices.method_46416(0.0F, h, 0.0F);
      }

      float h = 1.0F - (float)Math.pow((double)g, 27.0);
      int i = arm == Arm.field_6183 ? 1 : -1;
      matrices.method_22904(
         (double)(h * 0.6F * (float)i) * ViewModel.INSTANCE.eatX.getValue(), (double)(h * -0.5F) * ViewModel.INSTANCE.eatY.getValue(), (double)(h * 0.0F)
      );
      matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * h * 90.0F));
      matrices.method_22907(RotationAxis.field_40714.rotationDegrees(h * 10.0F));
      matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)i * h * 30.0F));
   }

   @Inject(
      method = {"applyEatOrDrinkTransformation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void applyEatOrDrinkTransformationHook(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
      if (ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.eatAnimation.getValue()) {
         this.applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, stack);
         ci.cancel();
      }
   }

   private void applyBrushTransformation(MatrixStack matrices, float tickDelta, Arm arm, @NotNull ItemStack stack, float equipProgress) {
      this.applyEquipOffset(matrices, arm, equipProgress);
      float f = (float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0F;
      float g = 1.0F - f / (float)stack.method_7935();
      float m = -15.0F + 75.0F * MathHelper.method_15362(g * 45.0F * (float) Math.PI);
      if (arm != Arm.field_6183) {
         matrices.method_22904(0.1, 0.83, 0.35);
         matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-80.0F));
         matrices.method_22907(RotationAxis.field_40716.rotationDegrees(-90.0F));
         matrices.method_22907(RotationAxis.field_40714.rotationDegrees(m));
         matrices.method_22904(-0.3, 0.22, 0.35);
      } else {
         matrices.method_22904(-0.25, 0.22, 0.35);
         matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-80.0F));
         matrices.method_22907(RotationAxis.field_40716.rotationDegrees(90.0F));
         matrices.method_22907(RotationAxis.field_40718.rotationDegrees(0.0F));
         matrices.method_22907(RotationAxis.field_40714.rotationDegrees(m));
      }
   }
}
