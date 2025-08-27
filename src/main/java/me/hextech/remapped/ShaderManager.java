package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import me.hextech.remapped.IShaderEffect;
import me.hextech.remapped.ShaderManager_YQoYchTCnXxJpEjCFEPm;
import me.hextech.remapped.ShaderManager_nSIALuQmpuiGKWaEurQW;
import me.hextech.remapped.Shader_CLqIXXaHSdAoBoxRSgjR;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ShaderManager
implements Wrapper {
    private static final List<ShaderManager_YQoYchTCnXxJpEjCFEPm> tasks = new ArrayList<ShaderManager_YQoYchTCnXxJpEjCFEPm>();
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
    public float time = 0.0f;
    private _WNhDoPXLcRvHUWNoquud shaderBuffer;

    public void renderShader(Runnable runnable, ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
        tasks.add(new ShaderManager_YQoYchTCnXxJpEjCFEPm(runnable, mode));
    }

    public void renderShaders() {
        tasks.forEach(t -> this.applyShader(t.task(), t.shader()));
        tasks.clear();
    }

    public void applyFlow(Runnable runnable) {
        if (this.fullNullCheck()) {
            return;
        }
        Framebuffer MCBuffer = MinecraftClient.getInstance().getFramebuffer();
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.shaderBuffer.field_1482 != MCBuffer.textureWidth || this.shaderBuffer.field_1481 != MCBuffer.textureHeight) {
            this.shaderBuffer.method_1234(MCBuffer.textureWidth, MCBuffer.textureHeight, false);
        }
        GlStateManager._glBindFramebuffer((int)36009, (int)this.shaderBuffer.field_1476);
        this.shaderBuffer.method_1235(true);
        runnable.run();
        this.shaderBuffer.method_1240();
        GlStateManager._glBindFramebuffer((int)36009, (int)MCBuffer.fbo);
        MCBuffer.beginWrite(false);
        Framebuffer mainBuffer = MinecraftClient.getInstance().getFramebuffer();
        PostEffectProcessor effect = FLOW.getShaderEffect();
        if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", this.shaderBuffer);
        }
        Framebuffer outBuffer = FLOW.getShaderEffect().method_1264("bufOut");
        FLOW.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
        FLOW.setUniformValue("time", this.time);
        FLOW.render(mc.method_1488());
        this.time += 0.01f;
        this.shaderBuffer.method_1230(false);
        mainBuffer.beginWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ZERO, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE);
        RenderSystem.backupProjectionMatrix();
        outBuffer.method_22594(outBuffer.textureWidth, outBuffer.textureHeight, false);
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public void applyShader(Runnable runnable, ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
        if (this.fullNullCheck()) {
            return;
        }
        Framebuffer MCBuffer = MinecraftClient.getInstance().getFramebuffer();
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.shaderBuffer.field_1482 != MCBuffer.textureWidth || this.shaderBuffer.field_1481 != MCBuffer.textureHeight) {
            this.shaderBuffer.method_1234(MCBuffer.textureWidth, MCBuffer.textureHeight, false);
        }
        GlStateManager._glBindFramebuffer((int)36009, (int)this.shaderBuffer.field_1476);
        this.shaderBuffer.method_1235(true);
        runnable.run();
        this.shaderBuffer.method_1240();
        GlStateManager._glBindFramebuffer((int)36009, (int)MCBuffer.fbo);
        MCBuffer.beginWrite(false);
        ManagedShaderEffect shader = this.getShader(mode);
        Framebuffer mainBuffer = MinecraftClient.getInstance().getFramebuffer();
        PostEffectProcessor effect = shader.getShaderEffect();
        if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", this.shaderBuffer);
        }
        Framebuffer outBuffer = shader.getShaderEffect().method_1264("bufOut");
        this.setupShader(mode, shader);
        this.shaderBuffer.method_1230(false);
        mainBuffer.beginWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ZERO, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE);
        RenderSystem.backupProjectionMatrix();
        outBuffer.method_22594(outBuffer.textureWidth, outBuffer.textureHeight, false);
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public ManagedShaderEffect getShader(@NotNull ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
        return switch (mode.ordinal()) {
            case 2 -> GRADIENT;
            case 1 -> SMOKE;
            case 3 -> SNOW;
            case 4 -> FLOW;
            case 5 -> RAINBOW;
            default -> DEFAULT;
        };
    }

    public ManagedShaderEffect getShaderOutline(@NotNull ShaderManager_nSIALuQmpuiGKWaEurQW mode) {
        return switch (mode.ordinal()) {
            case 2 -> GRADIENT_OUTLINE;
            case 1 -> SMOKE_OUTLINE;
            case 3 -> SNOW_OUTLINE;
            case 4 -> FLOW_OUTLINE;
            case 5 -> RAINBOW_OUTLINE;
            default -> DEFAULT_OUTLINE;
        };
    }

    public void setupShader(ShaderManager_nSIALuQmpuiGKWaEurQW shader, ManagedShaderEffect effect) {
        Shader_CLqIXXaHSdAoBoxRSgjR shaderChams = Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE;
        if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Rainbow) {
            effect.setUniformValue("alpha2", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.method_1488());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Gradient) {
            effect.setUniformValue("alpha2", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("oct", (int)shaderChams.octaves.getValue());
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("factor", (float)shaderChams.factor.getValue());
            effect.setUniformValue("moreGradient", (float)shaderChams.gradient.getValue());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.method_1488());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Smoke) {
            effect.setUniformValue("alpha1", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("first", (float)shaderChams.smoke1.getValue().getRed() / 255.0f, (float)shaderChams.smoke1.getValue().getGreen() / 255.0f, (float)shaderChams.smoke1.getValue().getBlue() / 255.0f, (float)shaderChams.smoke1.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("second", (float)shaderChams.smoke2.getValue().getRed() / 255.0f, (float)shaderChams.smoke2.getValue().getGreen() / 255.0f, (float)shaderChams.smoke2.getValue().getBlue() / 255.0f);
            effect.setUniformValue("third", (float)shaderChams.smoke3.getValue().getRed() / 255.0f, (float)shaderChams.smoke3.getValue().getGreen() / 255.0f, (float)shaderChams.smoke3.getValue().getBlue() / 255.0f);
            effect.setUniformValue("oct", (int)shaderChams.octaves.getValue());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.method_1488());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Solid) {
            effect.setUniformValue("mixFactor", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("minAlpha", shaderChams.alpha.getValueFloat() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("color", (float)shaderChams.fill.getValue().getRed() / 255.0f, (float)shaderChams.fill.getValue().getGreen() / 255.0f, (float)shaderChams.fill.getValue().getBlue() / 255.0f);
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.render(mc.method_1488());
        } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Snow) {
            effect.setUniformValue("color", (float)shaderChams.fill.getValue().getRed() / 255.0f, (float)shaderChams.fill.getValue().getGreen() / 255.0f, (float)shaderChams.fill.getValue().getBlue() / 255.0f, (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.method_1488());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (shader == ShaderManager_nSIALuQmpuiGKWaEurQW.Flow) {
            effect.setUniformValue("mixFactor", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.method_1488());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
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
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
        SMOKE_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/smoke.json"), managedShaderEffect -> {
            PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
        GRADIENT_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/gradient.json"), managedShaderEffect -> {
            PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
        SNOW_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/snow.json"), managedShaderEffect -> {
            PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
        FLOW_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/flow.json"), managedShaderEffect -> {
            PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
        RAINBOW_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/rainbow.json"), managedShaderEffect -> {
            PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufOut", ShaderManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
    }

    public boolean fullNullCheck() {
        if (GRADIENT == null || SMOKE == null || DEFAULT == null || FLOW == null || RAINBOW == null || GRADIENT_OUTLINE == null || SMOKE_OUTLINE == null || DEFAULT_OUTLINE == null || FLOW_OUTLINE == null || RAINBOW_OUTLINE == null || this.shaderBuffer == null) {
            if (mc.getFramebuffer() == null) {
                return true;
            }
            this.shaderBuffer = new _WNhDoPXLcRvHUWNoquud(ShaderManager.mc.getFramebuffer().textureWidth, ShaderManager.mc.getFramebuffer().textureHeight);
            this.reloadShaders();
            return true;
        }
        return false;
    }

    public static class _WNhDoPXLcRvHUWNoquud
    extends Framebuffer {
        public _WNhDoPXLcRvHUWNoquud(int width, int height) {
            super(false);
            RenderSystem.assertOnRenderThreadOrInit();
            this.method_1234(width, height, true);
            this.method_1236(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
}
