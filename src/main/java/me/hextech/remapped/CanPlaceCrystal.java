package me.hextech.remapped;

import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class CanPlaceCrystal
implements Wrapper {
    public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        if (CanPlaceCrystal.mc.world == null || CanPlaceCrystal.mc.player == null) {
            return false;
        }
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        return !(BlockUtil.getBlock(obsPos) != Blocks.BEDROCK && BlockUtil.getBlock(obsPos) != Blocks.OBSIDIAN || BlockUtil.getClickSideStrict(obsPos) == null || !CanPlaceCrystal.hasEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) || !CanPlaceCrystal.hasEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) || !BlockUtil.isAir(boost) && (!BlockUtil.hasCrystal(boost) || BlockUtil.getBlock(boost) != Blocks.FIRE) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() && !BlockUtil.isAir(boost2));
    }

    private static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        if (CanPlaceCrystal.mc.world != null) {
            for (Entity entity : CanPlaceCrystal.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
                if (!entity.isAlive() || ignoreItem && entity instanceof ItemEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
                if (entity instanceof EndCrystalEntity) {
                    if (!ignoreCrystal) {
                        return false;
                    }
                    if (CanPlaceCrystal.mc.player != null && (CanPlaceCrystal.mc.player.canSee(entity) || CanPlaceCrystal.mc.player.getEyePos().distanceTo(entity.getPos()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue())) continue;
                }
                return false;
            }
        }
        return true;
    }
}
