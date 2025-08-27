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
        Framebuffer MCBuffer = MinecraftClient.method_1551().method_1522();
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
            this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
        }
        GlStateManager._glBindFramebuffer((int)36009, (int)this.shaderBuffer.field_1476);
        this.shaderBuffer.method_1235(true);
        task.run();
        this.shaderBuffer.method_1240();
        GlStateManager._glBindFramebuffer((int)36009, (int)MCBuffer.field_1476);
        MCBuffer.method_1235(false);
        ManagedShaderEffect shader = BLUR;
        Framebuffer mainBuffer = MinecraftClient.method_1551().method_1522();
        Framebuffer outBuffer = shader.getShaderEffect().method_1264("bufOut");
        this.setupShader(shader, Radius, BlurX, BlurY, BlurCoordX, BlurCoordY, offsetX, offsetY);
        this.shaderBuffer.method_1230(false);
        mainBuffer.method_1235(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ZERO, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE);
        RenderSystem.backupProjectionMatrix();
        outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void setup(ManagedShaderEffect effect, float Radius, float BlurX, float BlurY, float BlurCoordX, float BlurCoordY, float offsetX, float offsetY) {
        if (mc.method_22683() != null) {
            this.updateResolution();
        }
        effect.setUniformValue("Radius", Radius);
        effect.setUniformValue("BlurXY", BlurX + offsetX, (float)mc.method_22683().method_4502() - BlurY - offsetY);
        effect.setUniformValue("BlurCoord", (float)((int)BlurCoordX), BlurCoordY);
        effect.render(mc.getTickDelta());
    }

    public void updateResolution() {
        this.scaledWidth = mc.method_22683().method_4489();
        this.scaledHeight = mc.method_22683().method_4506();
        this.scaleFactor = 1;
        boolean flag = mc.method_22683().method_4495() > 1.0;
        int i = (Integer)BlurManager.mc.field_1690.method_42474().method_41753();
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
        this.scaledWidth = MathHelper.method_15384((double)scaledWidthD);
        this.scaledHeight = MathHelper.method_15384((double)scaledHeightD);
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
            ((IShaderEffect)effect).addHook("bufOut", BlurManager.mc.field_1769.method_22990());
        });
    }

    public boolean fullNullCheck() {
        if (this.shaderBuffer == null || BLUR == null) {
            if (mc.method_1522() == null) {
                return true;
            }
            this.shaderBuffer = new _cScSUnWhWBxyFlTcoHKo(BlurManager.mc.method_1522().field_1482, BlurManager.mc.method_1522().field_1481);
            this.reloadShaders();
            return true;
        }
        return false;
    }

    public static class _cScSUnWhWBxyFlTcoHKo
    extends Framebuffer {
        public _cScSUnWhWBxyFlTcoHKo(int n, int n2) {
        }
    }
}
