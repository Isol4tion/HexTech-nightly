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

@Mixin(value={HeldItemRenderer.class})
public abstract class MixinHeldItemRenderer {
    @Inject(method={"renderFirstPersonItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")}, cancellable=true)
    private void onRenderItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
        HexTech.EVENT_BUS.post(event);
    }

    @Shadow
    public abstract void method_3233(LivingEntity var1, ItemStack var2, ModelTransformationMode var3, boolean var4, MatrixStack var5, VertexConsumerProvider var6, int var7);

    @Shadow
    protected abstract void method_3218(MatrixStack var1, float var2, Arm var3, ItemStack var4);

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderItemHook(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.swingAnimation.getValue() && !item.method_7960() && !(item.method_7909() instanceof FilledMapItem)) {
            ci.cancel();
            this.renderFirstPersonItemCustom(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
        }
    }

    private void renderFirstPersonItemCustom(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!player.method_31550()) {
            boolean bl = hand == Hand.field_5808;
            Arm arm = bl ? player.method_6068() : player.method_6068().method_5928();
            matrices.method_22903();
            if (item.method_31574(Items.field_8399)) {
                int i;
                boolean bl2 = CrossbowItem.method_7781((ItemStack)item);
                boolean bl3 = arm == Arm.field_6183;
                int n = i = bl3 ? 1 : -1;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    matrices.method_46416((float)i * -0.4785682f, -0.094387f, 0.05731531f);
                    matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-11.935f));
                    matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * 65.3f));
                    matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)i * -9.785f));
                    float f = (float)item.method_7935() - ((float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0f);
                    float g = f / (float)CrossbowItem.method_7775((ItemStack)item);
                    if (g > 1.0f) {
                        g = 1.0f;
                    }
                    if (g > 0.1f) {
                        float h = MathHelper.method_15374((float)((f - 0.1f) * 1.3f));
                        float j = g - 0.1f;
                        float k = h * j;
                        matrices.method_46416(k * 0.0f, k * 0.004f, k * 0.0f);
                    }
                    matrices.method_46416(g * 0.0f, g * 0.0f, g * 0.04f);
                    matrices.method_22905(1.0f, 1.0f, 1.0f + g * 0.2f);
                    matrices.method_22907(RotationAxis.field_40715.rotationDegrees((float)i * 45.0f));
                } else {
                    float f = -0.4f * MathHelper.method_15374((float)(MathHelper.method_15355((float)swingProgress) * (float)Math.PI));
                    float g = 0.2f * MathHelper.method_15374((float)(MathHelper.method_15355((float)swingProgress) * ((float)Math.PI * 2)));
                    float h = -0.2f * MathHelper.method_15374((float)(swingProgress * (float)Math.PI));
                    matrices.method_46416((float)i * f, g, h);
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    this.applySwingOffset(matrices, arm, swingProgress);
                    if (bl2 && swingProgress < 0.001f && bl) {
                        matrices.method_46416((float)i * -0.641864f, 0.0f, 0.0f);
                        matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * 10.0f));
                    }
                }
                HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
                HexTech.EVENT_BUS.post(event);
                this.method_3233((LivingEntity)player, item, bl3 ? ModelTransformationMode.field_4322 : ModelTransformationMode.field_4321, !bl3, matrices, vertexConsumers, light);
            } else {
                boolean bl2;
                boolean bl3 = bl2 = arm == Arm.field_6183;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    int l = bl2 ? 1 : -1;
                    UseAction useAction = item.method_7976();
                    if (Objects.requireNonNull(useAction) == UseAction.field_8952 || useAction == UseAction.field_8949) {
                        this.applyEquipOffset(matrices, arm, equipProgress);
                    } else if (useAction == UseAction.field_8950 || useAction == UseAction.field_8946) {
                        if (ViewModel.INSTANCE.eatAnimation.getValue()) {
                            this.applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, item);
                        } else {
                            this.method_3218(matrices, tickDelta, arm, item);
                        }
                        this.applyEquipOffset(matrices, arm, equipProgress);
                    } else if (useAction == UseAction.field_8953) {
                        this.applyEquipOffset(matrices, arm, equipProgress);
                        matrices.method_46416((float)l * -0.2785682f, 0.18344387f, 0.15731531f);
                        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-13.935f));
                        matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)l * 35.3f));
                        matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)l * -9.785f));
                        float m = (float)item.method_7935() - ((float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0f);
                        float f = m / 20.0f;
                        f = (f * f + f * 2.0f) / 3.0f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        if (f > 0.1f) {
                            float g = MathHelper.method_15374((float)((m - 0.1f) * 1.3f));
                            float h = f - 0.1f;
                            float j = g * h;
                            matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                        }
                        matrices.method_46416(f * 0.0f, f * 0.0f, f * 0.04f);
                        matrices.method_22905(1.0f, 1.0f, 1.0f + f * 0.2f);
                        matrices.method_22907(RotationAxis.field_40715.rotationDegrees((float)l * 45.0f));
                    } else if (useAction == UseAction.field_8951) {
                        this.applyEquipOffset(matrices, arm, equipProgress);
                        matrices.method_46416((float)l * -0.5f, 0.7f, 0.1f);
                        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-55.0f));
                        matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)l * 35.3f));
                        matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)l * -9.785f));
                        float m = (float)item.method_7935() - ((float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0f);
                        float f = m / 10.0f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        if (f > 0.1f) {
                            float g = MathHelper.method_15374((float)((m - 0.1f) * 1.3f));
                            float h = f - 0.1f;
                            float j = g * h;
                            matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                        }
                        matrices.method_46416(0.0f, 0.0f, f * 0.2f);
                        matrices.method_22905(1.0f, 1.0f, 1.0f + f * 0.2f);
                        matrices.method_22907(RotationAxis.field_40715.rotationDegrees((float)l * 45.0f));
                    } else if (useAction == UseAction.field_42717) {
                        this.applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
                    }
                } else if (player.method_6123()) {
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    int l = bl2 ? 1 : -1;
                    matrices.method_46416((float)l * -0.4f, 0.8f, 0.3f);
                    matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)l * 65.0f));
                    matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)l * -85.0f));
                } else {
                    matrices.method_46416(0.0f, 0.0f, 0.0f);
                    this.applyEquipOffset(matrices, arm, equipProgress);
                    this.applySwingOffset(matrices, arm, swingProgress);
                }
                HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
                HexTech.EVENT_BUS.post(event);
                this.method_3233((LivingEntity)player, item, bl2 ? ModelTransformationMode.field_4322 : ModelTransformationMode.field_4321, !bl2, matrices, vertexConsumers, light);
            }
            matrices.method_22909();
        }
    }

    private void applyEquipOffset(@NotNull MatrixStack matrices, Arm arm, float equipProgress) {
        int i = arm == Arm.field_6183 ? 1 : -1;
        matrices.method_46416((float)i * 0.56f, -0.52f + equipProgress * -0.6f, -0.72f);
    }

    private void applySwingOffset(@NotNull MatrixStack matrices, Arm arm, float swingProgress) {
        int i = arm == Arm.field_6183 ? 1 : -1;
        float f = MathHelper.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * (45.0f + f * -20.0f)));
        float g = MathHelper.method_15374((float)(MathHelper.method_15355((float)swingProgress) * (float)Math.PI));
        matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)i * g * -20.0f));
        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(g * -80.0f));
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * -45.0f));
    }

    private void applyEatOrDrinkTransformationCustom(MatrixStack matrices, float tickDelta, Arm arm, @NotNull ItemStack stack) {
        float h;
        float f = (float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0f;
        float g = f / (float)stack.method_7935();
        if (g < 0.8f) {
            h = MathHelper.method_15379((float)(MathHelper.method_15362((float)(f / 4.0f * (float)Math.PI)) * 0.005f));
            matrices.method_46416(0.0f, h, 0.0f);
        }
        h = 1.0f - (float)Math.pow(g, 27.0);
        int i = arm == Arm.field_6183 ? 1 : -1;
        matrices.method_22904((double)(h * 0.6f * (float)i) * ViewModel.INSTANCE.eatX.getValue(), (double)(h * -0.5f) * ViewModel.INSTANCE.eatY.getValue(), (double)(h * 0.0f));
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees((float)i * h * 90.0f));
        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(h * 10.0f));
        matrices.method_22907(RotationAxis.field_40718.rotationDegrees((float)i * h * 30.0f));
    }

    @Inject(method={"applyEatOrDrinkTransformation"}, at={@At(value="HEAD")}, cancellable=true)
    private void applyEatOrDrinkTransformationHook(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        if (ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.eatAnimation.getValue()) {
            this.applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, stack);
            ci.cancel();
        }
    }

    private void applyBrushTransformation(MatrixStack matrices, float tickDelta, Arm arm, @NotNull ItemStack stack, float equipProgress) {
        this.applyEquipOffset(matrices, arm, equipProgress);
        float f = (float)Wrapper.mc.field_1724.method_6014() - tickDelta + 1.0f;
        float g = 1.0f - f / (float)stack.method_7935();
        float m = -15.0f + 75.0f * MathHelper.method_15362((float)(g * 45.0f * (float)Math.PI));
        if (arm != Arm.field_6183) {
            matrices.method_22904(0.1, 0.83, 0.35);
            matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(RotationAxis.field_40716.rotationDegrees(-90.0f));
            matrices.method_22907(RotationAxis.field_40714.rotationDegrees(m));
            matrices.method_22904(-0.3, 0.22, 0.35);
        } else {
            matrices.method_22904(-0.25, 0.22, 0.35);
            matrices.method_22907(RotationAxis.field_40714.rotationDegrees(-80.0f));
            matrices.method_22907(RotationAxis.field_40716.rotationDegrees(90.0f));
            matrices.method_22907(RotationAxis.field_40718.rotationDegrees(0.0f));
            matrices.method_22907(RotationAxis.field_40714.rotationDegrees(m));
        }
    }
}
