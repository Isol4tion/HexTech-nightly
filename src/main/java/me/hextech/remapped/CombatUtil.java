package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.hextech.remapped.api.utils.Wrapper;
import me.hextech.remapped.api.utils.world.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CombatUtil
implements Wrapper {
    public static final Timer breakTimer = new Timer();
    public static boolean terrainIgnore;
    public static BlockPos modifyPos;
    public static BlockState modifyBlockState;

    public static List<PlayerEntity> getEnemies(double range) {
        ArrayList<PlayerEntity> list = new ArrayList<PlayerEntity>();
        if (CombatUtil.mc.world != null) {
            for (PlayerEntity player : CombatUtil.mc.world.getPlayers()) {
                if (!CombatUtil.isValid(player, range)) continue;
                list.add(player);
            }
        }
        return list;
    }

    public static void attackCrystal(BlockPos pos, boolean rotate, boolean eatingPause) {
        block0: {
            Iterator iterator;
            if (CombatUtil.mc.world == null || !(iterator = CombatUtil.mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box(pos)).iterator()).hasNext()) break block0;
            EndCrystalEntity entity = (EndCrystalEntity)iterator.next();
            CombatUtil.attackCrystal(entity, rotate, eatingPause);
        }
    }

    public static void attackCrystal(Box box, boolean rotate, boolean eatingPause) {
        block0: {
            Iterator iterator;
            if (CombatUtil.mc.world == null || !(iterator = CombatUtil.mc.world.getNonSpectatingEntities(EndCrystalEntity.class, box).iterator()).hasNext()) break block0;
            EndCrystalEntity entity = (EndCrystalEntity)iterator.next();
            CombatUtil.attackCrystal(entity, rotate, eatingPause);
        }
    }

    public static void attackCrystal(Entity crystal, boolean rotate, boolean usingPause) {
        if (!breakTimer.passedMs((long)(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackDelay.getValue() * 1000.0))) {
            return;
        }
        if (usingPause && EntityUtil.isUsing()) {
            return;
        }
        if (crystal != null) {
            breakTimer.reset();
            if (rotate && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue()) {
                EntityUtil.faceVector(new Vec3d(crystal.getX(), crystal.getY() + 0.25, crystal.getZ()));
            }
            if (CombatUtil.mc.player != null) {
                CombatUtil.mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, CombatUtil.mc.player.isSneaking()));
            }
            if (CombatUtil.mc.player != null) {
                CombatUtil.mc.player.resetLastAttackedTicks();
            }
            EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        }
    }

    public static boolean isValid(Entity entity, double range) {
        boolean invalid = entity == null || !entity.isAlive() || entity.equals(CombatUtil.mc.player) || entity instanceof PlayerEntity && FriendManager.isFriend(entity.getName().getString()) || CombatUtil.mc.player.squaredDistanceTo(entity) > MathUtil.square(range);
        return !invalid;
    }

    public static BlockPos getHole(float range, boolean doubleHole, boolean any) {
        BlockPos bestPos = null;
        double bestDistance = range + 1.0f;
        for (BlockPos pos : BlockUtil.getSphere(range)) {
            if (CombatUtil.mc.player == null || pos.getX() == CombatUtil.mc.player.getBlockX() && pos.getZ() == CombatUtil.mc.player.getBlockZ() || !BlockUtil.isHole(pos, true, true, any) && (!doubleHole || !CombatUtil.isDoubleHole(pos)) || pos.getY() - CombatUtil.mc.player.getBlockY() > 1) continue;
            double distance = MathHelper.sqrt((float)CombatUtil.mc.player.squaredDistanceTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5));
            if (bestPos != null && !(distance < bestDistance)) continue;
            bestPos = pos;
            bestDistance = distance;
        }
        return bestPos;
    }

    public static boolean isDoubleHole(BlockPos pos) {
        Direction unHardFacing = CombatUtil.is3Block(pos);
        if (unHardFacing != null) {
            return (unHardFacing = CombatUtil.is3Block(pos = pos.offset(unHardFacing))) != null;
        }
        return false;
    }

    public static Direction is3Block(BlockPos pos) {
        if (!CombatUtil.isHard(pos.down())) {
            return null;
        }
        if (!(BlockUtil.isAir(pos) && BlockUtil.isAir(pos.up()) && BlockUtil.isAir(pos.up(2)))) {
            return null;
        }
        int progress = 0;
        Direction unHardFacing = null;
        for (Direction facing : Direction.values()) {
            if (facing == Direction.UP || facing == Direction.DOWN) continue;
            if (CombatUtil.isHard(pos.offset(facing))) {
                ++progress;
                continue;
            }
            int progress2 = 0;
            for (Direction facing2 : Direction.values()) {
                if (facing2 == Direction.DOWN || facing2 == facing.getOpposite() || !CombatUtil.isHard(pos.offset(facing).offset(facing2))) continue;
                ++progress2;
            }
            if (progress2 == 4) {
                ++progress;
                continue;
            }
            unHardFacing = facing;
        }
        if (progress == 3) {
            return unHardFacing;
        }
        return null;
    }

    public static PlayerEntity getClosestEnemy(double distance) {
        PlayerEntity closest = null;
        for (PlayerEntity player : CombatUtil.getEnemies(distance)) {
            if (closest == null) {
                closest = player;
                continue;
            }
            if (!(CombatUtil.mc.player.getEyePos().squaredDistanceTo(player.getPos()) < CombatUtil.mc.player.squaredDistanceTo(closest))) continue;
            closest = player;
        }
        return closest;
    }

    public static Vec3d getEntityPosVec(PlayerEntity entity, int ticks) {
        if (ticks <= 0) {
            return entity.getPos();
        }
        return entity.getPos().add(CombatUtil.getMotionVec(entity, ticks, true));
    }

    public static Vec3d getMotionVec(Entity entity, float ticks, boolean collision) {
        double dX = entity.getX() - entity.prevX;
        double dZ = entity.getZ() - entity.prevZ;
        double entityMotionPosX = 0.0;
        double entityMotionPosZ = 0.0;
        if (collision) {
            for (double i = 1.0; i <= (double)ticks && !CombatUtil.mc.world.canCollide(entity, entity.getBoundingBox().offset(new Vec3d(dX * i, 0.0, dZ * i))); i += 0.5) {
                entityMotionPosX = dX * i;
                entityMotionPosZ = dZ * i;
            }
        } else {
            entityMotionPosX = dX * (double)ticks;
            entityMotionPosZ = dZ * (double)ticks;
        }
        return new Vec3d(entityMotionPosX, 0.0, entityMotionPosZ);
    }

    public static Vec3d getEntityPosVecWithY(PlayerEntity entity, int ticks) {
        if (ticks <= 0) {
            return entity.getPos();
        }
        return entity.getPos().add(CombatUtil.getMotionVecWithY(entity, ticks, true));
    }

    public static Vec3d getMotionVecWithY(Entity entity, int ticks, boolean collision) {
        double dX = entity.getX() - entity.prevX;
        double dY = entity.getY() - entity.prevY;
        double dZ = entity.getZ() - entity.prevZ;
        double entityMotionPosX = 0.0;
        double entityMotionPosY = 0.0;
        double entityMotionPosZ = 0.0;
        if (collision) {
            for (double i = 1.0; i <= (double)ticks && !CombatUtil.mc.world.canCollide(entity, entity.getBoundingBox().offset(new Vec3d(dX * i, dY * i, dZ * i))); i += 0.5) {
                entityMotionPosX = dX * i;
                entityMotionPosY = dY * i;
                entityMotionPosZ = dZ * i;
            }
        } else {
            entityMotionPosX = dX * (double)ticks;
            entityMotionPosY = dY * (double)ticks;
            entityMotionPosZ = dZ * (double)ticks;
        }
        return new Vec3d(entityMotionPosX, entityMotionPosY, entityMotionPosZ);
    }

    public static boolean isHard(BlockPos pos) {
        Block block = BlockUtil.getState(pos).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.NETHERITE_BLOCK || block == Blocks.ENDER_CHEST || block == Blocks.BEDROCK || block == Blocks.ANVIL;
    }
}
