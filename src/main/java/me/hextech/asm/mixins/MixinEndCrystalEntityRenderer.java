package me.hextech.asm.mixins;

import java.awt.Color;
import me.hextech.remapped.CrystalChams;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EndCrystalEntityRenderer.class})
public abstract class MixinEndCrystalEntityRenderer extends EntityRenderer<EndCrystalEntity> {
   @Mutable
   @Final
   @Shadow
   private static RenderLayer field_21736;
   @Shadow
   @Final
   private static Identifier field_4663;
   @Final
   @Shadow
   private static float field_21002;
   @Unique
   final Identifier BLANK = new Identifier("textures/blank.png");
   @Final
   @Shadow
   private ModelPart field_21003;
   @Final
   @Shadow
   private ModelPart field_21004;
   @Final
   @Shadow
   private ModelPart field_21005;

   protected MixinEndCrystalEntityRenderer(Context ctx) {
      super(ctx);
   }

   @Unique
   private float yOffset(EndCrystalEntity crystal, float tickDelta) {
      float f = ((float)crystal.field_7034 + tickDelta) * CrystalChams.INSTANCE.floatValue.getValueFloat();
      float g = MathHelper.method_15374(f * 0.2F) / 2.0F + 0.5F;
      g = (g * g + g) * 0.4F * CrystalChams.INSTANCE.bounceHeight.getValueFloat();
      return g - 1.4F + CrystalChams.INSTANCE.floatOffset.getValueFloat();
   }

   @Inject(
      method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(
      EndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci
   ) {
      CrystalChams module = CrystalChams.INSTANCE;
      if (module.sync.getValue()) {
         field_21736 = RenderLayer.method_23580(module.isOn() && !module.texture.getValue() ? this.BLANK : field_4663);
         if (module.isOn()) {
            ci.cancel();
            matrixStack.method_22903();
            float h = this.yOffset(endCrystalEntity, g);
            float j = (float)((double)(((float)endCrystalEntity.field_7034 + g) * 3.0F) * module.spinValue.getValue());
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(field_21736);
            matrixStack.method_22903();
            matrixStack.method_22905(2.0F * module.scale.getValueFloat(), 2.0F * module.scale.getValueFloat(), 2.0F * module.scale.getValueFloat());
            matrixStack.method_46416(0.0F, -0.5F, 0.0F);
            int k = OverlayTexture.field_21444;
            if (endCrystalEntity.method_6836()) {
               this.field_21005.method_22698(matrixStack, vertexConsumer, i, k);
            }

            matrixStack.method_22907(RotationAxis.field_40716.rotationDegrees(j));
            matrixStack.method_46416(0.0F, 1.5F + h / 2.0F, 0.0F);
            matrixStack.method_22907(new Quaternionf().setAngleAxis((float) (Math.PI / 3), field_21002, 0.0F, field_21002));
            Color color = module.outerFrame.getValue();
            if (module.outerFrame.booleanValue) {
               this.field_21004
                  .method_22699(
                     matrixStack,
                     vertexConsumer,
                     i,
                     k,
                     (float)color.getRed() / 255.0F,
                     (float)color.getGreen() / 255.0F,
                     (float)color.getBlue() / 255.0F,
                     (float)color.getAlpha() / 255.0F
                  );
            }

            matrixStack.method_22905(0.875F, 0.875F, 0.875F);
            matrixStack.method_22907(new Quaternionf().setAngleAxis((float) (Math.PI / 3), field_21002, 0.0F, field_21002));
            matrixStack.method_22907(RotationAxis.field_40716.rotationDegrees(j));
            color = module.innerFrame.getValue();
            if (module.innerFrame.booleanValue) {
               this.field_21004
                  .method_22699(
                     matrixStack,
                     vertexConsumer,
                     i,
                     k,
                     (float)color.getRed() / 255.0F,
                     (float)color.getGreen() / 255.0F,
                     (float)color.getBlue() / 255.0F,
                     (float)color.getAlpha() / 255.0F
                  );
            }

            matrixStack.method_22905(0.875F, 0.875F, 0.875F);
            matrixStack.method_22907(new Quaternionf().setAngleAxis((float) (Math.PI / 3), field_21002, 0.0F, field_21002));
            matrixStack.method_22907(RotationAxis.field_40716.rotationDegrees(j));
            color = module.core.getValue();
            if (module.core.booleanValue) {
               this.field_21003
                  .method_22699(
                     matrixStack,
                     vertexConsumer,
                     i,
                     k,
                     (float)color.getRed() / 255.0F,
                     (float)color.getGreen() / 255.0F,
                     (float)color.getBlue() / 255.0F,
                     (float)color.getAlpha() / 255.0F
                  );
            }

            matrixStack.method_22909();
            matrixStack.method_22909();
            BlockPos blockPos = endCrystalEntity.method_6838();
            if (blockPos != null) {
               float m = (float)blockPos.method_10263() + 0.5F;
               float n = (float)blockPos.method_10264() + 0.5F;
               float o = (float)blockPos.method_10260() + 0.5F;
               float p = (float)((double)m - endCrystalEntity.method_23317());
               float q = (float)((double)n - endCrystalEntity.method_23318());
               float r = (float)((double)o - endCrystalEntity.method_23321());
               matrixStack.method_46416(p, q, r);
               EnderDragonEntityRenderer.method_3917(-p, -q + h, -r, g, endCrystalEntity.field_7034, matrixStack, vertexConsumerProvider, i);
            }

            super.method_3936(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
         }
      }
   }
}
