package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import me.hextech.remapped.BlurManager_XkqtwkkpoPNjOLasIQyK;
import me.hextech.remapped.IShaderEffect;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BlurManager
implements Wrapper {
    private static final List<BlurManager_XkqtwkkpoPNjOLasIQyK> tasks = new ArrayList<BlurManager_XkqtwkkpoPNjOLasIQyK>();
    public static ManagedShaderEffect BLUR_OUTLINE;
    public static ManagedShaderEffect BLUR;
    public float time = 0.0f;
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private _cScSUnWhWBxyFlTcoHKo shaderBuffer;

    public void renderShader(Runnable task, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
        tasks.add(new BlurManager_XkqtwkkpoPNjOLasIQyK(task, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY));
    }

    public void renderShaders() {
        tasks.forEach(t -> this.applyShader(t.task(), t.Radius, t.BlurX, t.BlurY, t.BlurCoordX, t.BlurCoordY, t.offsetX, t.offsetY));
        tasks.clear();
    }

    public void applyShader(Runnable task, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
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
        task.run();
        this.shaderBuffer.method_1240();
        GlStateManager._glBindFramebuffer((int)36009, (int)MCBuffer.fbo);
        MCBuffer.beginWrite(false);
        ManagedShaderEffect shader = BLUR;
        Framebuffer mainBuffer = MinecraftClient.getInstance().getFramebuffer();
        Framebuffer outBuffer = shader.getShaderEffect().method_1264("bufOut");
        this.setupShader(shader, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY);
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

    private void setup(ManagedShaderEffect effect, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
        if (mc.getWindow() != null) {
            this.updateResolution();
        }
        effect.setUniformValue("Radius", Radius);
        effect.setUniformValue("BlurXY", BlurX + offsetX, (float)mc.getWindow().getScaledHeight() - BlurY - offsetY);
        effect.setUniformValue("BlurCoord", (float)((int)BlurCoordX), BlurCoordY);
        effect.render(mc.getTickDelta());
    }

    public void updateResolution() {
        this.scaledWidth = mc.getWindow().getFramebufferWidth();
        this.scaledHeight = mc.getWindow().getFramebufferHeight();
        this.scaleFactor = 1;
        boolean flag = mc.getWindow().getScaleFactor() > 1.0;
        int i = (Integer)BlurManager.mc.options.getGuiScale().getValue();
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        double scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
        double scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceil((double)scaledWidthD);
        this.scaledHeight = MathHelper.ceil((double)scaledHeightD);
    }

    public void setupShader(ManagedShaderEffect effect, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
        this.setup(effect, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY);
    }

    public void reloadShaders() {
        BLUR = ShaderEffectManager.getInstance().manage(new Identifier("minecraft", "shaders/post/blurarea.json"));
        BLUR_OUTLINE = ShaderEffectManager.getInstance().manage(new Identifier("minecraft", "shaders/post/blurarea.json"), managedShaderEffect -> {
            PostEffectProcessor effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).addHook("bufOut", BlurManager.mc.worldRenderer.getEntityOutlinesFramebuffer());
        });
    }

    public boolean fullNullCheck() {
        if (this.shaderBuffer == null || BLUR == null) {
            if (mc.getFramebuffer() == null) {
                return true;
            }
            this.shaderBuffer = new _cScSUnWhWBxyFlTcoHKo(BlurManager.mc.getFramebuffer().textureWidth, BlurManager.mc.getFramebuffer().textureHeight);
            this.reloadShaders();
            return true;
        }
        return false;
    }

    public class _cScSUnWhWBxyFlTcoHKo
    extends Framebuffer {
        public _cScSUnWhWBxyFlTcoHKo(int width, int height) {
            super(false);
            RenderSystem.assertOnRenderThreadOrInit();
            this.method_1234(width, height, true);
            this.method_1236(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
}
