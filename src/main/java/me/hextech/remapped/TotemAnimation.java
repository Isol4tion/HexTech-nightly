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
            matrixStack.method_22903();
            float f22 = (float)i + tickDelta;
            float n3 = 50.0f + 175.0f * MathHelper.method_15374((float)k);
            if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.FadeOut)) {
                float x2 = (float)(Math.sin(f22 * 112.0f / 180.0f) * 100.0);
                float y2 = (float)(Math.cos(f22 * 112.0f / 180.0f) * 50.0);
                matrixStack.method_46416((float)(scaledWidth / 2) + x2, (float)(scaledHeight / 2) + y2, -50.0f);
                matrixStack.method_22905(n3, -n3, n3);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Size)) {
                matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0f);
                matrixStack.method_22905(n3, -n3, n3);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Otkisuli)) {
                matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0f);
                matrixStack.method_22907(RotationAxis.field_40714.rotationDegrees(f22 * 2.0f));
                matrixStack.method_22907(RotationAxis.field_40718.rotationDegrees(f22 * 2.0f));
                matrixStack.method_22905(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Insert)) {
                matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0f);
                matrixStack.method_22907(RotationAxis.field_40714.rotationDegrees(f22 * 3.0f));
                matrixStack.method_22905(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Fall)) {
                downFactor = (float)(Math.pow(f22, 3.0) * (double)0.2f);
                matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2) + downFactor, -50.0f);
                matrixStack.method_22907(RotationAxis.field_40718.rotationDegrees(f22 * 5.0f));
                matrixStack.method_22905(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Rocket)) {
                downFactor = (float)(Math.pow(f22, 3.0) * (double)0.2f) - 20.0f;
                matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2) - downFactor, -50.0f);
                matrixStack.method_22907(RotationAxis.field_40716.rotationDegrees(f22 * (float)this.floatingItemTimeLeft * 2.0f));
                matrixStack.method_22905(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            } else if (((Enum)this.mode.getValue()).equals((Object)_ZVxiGMTWPCIGCYyTrffL.Roll)) {
                float rightFactor = (float)(Math.pow(f22, 2.0) * 4.5);
                matrixStack.method_46416((float)(scaledWidth / 2) + rightFactor, (float)(scaledHeight / 2), -50.0f);
                matrixStack.method_22907(RotationAxis.field_40718.rotationDegrees(f22 * 40.0f));
                matrixStack.method_22905(200.0f - f22 * 1.5f, -200.0f + f22 * 1.5f, 200.0f - f22 * 1.5f);
            }
            VertexConsumerProvider.Immediate immediate = mc.method_22940().method_23000();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)(1.0f - f2));
            mc.method_1480().method_23178(this.floatingItem, ModelTransformationMode.field_4319, 0xF000F0, OverlayTexture.field_21444, matrixStack, (VertexConsumerProvider)immediate, (World)TotemAnimation.mc.field_1687, 0);
            matrixStack.method_22909();
            immediate.method_22993();
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

    public static final class _ZVxiGMTWPCIGCYyTrffL
    extends Enum<_ZVxiGMTWPCIGCYyTrffL> {
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL FadeOut;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Size;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Otkisuli;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Insert;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Fall;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Rocket;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Roll;
        public static final /* enum */ _ZVxiGMTWPCIGCYyTrffL Off;

        public static _ZVxiGMTWPCIGCYyTrffL[] values() {
            return null;
        }

        public static _ZVxiGMTWPCIGCYyTrffL valueOf(String string) {
            return null;
        }
    }
}
