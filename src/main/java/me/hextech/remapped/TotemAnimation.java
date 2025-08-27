package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class TotemAnimation
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static TotemAnimation instance;
    private final EnumSetting mode = this.add(new EnumSetting<_ZVxiGMTWPCIGCYyTrffL>("Mode", _ZVxiGMTWPCIGCYyTrffL.FadeOut));
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
    public void renderFloatingItem(int n, int n2, float f) {
        if (this.floatingItem != null && this.floatingItemTimeLeft > 0 && !((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Off)) {
            void tickDelta;
            int i = this.getTime() - this.floatingItemTimeLeft;
            float f2 = ((float)i + tickDelta) / (float)this.getTime();
            float g = f2 * f2;
            float h = f2 * g;
            float j = 10.25f * h * g - 24.95f * g * g + 25.5f * h - 13.8f * g + 4.0f * f2;
            float k = j * (float)Math.PI;
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            float f22 = (float)i + tickDelta;
            float n3 = 50.0f + 175.0f * MathHelper.sin((float)k);
            if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.FadeOut)) {
                float x2 = (float)(Math.sin(f22 * 112.0f / 180.0f) * 100.0);
                float y2 = (float)(Math.cos(f22 * 112.0f / 180.0f) * 50.0);
                matrixStack.translate((float)(scaledWidth / 2) + x2, (float)(scaledHeight / 2) + y2, -50.0f);
                matrixStack.scale(n3, -n3, n3);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Size)) {
                matrixStack.translate((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0f);
                matrixStack.scale(n3, -n3, n3);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Otkisuli)) {
                matrixStack.translate((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f22 * 2.0f));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f22 * 2.0f));
                matrixStack.scale(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Insert)) {
                matrixStack.translate((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f22 * 3.0f));
                matrixStack.scale(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Fall)) {
                downFactor = (float)(Math.pow(f22, 3.0) * (double)0.2f);
                matrixStack.translate((float)(scaledWidth / 2), (float)(scaledHeight / 2) + downFactor, -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f22 * 5.0f));
                matrixStack.scale(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Rocket)) {
                downFactor = (float)(Math.pow(f22, 3.0) * (double)0.2f) - 20.0f;
                matrixStack.translate((float)(scaledWidth / 2), (float)(scaledHeight / 2) - downFactor, -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f22 * (float)this.floatingItemTimeLeft * 2.0f));
                matrixStack.scale(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Roll)) {
                float rightFactor = (float)(Math.pow(f22, 2.0) * 4.5);
                matrixStack.translate((float)(scaledWidth / 2) + rightFactor, (float)(scaledHeight / 2), -50.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f22 * 40.0f));
                matrixStack.scale(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            }
            VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)(1.0f - f2));
            mc.getItemRenderer().renderItem(this.floatingItem, ModelTransformationMode.FIXED, 0xF000F0, OverlayTexture.DEFAULT_UV, matrixStack, (VertexConsumerProvider)immediate, (World)TotemAnimation.mc.world, 0);
            matrixStack.pop();
            immediate.draw();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
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

    public static enum _ZVxiGMTWPCIGCYyTrffL {
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
