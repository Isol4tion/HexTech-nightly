package me.hextech.asm.mixins;

import java.awt.Color;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.NoRender;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class})
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    @Unique
    private LivingEntity lastEntity;
    @Unique
    private float originalYaw;
    @Unique
    private float originalHeadYaw;
    @Unique
    private float originalBodyYaw;
    @Unique
    private float originalPitch;
    @Unique
    private float originalPrevYaw;
    @Unique
    private float originalPrevHeadYaw;
    @Unique
    private float originalPrevBodyYaw;

    @Inject(method={"render"}, at={@At(value="HEAD")})
    public void onRenderPre(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (MinecraftClient.method_1551().field_1724 != null && livingEntity == MinecraftClient.method_1551().field_1724 && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotations.getValue()) {
            this.originalYaw = livingEntity.method_36454();
            this.originalHeadYaw = ((LivingEntity)livingEntity).field_6241;
            this.originalBodyYaw = ((LivingEntity)livingEntity).field_6283;
            this.originalPitch = livingEntity.method_36455();
            this.originalPrevYaw = ((LivingEntity)livingEntity).field_5982;
            this.originalPrevHeadYaw = ((LivingEntity)livingEntity).field_6259;
            this.originalPrevBodyYaw = ((LivingEntity)livingEntity).field_6220;
            livingEntity.method_36456(RotateManager.getRenderYawOffset());
            ((LivingEntity)livingEntity).field_6241 = RotateManager.getRotationYawHead();
            ((LivingEntity)livingEntity).field_6283 = RotateManager.getRenderYawOffset();
            livingEntity.method_36457(RotateManager.getRenderPitch());
            ((LivingEntity)livingEntity).field_5982 = RotateManager.getPrevRenderYawOffset();
            ((LivingEntity)livingEntity).field_6259 = RotateManager.getPrevRotationYawHead();
            ((LivingEntity)livingEntity).field_6220 = RotateManager.getPrevRenderYawOffset();
            ((LivingEntity)livingEntity).field_6004 = RotateManager.getPrevPitch();
        }
        this.lastEntity = livingEntity;
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    public void onRenderPost(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (MinecraftClient.method_1551().field_1724 != null && livingEntity == MinecraftClient.method_1551().field_1724 && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotations.getValue()) {
            livingEntity.method_36456(this.originalYaw);
            ((LivingEntity)livingEntity).field_6241 = this.originalHeadYaw;
            ((LivingEntity)livingEntity).field_6283 = this.originalBodyYaw;
            livingEntity.method_36457(this.originalPitch);
            ((LivingEntity)livingEntity).field_5982 = this.originalPrevYaw;
            ((LivingEntity)livingEntity).field_6259 = this.originalPrevHeadYaw;
            ((LivingEntity)livingEntity).field_6220 = this.originalPrevBodyYaw;
            ((LivingEntity)livingEntity).field_6004 = this.originalPitch;
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void onRenderModel(EntityModel entityModel, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        Color newColor = new Color(red, green, blue, alpha);
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.antiPlayerCollision.getValue() && this.lastEntity != Wrapper.mc.field_1724) {
            float overrideAlpha = (float)(Wrapper.mc.field_1724.method_5707(this.lastEntity.method_19538()) / 3.0) + 0.07f;
            newColor = ColorUtil.injectAlpha(newColor, (int)(255.0f * MathUtil.clamp(overrideAlpha, 0.0f, 1.0f)));
        }
        entityModel.method_2828(matrices, vertices, light, overlay, (float)newColor.getRed() / 255.0f, (float)newColor.getGreen() / 255.0f, (float)newColor.getBlue() / 255.0f, (float)newColor.getAlpha() / 255.0f);
    }
}
