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
import org.jetbrains.annotations.NotNull;

public class ShaderManager implements Wrapper {
   private static final List<ShaderManager_YQoYchTCnXxJpEjCFEPm> tasks = new ArrayList();
   public static ManagedShaderEffect DEFAULT_OUTLINE;
   public static ManagedShaderEffect SMOKE_OUTLINE;
   public static ManagedShaderEffect GRADIENT_OUTLINE;
   public static ManagedShaderEffect SNOW_OUTLINE;
   public static ManagedShaderEffect FLOW_OUTLINE;
   public static ManagedShaderEffect RAINBOW_OUTLINE;
   public static ManagedShaderEffect DEFAULT;
   public static ManagedShaderEffect SMOKE;
   public static ManagedShaderEffect GRADIENT;
   public static ManagedShaderEffect SNOW;
   public static ManagedShaderEffect FLOW;
   public static ManagedShaderEffect RAINBOW;
   public float time = 0.0F;
   private ShaderManager_WNhDoPXLcRvHUWNoquud shaderBuffer;

   public void renderShader(Runnable runnable, ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
      tasks.add(new ShaderManager_YQoYchTCnXxJpEjCFEPm(runnable, mode));
   }

   public void renderShaders() {
      tasks.forEach(t -> this.applyShader(t.task(), t.shader()));
      tasks.clear();
   }

