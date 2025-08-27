package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BlurManager implements Wrapper {
   private static final List<BlurManager_XkqtwkkpoPNjOLasIQyK> tasks = new ArrayList();
   public static ManagedShaderEffect BLUR_OUTLINE;
   public static ManagedShaderEffect BLUR;
   public float time = 0.0F;
   public int scaledWidth;
   public int scaledHeight;
   public int scaleFactor;
   private BlurManager_cScSUnWhWBxyFlTcoHKo shaderBuffer;

   public void renderShader(Runnable task, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
      tasks.add(new BlurManager_XkqtwkkpoPNjOLasIQyK(task, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY));
   }

   public void renderShaders() {
      tasks.forEach(t -> this.applyShader(t.task(), t.Radius, t.BlurX, t.BlurY, t.BlurCoordX, t.BlurCoordY, t.offsetX, t.offsetY));
      tasks.clear();
   }

   public void applyShader(Runnable task, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
      if (!this.fullNullCheck()) {
         Framebuffer MCBuffer = MinecraftClient.method_1551().method_1522();
         RenderSystem.assertOnRenderThreadOrInit();
         if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
            this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
         }

         GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.field_1476);
         this.shaderBuffer.method_1235(true);
         task.run();
         this.shaderBuffer.method_1240();
         GlStateManager._glBindFramebuffer(36009, MCBuffer.field_1476);
         MCBuffer.method_1235(false);
         ManagedShaderEffect shader = BLUR;
         Framebuffer mainBuffer = MinecraftClient.method_1551().method_1522();
         Framebuffer outBuffer = shader.getShaderEffect().method_1264("bufOut");
         this.setupShader(shader, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY);
         this.shaderBuffer.method_1230(false);
         mainBuffer.method_1235(false);
         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ZERO, DstFactor.ONE);
         RenderSystem.backupProjectionMatrix();
         outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
         RenderSystem.restoreProjectionMatrix();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
      }
   }

   private void setup(ManagedShaderEffect effect, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
      if (mc.method_22683() != null) {
         this.updateResolution();
      }

      effect.setUniformValue("Radius", Radius);
      effect.setUniformValue("BlurXY", BlurX + offsetX, (float)mc.method_22683().method_4502() - BlurY - offsetY);
      effect.setUniformValue("BlurCoord", (float)((int)BlurCoordX), BlurCoordY);
      effect.render(mc.method_1488());
   }

   public void updateResolution() {
      this.scaledWidth = mc.method_22683().method_4489();
      this.scaledHeight = mc.method_22683().method_4506();
      this.scaleFactor = 1;
      boolean flag = mc.method_22683().method_4495() > 1.0;
      int i = (Integer)mc.field_1690.method_42474().method_41753();
      if (i == 0) {
         i = 1000;
      }

      while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
         this.scaleFactor++;
      }

      if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
         this.scaleFactor--;
      }

      double scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
      double scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
      this.scaledWidth = MathHelper.method_15384(scaledWidthD);
      this.scaledHeight = MathHelper.method_15384(scaledHeightD);
   }

   public void setupShader(ManagedShaderEffect effect, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
      this.setup(effect, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY);
   }

   public void reloadShaders() {
      BLUR = ShaderEffectManager.getInstance().manage(new Identifier("minecraft", "shaders/post/blurarea.json"));
      BLUR_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("minecraft", "shaders/post/blurarea.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).addHook("bufOut", mc.field_1769.method_22990());
         }
      });
   }

   public boolean fullNullCheck() {
      if (this.shaderBuffer != null && BLUR != null) {
         return false;
      } else if (mc.method_1522() == null) {
         return true;
      } else {
         this.shaderBuffer = new BlurManager_cScSUnWhWBxyFlTcoHKo(mc.method_1522().field_1482, mc.method_1522().field_1481);
         this.reloadShaders();
         return true;
      }
   }
}
