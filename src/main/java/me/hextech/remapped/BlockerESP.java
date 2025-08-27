package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class BlockerESP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    final List<BlockPos> renderList = new ArrayList<BlockPos>();
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true));
    private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
    private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true));

    public BlockerESP() {
        super("BlockerESP", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        this.renderList.clear();
        float pOffset = (float)CombatSetting_kxXrLvbWbduSuFoeBUsC.getOffset();
        for (Entity entity : CombatUtil.getEnemies(10.0)) {
            BlockPos pos;
            if (this.burrow.getValue()) {
                float[] offset;
                for (float x : offset = new float[]{-pOffset, 0.0f, pOffset}) {
                    for (float z : offset) {
                        BlockPosX tempPos = new BlockPosX(entity.method_19538().method_1031((double)x, 0.0, (double)z));
                        if (this.isObsidian(tempPos)) {
                            this.renderList.add(tempPos);
                        }
                        if (!this.isObsidian(tempPos = new BlockPosX(entity.method_19538().method_1031((double)x, 0.5, (double)z)))) continue;
                        this.renderList.add(tempPos);
                    }
                }
            }
            if (!this.surround.getValue() || !BlockUtil.isHole(pos = EntityUtil.getEntityPos(entity, true))) continue;
            for (Direction i : Direction.values()) {
                if (i == Direction.UP || i == Direction.DOWN || !this.isObsidian(pos.offset(i))) continue;
                this.renderList.add(pos.offset(i));
            }
        }
        for (BlockPos blockPos : this.renderList) {
            Render3DUtil.draw3DBox(matrixStack, new Box(blockPos), this.color.getValue(), this.outline.getValue(), this.box.getValue());
        }
    }

    private boolean isObsidian(BlockPos pos) {
        return (BlockUtil.getBlock(pos) == Blocks.field_10540 || BlockUtil.getBlock(pos) == Blocks.field_10443) && !this.renderList.contains(pos);
    }
}
