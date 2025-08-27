package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.opengl.GL11;

public final class PopChams_WNWBvFQQYNjRmTHDKpkM extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static PopChams_WNWBvFQQYNjRmTHDKpkM INSTANCE;
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
   private final SliderSetting alphaSpeed = this.add(new SliderSetting("AlphaSpeed", 0.2, 0.0, 1.0, 0.01));
   private final CopyOnWriteArrayList<PopChams> popList = new CopyOnWriteArrayList();

   public PopChams_WNWBvFQQYNjRmTHDKpkM() {
      super("PopChams", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   private static void prepareScale(MatrixStack matrixStack) {
      matrixStack.method_22905(-1.0F, -1.0F, 1.0F);
      matrixStack.method_22905(1.6F, 1.8F, 1.6F);
      matrixStack.method_46416(0.0F, -1.501F, 0.0F);
   }

   @Override
   public void onUpdate() {
      this.popList.forEach(person -> person.update(this.popList));
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      RenderSystem.depthMask(false);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 771, 0, 1);
      this.popList.forEach(person -> {
         person.modelPlayer.field_3482.field_3665 = false;
         person.modelPlayer.field_3479.field_3665 = false;
         person.modelPlayer.field_3484.field_3665 = false;
         person.modelPlayer.field_3486.field_3665 = false;
         person.modelPlayer.field_3483.field_3665 = false;
         person.modelPlayer.field_3394.field_3665 = false;
         this.renderEntity(matrixStack, person.player, person.modelPlayer, person.getAlpha());
      });
      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
   }

   @EventHandler
   private void onTotemPop(TotemEvent e) {
      if (!e.getPlayer().equals(mc.field_1724) && mc.field_1687 != null) {
         PlayerEntity entity = new PlayerEntity(
            mc.field_1687,
            BlockPos.field_10980,
            e.getPlayer().field_6283,
            new GameProfile(e.getPlayer().method_5667(), e.getPlayer().method_5477().getString())
         ) {
            public boolean method_7325() {
               return false;
            }

            public boolean method_7337() {
               return false;
            }
         };
         entity.method_5719(e.getPlayer());
         entity.field_6283 = e.getPlayer().field_6283;
         entity.field_6241 = e.getPlayer().field_6241;
         entity.field_6251 = e.getPlayer().field_6251;
         entity.field_6279 = e.getPlayer().field_6279;
         entity.method_5660(e.getPlayer().method_5715());
         entity.field_42108.method_48567(e.getPlayer().field_42108.method_48566());
         entity.field_42108.field_42111 = e.getPlayer().field_42108.method_48569();
         this.popList.add(new PopChams(this, entity));
      }
   }

   private void renderEntity(MatrixStack matrices, LivingEntity entity, BipedEntityModel<PlayerEntity> modelBase, int alpha) {
      double x = entity.method_23317() - mc.method_1561().field_4686.method_19326().method_10216();
      double y = entity.method_23318() - mc.method_1561().field_4686.method_19326().method_10214();
      double z = entity.method_23321() - mc.method_1561().field_4686.method_19326().method_10215();
      matrices.method_22903();
      matrices.method_46416((float)x, (float)y, (float)z);
      matrices.method_22907(RotationAxis.field_40716.rotation(MathUtil.rad(180.0F - entity.field_6283)));
      prepareScale(matrices);
      modelBase.method_17086((PlayerEntity)entity, entity.field_42108.method_48569(), entity.field_42108.method_48566(), mc.method_1488());
      modelBase.method_17087(
         (PlayerEntity)entity,
         entity.field_42108.method_48569(),
         entity.field_42108.method_48566(),
         (float)entity.field_6012,
         entity.field_6241 - entity.field_6283,
         entity.method_36455()
      );
      RenderSystem.enableBlend();
      GL11.glDisable(2929);
      Tessellator tessellator = Tessellator.method_1348();
      BufferBuilder buffer = tessellator.method_1349();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.setShader(GameRenderer::method_34540);
      buffer.method_1328(DrawMode.field_27382, VertexFormats.field_1576);
      modelBase.method_2828(
         matrices,
         buffer,
         10,
         0,
         (float)this.color.getValue().getRed() / 255.0F,
         (float)this.color.getValue().getGreen() / 255.0F,
         (float)this.color.getValue().getBlue() / 255.0F,
         (float)alpha / 255.0F
      );
      tessellator.method_1350();
      RenderSystem.disableBlend();
      GL11.glEnable(2929);
      matrices.method_22909();
   }
}
