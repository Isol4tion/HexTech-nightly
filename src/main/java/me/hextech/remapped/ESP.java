package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.asm.accessors.IEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4d;

public class ESP extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public final EnumSetting mode = this.add(new EnumSetting("Type", ESP_iJUSlEiinRCZKkYYmFmY.text));
   private final ColorSetting item = this.add(new ColorSetting("Item", new Color(255, 255, 255, 100)).injectBoolean(true));
   private final ColorSetting boxcolor = this.add(new ColorSetting("Box Color", new Color(255, 255, 255, 100)));
   private final ColorSetting textcolor = this.add(
      new ColorSetting("Text Color", new Color(255, 255, 255, 255), v -> this.mode.getValue() == ESP_iJUSlEiinRCZKkYYmFmY.text)
   );
   private final ColorSetting player = this.add(new ColorSetting("Player", new Color(255, 255, 255, 100)).injectBoolean(true));
   private final ColorSetting chest = this.add(new ColorSetting("Chest", new Color(255, 255, 255, 100)).injectBoolean(false));

   public ESP() {
      super("ESP", Module_JlagirAibYQgkHtbRnhw.Render);
   }

   private static Vec3d[] getPoints(Entity ent) {
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_1488();
      double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)mc.method_1488();
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_1488();
      Box axisAlignedBB2 = ent.method_5829();
      Box axisAlignedBB = new Box(
         axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05,
         axisAlignedBB2.field_1322 - ent.method_23318() + y,
         axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05,
         axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05,
         axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15,
         axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05
      );
      return new Vec3d[]{
         new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321),
         new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321),
         new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321),
         new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321),
         new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324),
         new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324),
         new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324),
         new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)
      };
   }

   public static void endRender() {
      RenderSystem.disableBlend();
   }

   public static void setupRender() {
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void setTrianglePoints(BufferBuilder bufferBuilder, Matrix4f matrix, float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
      bufferBuilder.method_22918(matrix, x3, y3, 0.0F).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
      bufferBuilder.method_22918(matrix, x3, y3, 0.0F).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
   }

   @Override
   public void onRender2D(DrawContext context, float tickDelta) {
      for (Entity entity : mc.field_1687.method_18112()) {
         if (entity instanceof ItemEntity) {
            if (this.mode.getValue() == ESP_iJUSlEiinRCZKkYYmFmY.text) {
               Vec3d[] vectors = getPoints(entity);
               Vector4d position = null;

               for (Vec3d vector : vectors) {
                  vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.field_1352, vector.field_1351, vector.field_1350));
                  if (vector.field_1350 > 0.0 && vector.field_1350 < 1.0) {
                     if (position == null) {
                        position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
                     }

                     position.x = Math.min(vector.field_1352, position.x);
                     position.y = Math.min(vector.field_1351, position.y);
                     position.z = Math.max(vector.field_1352, position.z);
                     position.w = Math.max(vector.field_1351, position.w);
                  }
               }

               if (position != null) {
                  float posX = (float)position.x;
                  float posY = (float)position.y;
                  float endPosX = (float)position.z;
                  float diff = (endPosX - posX) / 2.0F;
                  float textWidth = FontRenderers.Arial.getStringWidth(entity.method_5476().getString()) * 1.0F;
                  float tagX = (posX + diff - textWidth / 2.0F) * 1.0F;
                  FontRenderers.Arial
                     .drawString(context.method_51448(), entity.method_5476().getString(), tagX, posY - 10.0F, this.textcolor.getValue().getRGB());
               }
            }

            Matrix4f matrix = context.method_51448().method_23760().method_23761();
            BufferBuilder bufferBuilder = Tessellator.method_1348().method_1349();
            setupRender();
            RenderSystem.setShader(GameRenderer::method_34540);
            bufferBuilder.method_1328(DrawMode.field_27377, VertexFormats.field_1576);

            for (Entity ent : mc.field_1687.method_18112()) {
               if (ent instanceof ItemEntity) {
                  Vec3d[] vectors = getPoints(ent);
                  Vector4d position = null;

                  for (Vec3d vectorx : vectors) {
                     vectorx = TextUtil.worldSpaceToScreenSpace(new Vec3d(vectorx.field_1352, vectorx.field_1351, vectorx.field_1350));
                     if (vectorx.field_1350 > 0.0 && vectorx.field_1350 < 1.0) {
                        if (position == null) {
                           position = new Vector4d(vectorx.field_1352, vectorx.field_1351, vectorx.field_1350, 0.0);
                        }

                        position.x = Math.min(vectorx.field_1352, position.x);
                        position.y = Math.min(vectorx.field_1351, position.y);
                        position.z = Math.max(vectorx.field_1352, position.z);
                        position.w = Math.max(vectorx.field_1351, position.w);
                     }
                  }

                  if (position != null) {
                     float posX = (float)position.x;
                     float posY = (float)position.y;
                     float endPosX = (float)position.z;
                     float endPosY = (float)position.w;
                     this.drawEquilateralTriangle(bufferBuilder, matrix, posX, posY, endPosX, endPosY, this.boxcolor.getValue());
                  }
               }
            }

            BufferRenderer.method_43433(bufferBuilder.method_1326());
            endRender();
         }
      }
   }

   private double interpolate(double previous, double current, float delta) {
      return previous + (current - previous) * (double)delta;
   }

   private void drawEquilateralTriangle(BufferBuilder bufferBuilder, Matrix4f stack, float posX, float posY, float endX, float endY, Color color) {
      float sideLength = Math.abs(endX - posX);
      float height = (float)((double)sideLength * Math.sqrt(3.0) / 2.0);
      float halfSide = sideLength / 2.0F;
      setTrianglePoints(bufferBuilder, stack, posX, endY, endX, endY, posX + halfSide, endY - height, color);
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (this.item.booleanValue || this.player.booleanValue) {
         for (Entity entity : mc.field_1687.method_18112()) {
            if (entity instanceof ItemEntity && this.item.booleanValue) {
               Color color = this.item.getValue();
               Render3DUtil.draw3DBox(
                  matrixStack,
                  ((IEntity)entity)
                     .getDimensions()
                     .method_30757(
                        new Vec3d(
                           MathUtil.interpolate(entity.field_6038, entity.method_23317(), partialTicks),
                           MathUtil.interpolate(entity.field_5971, entity.method_23318(), partialTicks),
                           MathUtil.interpolate(entity.field_5989, entity.method_23321(), partialTicks)
                        )
                     ),
                  color,
                  false,
                  true
               );
            } else if (entity instanceof PlayerEntity && this.player.booleanValue) {
               Color color = this.player.getValue();
               Render3DUtil.draw3DBox(
                  matrixStack,
                  ((IEntity)entity)
                     .getDimensions()
                     .method_30757(
                        new Vec3d(
                           MathUtil.interpolate(entity.field_6038, entity.method_23317(), partialTicks),
                           MathUtil.interpolate(entity.field_5971, entity.method_23318(), partialTicks),
                           MathUtil.interpolate(entity.field_5989, entity.method_23321(), partialTicks)
                        )
                     )
                     .method_1009(0.0, 0.1, 0.0),
                  color,
                  false,
                  true
               );
            }
         }
      }

      if (this.chest.booleanValue) {
         for (BlockEntity blockEntity : BlockUtil.getTileEntities()) {
            if (blockEntity instanceof ChestBlockEntity) {
               Box box = new Box(blockEntity.method_11016());
               Render3DUtil.draw3DBox(matrixStack, box, this.chest.getValue());
            }
         }
      }
   }
}
