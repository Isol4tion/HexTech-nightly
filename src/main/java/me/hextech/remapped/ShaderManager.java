package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ShaderManager
implements Wrapper {
    private static final List<RenderTask> tasks = new ArrayList<RenderTask>();
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

    public void renderShader(Runnable runnable, Mode mode) {
        tasks.add(new RenderTask(runnable, mode));
    }

    public void renderShaders() {
        tasks.forEach(t -> this.applyShader(t.task(), t.mode()));
        tasks.clear();
    }

    public void applyFlow(Runnable runnable) {
        if (this.fullNullCheck()) {
            return;
        }
        Framebuffer MCBuffer = MinecraftClient.getInstance().getFramebuffer();
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.shaderBuffer.textureWidth != MCBuffer.textureWidth || this.shaderBuffer.textureHeight != MCBuffer.textureHeight) {
            this.shaderBuffer.resize(MCBuffer.textureWidth, MCBuffer.textureHeight, false);
        }
        GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.fbo);
        this.shaderBuffer.beginWrite(true);
        runnable.run();
        this.shaderBuffer.endWrite();
        GlStateManager._glBindFramebuffer(36009, MCBuffer.fbo);
        MCBuffer.beginWrite(false);
        Framebuffer mainBuffer = MinecraftClient.getInstance().getFramebuffer();
        PostEffectProcessor effect = FLOW.getShaderEffect();
        if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", this.shaderBuffer);
        }
        Framebuffer outBuffer = FLOW.getShaderEffect().getSecondaryTarget("bufOut");
        FLOW.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
        FLOW.setUniformValue("time", this.time);
        FLOW.render(mc.getTickDelta());
        this.time += 0.01f;
        this.shaderBuffer.clear(false);
        mainBuffer.beginWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.backupProjectionMatrix();
        outBuffer.draw(outBuffer.textureWidth, outBuffer.textureHeight, false);
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public void applyShader(Runnable runnable, Mode mode) {
        if (this.fullNullCheck()) {
            return;
        }
        Framebuffer MCBuffer = MinecraftClient.getInstance().getFramebuffer();
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.shaderBuffer.textureWidth != MCBuffer.textureWidth || this.shaderBuffer.textureHeight != MCBuffer.textureHeight) {
            this.shaderBuffer.resize(MCBuffer.textureWidth, MCBuffer.textureHeight, false);
        }
        GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.fbo);
        this.shaderBuffer.beginWrite(true);
        runnable.run();
        this.shaderBuffer.endWrite();
        GlStateManager._glBindFramebuffer(36009, MCBuffer.fbo);
        MCBuffer.beginWrite(false);
        ManagedShaderEffect shader = this.getShader(mode);
        Framebuffer mainBuffer = MinecraftClient.getInstance().getFramebuffer();
        PostEffectProcessor effect = shader.getShaderEffect();
        if (effect != null) {
            ((IShaderEffect)effect).nullpoint_nextgen_master$addFakeTargetHook("bufIn", this.shaderBuffer);
        }
        Framebuffer outBuffer = shader.getShaderEffect().getSecondaryTarget("bufOut");
        this.setupShader(mode, shader);
        this.shaderBuffer.clear(false);
        mainBuffer.beginWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.backupProjectionMatrix();
        outBuffer.draw(outBuffer.textureWidth, outBuffer.textureHeight, false);
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public ManagedShaderEffect getShader(@NotNull ShaderManager.Mode mode) {
        return switch (mode.ordinal()) {
            case 2 -> GRADIENT;
            case 1 -> SMOKE;
            case 3 -> SNOW;
            case 4 -> FLOW;
            case 5 -> RAINBOW;
            default -> DEFAULT;
        };
    }

    public ManagedShaderEffect getShaderOutline(@NotNull ShaderManager.Mode mode) {
        return switch (mode.ordinal()) {
            case 2 -> GRADIENT_OUTLINE;
            case 1 -> SMOKE_OUTLINE;
            case 3 -> SNOW_OUTLINE;
            case 4 -> FLOW_OUTLINE;
            case 5 -> RAINBOW_OUTLINE;
            default -> DEFAULT_OUTLINE;
        };
    }

    public void setupShader(Mode mode, ManagedShaderEffect effect) {
        Shader_CLqIXXaHSdAoBoxRSgjR shaderChams = Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE;
        if (mode == Mode.Rainbow) {
            effect.setUniformValue("alpha2", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.getTickDelta());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (mode == Mode.Gradient) {
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
            effect.render(mc.getTickDelta());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (mode == Mode.Smoke) {
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
            effect.render(mc.getTickDelta());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (mode == Mode.Solid) {
            effect.setUniformValue("mixFactor", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("minAlpha", shaderChams.alpha.getValueFloat() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("color", (float)shaderChams.fill.getValue().getRed() / 255.0f, (float)shaderChams.fill.getValue().getGreen() / 255.0f, (float)shaderChams.fill.getValue().getBlue() / 255.0f);
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.render(mc.getTickDelta());
        } else if (mode == Mode.Snow) {
            effect.setUniformValue("color", (float)shaderChams.fill.getValue().getRed() / 255.0f, (float)shaderChams.fill.getValue().getGreen() / 255.0f, (float)shaderChams.fill.getValue().getBlue() / 255.0f, (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.getTickDelta());
            this.time += (float)shaderChams.speed.getValue() * 0.002f;
        } else if (mode == Mode.Flow) {
            effect.setUniformValue("mixFactor", (float)shaderChams.fill.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("radius", shaderChams.radius.getValueFloat());
            effect.setUniformValue("quality", shaderChams.smoothness.getValueFloat());
            effect.setUniformValue("divider", shaderChams.divider.getValueFloat());
            effect.setUniformValue("maxSample", shaderChams.maxSample.getValueFloat());
            effect.setUniformValue("resolution", (float)mc.getWindow().getScaledWidth(), (float)mc.getWindow().getScaledHeight());
            effect.setUniformValue("time", this.time);
            effect.render(mc.getTickDelta());
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

    public class _WNhDoPXLcRvHUWNoquud
    extends Framebuffer {
        public _WNhDoPXLcRvHUWNoquud(int width, int height) {
            super(false);
            RenderSystem.assertOnRenderThreadOrInit();
            this.resize(width, height, true);
            this.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public enum Mode
    {
        Solid,
        Smoke,
        Gradient,
        Snow,
        Flow,
        Rainbow
    }

    record RenderTask(Runnable task, Mode mode) {}
}
