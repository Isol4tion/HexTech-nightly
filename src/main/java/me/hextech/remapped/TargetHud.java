package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Stack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4d;

public class TargetHud extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final Stack<Float> alphaMultipliers = new Stack();
   public static TargetHud INSTANCE;
   public static BetterDynamicAnimation healthAnimation = new BetterDynamicAnimation();
   public static BetterDynamicAnimation hurtAnimation = new BetterDynamicAnimation();
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
      matrixStack.method_22904(width, height, 0.0);
      matrixStack.method_22905((float)animation, (float)animation, 1.0F);
      matrixStack.method_22904(-width, -height, 0.0);
   }

   public static Color rainbowHSB(int delay, float s, float b) {
      double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
      rainbowState %= 360.0;
      return new Color((float)(rainbowState / 360.0), s, b);
   }

   public static void endRender() {
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static float compute(int initialAlpha) {
      float alpha = (float)initialAlpha;

      for (Float alphaMultiplier : alphaMultipliers) {
         alpha *= alphaMultiplier;
      }

      return alpha;
   }

   public static float transformColor(float f) {
      return compute((int)(f * 255.0F)) / 255.0F;
   }

   public static void setupRender() {
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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
      PlayerEntity target = null;
      if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOn()
         && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget != null
         && !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget.method_29504()) {
         target = AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget;
         this.direction = true;
      } else if (mc.field_1755 instanceof ChatScreen) {
         target = mc.field_1724;
         this.direction = true;
      } else {
         this.direction = false;
      }

      if (target != null) {
         drawContext.method_51448().method_22903();
         float posX = 114514.0F;
         float posY = 114514.0F;
         if (this.move.getValue() && (double)target.method_5739(mc.field_1724) <= this.moveDis.getValue()) {
            double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)mc.method_1488();
            double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)mc.method_1488();
            double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)mc.method_1488();
            Vec3d vector = new Vec3d(x, y + target.method_5829().method_17940() + (double)this.moveY.getValueInt(), z);
            vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.field_1352, vector.field_1351, vector.field_1350));
            if (vector.field_1350 > 0.0 && vector.field_1350 < 1.0) {
               Vector4d position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
               position.x = Math.min(vector.field_1352, position.x);
               position.y = Math.min(vector.field_1351, position.y);
               position.z = Math.max(vector.field_1352, position.z);
               float diff = (float)(position.z - position.x) / 2.0F;
               posX = (float)((position.x + (double)diff) * 1.0);
               posY = (float)(position.y - (double)(target.method_17682() / 2.0F));
            }
         }

         if (!this.move.getValue()) {
            posX = this.xOffset.getValueFloat();
            posY = this.yOffset.getValueFloat();
         }

         if (posX != 114514.0F && posY != 114514.0F) {
            float hurtPercent = (float)target.field_6235 / 8.0F;
            hurtAnimation.setValue((double)hurtPercent);
            String name = "Enemy: " + target.method_5477().getString();
            double health = (double)(target.method_6032() + target.method_6067());
            healthAnimation.setValue(health);
            health = healthAnimation.getAnimationD();
            String healthText = "Health: " + Math.round(health);
            String pops = "0";
            if (me.hextech.HexTech.POP.popContainer.containsKey(target.method_5477().getString())) {
               pops = String.valueOf(me.hextech.HexTech.POP.popContainer.get(target.method_5477().getString()));
            }

            String popText = "Kills: " + pops;
            float maxWidth = (float)Math.max(
               Math.max((double)mc.field_1772.method_1727(name), health * 100.0 / 36.0 + 10.0 + (double)mc.field_1772.method_1727(healthText)),
               (double)mc.field_1772.method_1727(popText)
            );
            this.renderRoundedQuad(
               drawContext.method_51448(),
               this.colorBack.getValue(),
               (double)posX,
               (double)((int)posY),
               (double)(posX + 46.0F + maxWidth + 5.0F),
               (double)(posY + 55.0F),
               this.ra.getValue(),
               4.0,
               !(this.blur.getValue() <= 0.0)
            );
            RenderSystem.setShaderColor(1.0F, (float)(1.0 - hurtAnimation.getAnimationD()), (float)(1.0 - hurtAnimation.getAnimationD()), 1.0F);
            drawContext.method_25293(
               ((AbstractClientPlayerEntity)target).method_52814().comp_1626(),
               (int)((double)posX + hurtAnimation.getAnimationD()) + 5,
               (int)((double)posY + hurtAnimation.getAnimationD()) + 5,
               (int)(44.0 - hurtAnimation.getAnimationD() * 2.0),
               (int)(44.0 - hurtAnimation.getAnimationD() * 2.0),
               8.0F,
               8.0F,
               8,
               8,
               64,
               64
            );
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.customFont.getValue()) {
               FontRenderers.Arial
                  .drawString(drawContext.method_51448(), name, (float)((int)(posX + 50.0F)), (float)((int)posY + 5), this.textColor.getValue().getRGB(), false);
            } else {
               FontRenderers.Calibri
                  .drawString(drawContext.method_51448(), name, (float)((int)(posX + 50.0F)), (float)((int)posY + 5), this.textColor.getValue().getRGB(), false);
            }

            if (!this.rainBow.getValue()) {
               this.renderRoundedQuad(
                  drawContext.method_51448(),
                  this.healthColor.getValue(),
                  (double)(posX + 50.0F),
                  (double)(posY + (float)((55 - 9) / 3)),
                  (double)(posX + 50.0F) + health * 100.0 / 36.0,
                  (double)(posY + (float)((55 - 9) / 3) + 9.0F),
                  this.ra.getValue(),
                  4.0,
                  false
               );
               if (this.ifglow.getValue()) {
                  Render2DUtil.drawBlurredShadow(
                     drawContext.method_51448(),
                     posX + 50.0F,
                     posY + (float)((55 - 9) / 3),
                     (float)(health * 100.0 / 36.0),
                     9 / 3 + 9,
                     this.glow.getValueInt(),
                     this.healthColor.getValue()
                  );
               }
            } else {
               if (this.timer.passed(50L)) {
                  this.progress = this.progress - this.rainbowSpeed.getValueInt();
                  this.timer.reset();
               }

               int counter = 20;

               for (double i = 0.0; i <= health * 100.0 / 36.0 - 1.5; i++) {
                  if (i <= health * 100.0 / 36.0 - 1.5) {
                     this.renderRoundedQuad(
                        drawContext.method_51448(),
                        this.rainbow(counter),
                        (double)(posX + 50.0F) + i,
                        (double)(posY + (float)((55 - 9) / 3)),
                        (double)(posX + 50.0F) + i + 1.5,
                        (double)(posY + (float)((55 - 9) / 3) + 9.0F),
                        0.0,
                        4.0,
                        false
                     );
                  } else {
                     this.renderRoundedQuad(
                        drawContext.method_51448(),
                        this.rainbow(counter),
                        (double)(posX + 50.0F) + i,
                        (double)(posY + (float)((55 - 9) / 3)),
                        (double)(posX + 50.0F) + i + 1.5,
                        (double)(posY + (float)((55 - 9) / 3) + 9.0F),
                        this.ra.getValue(),
                        4.0,
                        false
                     );
                  }

                  counter += this.pulseCounter.getValueInt();
               }
            }

            if (this.customFont.getValue()) {
               FontRenderers.Arial
                  .drawString(
                     drawContext.method_51448(),
                     healthText,
                     (float)((int)((double)(posX + 50.0F) + health * 100.0 / 36.0 + 10.0)),
                     (float)((int)(posY + (float)((55 - 9) / 3))),
                     this.textColor.getValue().getRGB(),
                     false
                  );
               FontRenderers.Arial
                  .drawString(
                     drawContext.method_51448(),
                     popText,
                     (float)((int)(posX + 50.0F)),
                     (float)((int)(posY + (float)((55 - 9) / 3 * 2))),
                     this.textColor.getValue().getRGB(),
                     false
                  );
            } else {
               FontRenderers.Calibri
                  .drawString(
                     drawContext.method_51448(),
                     healthText,
                     (float)((int)((double)(posX + 50.0F) + health * 100.0 / 36.0 + 10.0)),
                     (float)((int)(posY + (float)((55 - 9) / 3))),
                     this.textColor.getValue().getRGB(),
                     false
                  );
               FontRenderers.Calibri
                  .drawString(
                     drawContext.method_51448(),
                     popText,
                     (float)((int)(posX + 50.0F)),
                     (float)((int)(posY + (float)((55 - 9) / 3 * 2))),
                     this.textColor.getValue().getRGB(),
                     false
                  );
            }

            drawContext.method_51448().method_22909();
         }
      }
   }

   public void renderRoundedQuad(
      MatrixStack matrices,
      Color c,
      double fromX,
      double fromY,
      double toX,
      double toY,
      double radC1,
      double radC2,
      double radC3,
      double radC4,
      double samples,
      boolean blur
   ) {
      int color = c.getRGB();
      Matrix4f matrix = matrices.method_23760().method_23761();
      float f = transformColor((float)(color >> 24 & 0xFF) / 255.0F);
      float g = (float)(color >> 16 & 0xFF) / 255.0F;
      float h = (float)(color >> 8 & 0xFF) / 255.0F;
      float k = (float)(color & 0xFF) / 255.0F;
      setupRender();
      RenderSystem.setShader(GameRenderer::method_34540);
      this.renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, radC1, radC2, radC3, radC4, samples);
      if (blur) {
      }

      endRender();
   }

   private Color rainbow(int delay) {
      double rainbowState = java.lang.Math.ceil(((double)this.progress + (double)delay * this.rainbowDelay.getValue()) / 20.0);
      return Color.getHSBColor((float)(rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0F, 1.0F);
   }

   public void renderRoundedQuadInternal(
      Matrix4f matrix,
      float cr,
      float cg,
      float cb,
      float ca,
      double fromX,
      double fromY,
      double toX,
      double toY,
      double radC1,
      double radC2,
      double radC3,
      double radC4,
      double samples
   ) {
      BufferBuilder bufferBuilder = Tessellator.method_1348().method_1349();
      bufferBuilder.method_1328(DrawMode.field_27381, VertexFormats.field_1576);
      double[][] map = new double[][]{
         {toX - radC4, toY - radC4, radC4}, {toX - radC2, fromY + radC2, radC2}, {fromX + radC1, fromY + radC1, radC1}, {fromX + radC3, toY - radC3, radC3}
      };

      for (int i = 0; i < 4; i++) {
         double[] current = map[i];
         double rad = current[2];
         double r = (double)i * 90.0;

         while (r < 90.0 + (double)i * 90.0) {
            float rad1 = (float)Math.toRadians(r);
            float sin = (float)((double)Math.sin(rad1) * rad);
            float cos = (float)((double)Math.cos(rad1) * rad);
            bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr, cg, cb, ca).method_1344();
            r += 90.0 / samples;
         }

         float rad1 = (float)Math.toRadians(90.0 + (double)i * 90.0);
         float sin = (float)((double)Math.sin(rad1) * rad);
         float cos = (float)((double)Math.cos(rad1) * rad);
         bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr, cg, cb, ca).method_1344();
      }

      BufferRenderer.method_43433(bufferBuilder.method_1326());
   }

   public void renderRoundedQuad(MatrixStack stack, Color c, double x, double y, double x1, double y1, double rad, double samples, boolean blur) {
      this.renderRoundedQuad(stack, c, x, y, x1, y1, rad, rad, rad, rad, samples, blur);
   }

   public PlayerEntity getTarget() {
      float min = 1000000.0F;
      PlayerEntity best = null;

      for (PlayerEntity player : mc.field_1687.method_18456()) {
         if (player.method_5739(mc.field_1724) < min && !player.method_29504() && player != mc.field_1724 && !me.hextech.HexTech.FRIEND.isFriend(player)) {
            min = player.method_5739(mc.field_1724);
            best = player;
         }
      }

      return best;
   }
}
