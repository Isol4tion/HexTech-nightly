package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.opengl.GL11;

public final class DesyncESP_dCvptoNghaTFSegtZyHR extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static DesyncESP_dCvptoNghaTFSegtZyHR INSTANCE;
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
   private final EnumSetting<DesyncESP> type = this.add(new EnumSetting("Type", DesyncESP.ServerSide));
   DesyncESP_EZqjXHyHjNyfFrdyAmeM model;
   boolean update = true;
   float lastYaw;
   float lastPitch;

   public DesyncESP_dCvptoNghaTFSegtZyHR() {
      super("DesyncESP", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   private static void prepareScale(MatrixStack matrixStack) {
      matrixStack.method_22905(-1.0F, -1.0F, 1.0F);
      matrixStack.method_22905(1.6F, 1.8F, 1.6F);
      matrixStack.method_46416(0.0F, -1.501F, 0.0F);
   }

   @Override
   public void onLogin() {
      this.update = true;
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         if (this.update) {
            this.model = new DesyncESP_EZqjXHyHjNyfFrdyAmeM();
            this.update = false;
         }
      }
   }

   @EventHandler
   public void onUpdateWalkingPost(UpdateWalkingEvent event) {
      if (event.getStage() == Event.Post) {
         this.lastYaw = mc.field_1724.method_36454();
         this.lastPitch = mc.field_1724.method_36455();
      }
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (!nullCheck() && this.model != null) {
         if (mc.field_1690.method_31044() != Perspective.field_26664) {
            if (!(Math.abs(this.lastYaw - me.hextech.HexTech.ROTATE.lastYaw) < 1.0F) || !(Math.abs(this.lastPitch - RotateManager.lastPitch) < 1.0F)) {
               RenderSystem.depthMask(false);
               RenderSystem.enableBlend();
               RenderSystem.blendFuncSeparate(770, 771, 0, 1);
               double x = mc.field_1724.field_6014
                  + (mc.field_1724.method_23317() - mc.field_1724.field_6014) * (double)mc.method_1488()
                  - mc.method_1561().field_4686.method_19326().method_10216();
               double y = mc.field_1724.field_6036
                  + (mc.field_1724.method_23318() - mc.field_1724.field_6036) * (double)mc.method_1488()
                  - mc.method_1561().field_4686.method_19326().method_10214();
               double z = mc.field_1724.field_5969
                  + (mc.field_1724.method_23321() - mc.field_1724.field_5969) * (double)mc.method_1488()
                  - mc.method_1561().field_4686.method_19326().method_10215();
               float bodyYaw = this.type.getValue() == DesyncESP.ServerSide
                  ? RotateManager.getPrevRenderYawOffset() + (RotateManager.getRenderYawOffset() - RotateManager.getPrevRenderYawOffset()) * mc.method_1488()
                  : mc.field_1724.field_6220 + (mc.field_1724.field_6283 - mc.field_1724.field_6220) * mc.method_1488();
               float headYaw = this.type.getValue() == DesyncESP.ServerSide
                  ? RotateManager.getPrevRotationYawHead() + (RotateManager.getRotationYawHead() - RotateManager.getPrevRotationYawHead()) * mc.method_1488()
                  : mc.field_1724.field_6259 + (mc.field_1724.field_6241 - mc.field_1724.field_6259) * mc.method_1488();
               float pitch = this.type.getValue() == DesyncESP.ServerSide
                  ? RotateManager.getPrevPitch() + (RotateManager.getRenderPitch() - RotateManager.getPrevPitch()) * mc.method_1488()
                  : mc.field_1724.field_6004 + (mc.field_1724.method_36455() - mc.field_1724.field_6004) * mc.method_1488();
               matrixStack.method_22903();
               matrixStack.method_46416((float)x, (float)y, (float)z);
               matrixStack.method_22907(RotationAxis.field_40716.rotation(MathUtil.rad(180.0F - bodyYaw)));
               prepareScale(matrixStack);
               this.model
                  .modelPlayer
                  .method_17086(
                     mc.field_1724,
                     mc.field_1724.field_42108.method_48572(mc.method_1488()),
                     mc.field_1724.field_42108.method_48570(mc.method_1488()),
                     mc.method_1488()
                  );
               this.model
                  .modelPlayer
                  .method_17087(
                     mc.field_1724,
                     mc.field_1724.field_42108.method_48572(mc.method_1488()),
                     mc.field_1724.field_42108.method_48570(mc.method_1488()),
                     (float)mc.field_1724.field_6012,
                     headYaw - bodyYaw,
                     pitch
                  );
               RenderSystem.enableBlend();
               GL11.glDisable(2929);
               Tessellator tessellator = Tessellator.method_1348();
               BufferBuilder buffer = tessellator.method_1349();
               RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
               RenderSystem.setShader(GameRenderer::method_34540);
               buffer.method_1328(DrawMode.field_27382, VertexFormats.field_1576);
               this.model
                  .modelPlayer
                  .method_2828(
                     matrixStack,
                     buffer,
                     10,
                     0,
                     (float)this.color.getValue().getRed() / 255.0F,
                     (float)this.color.getValue().getGreen() / 255.0F,
                     (float)this.color.getValue().getBlue() / 255.0F,
                     (float)this.color.getValue().getAlpha() / 255.0F
                  );
               tessellator.method_1350();
               RenderSystem.disableBlend();
               GL11.glEnable(2929);
               matrixStack.method_22909();
               RenderSystem.disableBlend();
               RenderSystem.depthMask(true);
            }
         }
      }
   }
}
