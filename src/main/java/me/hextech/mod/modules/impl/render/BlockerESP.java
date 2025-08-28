package me.hextech.mod.modules.impl.render;

import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlockerESP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    final List<BlockPos> renderList = new ArrayList<BlockPos>();
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true));
    private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
    private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true));

    public BlockerESP() {
        super("BlockerESP", Category.Render);
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
                        BlockPosX tempPos = new BlockPosX(entity.getPos().add(x, 0.0, z));
                        if (this.isObsidian(tempPos)) {
                            this.renderList.add(tempPos);
                        }
                        if (!this.isObsidian(tempPos = new BlockPosX(entity.getPos().add(x, 0.5, z)))) continue;
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
        return (BlockUtil.getBlock(pos) == Blocks.OBSIDIAN || BlockUtil.getBlock(pos) == Blocks.ENDER_CHEST) && !this.renderList.contains(pos);
    }
}
