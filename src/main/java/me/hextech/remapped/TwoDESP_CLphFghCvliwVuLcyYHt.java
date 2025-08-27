package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import me.hextech.HexTech;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.FontRenderers;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.TextUtil;
import me.hextech.remapped.TwoDESP;
import me.hextech.remapped.TwoDESP_pAuUDPcZwSGmXQFRqNNy;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;

public class TwoDESP_CLphFghCvliwVuLcyYHt
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final EnumSetting page = this.add(new EnumSetting<TwoDESP>("Settings", TwoDESP.Target));
    public final ColorSetting armorDuraColor = this.add(new ColorSetting("Armor Dura Color", new Color(0x2FFF00), v -> this.page.getValue() == TwoDESP.Color));
    public final ColorSetting hHealth = this.add(new ColorSetting("High Health Color", new Color(0, 255, 0, 255), v -> this.page.getValue() == TwoDESP.Color));
    public final ColorSetting mHealth = this.add(new ColorSetting("Mid Health Color", new Color(255, 255, 0, 255), v -> this.page.getValue() == TwoDESP.Color));
    public final ColorSetting lHealth = this.add(new ColorSetting("Low Health Color", new Color(255, 0, 0, 255), v -> this.page.getValue() == TwoDESP.Color));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, v -> this.page.getValue() == TwoDESP.Setting));
    private final BooleanSetting renderHealth = this.add(new BooleanSetting("renderHealth", true, v -> this.page.getValue() == TwoDESP.Setting));
    private final BooleanSetting renderArmor = this.add(new BooleanSetting("Armor Dura", true, v -> this.page.getValue() == TwoDESP.Setting));
    private final SliderSetting durascale = this.add(new SliderSetting("DuraScale", 1.0, 0.0, 2.0, 0.1, v -> this.renderArmor.getValue()));
    private final BooleanSetting drawItem = this.add(new BooleanSetting("draw Item Name", true, v -> this.page.getValue() == TwoDESP.Setting));
    private final BooleanSetting drawItemC = this.add(new BooleanSetting("draw Item Count", true, v -> this.page.getValue() == TwoDESP.Setting && this.drawItem.getValue()));
    public final ColorSetting countColor = this.add(new ColorSetting("Item Count Color", new Color(255, 255, 0, 255), v -> this.page.getValue() == TwoDESP.Color && this.drawItemC.getValue()));
    public final ColorSetting textcolor = this.add(new ColorSetting("Item Name Color", new Color(255, 255, 255, 255), v -> this.page.getValue() == TwoDESP.Color && this.drawItem.getValue()));
    private final BooleanSetting font = this.add(new BooleanSetting("CustomFont", true, v -> this.page.getValue() == TwoDESP.Setting));
    private final BooleanSetting players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == TwoDESP.Target));
    private final BooleanSetting friends = this.add(new BooleanSetting("Friends", true, v -> this.page.getValue() == TwoDESP.Target));
    private final BooleanSetting crystals = this.add(new BooleanSetting("Crystals", true, v -> this.page.getValue() == TwoDESP.Target));
    private final BooleanSetting creatures = this.add(new BooleanSetting("Creatures", false, v -> this.page.getValue() == TwoDESP.Target));
    private final BooleanSetting monsters = this.add(new BooleanSetting("Monsters", false, v -> this.page.getValue() == TwoDESP.Target));
    private final BooleanSetting ambients = this.add(new BooleanSetting("Ambients", false, v -> this.page.getValue() == TwoDESP.Target));
    private final BooleanSetting others = this.add(new BooleanSetting("Others", false, v -> this.page.getValue() == TwoDESP.Target));
    private final ColorSetting playersC = this.add(new ColorSetting("PlayersBox", new Color(16749056), v -> this.page.getValue() == TwoDESP.Color));
    private final ColorSetting friendsC = this.add(new ColorSetting("FriendsBox", new Color(0x30FF00), v -> this.page.getValue() == TwoDESP.Color));
    private final ColorSetting crystalsC = this.add(new ColorSetting("CrystalsBox", new Color(48127), v -> this.page.getValue() == TwoDESP.Color));
    private final ColorSetting creaturesC = this.add(new ColorSetting("CreaturesBox", new Color(10527910), v -> this.page.getValue() == TwoDESP.Color));
    private final ColorSetting monstersC = this.add(new ColorSetting("MonstersBox", new Color(0xFF0000), v -> this.page.getValue() == TwoDESP.Color));
    private final ColorSetting ambientsC = this.add(new ColorSetting("AmbientsBox", new Color(8061183), v -> this.page.getValue() == TwoDESP.Color));
    private final ColorSetting othersC = this.add(new ColorSetting("OthersBox", new Color(16711778), v -> this.page.getValue() == TwoDESP.Color));

    public TwoDESP_CLphFghCvliwVuLcyYHt() {
        super("2DESP", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    public static float getRotations(Vec2f vec) {
        if (TwoDESP_CLphFghCvliwVuLcyYHt.mc.player == null) {
            return 0.0f;
        }
        double x = (double)vec.field_1343 - TwoDESP_CLphFghCvliwVuLcyYHt.mc.player.method_19538().field_1352;
        double z = (double)vec.field_1342 - TwoDESP_CLphFghCvliwVuLcyYHt.mc.player.method_19538().field_1350;
        return (float)(-(Math.atan2(x, z) * 57.29577951308232));
    }

    @Override
    public void onRender2D(DrawContext context, float tickDelta) {
        Matrix4f matrix = context.method_51448().method_23760().method_23761();
        BufferBuilder bufferBuilder = Tessellator.method_1348().method_1349();
        Render2DUtil.setupRender();
        RenderSystem.setShader(GameRenderer::method_34540);
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1576);
        for (Entity ent : TwoDESP_CLphFghCvliwVuLcyYHt.mc.world.method_18112()) {
            if (!this.shouldRender(ent)) continue;
            this.drawBox(bufferBuilder, ent, matrix, context);
        }
        BufferRenderer.method_43433((BufferBuilder.BuiltBuffer)bufferBuilder.method_1326());
        Render2DUtil.endRender();
        for (Entity ent : TwoDESP_CLphFghCvliwVuLcyYHt.mc.world.method_18112()) {
            if (!this.shouldRender(ent)) continue;
            this.drawText(ent, context);
        }
    }

    public boolean shouldRender(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (TwoDESP_CLphFghCvliwVuLcyYHt.mc.player == null) {
            return false;
        }
        if (entity instanceof PlayerEntity) {
            if (entity == TwoDESP_CLphFghCvliwVuLcyYHt.mc.player && TwoDESP_CLphFghCvliwVuLcyYHt.mc.field_1690.method_31044().method_31034()) {
                return false;
            }
            if (HexTech.FRIEND.isFriend((PlayerEntity)entity)) {
                return this.friends.getValue();
            }
            return this.players.getValue();
        }
        if (entity instanceof EndCrystalEntity) {
            return this.crystals.getValue();
        }
        return switch (TwoDESP_pAuUDPcZwSGmXQFRqNNy.$SwitchMap$net$minecraft$entity$SpawnGroup[entity.method_5864().method_5891().ordinal()]) {
            case 1, 2 -> this.creatures.getValue();
            case 3 -> this.monsters.getValue();
            case 4, 5 -> this.ambients.getValue();
            default -> this.others.getValue();
        };
    }

    public Color getEntityColor(Entity entity) {
        if (entity == null) {
            return new Color(-1);
        }
        if (entity instanceof PlayerEntity) {
            if (HexTech.FRIEND.isFriend((PlayerEntity)entity)) {
                return this.friendsC.getValue();
            }
            return this.playersC.getValue();
        }
        if (entity instanceof EndCrystalEntity) {
            return this.crystalsC.getValue();
        }
        return switch (TwoDESP_pAuUDPcZwSGmXQFRqNNy.$SwitchMap$net$minecraft$entity$SpawnGroup[entity.method_5864().method_5891().ordinal()]) {
            case 1, 2 -> this.creaturesC.getValue();
            case 3 -> this.monstersC.getValue();
            case 4, 5 -> this.ambientsC.getValue();
            default -> this.othersC.getValue();
        };
    }

    public void drawBox(BufferBuilder bufferBuilder, @NotNull Entity ent, Matrix4f matrix, DrawContext context) {
        double x = ent.field_6014 + (ent.getX() - ent.field_6014) * (double)mc.getTickDelta();
        double y = ent.field_6036 + (ent.getY() - ent.field_6036) * (double)mc.getTickDelta();
        double z = ent.field_5969 + (ent.getZ() - ent.field_5969) * (double)mc.getTickDelta();
        Box axisAlignedBB2 = ent.method_5829();
        Box axisAlignedBB = new Box(axisAlignedBB2.field_1323 - ent.getX() + x - 0.05, axisAlignedBB2.field_1322 - ent.getY() + y, axisAlignedBB2.field_1321 - ent.getZ() + z - 0.05, axisAlignedBB2.field_1320 - ent.getX() + x + 0.05, axisAlignedBB2.field_1325 - ent.getY() + y + 0.15, axisAlignedBB2.field_1324 - ent.getZ() + z + 0.05);
        Vec3d[] vectors = new Vec3d[]{new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
        Color col = this.getEntityColor(ent);
        Vector4d position = null;
        for (Vec3d vector : vectors) {
            vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.field_1352, vector.field_1351, vector.field_1350));
            if (!(vector.field_1350 > 0.0) || !(vector.field_1350 < 1.0)) continue;
            if (position == null) {
                position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
            }
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
        }
        if (position != null) {
            LivingEntity lent;
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            if (this.outline.getValue()) {
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 1.0), (float)posY, (float)(posX + 0.5), (float)(endPosY + 0.5), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 1.0), (float)(posY - 0.5), (float)(endPosX + 0.5), (float)(posY + 0.5 + 0.5), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(endPosX - 0.5 - 0.5), (float)posY, (float)(endPosX + 0.5), (float)(endPosY + 0.5), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 1.0), (float)(endPosY - 0.5 - 0.5), (float)(endPosX + 0.5), (float)(endPosY + 0.5), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 0.5), (float)posY, (float)(posX + 0.5 - 0.5), (float)endPosY, col, col, col, col);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)posX, (float)(endPosY - 0.5), (float)endPosX, (float)endPosY, col, col, col, col);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 0.5), (float)posY, (float)endPosX, (float)(posY + 0.5), col, col, col, col);
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(endPosX - 0.5), (float)posY, (float)endPosX, (float)endPosY, col, col, col, col);
            }
            if (ent instanceof LivingEntity && (lent = (LivingEntity)ent).method_6032() != 0.0f && this.renderHealth.getValue()) {
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 4.0), (float)posY, (float)posX - 3.0f, (float)endPosY, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                Color color = this.getcolor(lent.method_6032());
                Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(posX - 4.0), (float)(endPosY + (posY - endPosY) * (double)lent.method_6032() / (double)lent.method_6063()), (float)posX - 3.0f, (float)endPosY, color, color, color, color);
            }
            if (ent instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)ent;
                if (this.renderArmor.getValue()) {
                    double height = (endPosY - posY) / 4.0;
                    ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                    stacks.add((ItemStack)player.method_31548().field_7548.get(3));
                    stacks.add((ItemStack)player.method_31548().field_7548.get(2));
                    stacks.add((ItemStack)player.method_31548().field_7548.get(1));
                    stacks.add((ItemStack)player.method_31548().field_7548.get(0));
                    int i = -1;
                    for (ItemStack armor : stacks) {
                        ++i;
                        if (armor.method_7960()) continue;
                        float durability = armor.method_7936() - armor.method_7919();
                        int percent = (int)(durability / (float)armor.method_7936() * 100.0f);
                        double finalH = height * (double)(percent / 100);
                        Render2DUtil.setRectPoints(bufferBuilder, matrix, (float)(endPosX + 1.5), (float)((double)((float)posY) + height * (double)i + 1.2 * (double)(i + 1)), (float)endPosX + 3.0f, (int)(posY + height * (double)i + 1.2 * (double)(i + 1) + finalH), this.armorDuraColor.getValue(), this.armorDuraColor.getValue(), this.armorDuraColor.getValue(), this.armorDuraColor.getValue());
                    }
                }
            }
        }
    }

    public void drawText(Entity ent, DrawContext context) {
        double x = ent.field_6014 + (ent.getX() - ent.field_6014) * (double)mc.getTickDelta();
        double y = ent.field_6036 + (ent.getY() - ent.field_6036) * (double)mc.getTickDelta();
        double z = ent.field_5969 + (ent.getZ() - ent.field_5969) * (double)mc.getTickDelta();
        Box axisAlignedBB2 = ent.method_5829();
        Box axisAlignedBB = new Box(axisAlignedBB2.field_1323 - ent.getX() + x - 0.05, axisAlignedBB2.field_1322 - ent.getY() + y, axisAlignedBB2.field_1321 - ent.getZ() + z - 0.05, axisAlignedBB2.field_1320 - ent.getX() + x + 0.05, axisAlignedBB2.field_1325 - ent.getY() + y + 0.15, axisAlignedBB2.field_1324 - ent.getZ() + z + 0.05);
        Vec3d[] vectors = new Vec3d[]{new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new Vec3d(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new Vec3d(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
        Color col = this.getEntityColor(ent);
        Vector4d position = null;
        for (Vec3d vector : vectors) {
            vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.field_1352, vector.field_1351, vector.field_1350));
            if (!(vector.field_1350 > 0.0) || !(vector.field_1350 < 1.0)) continue;
            if (position == null) {
                position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
            }
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
        }
        if (position != null) {
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            if (ent instanceof ItemEntity) {
                ItemEntity entity = (ItemEntity)ent;
                if (this.drawItem.getValue()) {
                    float diff = (float)((endPosX - posX) / 2.0);
                    float textWidth = FontRenderers.Arial.getWidth(entity.method_5476().getString()) * 1.0f;
                    float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0f)) * 1.0);
                    int count = entity.method_6983().method_7947();
                    context.method_51433(TwoDESP_CLphFghCvliwVuLcyYHt.mc.field_1772, entity.method_5476().getString(), (int)tagX, (int)(posY - 10.0), this.textcolor.getValue().getRGB(), false);
                    if (this.drawItemC.getValue()) {
                        context.method_51433(TwoDESP_CLphFghCvliwVuLcyYHt.mc.field_1772, "x" + count, (int)(tagX + (float)TwoDESP_CLphFghCvliwVuLcyYHt.mc.field_1772.method_1727(entity.method_5476().getString() + " ")), (int)posY - 10, this.countColor.getValue().getRGB(), false);
                    }
                }
            }
            if (ent instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)ent;
                if (this.renderArmor.getValue()) {
                    double height = (endPosY - posY) / 4.0;
                    ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                    stacks.add((ItemStack)player.method_31548().field_7548.get(3));
                    stacks.add((ItemStack)player.method_31548().field_7548.get(2));
                    stacks.add((ItemStack)player.method_31548().field_7548.get(1));
                    stacks.add((ItemStack)player.method_31548().field_7548.get(0));
                    int i = -1;
                    for (ItemStack armor : stacks) {
                        ++i;
                        if (armor.method_7960()) continue;
                        float durability = armor.method_7936() - armor.method_7919();
                        int percent = (int)(durability / (float)armor.method_7936() * 100.0f);
                        double finalH = height * (double)(percent / 100);
                        context.method_51427(armor, (int)(endPosX + 4.0), (int)(posY + height * (double)i + 1.2 * (double)(i + 1) + finalH / 2.0));
                    }
                }
            }
        }
    }

    public Color getcolor(float health) {
        if (health >= 20.0f) {
            return this.hHealth.getValue();
        }
        if (20.0f > health && health > 10.0f) {
            return this.mHealth.getValue();
        }
        return this.lHealth.getValue();
    }
}
