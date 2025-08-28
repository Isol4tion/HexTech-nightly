package me.hextech.asm.mixins;

import me.hextech.mod.modules.impl.render.CrystalChams;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value={EndCrystalEntityRenderer.class})
public abstract class MixinEndCrystalEntityRenderer
extends EntityRenderer<EndCrystalEntity> {
    @Mutable
    @Final
    @Shadow
    private static RenderLayer END_CRYSTAL;
    @Shadow
    @Final
    private static Identifier TEXTURE;
    @Final
    @Shadow
    private static float SINE_45_DEGREES;
    @Unique
    final Identifier BLANK = new Identifier("textures/blank.png");
    @Final
    @Shadow
    private ModelPart core;
    @Final
    @Shadow
    private ModelPart frame;
    @Final
    @Shadow
    private ModelPart bottom;

    protected MixinEndCrystalEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Unique
    private float yOffset(EndCrystalEntity crystal, float tickDelta) {
        float f = ((float)crystal.endCrystalAge + tickDelta) * CrystalChams.INSTANCE.floatValue.getValueFloat();
        float g = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f * CrystalChams.INSTANCE.bounceHeight.getValueFloat();
        return g - 1.4f + CrystalChams.INSTANCE.floatOffset.getValueFloat();
    }

    @Inject(method={"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void render(EndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        CrystalChams module = CrystalChams.INSTANCE;
        if (!module.sync.getValue()) {
            return;
        }
        END_CRYSTAL = RenderLayer.getEntityTranslucent(module.isOn() && !module.texture.getValue() ? this.BLANK : TEXTURE);
        if (!module.isOn()) {
            return;
        }
        ci.cancel();
        matrixStack.push();
        float h = this.yOffset(endCrystalEntity, g);
        float j = (float)((double)(((float)endCrystalEntity.endCrystalAge + g) * 3.0f) * module.spinValue.getValue());
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        matrixStack.push();
        matrixStack.scale(2.0f * module.scale.getValueFloat(), 2.0f * module.scale.getValueFloat(), 2.0f * module.scale.getValueFloat());
        matrixStack.translate(0.0f, -0.5f, 0.0f);
        int k = OverlayTexture.DEFAULT_UV;
        if (endCrystalEntity.shouldShowBottom()) {
            this.bottom.render(matrixStack, vertexConsumer, i, k);
        }
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        matrixStack.translate(0.0f, 1.5f + h / 2.0f, 0.0f);
        matrixStack.multiply(new Quaternionf().setAngleAxis(1.0471976f, SINE_45_DEGREES, 0.0f, SINE_45_DEGREES));
        Color color = module.outerFrame.getValue();
        if (module.outerFrame.booleanValue) {
            this.frame.render(matrixStack, vertexConsumer, i, k, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        }
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternionf().setAngleAxis(1.0471976f, SINE_45_DEGREES, 0.0f, SINE_45_DEGREES));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        color = module.innerFrame.getValue();
        if (module.innerFrame.booleanValue) {
            this.frame.render(matrixStack, vertexConsumer, i, k, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        }
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternionf().setAngleAxis(1.0471976f, SINE_45_DEGREES, 0.0f, SINE_45_DEGREES));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        color = module.core.getValue();
        if (module.core.booleanValue) {
            this.core.render(matrixStack, vertexConsumer, i, k, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        }
        matrixStack.pop();
        matrixStack.pop();
        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        if (blockPos != null) {
            float m = (float)blockPos.getX() + 0.5f;
            float n = (float)blockPos.getY() + 0.5f;
            float o = (float)blockPos.getZ() + 0.5f;
            float p = (float)((double)m - endCrystalEntity.getX());
            float q = (float)((double)n - endCrystalEntity.getY());
            float r = (float)((double)o - endCrystalEntity.getZ());
            matrixStack.translate(p, q, r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + h, -r, g, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, i);
        }
        super.render(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
