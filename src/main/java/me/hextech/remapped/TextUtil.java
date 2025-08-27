package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class TextUtil implements Wrapper {
   public static final Matrix4f lastProjMat = new Matrix4f();
   public static final Matrix4f lastModMat = new Matrix4f();
   public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

   public static int drawStringPulse(DrawContext drawContext, OrderedText text, double x, double y, Color startColor, Color endColor, double speed, int counter) {
      char[] stringToCharArray = ((StringVisitable)ChatSetting_qVnAbgCzNciNTevKRovy.chatMessage.get(text)).getString().toCharArray();
      int index = 0;
      boolean color = false;
      String s = null;

      for (char c : stringToCharArray) {
         if (c == 167) {
            color = true;
         } else if (color) {
            if (c == 'r') {
               s = null;
            } else {
               s = "§" + c;
            }

            color = false;
         } else {
            index++;
            if (s != null) {
               drawContext.method_25303(mc.field_1772, s + c, (int)x, (int)y, startColor.getRGB());
            } else {
               drawContext.method_25303(
                  mc.field_1772, String.valueOf(c), (int)x, (int)y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB()
               );
            }

            x += (double)((float)mc.field_1772.method_1727(String.valueOf(c)));
         }
      }

      return (int)x;
   }

   public static void drawStringPulse(
      DrawContext drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter, boolean customFont
   ) {
      char[] stringToCharArray = text.toCharArray();
      int index = 0;
      boolean color = false;
      String s = null;

      for (char c : stringToCharArray) {
         if (c == 167) {
            color = true;
         } else if (color) {
            if (c == 'r') {
               s = null;
            } else {
               s = "§" + c;
            }

            color = false;
         } else {
            index++;
            if (s != null) {
               drawString(drawContext, s + c, x, y, startColor.getRGB(), customFont);
            } else {
               drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB(), customFont);
            }

            x += customFont ? (double)FontRenderers.Arial.getWidth(String.valueOf(c)) : (double)mc.field_1772.method_1727(String.valueOf(c));
         }
      }
   }

   public static boolean isCustomFont() {
      return ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.customFont.getValue() && FontRenderers.Arial != null;
   }

   public static float getWidth(String s) {
      return isCustomFont() ? FontRenderers.Arial.getWidth(s) : (float)mc.field_1772.method_1727(s);
   }

   public static float getHeight() {
      return isCustomFont() ? FontRenderers.Arial.getFontHeight() : 9.0F;
   }

   public static void drawStringWithScale(DrawContext drawContext, String text, float x, float y, Color color, float scale) {
      drawStringWithScale(drawContext, text, x, y, color.getRGB(), scale);
   }

   public static void drawStringWithScale(DrawContext drawContext, String text, float x, float y, int color, float scale) {
      MatrixStack matrixStack = drawContext.method_51448();
      if (scale != 1.0F) {
         matrixStack.method_22903();
         matrixStack.method_22905(scale, scale, 1.0F);
         if (scale > 1.0F) {
            matrixStack.method_46416(-x / scale, -y / scale, 0.0F);
         } else {
            matrixStack.method_46416(x / scale / 2.0F, y / scale / 2.0F, 0.0F);
         }
      }

      drawString(drawContext, text, (double)x, (double)y, color);
      matrixStack.method_22909();
   }

   public static void drawString(DrawContext drawContext, String text, double x, double y, int color, boolean customFont) {
      if (customFont) {
         FontRenderers.Arial.drawString(drawContext.method_51448(), text, (float)x, (float)y + 2.0F, color);
      } else {
         drawContext.method_51433(mc.field_1772, text, (int)x, (int)y, color, true);
      }
   }

   public static void drawString(DrawContext drawContext, String text, double x, double y, Color color) {
      drawString(drawContext, text, x, y, color.getRGB());
   }

   public static void drawString(DrawContext drawContext, String text, double x, double y, int color) {
      if (isCustomFont()) {
         FontRenderers.Arial.drawString(drawContext.method_51448(), text, (float)x, (float)y + 2.0F, color);
      } else {
         drawContext.method_51433(mc.field_1772, text, (int)x, (int)y, color, true);
      }
   }

   public static Vec3d worldSpaceToScreenSpace(Vec3d pos) {
      Camera camera = mc.method_1561().field_4686;
      int displayHeight = mc.method_22683().method_4507();
      int[] viewport = new int[4];
      GL11.glGetIntegerv(2978, viewport);
      Vector3f target = new Vector3f();
      double deltaX = pos.field_1352 - camera.method_19326().field_1352;
      double deltaY = pos.field_1351 - camera.method_19326().field_1351;
      double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
      Vector4f transformedCoordinates = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0F).mul(lastWorldSpaceMatrix);
      Matrix4f matrixProj = new Matrix4f(lastProjMat);
      Matrix4f matrixModel = new Matrix4f(lastModMat);
      matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
      return new Vec3d(
         (double)target.x / mc.method_22683().method_4495(), (double)((float)displayHeight - target.y) / mc.method_22683().method_4495(), (double)target.z
      );
   }

   public static void drawText(DrawContext context, String text, Vec3d vector) {
      drawText(context, text, vector, -1);
   }

   public static void drawText(DrawContext context, String text, Vec3d vector, int color) {
      vector = worldSpaceToScreenSpace(new Vec3d(vector.field_1352, vector.field_1351, vector.field_1350));
      if (vector.field_1350 > 0.0 && vector.field_1350 < 1.0) {
         double posX = vector.field_1352;
         double posY = vector.field_1351;
         double endPosX = Math.max(vector.field_1352, vector.field_1350);
         float scale = (float)Math.max(1.0 - EntityUtil.getEyesPos().method_1022(vector) * 0.025, 0.0);
         float diff = (float)(endPosX - posX) / 2.0F;
         float textWidth = (float)mc.field_1772.method_1727(text) * scale;
         float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0F)) * 1.0);
         context.method_51448().method_22903();
         context.method_51448().method_22905(scale, scale, scale);
         context.method_51433(mc.field_1772, text, (int)(tagX / scale), (int)((posY - 11.0 + 9.0 * 1.2) / (double)scale), color, true);
         context.method_51448().method_22909();
      }
   }
}
