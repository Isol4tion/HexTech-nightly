package me.hextech.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.api.utils.math.MathUtil;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.api.utils.render.TextUtil;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.asm.accessors.IEntity;
import me.hextech.mod.gui.font.FontRenderers;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4d;

import java.awt.*;
import java.util.ArrayList;

public class ESP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final EnumSetting mode = this.add(new EnumSetting<_iJUSlEiinRCZKkYYmFmY>("Type", _iJUSlEiinRCZKkYYmFmY.text));
    private final ColorSetting item = this.add(new ColorSetting("Item", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting boxcolor = this.add(new ColorSetting("Box Color", new Color(255, 255, 255, 100)));
    private final ColorSetting textcolor = this.add(new ColorSetting("Text Color", new Color(255, 255, 255, 255), v -> this.mode.getValue() == _iJUSlEiinRCZKkYYmFmY.text));
    private final ColorSetting player = this.add(new ColorSetting("Player", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting chest = this.add(new ColorSetting("Chest", new Color(255, 255, 255, 100)).injectBoolean(false));

    public ESP() {
        super("ESP", Category.Render);
    }

    private static Vec3d[] getPoints(Entity ent) {
        double x = ent.prevX + (ent.getX() - ent.prevX) * (double)mc.getTickDelta();
        double y = ent.prevY + (ent.getY() - ent.prevY) * (double)mc.getTickDelta();
        double z = ent.prevZ + (ent.getZ() - ent.prevZ) * (double)mc.getTickDelta();
        Box axisAlignedBB2 = ent.getBoundingBox();
        Box axisAlignedBB = new Box(axisAlignedBB2.minX - ent.getX() + x - 0.05, axisAlignedBB2.minY - ent.getY() + y, axisAlignedBB2.minZ - ent.getZ() + z - 0.05, axisAlignedBB2.maxX - ent.getX() + x + 0.05, axisAlignedBB2.maxY - ent.getY() + y + 0.15, axisAlignedBB2.maxZ - ent.getZ() + z + 0.05);
        Vec3d[] vectors = new Vec3d[]{new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
        return vectors;
    }

    public static void endRender() {
        RenderSystem.disableBlend();
    }

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void setTrianglePoints(BufferBuilder bufferBuilder, Matrix4f matrix, float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
        bufferBuilder.vertex(matrix, x1, y1, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(matrix, x3, y3, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(matrix, x3, y3, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(matrix, x1, y1, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
    }

    @Override
    public void onRender2D(DrawContext context, float tickDelta) {
        for (Entity entity : ESP.mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity)) continue;
            if (this.mode.getValue() == _iJUSlEiinRCZKkYYmFmY.text) {
                Vec3d[] vectors = ESP.getPoints(entity);
                Vector4d position = null;
                for (Vec3d vector : vectors) {
                    vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
                    if (!(vector.z > 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                if (position != null) {
                    float posX = (float)position.x;
                    float posY = (float)position.y;
                    float endPosX = (float)position.z;
                    float diff = (endPosX - posX) / 2.0f;
                    float textWidth = FontRenderers.Arial.getStringWidth(entity.getDisplayName().getString());
                    float tagX = (posX + diff - textWidth / 2.0f);
                    FontRenderers.Arial.drawString(context.getMatrices(), entity.getDisplayName().getString(), tagX, posY - 10.0f, this.textcolor.getValue().getRGB());
                }
            }
            Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            ESP.setupRender();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
            for (Entity ent : ESP.mc.world.getEntities()) {
                if (!(ent instanceof ItemEntity)) continue;
                Vec3d[] vectors = ESP.getPoints(ent);
                Vector4d position = null;
                for (Vec3d vector : vectors) {
                    vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
                    if (!(vector.z > 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                if (position == null) continue;
                float posX = (float)position.x;
                float posY = (float)position.y;
                float endPosX = (float)position.z;
                float endPosY = (float)position.w;
                this.drawEquilateralTriangle(bufferBuilder, matrix, posX, posY, endPosX, endPosY, this.boxcolor.getValue());
            }
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            ESP.endRender();
        }
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    private void drawEquilateralTriangle(BufferBuilder bufferBuilder, Matrix4f stack, float posX, float posY, float endX, float endY, Color color) {
        float sideLength = Math.abs(endX - posX);
        float height = (float)((double)sideLength * Math.sqrt(3.0) / 2.0);
        float halfSide = sideLength / 2.0f;
        ESP.setTrianglePoints(bufferBuilder, stack, posX, endY, endX, endY, posX + halfSide, endY - height, color);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.item.booleanValue || this.player.booleanValue) {
            for (Entity entity : ESP.mc.world.getEntities()) {
                Color color;
                if (entity instanceof ItemEntity && this.item.booleanValue) {
                    color = this.item.getValue();
                    Render3DUtil.draw3DBox(matrixStack, ((IEntity)entity).getDimensions().getBoxAt(new Vec3d(MathUtil.interpolate(entity.lastRenderX, entity.getX(), partialTicks), MathUtil.interpolate(entity.lastRenderY, entity.getY(), partialTicks), MathUtil.interpolate(entity.lastRenderZ, entity.getZ(), partialTicks))), color, false, true);
                    continue;
                }
                if (!(entity instanceof PlayerEntity) || !this.player.booleanValue) continue;
                color = this.player.getValue();
                Render3DUtil.draw3DBox(matrixStack, ((IEntity)entity).getDimensions().getBoxAt(new Vec3d(MathUtil.interpolate(entity.lastRenderX, entity.getX(), partialTicks), MathUtil.interpolate(entity.lastRenderY, entity.getY(), partialTicks), MathUtil.interpolate(entity.lastRenderZ, entity.getZ(), partialTicks))).expand(0.0, 0.1, 0.0), color, false, true);
            }
        }
        if (this.chest.booleanValue) {
            ArrayList<BlockEntity> blockEntities = BlockUtil.getTileEntities();
            for (BlockEntity blockEntity : blockEntities) {
                if (!(blockEntity instanceof ChestBlockEntity)) continue;
                Box box = new Box(blockEntity.getPos());
                Render3DUtil.draw3DBox(matrixStack, box, this.chest.getValue());
            }
        }
    }

    public enum _iJUSlEiinRCZKkYYmFmY {
        None,
        text

    }
}
