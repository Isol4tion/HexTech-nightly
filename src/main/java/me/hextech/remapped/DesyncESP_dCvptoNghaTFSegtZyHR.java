package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.HexTech;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
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
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.scale(1.6f, 1.8f, 1.6f);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
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
            this.lastYaw = DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.getYaw();
            this.lastPitch = DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.getPitch();
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (DesyncESP_dCvptoNghaTFSegtZyHR.nullCheck() || this.model == null) {
            return;
        }
        if (DesyncESP_dCvptoNghaTFSegtZyHR.mc.options.getPerspective() == Perspective.FIRST_PERSON) {
            return;
        }
        if (Math.abs(this.lastYaw - HexTech.ROTATE.lastYaw) < 1.0f && Math.abs(this.lastPitch - RotateManager.lastPitch) < 1.0f) {
            return;
        }
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 0, 1);
        double x = DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevX + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.getX() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevX) * (double)mc.getTickDelta() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.getEntityRenderDispatcher().camera.getPos().getX();
        double y = DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevY + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.getY() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevY) * (double)mc.getTickDelta() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.getEntityRenderDispatcher().camera.getPos().getY();
        double z = DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevZ + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.getZ() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevZ) * (double)mc.getTickDelta() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.getEntityRenderDispatcher().camera.getPos().getZ();
        float bodyYaw = this.type.getValue() == DesyncESP.ServerSide ? RotateManager.getPrevRenderYawOffset() + (RotateManager.getRenderYawOffset() - RotateManager.getPrevRenderYawOffset()) * mc.getTickDelta() : DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevBodyYaw + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.bodyYaw - DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevBodyYaw) * mc.getTickDelta();
        float headYaw = this.type.getValue() == DesyncESP.ServerSide ? RotateManager.getPrevRotationYawHead() + (RotateManager.getRotationYawHead() - RotateManager.getPrevRotationYawHead()) * mc.getTickDelta() : DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevHeadYaw + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.headYaw - DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevHeadYaw) * mc.getTickDelta();
        float pitch = this.type.getValue() == DesyncESP.ServerSide ? RotateManager.getPrevPitch() + (RotateManager.getRenderPitch() - RotateManager.getPrevPitch()) * mc.getTickDelta() : DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevPitch + (DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.getPitch() - DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.prevPitch) * mc.getTickDelta();
        matrixStack.push();
        matrixStack.translate((float)x, (float)y, (float)z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(MathUtil.rad(180.0f - bodyYaw)));
        DesyncESP_dCvptoNghaTFSegtZyHR.prepareScale(matrixStack);
        this.model.modelPlayer.animateModel(DesyncESP_dCvptoNghaTFSegtZyHR.mc.player, DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.limbAnimator.getPos(mc.getTickDelta()), DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.limbAnimator.getSpeed(mc.getTickDelta()), mc.getTickDelta());
        this.model.modelPlayer.setAngles(DesyncESP_dCvptoNghaTFSegtZyHR.mc.player, DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.limbAnimator.getPos(mc.getTickDelta()), DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.limbAnimator.getSpeed(mc.getTickDelta()), (float)DesyncESP_dCvptoNghaTFSegtZyHR.mc.player.age, headYaw - bodyYaw, pitch);
        RenderSystem.enableBlend();
        GL11.glDisable(2929);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        this.model.modelPlayer.render(matrixStack, buffer, 10, 0, (float)this.color.getValue().getRed() / 255.0f, (float)this.color.getValue().getGreen() / 255.0f, (float)this.color.getValue().getBlue() / 255.0f, (float)this.color.getValue().getAlpha() / 255.0f);
        tessellator.draw();
        RenderSystem.disableBlend();
        GL11.glEnable(2929);
        matrixStack.pop();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class DesyncESP_EZqjXHyHjNyfFrdyAmeM {
        private final PlayerEntityModel<PlayerEntity> modelPlayer;

        public DesyncESP_EZqjXHyHjNyfFrdyAmeM() {
            this.modelPlayer = new PlayerEntityModel<>(new EntityRendererFactory.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderManager(), mc.getEntityRenderDispatcher().getHeldItemRenderer(), mc.getResourceManager(), mc.getEntityModelLoader(), mc.textRenderer).getPart(EntityModelLayers.PLAYER), false);
            this.modelPlayer.getHead().scale(new Vector3f(-0.3f, -0.3f, -0.3f));
        }
    }
}
