package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.HexTech;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.DesyncESP;
import me.hextech.remapped.DesyncESP_EZqjXHyHjNyfFrdyAmeM;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Event;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.opengl.GL11;

public final class DesyncESP_dCvptoNghaTFSegtZyHR
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static DesyncESP_dCvptoNghaTFSegtZyHR INSTANCE;
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
    private final EnumSetting<DesyncESP> type = this.add(new EnumSetting<DesyncESP>("Type", DesyncESP.ServerSide));
    DesyncESP_EZqjXHyHjNyfFrdyAmeM model;
    boolean update = true;
    float lastYaw;
    float lastPitch;

    public DesyncESP_dCvptoNghaTFSegtZyHR() {
        super("DesyncESP", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    private static void prepareScale(MatrixStack matrixStack) {
        matrixStack.method_22905(-1.0f, -1.0f, 1.0f);
        matrixStack.method_22905(1.6f, 1.8f, 1.6f);
        matrixStack.method_46416(0.0f, -1.501f, 0.0f);
    }

    @Override
    public void onLogin() {
        this.update = true;
    }

    @Override
    public void onUpdate() {
        if (DesyncESP_dCvptoNghaTFSegtZyHR.nullCheck()) {
            return;
        }
        if (this.update) {
            this.model = new DesyncESP_EZqjXHyHjNyfFrdyAmeM();
            this.update = false;
        }
    }

    @EventHandler
    public void onUpdateWalkingPost(UpdateWalkingEvent event) {
        if (event.getStage() == Event.Post) {
            this.lastYaw = DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.method_36454();
            this.lastPitch = DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.method_36455();
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (DesyncESP_dCvptoNghaTFSegtZyHR.nullCheck() || this.model == null) {
            return;
        }
        if (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1690.method_31044() == Perspective.field_26664) {
            return;
        }
        if (Math.abs(this.lastYaw - HexTech.ROTATE.lastYaw) < 1.0f && Math.abs(this.lastPitch - RotateManager.lastPitch) < 1.0f) {
            return;
        }
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        double x = DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6014 + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.method_23317() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6014) * (double)mc.method_1488() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.method_1561().field_4686.method_19326().method_10216();
        double y = DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6036 + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.method_23318() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6036) * (double)mc.method_1488() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.method_1561().field_4686.method_19326().method_10214();
        double z = DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_5969 + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.method_23321() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_5969) * (double)mc.method_1488() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.method_1561().field_4686.method_19326().method_10215();
        float bodyYaw = this.type.getValue() == DesyncESP.ServerSide ? RotateManager.getPrevRenderYawOffset() + (RotateManager.getRenderYawOffset() - RotateManager.getPrevRenderYawOffset()) * mc.method_1488() : DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6220 + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6283 - DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6220) * mc.method_1488();
        float headYaw = this.type.getValue() == DesyncESP.ServerSide ? RotateManager.getPrevRotationYawHead() + (RotateManager.getRotationYawHead() - RotateManager.getPrevRotationYawHead()) * mc.method_1488() : DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6259 + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6241 - DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6259) * mc.method_1488();
        float pitch = this.type.getValue() == DesyncESP.ServerSide ? RotateManager.getPrevPitch() + (RotateManager.getRenderPitch() - RotateManager.getPrevPitch()) * mc.method_1488() : DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6004 + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.method_36455() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6004) * mc.method_1488();
        matrixStack.method_22903();
        matrixStack.method_46416((float)x, (float)y, (float)z);
        matrixStack.method_22907(RotationAxis.field_40716.rotation(MathUtil.rad(180.0f - bodyYaw)));
        DesyncESP_dCvptoNghaTFSegtZyHR.prepareScale(matrixStack);
        this.model.modelPlayer.method_17086((LivingEntity)DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724, DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_42108.method_48572(mc.method_1488()), DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_42108.method_48570(mc.method_1488()), mc.method_1488());
        this.model.modelPlayer.method_17087((LivingEntity)DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724, DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_42108.method_48572(mc.method_1488()), DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_42108.method_48570(mc.method_1488()), (float)DesyncESP_dCvptoNghaTFSegtZyHR.mc.field_1724.field_6012, headYaw - bodyYaw, pitch);
        RenderSystem.enableBlend();
        GL11.glDisable((int)2929);
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ONE, (GlStateManager.DstFactor)GlStateManager.DstFactor.ZERO);
        RenderSystem.setShader(GameRenderer::method_34540);
        buffer.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1576);
        this.model.modelPlayer.method_2828(matrixStack, (VertexConsumer)buffer, 10, 0, (float)this.color.getValue().getRed() / 255.0f, (float)this.color.getValue().getGreen() / 255.0f, (float)this.color.getValue().getBlue() / 255.0f, (float)this.color.getValue().getAlpha() / 255.0f);
        tessellator.method_1350();
        RenderSystem.disableBlend();
        GL11.glEnable((int)2929);
        matrixStack.method_22909();
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
    }
}