   public void applyFlow(Runnable runnable) {
      if (!this.fullNullCheck()) {
         Framebuffer MCBuffer = MinecraftClient.method_1551().method_1522();
         RenderSystem.assertOnRenderThreadOrInit();
         if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
            this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
         }

         GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.field_1476);
         this.shaderBuffer.method_1235(true);
         runnable.run();
         this.shaderBuffer.method_1240();
         GlStateManager._glBindFramebuffer(36009, MCBuffer.field_1476);
         MCBuffer.method_1235(false);
         Framebuffer mainBuffer = MinecraftClient.method_1551().method_1522();
         PostEffectProcessor effect = FLOW.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", this.shaderBuffer);
         }

         Framebuffer outBuffer = FLOW.getShaderEffect().method_1264("bufOut");
         FLOW.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         FLOW.setUniformValue("time", this.time);
         FLOW.render(mc.method_1488());
         this.time += 0.01F;
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

   public void applyShader(Runnable runnable, ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
      if (!this.fullNullCheck()) {
         Framebuffer MCBuffer = MinecraftClient.method_1551().method_1522();
         RenderSystem.assertOnRenderThreadOrInit();
         if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
            this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
         }

         GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.field_1476);
         this.shaderBuffer.method_1235(true);
         runnable.run();
         this.shaderBuffer.method_1240();
         GlStateManager._glBindFramebuffer(36009, MCBuffer.field_1476);
         MCBuffer.method_1235(false);
         ManagedShaderEffect shader = this.getShader(mode);
         Framebuffer mainBuffer = MinecraftClient.method_1551().method_1522();
         PostEffectProcessor effect = shader.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", this.shaderBuffer);
         }

         Framebuffer outBuffer = shader.getShaderEffect().method_1264("bufOut");
         this.setupShader(mode, shader);
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

   public ManagedShaderEffect getShader(@NotNull ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
      return switch (mode) {
         case Smoke -> SMOKE;
         case Gradient -> GRADIENT;
         case Snow -> SNOW;
         case Flow -> FLOW;
         case Rainbow -> RAINBOW;
         default -> DEFAULT;
      };
   }

   public ManagedShaderEffect getShaderOutline(@NotNull ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
      return switch (mode) {
         case Smoke -> SMOKE_OUTLINE;
         case Gradient -> GRADIENT_OUTLINE;
         case Snow -> SNOW_OUTLINE;
         case Flow -> FLOW_OUTLINE;
         case Rainbow -> RAINBOW_OUTLINE;
         default -> DEFAULT_OUTLINE;
      };
   }

   public void setupShader(ShaderManager_nSIALuQmpuiGKWaEurQW shader, ManagedShaderEffect effect) {
      Shader_CLqIXXaHSdAoBoxRSgjR shaderChams = Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE;
      if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Rainbow) {
         effect.setUniformValue("alpha2", (float)shaderChams.fill.getValue().getAlpha() / 255.0F);
         effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
         effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
         effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
         effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(mc.method_1488());
         this.time = this.time + (float)shaderChams.speed.getValue() * 0.002F;
      } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Gradient) {
         effect.setUniformValue("alpha2", (float)shaderChams.fill.getValue().getAlpha() / 255.0F);
         effect.setUniformValue("oct", (int)shaderChams.octaves.getValue());
         effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
         effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
         effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
         effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
         effect.setUniformValue("factor", (float)shaderChams.factor.getValue());
         effect.setUniformValue("moreGradient", (float)shaderChams.gradient.getValue());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(mc.method_1488());
         this.time = this.time + (float)shaderChams.speed.getValue() * 0.002F;
      } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Smoke) {
         effect.setUniformValue("alpha1", (float)shaderChams.fill.getValue().getAlpha() / 255.0F);
         effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
         effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
         effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
         effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
         effect.setUniformValue(
            "first",
            (float)shaderChams.smoke1.getValue().getRed() / 255.0F,
            (float)shaderChams.smoke1.getValue().getGreen() / 255.0F,
            (float)shaderChams.smoke1.getValue().getBlue() / 255.0F,
            (float)shaderChams.smoke1.getValue().getAlpha() / 255.0F
         );
         effect.setUniformValue(
            "second",
            (float)shaderChams.smoke2.getValue().getRed() / 255.0F,
            (float)shaderChams.smoke2.getValue().getGreen() / 255.0F,
            (float)shaderChams.smoke2.getValue().getBlue() / 255.0F
         );
         effect.setUniformValue(
            "third",
            (float)shaderChams.smoke3.getValue().getRed() / 255.0F,
            (float)shaderChams.smoke3.getValue().getGreen() / 255.0F,
            (float)shaderChams.smoke3.getValue().getBlue() / 255.0F
         );
         effect.setUniformValue("oct", (int)shaderChams.octaves.getValue());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(mc.method_1488());
         this.time = this.time + (float)shaderChams.speed.getValue() * 0.002F;
      } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Solid) {
         effect.setUniformValue("mixFactor", (float)shaderChams.fill.getValue().getAlpha() / 255.0F);
         effect.setUniformValue("minAlpha", shaderChams.alpha.getValueFloat() / 255.0F);
         effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
         effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
         effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
         effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
         effect.setUniformValue(
            "color",
            (float)shaderChams.fill.getValue().getRed() / 255.0F,
            (float)shaderChams.fill.getValue().getGreen() / 255.0F,
            (float)shaderChams.fill.getValue().getBlue() / 255.0F
         );
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.render(mc.method_1488());
      } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Snow) {
         effect.setUniformValue(
            "color",
            (float)shaderChams.fill.getValue().getRed() / 255.0F,
            (float)shaderChams.fill.getValue().getGreen() / 255.0F,
            (float)shaderChams.fill.getValue().getBlue() / 255.0F,
            (float)shaderChams.fill.getValue().getAlpha() / 255.0F
         );
         effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
         effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
         effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
         effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(mc.method_1488());
         this.time = this.time + (float)shaderChams.speed.getValue() * 0.002F;
      } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Flow) {
         effect.setUniformValue("mixFactor", (float)shaderChams.fill.getValue().getAlpha() / 255.0F);
         effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
         effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
         effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
         effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(mc.method_1488());
         this.time = this.time + (float)shaderChams.speed.getValue() * 0.002F;
      }
   }

   public void reloadShaders() {
      DEFAULT = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/outline.json"));
      SMOKE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/smoke.json"));
      GRADIENT = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/gradient.json"));
      SNOW = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/snow.json"));
      FLOW = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/flow.json"));
      RAINBOW = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/rainbow.json"));
      DEFAULT_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/outline.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      SMOKE_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/smoke.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      GRADIENT_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/gradient.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      SNOW_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/snow.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      FLOW_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/flow.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      RAINBOW_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/rainbow.json"), managedShaderEffect -> {
         PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
   }

   public boolean fullNullCheck() {
      if (GRADIENT != null
         && SMOKE != null
         && DEFAULT != null
         && FLOW != null
         && RAINBOW != null
         && GRADIENT_OUTLINE != null
         && SMOKE_OUTLINE != null
         && DEFAULT_OUTLINE != null
         && FLOW_OUTLINE != null
         && RAINBOW_OUTLINE != null
         && this.shaderBuffer != null) {
         return false;
      } else if (mc.method_1522() == null) {
         return true;
      } else {
         this.shaderBuffer = new ShaderManager_WNhDoPXLcRvHUWNoquud(mc.method_1522().field_1482, mc.method_1522().field_1481);
         this.reloadShaders();
         return true;
      }
   }
}
