package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Objects;
import java.util.Stack;
import me.hextech.HexTech;
import me.hextech.remapped.api.utils.render.BetterAnimation;
import me.hextech.remapped.api.utils.render.BetterDynamicAnimation;
import me.hextech.remapped.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4d;

public class TargetHud
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final Stack<Float> alphaMultipliers;
    public static TargetHud INSTANCE;
    public static BetterDynamicAnimation healthAnimation;
    public static BetterDynamicAnimation hurtAnimation;
    private final SliderSetting xOffset = this.add(new SliderSetting("X", 0, 0, 2000));
    private final SliderSetting yOffset = this.add(new SliderSetting("Y", 10, 0, 2000));
    private final ColorSetting colorBack = this.add(new ColorSetting("BackGround", new Color(0, 0, 0, 200)));
    private final SliderSetting blur = this.add(new SliderSetting("Blur", 20, 0, 50));
    private final ColorSetting healthColor = this.add(new ColorSetting("Health", new Color(255, 0, 0, 200)));
    private final ColorSetting textColor = this.add(new ColorSetting("Text", new Color(255, 255, 255, 255)));
    private final BooleanSetting rainBow = this.add(new BooleanSetting("Rainbow", false)).setParent();
    private final SliderSetting pulseCounter = this.add(new SliderSetting("PulseCounter", 1, 1, 10, v -> this.rainBow.isOpen()));
    private final SliderSetting rainbowSpeed = this.add(new SliderSetting("RainbowSpeed", 200, 1, 400, v -> this.rainBow.isOpen()));
    private final SliderSetting saturation = this.add(new SliderSetting("Saturation", 130.0, 1.0, 255.0, v -> this.rainBow.isOpen()));
    private final SliderSetting rainbowDelay = this.add(new SliderSetting("Delay", 350, 0, 600, v -> this.rainBow.isOpen()));
    private final BooleanSetting move = this.add(new BooleanSetting("TargetPlayer", false)).setParent();
    private final SliderSetting moveDis = this.add(new SliderSetting("PlayerDis", 8, 1, 100, v -> this.move.isOpen()));
    private final SliderSetting moveY = this.add(new SliderSetting("DisY", 0.3, -1.0, 1.0, 0.1, v -> this.move.isOpen()));
    private final SliderSetting T1 = this.add(new SliderSetting("TextW", 0.3, -1.0, 1.0, 0.1));
    private final SliderSetting ra = this.add(new SliderSetting("radius", 3, 0, 10));
    private final Timer timer = new Timer();
    public BooleanSetting customFont = this.add(new BooleanSetting("CustomFont", false));
    public BooleanSetting ifglow = this.add(new BooleanSetting("isGlow", false)).setParent();
    private final SliderSetting glow = this.add(new SliderSetting("Glow", 20, 1, 50, v -> this.ifglow.isOpen()));
    public BetterAnimation animation = new BetterAnimation();
    int progress = 0;
    private boolean direction = false;

    public TargetHud() {
        super("TargetHud", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    public static void sizeAnimation(MatrixStack matrixStack, double width, double height, double animation) {
        matrixStack.translate(width, height, 0.0);
        matrixStack.scale((float)animation, (float)animation, 1.0f);
        matrixStack.translate(-width, -height, 0.0);
    }

    public static Color rainbowHSB(int delay, float s, float b) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return new Color((float)((rainbowState %= 360.0) / 360.0), s, b);
    }

    public static void endRender() {
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static float compute(int initialAlpha) {
        float alpha = initialAlpha;
        for (Float alphaMultiplier : alphaMultipliers) {
            alpha *= alphaMultiplier.floatValue();
        }
        return alpha;
    }

    public static float transformColor(float f) {
        return TargetHud.compute((int)(f * 255.0f)) / 255.0f;
    }

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onEnable() {
        this.direction = false;
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.direction = false;
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        healthAnimation.update();
        hurtAnimation.update();
        this.animation.update(this.direction);
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        ClientPlayerEntity target = null;
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOn() && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget != null && !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget.isDead()) {
            target = (ClientPlayerEntity) AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget;
            this.direction = true;
        } else if (TargetHud.mc.currentScreen instanceof ChatScreen) {
            target = TargetHud.mc.player;
            this.direction = true;
        } else {
            this.direction = false;
        }
        if (target == null) {
            return;
        }
        drawContext.getMatrices().push();
        float posX = 114514.0f;
        float posY = 114514.0f;
        if (this.move.getValue() && (double)target.distanceTo(TargetHud.mc.player) <= this.moveDis.getValue()) {
            double x = target.prevX + (target.getX() - target.prevX) * (double)mc.getTickDelta();
            double y = target.prevY + (target.getY() - target.prevY) * (double)mc.getTickDelta();
            double z = target.prevZ + (target.getZ() - target.prevZ) * (double)mc.getTickDelta();
            Vec3d vector = new Vec3d(x, y + target.getBoundingBox().getLengthY() + (double)this.moveY.getValueInt(), z);
            vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
            if (vector.z > 0.0 && vector.z < 1.0) {
                Vector4d position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                float diff = (float)(position.z - position.x) / 2.0f;
                posX = (float)((position.x + (double) diff));
                posY = (float)(position.y - (double)(target.getHeight() / 2.0f));
            }
        }
        if (!this.move.getValue()) {
            posX = this.xOffset.getValueFloat();
            posY = this.yOffset.getValueFloat();
        }
        if (posX == 114514.0f || posY == 114514.0f) {
            return;
        }
        float hurtPercent = (float)target.hurtTime / 8.0f;
        hurtAnimation.setValue(hurtPercent);
        String name = "Enemy: " + target.getName().getString();
        double health = target.getHealth() + target.getAbsorptionAmount();
        healthAnimation.setValue(health);
        health = healthAnimation.getAnimationD();
        String healthText = "Health: " + Math.round(health);
        String pops = "0";
        if (HexTech.POP.popContainer.containsKey(target.getName().getString())) {
            pops = String.valueOf(HexTech.POP.popContainer.get(target.getName().getString()));
        }
        String popText = "Kills: " + pops;
        float maxWidth = (float)Math.max(Math.max(TargetHud.mc.textRenderer.getWidth(name), health * 100.0 / 36.0 + 10.0 + (double)TargetHud.mc.textRenderer.getWidth(healthText)), TargetHud.mc.textRenderer.getWidth(popText));
        this.renderRoundedQuad(drawContext.getMatrices(), this.colorBack.getValue(), posX, (int)posY, posX + 46.0f + maxWidth + 5.0f, posY + 55.0f, this.ra.getValue(), 4.0, !(this.blur.getValue() <= 0.0));
        RenderSystem.setShaderColor(1.0f, (float)(1.0 - hurtAnimation.getAnimationD()), (float)(1.0 - hurtAnimation.getAnimationD()), 1.0f);
        drawContext.drawTexture(target.getSkinTextures().texture(), (int)((double)posX + hurtAnimation.getAnimationD()) + 5, (int)((double)posY + hurtAnimation.getAnimationD()) + 5, (int)(44.0 - hurtAnimation.getAnimationD() * 2.0), (int)(44.0 - hurtAnimation.getAnimationD() * 2.0), 8.0f, 8.0f, 8, 8, 64, 64);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.customFont.getValue()) {
            FontRenderers.Arial.drawString(drawContext.getMatrices(), name, (int)(posX + 50.0f), (int)posY + 5, this.textColor.getValue().getRGB(), false);
        } else {
            FontRenderers.Calibri.drawString(drawContext.getMatrices(), name, (int)(posX + 50.0f), (int)posY + 5, this.textColor.getValue().getRGB(), false);
        }
        if (!this.rainBow.getValue()) {
            MatrixStack matrixStack = drawContext.getMatrices();
            Color color = this.healthColor.getValue();
            double d = posX + 50.0f;
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            double d2 = posY + (float)((55 - 9) / 3);
            double d3 = (double)(posX + 50.0f) + health * 100.0 / 36.0;
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            float f = posY + (float)((55 - 9) / 3);
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            this.renderRoundedQuad(matrixStack, color, d, d2, d3, f + 9.0f, this.ra.getValue(), 4.0, false);
            if (this.ifglow.getValue()) {
                MatrixStack matrixStack2 = drawContext.getMatrices();
                Objects.requireNonNull(TargetHud.mc.textRenderer);
                float f2 = posY + (float)((55 - 9) / 3);
                float f3 = (float)(health * 100.0 / 36.0);
                Objects.requireNonNull(TargetHud.mc.textRenderer);
                int n = 9 / 3;
                Objects.requireNonNull(TargetHud.mc.textRenderer);
                Render2DUtil.drawBlurredShadow(matrixStack2, posX + 50.0f, f2, f3, n + 9, this.glow.getValueInt(), this.healthColor.getValue());
            }
        } else {
            if (this.timer.passed(50L)) {
                this.progress -= this.rainbowSpeed.getValueInt();
                this.timer.reset();
            }
            int counter = 20;
            for (double i = 0.0; i <= health * 100.0 / 36.0 - 1.5; i += 1.5) {
                if (i <= health * 100.0 / 36.0 - 1.5) {
                    MatrixStack matrixStack = drawContext.getMatrices();
                    Color color = this.rainbow(counter);
                    double d = (double)(posX + 50.0f) + i;
                    Objects.requireNonNull(TargetHud.mc.textRenderer);
                    double d4 = posY + (float)((55 - 9) / 3);
                    double d5 = (double)(posX + 50.0f) + i + 1.5;
                    Objects.requireNonNull(TargetHud.mc.textRenderer);
                    float f = posY + (float)((55 - 9) / 3);
                    Objects.requireNonNull(TargetHud.mc.textRenderer);
                    this.renderRoundedQuad(matrixStack, color, d, d4, d5, f + 9.0f, 0.0, 4.0, false);
                } else {
                    MatrixStack matrixStack = drawContext.getMatrices();
                    Color color = this.rainbow(counter);
                    double d = (double)(posX + 50.0f) + i;
                    Objects.requireNonNull(TargetHud.mc.textRenderer);
                    double d6 = posY + (float)((55 - 9) / 3);
                    double d7 = (double)(posX + 50.0f) + i + 1.5;
                    Objects.requireNonNull(TargetHud.mc.textRenderer);
                    float f = posY + (float)((55 - 9) / 3);
                    Objects.requireNonNull(TargetHud.mc.textRenderer);
                    this.renderRoundedQuad(matrixStack, color, d, d6, d7, f + 9.0f, this.ra.getValue(), 4.0, false);
                }
                counter += this.pulseCounter.getValueInt();
            }
        }
        if (this.customFont.getValue()) {
            MatrixStack matrixStack = drawContext.getMatrices();
            float f = (int)((double)(posX + 50.0f) + health * 100.0 / 36.0 + 10.0);
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            FontRenderers.Arial.drawString(matrixStack, healthText, f, (int)(posY + (float)((55 - 9) / 3)), this.textColor.getValue().getRGB(), false);
            MatrixStack matrixStack3 = drawContext.getMatrices();
            float f4 = (int)(posX + 50.0f);
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            FontRenderers.Arial.drawString(matrixStack3, popText, f4, (int)(posY + (float)((55 - 9) / 3 * 2)), this.textColor.getValue().getRGB(), false);
        } else {
            MatrixStack matrixStack = drawContext.getMatrices();
            float f = (int)((double)(posX + 50.0f) + health * 100.0 / 36.0 + 10.0);
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            FontRenderers.Calibri.drawString(matrixStack, healthText, f, (int)(posY + (float)((55 - 9) / 3)), this.textColor.getValue().getRGB(), false);
            MatrixStack matrixStack4 = drawContext.getMatrices();
            float f5 = (int)(posX + 50.0f);
            Objects.requireNonNull(TargetHud.mc.textRenderer);
            FontRenderers.Calibri.drawString(matrixStack4, popText, f5, (int)(posY + (float)((55 - 9) / 3 * 2)), this.textColor.getValue().getRGB(), false);
        }
        drawContext.getMatrices().pop();
    }

    public void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double radC1, double radC2, double radC3, double radC4, double samples, boolean blur) {
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = TargetHud.transformColor((float)(color >> 24 & 0xFF) / 255.0f);
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        TargetHud.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        this.renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, radC1, radC2, radC3, radC4, samples);
        if (blur) {
            // empty if block
        }
        TargetHud.endRender();
    }

    private Color rainbow(int delay) {
        double rainbowState = java.lang.Math.ceil(((double)this.progress + (double)delay * this.rainbowDelay.getValue()) / 20.0);
        return Color.getHSBColor((float)(rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0f, 1.0f);
    }

    public void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radC1, double radC2, double radC3, double radC4, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        double[][] map = new double[][]{{toX - radC4, toY - radC4, radC4}, {toX - radC2, fromY + radC2, radC2}, {fromX + radC1, fromY + radC1, radC1}, {fromX + radC3, toY - radC3, radC3}};
        for (int i = 0; i < 4; ++i) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = (double)i * 90.0; r < 90.0 + (double)i * 90.0; r += 90.0 / samples) {
                float rad1 = (float)Math.toRadians(r);
                float sin = (float)((double)Math.sin(rad1) * rad);
                float cos = (float)((double)Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca).next();
            }
            float rad1 = (float)Math.toRadians(90.0 + (double)i * 90.0);
            float sin = (float)((double)Math.sin(rad1) * rad);
            float cos = (float)((double)Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca).next();
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public void renderRoundedQuad(MatrixStack stack, Color c, double x, double y, double x1, double y1, double rad, double samples, boolean blur) {
        this.renderRoundedQuad(stack, c, x, y, x1, y1, rad, rad, rad, rad, samples, blur);
    }

    public PlayerEntity getTarget() {
        float min = 1000000.0f;
        PlayerEntity best = null;
        for (PlayerEntity player : TargetHud.mc.world.getPlayers()) {
            if (!(player.distanceTo(TargetHud.mc.player) < min) || player.isDead() || player == TargetHud.mc.player || HexTech.FRIEND.isFriend(player)) continue;
            min = player.distanceTo(TargetHud.mc.player);
            best = player;
        }
        return best;
    }

    static {
        healthAnimation = new BetterDynamicAnimation();
        hurtAnimation = new BetterDynamicAnimation();
        alphaMultipliers = new Stack();
    }
}
