package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.asm.accessors.IClientWorld;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.ComboBreaks;
import me.hextech.remapped.EntityUtil_jqZdaOopZvRmVGkTNyyz;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SwingSide;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class EntityUtil
implements Wrapper {
    public static boolean rotating;

    public static boolean isHoldingWeapon(PlayerEntity player) {
        return player.getMainHandStack().getItem() instanceof SwordItem || player.getMainHandStack().getItem() instanceof AxeItem;
    }

    public static boolean isUsing() {
        return EntityUtil.mc.player.isUsingItem();
    }

    public static boolean isInsideBlock() {
        if (BlockUtil.getBlock(EntityUtil.getPlayerPos(true)) == Blocks.ENDER_CHEST) {
            return true;
        }
        return EntityUtil.mc.world.canCollide((Entity)EntityUtil.mc.player, EntityUtil.mc.player.getBoundingBox());
    }

    public static int getDamagePercent(ItemStack stack) {
        if (stack.getDamage() == stack.getMaxDamage()) {
            return 100;
        }
        return (int)((double)(stack.getMaxDamage() - stack.getDamage()) / Math.max(0.1, (double)stack.getMaxDamage()) * 100.0);
    }

    public static boolean isArmorLow(PlayerEntity player, int durability) {
        for (ItemStack piece : player.method_5661()) {
            if (piece == null || piece.isEmpty()) {
                return true;
            }
            if (EntityUtil.getDamagePercent(piece) >= durability) continue;
            return true;
        }
        return false;
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = EntityUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapDegrees((float)yaw), MathHelper.wrapDegrees((float)pitch)};
    }

    public static float getHealth(Entity entity) {
        if (entity.isLiving()) {
            LivingEntity livingBase = (LivingEntity)entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPosX(EntityUtil.mc.player.getPos());
    }

    public static BlockPos getEntityPos(Entity entity) {
        return new BlockPosX(entity.getPos());
    }

    public static BlockPos getPlayerPos(boolean fix) {
        return new BlockPosX(EntityUtil.mc.player.getPos(), fix);
    }

    public static BlockPos getEntityPos(Entity entity, boolean fix) {
        return new BlockPosX(entity.getPos(), fix);
    }

    public static Vec3d getEyesPos() {
        return EntityUtil.mc.player.getEyePos();
    }

    public static boolean canSee(BlockPos pos, Direction side) {
        Vec3d testVec = pos.toCenterPos().add((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5);
        BlockHitResult result = EntityUtil.mc.world.method_17742(new RaycastContext(EntityUtil.getEyesPos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)EntityUtil.mc.player));
        return result == null || result.getType() == HitResult.Type.MISS;
    }

    public static void sendYawAndPitch(float yaw, float pitch) {
        EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, EntityUtil.mc.player.isOnGround()));
    }

    public static void faceVector(Vec3d directionVec) {
        RotateManager.TrueVec3d(directionVec);
        EntityUtil.faceVectorNoStay(directionVec);
    }

    public static void faceVectorNoStay(Vec3d directionVec) {
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (angle[0] == HexTech.ROTATE.lastYaw && angle[1] == RotateManager.lastPitch) {
            return;
        }
        if (ComboBreaks.INSTANCE.isOff()) {
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incRotate.getValue() && MathHelper.angleBetween((float)angle[0], (float)HexTech.ROTATE.lastYaw) < CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incStrike.getValueFloat() && MathHelper.angleBetween((float)angle[1], (float)RotateManager.lastPitch) < CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incStrike.getValueFloat()) {
                return;
            }
            EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], EntityUtil.mc.player.isOnGround()));
        }
    }

    public static void sendLook(PlayerMoveC2SPacket packet) {
        if (!packet.changesLook() || packet.getYaw(114514.0f) == HexTech.ROTATE.lastYaw && packet.getPitch(114514.0f) == RotateManager.lastPitch) {
            return;
        }
        rotating = true;
        HexTech.ROTATE.setRotation(packet.getYaw(0.0f), packet.getPitch(0.0f), true);
        EntityUtil.mc.player.networkHandler.sendPacket((Packet)packet);
        rotating = false;
    }

    public static void facePosSide(BlockPos pos, Direction side) {
        Vec3d hitVec = pos.toCenterPos().add(new Vec3d((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5));
        EntityUtil.faceVector(hitVec);
    }

    public static void facePosSideNoStay(BlockPos pos, Direction side) {
        Vec3d hitVec = pos.toCenterPos().add(new Vec3d((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5));
        EntityUtil.faceVectorNoStay(hitVec);
    }

    public static int getWorldActionId(ClientWorld world) {
        PendingUpdateManager pum = EntityUtil.getUpdateManager(world);
        int p = pum.getSequence();
        pum.close();
        return p;
    }

    public static boolean isElytraFlying() {
        return EntityUtil.mc.player.method_6128();
    }

    static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((IClientWorld)world).acquirePendingUpdateManager();
    }

    public static void swingHand(Hand hand, SwingSide side) {
        switch (EntityUtil_jqZdaOopZvRmVGkTNyyz.$SwitchMap$me$hextech$mod$modules$settings$SwingSide[side.ordinal()]) {
            case 1: {
                EntityUtil.mc.player.method_6104(hand);
                break;
            }
            case 2: {
                EntityUtil.mc.player.method_23667(hand, false);
                break;
            }
            case 3: {
                EntityUtil.mc.player.networkHandler.sendPacket((Packet)new HandSwingC2SPacket(hand));
            }
        }
    }

    public static void syncInventory() {
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.inventorySync.getValue()) {
            EntityUtil.mc.player.networkHandler.sendPacket((Packet)new CloseHandledScreenC2SPacket(EntityUtil.mc.player.field_7512.syncId));
        }
    }
}
