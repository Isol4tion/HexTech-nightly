package me.hextech.remapped;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gl.VertexBuffer.Usage;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.BufferBuilder.BuiltBuffer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CustomSkyRenderer_zKYRFtdqYKdqrQYfieOq implements SkyRenderer {
   private static final Identifier NEBULA_1;
   private static final Identifier NEBULA_2;
   private static final Identifier HORIZON;
   private static final Identifier STARS;
   private static final Identifier FOG;
   private VertexBuffer nebula1;
   private VertexBuffer nebula2;
   private VertexBuffer horizon;
   private VertexBuffer stars1;
   private VertexBuffer stars2;
   private VertexBuffer stars3;
   private VertexBuffer stars4;
   private VertexBuffer fog;
   private Vector3f axis1;
   private Vector3f axis2;
   private Vector3f axis3;
   private Vector3f axis4;
   private boolean initialised;

   public static Identifier id(String path) {
      return new Identifier("better_sky", path);
   }

   public static double randRange(double min, double max, Random random) {
      return min + random.method_43058() * (max - min);
   }

   private void initialise() {
      if (!this.initialised) {
         this.initStars();
         CheckedRandom random = new CheckedRandom(131L);
         this.axis1 = new Vector3f(random.method_43057(), random.method_43057(), random.method_43057());
         this.axis2 = new Vector3f(random.method_43057(), random.method_43057(), random.method_43057());
         this.axis3 = new Vector3f(random.method_43057(), random.method_43057(), random.method_43057());
         this.axis4 = new Vector3f(random.method_43057(), random.method_43057(), random.method_43057());
         this.axis1.normalize();
         this.axis2.normalize();
         this.axis3.normalize();
         this.axis4.normalize();
         this.initialised = true;
      }
   }

   public void render(WorldRenderContext context) {
      if (context.world() != null && context.matrixStack() != null) {
         this.initialise();
         Matrix4f projectionMatrix = context.projectionMatrix();
         MatrixStack matrices = context.matrixStack();
         float time = ((float)context.world().method_8532() + context.tickDelta()) % 360000.0F * 1.7453292E-5F;
         float time2 = time * 2.0F;
         float time3 = time * 3.0F;
         BackgroundRenderer.method_3212();
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
         float blindA = 1.0F;
         float blind02 = blindA * 0.2F;
         float blind06 = blindA * 0.6F;
         if (blindA > 0.0F) {
            matrices.method_22903();
            matrices.method_22907(new Quaternionf().rotationXYZ(0.0F, time, 0.0F));
            RenderSystem.setShaderTexture(0, HORIZON);
            Color color = Skybox.INSTANCE.color.getValue();
            this.renderBuffer(
               matrices,
               projectionMatrix,
               this.horizon,
               VertexFormats.field_1585,
               (float)color.getRed() / 255.0F,
               (float)color.getGreen() / 255.0F,
               (float)color.getBlue() / 255.0F,
               0.7F * blindA
            );
            matrices.method_22909();
            matrices.method_22903();
            matrices.method_22907(new Quaternionf().rotationXYZ(0.0F, -time, 0.0F));
            RenderSystem.setShaderTexture(0, NEBULA_1);
            color = Skybox.INSTANCE.color2.getValue();
            this.renderBuffer(
               matrices,
               projectionMatrix,
               this.nebula1,
               VertexFormats.field_1585,
               (float)color.getRed() / 255.0F,
               (float)color.getGreen() / 255.0F,
               (float)color.getBlue() / 255.0F,
               blind02
            );
            matrices.method_22909();
            matrices.method_22903();
            matrices.method_22907(new Quaternionf().rotationXYZ(0.0F, time2, 0.0F));
            RenderSystem.setShaderTexture(0, NEBULA_2);
            color = Skybox.INSTANCE.color3.getValue();
            this.renderBuffer(
               matrices,
               projectionMatrix,
               this.nebula2,
               VertexFormats.field_1585,
               (float)color.getRed() / 255.0F,
               (float)color.getGreen() / 255.0F,
               (float)color.getBlue() / 255.0F,
               blind02
            );
            matrices.method_22909();
            if (Skybox.INSTANCE.stars.getValue()) {
               RenderSystem.setShaderTexture(0, STARS);
               matrices.method_22903();
               matrices.method_22907(new Quaternionf().setAngleAxis(time, this.axis3.x, this.axis3.y, this.axis3.z));
               color = Skybox.INSTANCE.color4.getValue();
               this.renderBuffer(
                  matrices,
                  projectionMatrix,
                  this.stars3,
                  VertexFormats.field_1585,
                  (float)color.getRed() / 255.0F,
                  (float)color.getGreen() / 255.0F,
                  (float)color.getBlue() / 255.0F,
                  blind06
               );
               matrices.method_22909();
               matrices.method_22903();
               matrices.method_22907(new Quaternionf().setAngleAxis(time2, this.axis4.x, this.axis4.y, this.axis4.z));
               color = Skybox.INSTANCE.color5.getValue();
               this.renderBuffer(
                  matrices,
                  projectionMatrix,
                  this.stars4,
                  VertexFormats.field_1585,
                  (float)color.getRed() / 255.0F,
                  (float)color.getGreen() / 255.0F,
                  (float)color.getBlue() / 255.0F,
                  blind06
               );
               matrices.method_22909();
            }
         }

         float a = 0.0F;
         if (0.0F > 0.0F) {
            if (a > 1.0F) {
               a = 1.0F;
            }

            RenderSystem.setShaderTexture(0, FOG);
            this.renderBuffer(matrices, projectionMatrix, this.fog, VertexFormats.field_1585, 0.3061791F, 0.2449433F, 0.3061791F, a);
         }

         if (Skybox.INSTANCE.stars.getValue() && blindA > 0.0F) {
            matrices.method_22903();
            matrices.method_22907(new Quaternionf().setAngleAxis(time3, this.axis1.x, this.axis1.y, this.axis1.z));
            this.renderBuffer(matrices, projectionMatrix, this.stars1, VertexFormats.field_1592, 1.0F, 1.0F, 1.0F, blind06);
            matrices.method_22909();
            matrices.method_22903();
            matrices.method_22907(new Quaternionf().setAngleAxis(time2, this.axis2.x, this.axis2.y, this.axis2.z));
            this.renderBuffer(matrices, projectionMatrix, this.stars2, VertexFormats.field_1592, 0.95F, 0.64F, 0.93F, blind06);
            matrices.method_22909();
         }

         RenderSystem.depthMask(true);
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   private void renderBuffer(MatrixStack matrices, Matrix4f matrix4f, VertexBuffer buffer, VertexFormat format, float r, float g, float b, float a) {
      RenderSystem.setShaderColor(r, g, b, a);
      buffer.method_1353();
      if (format == VertexFormats.field_1592) {
         buffer.method_34427(matrices.method_23760().method_23761(), matrix4f, GameRenderer.method_34539());
      } else {
         buffer.method_34427(matrices.method_23760().method_23761(), matrix4f, GameRenderer.method_34542());
      }

      VertexBuffer.method_1354();
   }

   private void initStars() {
      BufferBuilder buffer = Tessellator.method_1348().method_1349();
      if (Skybox.INSTANCE.stars.getValue()) {
         this.stars1 = this.buildBuffer(buffer, this.stars1, 0.1, 0.3, 3500, 41315L, this::makeStars);
         this.stars2 = this.buildBuffer(buffer, this.stars2, 0.1, 0.35, 2000, 35151L, this::makeStars);
         this.stars3 = this.buildBuffer(buffer, this.stars3, 0.4, 1.2, 1000, 61354L, this::makeUVStars);
         this.stars4 = this.buildBuffer(buffer, this.stars4, 0.4, 1.2, 1000, 61355L, this::makeUVStars);
      }

      this.nebula1 = this.buildBuffer(buffer, this.nebula1, 40.0, 60.0, 30, 11515L, this::makeFarFog);
      this.nebula2 = this.buildBuffer(buffer, this.nebula2, 40.0, 60.0, 10, 14151L, this::makeFarFog);
      this.horizon = this.buildBufferHorizon(buffer, this.horizon);
      this.fog = this.buildBufferFog(buffer, this.fog);
   }

   private VertexBuffer buildBuffer(
      BufferBuilder bufferBuilder, VertexBuffer buffer, double minSize, double maxSize, int count, long seed, CustomSkyRenderer fkt
   ) {
      if (buffer != null) {
         buffer.close();
      }

      buffer = new VertexBuffer(Usage.field_44793);
      fkt.make(bufferBuilder, minSize, maxSize, count, seed);
      BuiltBuffer renderedBuffer = bufferBuilder.method_1326();
      buffer.method_1353();
      buffer.method_1352(renderedBuffer);
      return buffer;
   }

   private VertexBuffer buildBufferHorizon(BufferBuilder bufferBuilder, VertexBuffer buffer) {
      return this.buildBuffer(
         bufferBuilder, buffer, 0.0, 0.0, 0, 0L, (_builder, _minSize, _maxSize, _count, _seed) -> this.makeCylinder(_builder, 16, 50.0, 100.0)
      );
   }

   private VertexBuffer buildBufferFog(BufferBuilder bufferBuilder, VertexBuffer buffer) {
      return this.buildBuffer(
         bufferBuilder, buffer, 0.0, 0.0, 0, 0L, (_builder, _minSize, _maxSize, _count, _seed) -> this.makeCylinder(_builder, 16, 50.0, 70.0)
      );
   }

   private void makeStars(BufferBuilder buffer, double minSize, double maxSize, int count, long seed) {
      CheckedRandom random = new CheckedRandom(seed);
      RenderSystem.setShader(GameRenderer::method_34539);
      buffer.method_1328(DrawMode.field_27382, VertexFormats.field_1592);

      for (int i = 0; i < count; i++) {
         double posX = random.method_43058() * 2.0 - 1.0;
         double posY = random.method_43058() * 2.0 - 1.0;
         double posZ = random.method_43058() * 2.0 - 1.0;
         double size = randRange(minSize, maxSize, random);
         double length = posX * posX + posY * posY + posZ * posZ;
         if (length < 1.0 && length > 0.001) {
            length = 1.0 / Math.sqrt(length);
            double var58;
            double px = (var58 = posX * length) * 100.0;
            double var59;
            double py = (var59 = posY * length) * 100.0;
            double var60;
            double pz = (var60 = posZ * length) * 100.0;
            double angle = Math.atan2(var58, var60);
            double sin1 = Math.sin(angle);
            double cos1 = Math.cos(angle);
            angle = Math.atan2(Math.sqrt(var58 * var58 + var60 * var60), var59);
            double sin2 = Math.sin(angle);
            double cos2 = Math.cos(angle);
            angle = random.method_43058() * Math.PI * 2.0;
            double sin3 = Math.sin(angle);
            double cos3 = Math.cos(angle);

            for (int index = 0; index < 4; index++) {
               double x = (double)((index & 2) - 1) * size;
               double y = (double)((index + 1 & 2) - 1) * size;
               double aa = x * cos3 - y * sin3;
               double ab = y * cos3 + x * sin3;
               double dy = aa * sin2 + 0.0 * cos2;
               double ae = 0.0 * sin2 - aa * cos2;
               double dx = ae * sin1 - ab * cos1;
               double dz = ab * sin1 + ae * cos1;
               buffer.method_22912(px + dx, py + dy, pz + dz).method_1344();
            }
         }
      }
   }

   private void makeUVStars(BufferBuilder buffer, double minSize, double maxSize, int count, long seed) {
      CheckedRandom random = new CheckedRandom(seed);
      RenderSystem.setShader(GameRenderer::method_34542);
      buffer.method_1328(DrawMode.field_27382, VertexFormats.field_1585);

      for (int i = 0; i < count; i++) {
         double posX = random.method_43058() * 2.0 - 1.0;
         double posY = random.method_43058() * 2.0 - 1.0;
         double posZ = random.method_43058() * 2.0 - 1.0;
         double size = randRange(minSize, maxSize, random);
         double length = posX * posX + posY * posY + posZ * posZ;
         if (length < 1.0 && length > 0.001) {
            length = 1.0 / Math.sqrt(length);
            double var61;
            double px = (var61 = posX * length) * 100.0;
            double var62;
            double py = (var62 = posY * length) * 100.0;
            double var63;
            double pz = (var63 = posZ * length) * 100.0;
            double angle = Math.atan2(var61, var63);
            double sin1 = Math.sin(angle);
            double cos1 = Math.cos(angle);
            angle = Math.atan2(Math.sqrt(var61 * var61 + var63 * var63), var62);
            double sin2 = Math.sin(angle);
            double cos2 = Math.cos(angle);
            angle = random.method_43058() * Math.PI * 2.0;
            double sin3 = Math.sin(angle);
            double cos3 = Math.cos(angle);
            float minV = (float)random.method_43048(4) / 4.0F;

            for (int index = 0; index < 4; index++) {
               double x = (double)((index & 2) - 1) * size;
               double y = (double)((index + 1 & 2) - 1) * size;
               double aa = x * cos3 - y * sin3;
               double ab = y * cos3 + x * sin3;
               double dy = aa * sin2 + 0.0 * cos2;
               double ae = 0.0 * sin2 - aa * cos2;
               double dx = ae * sin1 - ab * cos1;
               double dz = ab * sin1 + ae * cos1;
               float texU = (float)(index >> 1 & 1);
               float texV = (float)(index + 1 >> 1 & 1) / 4.0F + minV;
               buffer.method_22912(px + dx, py + dy, pz + dz).method_22913(texU, texV).method_1344();
            }
         }
      }
   }

   private void makeFarFog(BufferBuilder buffer, double minSize, double maxSize, int count, long seed) {
      CheckedRandom random = new CheckedRandom(seed);
      RenderSystem.setShader(GameRenderer::method_34542);
      buffer.method_1328(DrawMode.field_27382, VertexFormats.field_1585);

      for (int i = 0; i < count; i++) {
         double posX = random.method_43058() * 2.0 - 1.0;
         double posY = random.method_43058() - 0.5;
         double posZ = random.method_43058() * 2.0 - 1.0;
         double size = randRange(minSize, maxSize, random);
         double length = posX * posX + posY * posY + posZ * posZ;
         double distance = 2.0;
         if (length < 1.0 && length > 0.001) {
            length = distance / Math.sqrt(length);
            size *= distance;
            double var62;
            double px = (var62 = posX * length) * 100.0;
            double var63;
            double py = (var63 = posY * length) * 100.0;
            double var64;
            double pz = (var64 = posZ * length) * 100.0;
            double angle = Math.atan2(var62, var64);
            double sin1 = Math.sin(angle);
            double cos1 = Math.cos(angle);
            angle = Math.atan2(Math.sqrt(var62 * var62 + var64 * var64), var63);
            double sin2 = Math.sin(angle);
            double cos2 = Math.cos(angle);
            angle = random.method_43058() * Math.PI * 2.0;
            double sin3 = Math.sin(angle);
            double cos3 = Math.cos(angle);

            for (int index = 0; index < 4; index++) {
               double x = (double)((index & 2) - 1) * size;
               double y = (double)((index + 1 & 2) - 1) * size;
               double aa = x * cos3 - y * sin3;
               double ab = y * cos3 + x * sin3;
               double dy = aa * sin2 + 0.0 * cos2;
               double ae = 0.0 * sin2 - aa * cos2;
               double dx = ae * sin1 - ab * cos1;
               double dz = ab * sin1 + ae * cos1;
               float texU = (float)(index >> 1 & 1);
               float texV = (float)(index + 1 >> 1 & 1);
               buffer.method_22912(px + dx, py + dy, pz + dz).method_22913(texU, texV).method_1344();
            }
         }
      }
   }

   private void makeCylinder(BufferBuilder buffer, int segments, double height, double radius) {
      RenderSystem.setShader(GameRenderer::method_34542);
      buffer.method_1328(DrawMode.field_27382, VertexFormats.field_1585);

      for (int i = 0; i < segments; i++) {
         double a1 = (double)i * Math.PI * 2.0 / (double)segments;
         double a2 = (double)(i + 1) * Math.PI * 2.0 / (double)segments;
         double px1 = Math.sin(a1) * radius;
         double pz1 = Math.cos(a1) * radius;
         double px2 = Math.sin(a2) * radius;
         double pz2 = Math.cos(a2) * radius;
         float u0 = (float)i / (float)segments;
         float u1 = (float)(i + 1) / (float)segments;
         buffer.method_22912(px1, -height, pz1).method_22913(u0, 0.0F).method_1344();
         buffer.method_22912(px1, height, pz1).method_22913(u0, 1.0F).method_1344();
         buffer.method_22912(px2, height, pz2).method_22913(u1, 1.0F).method_1344();
         buffer.method_22912(px2, -height, pz2).method_22913(u1, 0.0F).method_1344();
      }
   }
}
