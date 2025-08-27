package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class TotemAnimation
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static TotemAnimation instance;
    private final EnumSetting mode = this.add(new EnumSetting<>("Mode", _ZVxiGMTWPCIGCYyTrffL.FadeOut));
    private ItemStack floatingItem = null;
    private int floatingItemTimeLeft;

    public TotemAnimation() {
        super("TotemAnimation", Module_JlagirAibYQgkHtbRnhw.Render);
        instance = this;
    }

    public void showFloatingItem(ItemStack floatingItem) {
        this.floatingItem = floatingItem;
        this.floatingItemTimeLeft = this.getTime();
    }

    @Override
    public void onUpdate() {
        if (this.floatingItemTimeLeft > 0) {
            --this.floatingItemTimeLeft;
            if (this.floatingItemTimeLeft == 0) {
                this.floatingItem = null;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    public void renderFloatingItem(int scaledWidth, int scaledHeight, float tickDelta) {
        if (this.floatingItem != null && this.floatingItemTimeLeft > 0 && !this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Off)) {
            final int i = this.getTime() - this.floatingItemTimeLeft;
            final float f = (i + tickDelta) / this.getTime();
            final float g = f * f;
            final float h = f * g;
            final float j = 10.25f * h * g - 24.95f * g * g + 25.5f * h - 13.8f * g + 4.0f * f;
            final float k = j * 3.1415927f;
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            final MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            final float f2 = i + tickDelta;
            final float n = 50.0f + 175.0f * MathHelper.sin(k);
            if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.FadeOut)) {
                final float x2 = (float) (Math.sin(f2 * 112.0f / 180.0f) * 100.0);
                final float y2 = (float) (Math.cos(f2 * 112.0f / 180.0f) * 50.0);
                matrixStack.translate(scaledWidth / 2 + x2, scaledHeight / 2 + y2, -50.0f);
                matrixStack.scale(n, -n, n);
            } else if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Size)) {
                matrixStack.translate((float) (scaledWidth / 2), (float) (scaledHeight / 2), -50.0f);
                matrixStack.scale(n, -n, n);
            } else if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Otkisuli)) {
                matrixStack.translate((float) (scaledWidth / 2), (float) (scaledHeight / 2), -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f2 * 2.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f2 * 2.0f));
                matrixStack.scale(200.0f - f2 * 1.5f, -200.0f + f2 * 1.5f, 200.0f - f2 * 1.5f);
            } else if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Insert)) {
                matrixStack.translate((float) (scaledWidth / 2), (float) (scaledHeight / 2), -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f2 * 3.0f));
                matrixStack.scale(200.0f - f2 * 1.5f, -200.0f + f2 * 1.5f, 200.0f - f2 * 1.5f);
            } else if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Fall)) {
                final float downFactor = (float) (Math.pow(f2, 3.0) * 0.20000000298023224);
                matrixStack.translate((float) (scaledWidth / 2), scaledHeight / 2 + downFactor, -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f2 * 5.0f));
                matrixStack.scale(200.0f - f2 * 1.5f, -200.0f + f2 * 1.5f, 200.0f - f2 * 1.5f);
            } else if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Rocket)) {
                final float downFactor = (float) (Math.pow(f2, 3.0) * 0.20000000298023224) - 20.0f;
                matrixStack.translate((float) (scaledWidth / 2), scaledHeight / 2 - downFactor, -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f2 * this.floatingItemTimeLeft * 2.0f));
                matrixStack.scale(200.0f - f2 * 1.5f, -200.0f + f2 * 1.5f, 200.0f - f2 * 1.5f);
            } else if (this.mode.getValue().equals(_ZVxiGMTWPCIGCYyTrffL.Roll)) {
                final float rightFactor = (float) (Math.pow(f2, 2.0) * 4.5);
                matrixStack.translate(scaledWidth / 2 + rightFactor, (float) (scaledHeight / 2), -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f2 * 40.0f));
                matrixStack.scale(200.0f - f2 * 1.5f, -200.0f + f2 * 1.5f, 200.0f - f2 * 1.5f);
            }
            final VertexConsumerProvider.Immediate immediate = TotemAnimation.mc.getBufferBuilders().getEntityVertexConsumers();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f - f);
            TotemAnimation.mc.getItemRenderer().renderItem(this.floatingItem, ModelTransformationMode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrixStack, (VertexConsumerProvider) immediate, (World) TotemAnimation.mc.world, 0);
            matrixStack.pop();
            immediate.draw();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.disableDepthTest();
        }
    }

    private int getTime() {
        if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.FadeOut)) {
            return 10;
        }
        if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Insert)) {
            return 20;
        }
        return 40;
    }

    public enum _ZVxiGMTWPCIGCYyTrffL {
        FadeOut,
        Size,
        Otkisuli,
        Insert,
        Fall,
        Rocket,
        Roll,
        Off;

    }
}
