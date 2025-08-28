package me.hextech.remapped;

import me.hextech.remapped.api.utils.Wrapper;
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
        Vec3d vec3d = new Vec3d((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5);
        return pos.toCenterPos().add(vec3d).distanceTo(ListenerHelperUtil.mc.player.getEyePos()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue();
    }

    public static float calculateBase(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
        return ListenerHelperUtil.calculateObsidian(pos.down(), new Vec3d((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5), player, predict);
    }

    public static float calculateObsidian(BlockPos obs, Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        CombatUtil.modifyPos = obs;
        CombatUtil.modifyBlockState = Blocks.OBSIDIAN.getDefaultState();
        float damage = ExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), player, predict, 6.0f);
        CombatUtil.modifyPos = null;
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    public static boolean behindWall(BlockPos pos) {
        Vec3d testVec = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.7, (double)pos.getZ() + 0.5);
        BlockHitResult result = null;
        if (ListenerHelperUtil.mc.world != null && ListenerHelperUtil.mc.player != null) {
            result = ListenerHelperUtil.mc.world.raycast(new RaycastContext(EntityUtil.getEyesPos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, ListenerHelperUtil.mc.player));
        }
        if (result == null || result.getType() == HitResult.Type.MISS) {
            return false;
        }
        return ListenerHelperUtil.mc.player.getEyePos().distanceTo(pos.toCenterPos().add(0.0, -0.5, 0.0)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue();
    }

    public static boolean canSee(Vec3d from, Vec3d to) {
        BlockHitResult result = null;
        if (ListenerHelperUtil.mc.world != null && ListenerHelperUtil.mc.player != null) {
            result = ListenerHelperUtil.mc.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, ListenerHelperUtil.mc.player));
        }
        return result == null || result.getType() == HitResult.Type.MISS;
    }

    public static boolean liteCheck(Vec3d from, Vec3d to) {
        return !ListenerHelperUtil.canSee(from, to) && !ListenerHelperUtil.canSee(from, to.add(0.0, 1.8, 0.0));
    }

    public static boolean canBasePlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        return BlockUtil.canPlace(obsPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue()) && BlockUtil.getClickSideStrict(obsPos) != null && ListenerHelperUtil.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) && ListenerHelperUtil.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) && (BlockUtil.isAir(boost) || BlockUtil.hasCrystal(boost) && ListenerHelper.getBlock(boost) == Blocks.FIRE) && (!CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() || BlockUtil.isAir(boost2)) && !BlockUtil.isAir(obsPos);
    }

    public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        return !(ListenerHelper.getBlock(obsPos) != Blocks.BEDROCK && ListenerHelper.getBlock(obsPos) != Blocks.OBSIDIAN && !BlockUtil.canPlace(obsPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue()) || BlockUtil.getClickSideStrict(obsPos) == null || !ListenerHelperUtil.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) || !ListenerHelperUtil.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) || !BlockUtil.isAir(boost) && (!BlockUtil.hasCrystal(boost) || ListenerHelper.getBlock(boost) != Blocks.FIRE) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() && !BlockUtil.isAir(boost2));
    }

    private static boolean noEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        if (ListenerHelperUtil.mc.world != null) {
            for (Entity entity : ListenerHelperUtil.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
                if (!entity.isAlive() || ignoreItem && entity instanceof ItemEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
                if (entity instanceof EndCrystalEntity) {
                    if (!ignoreCrystal) {
                        return false;
                    }
                    if (ListenerHelperUtil.mc.player != null && (ListenerHelperUtil.mc.player.canSee(entity) || ListenerHelperUtil.mc.player.getEyePos().distanceTo(entity.getPos()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())) continue;
                }
                return false;
            }
        }
        return true;
    }
}
