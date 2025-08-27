package me.hextech.remapped;

import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.ExplosionUtil;
import me.hextech.remapped.ListenerHelper;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ListenerHelperUtil
implements Wrapper {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean canTouch(BlockPos pos) {
        Direction side = BlockUtil.getClickSideStrict(pos);
        if (ListenerHelperUtil.mc.player == null) return false;
        if (side == null) return false;
        Vec3d vec3d = new Vec3d((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5);
        if (!(pos.toCenterPos().method_1019(vec3d).method_1022(ListenerHelperUtil.mc.player.getEyePos()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())) return false;
        return true;
    }

    public static float calculateBase(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
        return ListenerHelperUtil.calculateObsidian(pos.method_10074(), new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5), player, predict);
    }

    public static float calculateObsidian(BlockPos obs, Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        CombatUtil.modifyPos = obs;
        CombatUtil.modifyBlockState = Blocks.field_10540.method_9564();
        float damage = ExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), (Entity)player, (Entity)predict, 6.0f);
        CombatUtil.modifyPos = null;
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    public static boolean behindWall(BlockPos pos) {
        Vec3d testVec = new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
        BlockHitResult result = null;
        if (ListenerHelperUtil.mc.world != null && ListenerHelperUtil.mc.player != null) {
            result = ListenerHelperUtil.mc.world.raycast(new RaycastContext(EntityUtil.getEyesPos(), testVec, RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)ListenerHelperUtil.mc.player));
        }
        if (result == null || result.method_17783() == HitResult.Type.field_1333) {
            return false;
        }
        return ListenerHelperUtil.mc.player.getEyePos().method_1022(pos.toCenterPos().method_1031(0.0, -0.5, 0.0)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue();
    }

    public static boolean canSee(Vec3d from, Vec3d to) {
        BlockHitResult result = null;
        if (ListenerHelperUtil.mc.world != null && ListenerHelperUtil.mc.player != null) {
            result = ListenerHelperUtil.mc.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)ListenerHelperUtil.mc.player));
        }
        return result == null || result.method_17783() == HitResult.Type.field_1333;
    }

    public static boolean liteCheck(Vec3d from, Vec3d to) {
        return !ListenerHelperUtil.canSee(from, to) && !ListenerHelperUtil.canSee(from, to.method_1031(0.0, 1.8, 0.0));
    }

    public static boolean canBasePlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        BlockPos obsPos = pos.method_10074();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        return BlockUtil.canPlace(obsPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue()) && BlockUtil.getClickSideStrict(obsPos) != null && ListenerHelperUtil.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) && ListenerHelperUtil.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) && (BlockUtil.isAir(boost) || BlockUtil.hasCrystal(boost) && ListenerHelper.getBlock(boost) == Blocks.field_10036) && (!CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() || BlockUtil.isAir(boost2)) && !BlockUtil.isAir(obsPos);
    }

    public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        BlockPos obsPos = pos.method_10074();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        return !(ListenerHelper.getBlock(obsPos) != Blocks.field_9987 && ListenerHelper.getBlock(obsPos) != Blocks.field_10540 && !BlockUtil.canPlace(obsPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue()) || BlockUtil.getClickSideStrict(obsPos) == null || !ListenerHelperUtil.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) || !ListenerHelperUtil.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) || !BlockUtil.isAir(boost) && (!BlockUtil.hasCrystal(boost) || ListenerHelper.getBlock(boost) != Blocks.field_10036) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() && !BlockUtil.isAir(boost2));
    }

    private static boolean noEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        if (ListenerHelperUtil.mc.world != null) {
            for (Entity entity : ListenerHelperUtil.mc.world.method_18467(Entity.class, new Box(pos))) {
                if (!entity.method_5805() || ignoreItem && entity instanceof ItemEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
                if (entity instanceof EndCrystalEntity) {
                    if (!ignoreCrystal) {
                        return false;
                    }
                    if (ListenerHelperUtil.mc.player != null && (ListenerHelperUtil.mc.player.method_6057(entity) || ListenerHelperUtil.mc.player.getEyePos().method_1022(entity.method_19538()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())) continue;
                }
                return false;
            }
        }
        return true;
    }
}
