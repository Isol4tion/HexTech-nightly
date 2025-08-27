package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome.Precipitation;

public class Weather_BfaBZRqvqRbKrhUkvqny extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final Identifier RAIN;
   private static final Identifier SNOW;
   private static final float[] weatherXCoords;
   private static final float[] weatherYCoords;
   private final EnumSetting precipitationSetting = this.add(new EnumSetting("Type", Weather.Rain));
   private final SliderSetting height = this.add(new SliderSetting("height", 0.0, 0.0, 320.0, 0.01));
   private final SliderSetting strength = this.add(new SliderSetting("Strength", 0.8, 0.0, 200.0, 0.01));
   private final ColorSetting weatherColor = this.add(new ColorSetting("WeatherColor", new Color(14473686)));
   private final SliderSetting expandSize = this.add(new SliderSetting("ExpandSize", 5.0, 1.0, 10.0, 0.01));
   private final SliderSetting snowFallingSpeedMultiplier = this.add(new SliderSetting("Multi", 1.0, 1.0, 100.0, 0.01));
   private final int ticks = 0;

   public Weather_BfaBZRqvqRbKrhUkvqny() {
      super("Weather", Module_JlagirAibYQgkHtbRnhw.Render);
   }

   @EventHandler
   private void onWeather(WeatherRenderEvent event) {
      if (this.precipitationSetting.getValue().equals(Weather.Both)) {
         this.render(event, Weather.Rain);
         this.render(event, Weather.Snow);
         event.cancel();
      } else {
         event.cancel();
      }
   }

   private void render(WeatherRenderEvent event, Weather precipitationType) {
      LightmapTextureManager manager = event.lightmapTextureManager;
      double cameraX = event.cameraX;
      double cameraY = event.cameraY;
      double cameraZ = event.cameraZ;
      float tickDelta = event.tickDelta;
      float f = this.strength.getValueFloat();
      float red = (float)this.weatherColor.getValue().getRed() / 255.0F;
      float blue = (float)this.weatherColor.getValue().getBlue() / 255.0F;
      float green = (float)this.weatherColor.getValue().getGreen() / 255.0F;
      manager.method_3316();
      int cameraIntX = MathHelper.method_15357(cameraX);
      int cameraIntY = MathHelper.method_15357(cameraY);
      int cameraIntZ = MathHelper.method_15357(cameraZ);
      Tessellator tessellator = Tessellator.method_1348();
      BufferBuilder bufferBuilder = tessellator.method_1349();
      RenderSystem.disableCull();
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(MinecraftClient.method_29611());
      RenderSystem.setShader(GameRenderer::method_34546);
      Mutable mutable = new Mutable();
      int expand = this.expandSize.getValueInt();
      int tessPosition = -1;
      float fallingValue = 0.0F + tickDelta;

      for (int zRange = cameraIntZ - expand; zRange <= cameraIntZ + expand; zRange++) {
         for (int xRange = cameraIntX - expand; xRange <= cameraIntX + expand; xRange++) {
            int coordPos = (zRange - cameraIntZ + 16) * 32 + xRange - cameraIntX + 16;
            if (coordPos >= 0 && coordPos <= 1023) {
               double xCoord = (double)weatherXCoords[coordPos] * 0.5;
               double zCoord = (double)weatherYCoords[coordPos] * 0.5;
               mutable.method_10102((double)xRange, cameraY, (double)zRange);
               int maxHeight = this.height.getValueInt();
               int minIntY = cameraIntY - expand;
               int expandedCameraY = cameraIntY + expand;
               if (minIntY < maxHeight) {
                  minIntY = maxHeight;
               }

               if (expandedCameraY < maxHeight) {
                  expandedCameraY = maxHeight;
               }

               int maxRenderY = Math.max(maxHeight, cameraIntY);
               if (minIntY != expandedCameraY) {
                  Random random = Random.method_43049(
                     (long)xRange * (long)xRange * 3121L + (long)xRange * 45238971L ^ (long)zRange * (long)zRange * 418711L + (long)zRange * 13761L
                  );
                  mutable.method_10103(xRange, minIntY, zRange);
                  Precipitation precipitation = precipitationType.toMC();
                  if (precipitation == Precipitation.field_9382) {
                     if (tessPosition != 0) {
                        if (tessPosition >= 0) {
                           tessellator.method_1350();
                        }

                        tessPosition = 0;
                        RenderSystem.setShaderTexture(0, RAIN);
                        bufferBuilder.method_1328(DrawMode.field_27382, VertexFormats.field_1584);
                     }

                     int randomSeed = 0 + xRange * xRange * 3121 + xRange * 45238971 + zRange * zRange * 418711 + zRange * 13761 & 31;
                     float texTextureV = -((float)randomSeed + tickDelta) / 32.0F * (3.0F + random.method_43057());
                     double xOffset = (double)xRange + 0.5 - cameraX;
                     double yOffset = (double)zRange + 0.5 - cameraZ;
                     float dLength = (float)Math.sqrt(xOffset * xOffset + yOffset * yOffset) / (float)expand;
                     float weatherAlpha = ((1.0F - dLength * dLength) * 0.5F + 0.5F) * f;
                     mutable.method_10103(xRange, maxRenderY, zRange);
                     int lightmapCoord = WorldRenderer.method_23794(mc.field_1687, mutable);
                     bufferBuilder.method_22912(
                           (double)xRange - cameraX - xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ - zCoord + 0.5
                        )
                        .method_22913(0.0F, (float)minIntY * 0.25F + texTextureV)
                        .method_22915(red, green, blue, weatherAlpha)
                        .method_22916(lightmapCoord)
                        .method_1344();
                     bufferBuilder.method_22912(
                           (double)xRange - cameraX + xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ + zCoord + 0.5
                        )
                        .method_22913(1.0F, (float)minIntY * 0.25F + texTextureV)
                        .method_22915(red, green, blue, weatherAlpha)
                        .method_22916(lightmapCoord)
                        .method_1344();
                     bufferBuilder.method_22912((double)xRange - cameraX + xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ + zCoord + 0.5)
                        .method_22913(1.0F, (float)expandedCameraY * 0.25F + texTextureV)
                        .method_22915(red, green, blue, weatherAlpha)
                        .method_22916(lightmapCoord)
                        .method_1344();
                     bufferBuilder.method_22912((double)xRange - cameraX - xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ - zCoord + 0.5)
                        .method_22913(0.0F, (float)expandedCameraY * 0.25F + texTextureV)
                        .method_22915(red, green, blue, weatherAlpha)
                        .method_22916(lightmapCoord)
                        .method_1344();
                  } else if (precipitation == Precipitation.field_9383) {
                     if (tessPosition != 1) {
                        if (tessPosition == 0) {
                           tessellator.method_1350();
                        }

                        tessPosition = 1;
                        RenderSystem.setShaderTexture(0, SNOW);
                        bufferBuilder.method_1328(DrawMode.field_27382, VertexFormats.field_1584);
                     }

                     float snowSmooth = -((float)(0 & 511) + tickDelta) / 512.0F;
                     float texTextureV = (float)(random.method_43058() + (double)fallingValue * 0.01 * (double)((float)random.method_43059()));
                     float fallingSpeed = (float)(random.method_43058() + (double)(fallingValue * (float)random.method_43059()) * 0.001)
                        * (float)this.snowFallingSpeedMultiplier.getValueInt();
                     double xOffset = (double)xRange + 0.5 - cameraX;
                     double yOffset = (double)zRange + 0.5 - cameraZ;
                     float weatherAlpha = (float)Math.sqrt(xOffset * xOffset + yOffset * yOffset) / (float)expand;
                     float snowAlpha = ((1.0F - weatherAlpha * weatherAlpha) * 0.3F + 0.5F) * f;
                     mutable.method_10103(xRange, maxRenderY, zRange);
                     int lightMapCoord = WorldRenderer.method_23794(mc.field_1687, mutable);
                     int lightmapCalcV = lightMapCoord >> 16 & 65535;
                     int lightmapCalcU = lightMapCoord & 65535;
                     int lightmapV = (lightmapCalcV * 3 + 240) / 4;
                     int lightmapU = (lightmapCalcU * 3 + 240) / 4;
                     bufferBuilder.method_22912(
                           (double)xRange - cameraX - xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ - zCoord + 0.5
                        )
                        .method_22913(0.0F + texTextureV, (float)minIntY * 0.25F + snowSmooth + fallingSpeed)
                        .method_22915(red, green, blue, snowAlpha)
                        .method_22921(lightmapU, lightmapV)
                        .method_1344();
                     bufferBuilder.method_22912(
                           (double)xRange - cameraX + xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ + zCoord + 0.5
                        )
                        .method_22913(1.0F + texTextureV, (float)minIntY * 0.25F + snowSmooth + fallingSpeed)
                        .method_22915(red, green, blue, snowAlpha)
                        .method_22921(lightmapU, lightmapV)
                        .method_1344();
                     bufferBuilder.method_22912((double)xRange - cameraX + xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ + zCoord + 0.5)
                        .method_22913(1.0F + texTextureV, (float)expandedCameraY * 0.25F + snowSmooth + fallingSpeed)
                        .method_22915(red, green, blue, snowAlpha)
                        .method_22921(lightmapU, lightmapV)
                        .method_1344();
                     bufferBuilder.method_22912((double)xRange - cameraX - xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ - zCoord + 0.5)
                        .method_22913(0.0F + texTextureV, (float)expandedCameraY * 0.25F + snowSmooth + fallingSpeed)
                        .method_22915(red, green, blue, snowAlpha)
                        .method_22921(lightmapU, lightmapV)
                        .method_1344();
                  }
               }
            }
         }
      }

      if (tessPosition >= 0) {
         tessellator.method_1350();
      }

      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      manager.method_3315();
   }
}
