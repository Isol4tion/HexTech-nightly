package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Objects;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Weather;
import me.hextech.remapped.WeatherRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;

public class Weather_BfaBZRqvqRbKrhUkvqny
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final Identifier RAIN;
    private static final Identifier SNOW;
    private static final float[] weatherXCoords;
    private static final float[] weatherYCoords;
    private final EnumSetting precipitationSetting = this.add(new EnumSetting<Weather>("Type", Weather.Rain));
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
        if (((Enum)this.precipitationSetting.getValue()).equals((Object)Weather.Both)) {
            this.render(event, Weather.Rain);
            this.render(event, Weather.Snow);
            event.cancel();
            return;
        }
        event.cancel();
    }

    private void render(WeatherRenderEvent event, Weather precipitationType) {
        LightmapTextureManager manager = event.lightmapTextureManager;
        double cameraX = event.cameraX;
        double cameraY = event.cameraY;
        double cameraZ = event.cameraZ;
        float tickDelta = event.tickDelta;
        float f = this.strength.getValueFloat();
        float red = (float)this.weatherColor.getValue().getRed() / 255.0f;
        float blue = (float)this.weatherColor.getValue().getBlue() / 255.0f;
        float green = (float)this.weatherColor.getValue().getGreen() / 255.0f;
        manager.method_3316();
        int cameraIntX = MathHelper.method_15357((double)cameraX);
        int cameraIntY = MathHelper.method_15357((double)cameraY);
        int cameraIntZ = MathHelper.method_15357((double)cameraZ);
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)MinecraftClient.method_29611());
        RenderSystem.setShader(GameRenderer::method_34546);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int expand = this.expandSize.getValueInt();
        int tessPosition = -1;
        Objects.requireNonNull(this);
        float fallingValue = 0.0f + tickDelta;
        for (int zRange = cameraIntZ - expand; zRange <= cameraIntZ + expand; ++zRange) {
            for (int xRange = cameraIntX - expand; xRange <= cameraIntX + expand; ++xRange) {
                float weatherAlpha;
                float texTextureV;
                int coordPos = (zRange - cameraIntZ + 16) * 32 + xRange - cameraIntX + 16;
                if (coordPos < 0 || coordPos > 1023) continue;
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
                if (minIntY == expandedCameraY) continue;
                Random random = Random.method_43049((long)((long)xRange * (long)xRange * 3121L + (long)xRange * 45238971L ^ (long)zRange * (long)zRange * 418711L + (long)zRange * 13761L));
                mutable.method_10103(xRange, minIntY, zRange);
                Biome.Precipitation precipitation = precipitationType.toMC();
                if (precipitation == Biome.Precipitation.field_9382) {
                    if (tessPosition != 0) {
                        if (tessPosition >= 0) {
                            tessellator.method_1350();
                        }
                        tessPosition = 0;
                        RenderSystem.setShaderTexture((int)0, (Identifier)RAIN);
                        bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1584);
                    }
                    int randomSeed = this.ticks + xRange * xRange * 3121 + xRange * 45238971 + zRange * zRange * 418711 + zRange * 13761 & 0x1F;
                    texTextureV = -((float)randomSeed + tickDelta) / 32.0f * (3.0f + random.method_43057());
                    double xOffset = (double)xRange + 0.5 - cameraX;
                    double yOffset = (double)zRange + 0.5 - cameraZ;
                    float dLength = (float)Math.sqrt(xOffset * xOffset + yOffset * yOffset) / (float)expand;
                    weatherAlpha = ((1.0f - dLength * dLength) * 0.5f + 0.5f) * f;
                    mutable.method_10103(xRange, maxRenderY, zRange);
                    int lightmapCoord = WorldRenderer.method_23794((BlockRenderView)Weather_BfaBZRqvqRbKrhUkvqny.mc.world, (BlockPos)mutable);
                    bufferBuilder.method_22912((double)xRange - cameraX - xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ - zCoord + 0.5).method_22913(0.0f, (float)minIntY * 0.25f + texTextureV).method_22915(red, green, blue, weatherAlpha).method_22916(lightmapCoord).method_1344();
                    bufferBuilder.method_22912((double)xRange - cameraX + xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ + zCoord + 0.5).method_22913(1.0f, (float)minIntY * 0.25f + texTextureV).method_22915(red, green, blue, weatherAlpha).method_22916(lightmapCoord).method_1344();
                    bufferBuilder.method_22912((double)xRange - cameraX + xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ + zCoord + 0.5).method_22913(1.0f, (float)expandedCameraY * 0.25f + texTextureV).method_22915(red, green, blue, weatherAlpha).method_22916(lightmapCoord).method_1344();
                    bufferBuilder.method_22912((double)xRange - cameraX - xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ - zCoord + 0.5).method_22913(0.0f, (float)expandedCameraY * 0.25f + texTextureV).method_22915(red, green, blue, weatherAlpha).method_22916(lightmapCoord).method_1344();
                    continue;
                }
                if (precipitation != Biome.Precipitation.field_9383) continue;
                if (tessPosition != 1) {
                    if (tessPosition == 0) {
                        tessellator.method_1350();
                    }
                    tessPosition = 1;
                    RenderSystem.setShaderTexture((int)0, (Identifier)SNOW);
                    bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1584);
                }
                float snowSmooth = -((float)(this.ticks & 0x1FF) + tickDelta) / 512.0f;
                texTextureV = (float)(random.method_43058() + (double)fallingValue * 0.01 * (double)((float)random.method_43059()));
                float fallingSpeed = (float)(random.method_43058() + (double)(fallingValue * (float)random.method_43059()) * 0.001) * (float)this.snowFallingSpeedMultiplier.getValueInt();
                double xOffset = (double)xRange + 0.5 - cameraX;
                double yOffset = (double)zRange + 0.5 - cameraZ;
                weatherAlpha = (float)Math.sqrt(xOffset * xOffset + yOffset * yOffset) / (float)expand;
                float snowAlpha = ((1.0f - weatherAlpha * weatherAlpha) * 0.3f + 0.5f) * f;
                mutable.method_10103(xRange, maxRenderY, zRange);
                int lightMapCoord = WorldRenderer.method_23794((BlockRenderView)Weather_BfaBZRqvqRbKrhUkvqny.mc.world, (BlockPos)mutable);
                int lightmapCalcV = lightMapCoord >> 16 & 0xFFFF;
                int lightmapCalcU = lightMapCoord & 0xFFFF;
                int lightmapV = (lightmapCalcV * 3 + 240) / 4;
                int lightmapU = (lightmapCalcU * 3 + 240) / 4;
                bufferBuilder.method_22912((double)xRange - cameraX - xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ - zCoord + 0.5).method_22913(0.0f + texTextureV, (float)minIntY * 0.25f + snowSmooth + fallingSpeed).method_22915(red, green, blue, snowAlpha).method_22921(lightmapU, lightmapV).method_1344();
                bufferBuilder.method_22912((double)xRange - cameraX + xCoord + 0.5, (double)expandedCameraY - cameraY, (double)zRange - cameraZ + zCoord + 0.5).method_22913(1.0f + texTextureV, (float)minIntY * 0.25f + snowSmooth + fallingSpeed).method_22915(red, green, blue, snowAlpha).method_22921(lightmapU, lightmapV).method_1344();
                bufferBuilder.method_22912((double)xRange - cameraX + xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ + zCoord + 0.5).method_22913(1.0f + texTextureV, (float)expandedCameraY * 0.25f + snowSmooth + fallingSpeed).method_22915(red, green, blue, snowAlpha).method_22921(lightmapU, lightmapV).method_1344();
                bufferBuilder.method_22912((double)xRange - cameraX - xCoord + 0.5, (double)minIntY - cameraY, (double)zRange - cameraZ - zCoord + 0.5).method_22913(0.0f + texTextureV, (float)expandedCameraY * 0.25f + snowSmooth + fallingSpeed).method_22915(red, green, blue, snowAlpha).method_22921(lightmapU, lightmapV).method_1344();
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
