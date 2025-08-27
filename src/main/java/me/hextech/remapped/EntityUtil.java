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
        return player.method_6047().method_7909() instanceof SwordItem || player.method_6047().method_7909() instanceof AxeItem;
    }

    public static boolean isUsing() {
        return EntityUtil.mc.field_1724.method_6115();
    }

    public static boolean isInsideBlock() {
        if (BlockUtil.getBlock(EntityUtil.getPlayerPos(true)) == Blocks.field_10443) {
            return true;
        }
        return EntityUtil.mc.field_1687.method_39454((Entity)EntityUtil.mc.field_1724, EntityUtil.mc.field_1724.method_5829());
    }

    public static int getDamagePercent(ItemStack stack) {
        if (stack.method_7919() == stack.method_7936()) {
            return 100;
        }
        return (int)((double)(stack.method_7936() - stack.method_7919()) / Math.max(0.1, (double)stack.method_7936()) * 100.0);
    }

    public static boolean isArmorLow(PlayerEntity player, int durability) {
        for (ItemStack piece : player.method_5661()) {
            if (piece == null || piece.method_7960()) {
                return true;
            }
            if (EntityUtil.getDamagePercent(piece) >= durability) continue;
            return true;
        }
        return false;
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = EntityUtil.getEyesPos();
        double diffX = vec.field_1352 - eyesPos.field_1352;
        double diffY = vec.field_1351 - eyesPos.field_1351;
        double diffZ = vec.field_1350 - eyesPos.field_1350;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.method_15393((float)yaw), MathHelper.method_15393((float)pitch)};
    }

    public static float getHealth(Entity entity) {
        if (entity.method_5709()) {
            LivingEntity livingBase = (LivingEntity)entity;
            return livingBase.method_6032() + livingBase.method_6067();
        }
        return 0.0f;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPosX(EntityUtil.mc.field_1724.method_19538());
    }

    public static BlockPos getEntityPos(Entity entity) {
        return new BlockPosX(entity.method_19538());
    }

    public static BlockPos getPlayerPos(boolean fix) {
        return new BlockPosX(EntityUtil.mc.field_1724.method_19538(), fix);
    }

    public static BlockPos getEntityPos(Entity entity, boolean fix) {
        return new BlockPosX(entity.method_19538(), fix);
    }

    public static Vec3d getEyesPos() {
        return EntityUtil.mc.field_1724.method_33571();
    }

    public static boolean canSee(BlockPos pos, Direction side) {
        Vec3d testVec = pos.method_46558().method_1031((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5);
        BlockHitResult result = EntityUtil.mc.field_1687.method_17742(new RaycastContext(EntityUtil.getEyesPos(), testVec, RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)EntityUtil.mc.field_1724));
        return result == null || result.method_17783() == HitResult.Type.field_1333;
    }

    public static void sendYawAndPitch(float yaw, float pitch) {
        EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, EntityUtil.mc.field_1724.method_24828()));
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
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incRotate.getValue() && MathHelper.method_15356((float)angle[0], (float)HexTech.ROTATE.lastYaw) < CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incStrike.getValueFloat() && MathHelper.method_15356((float)angle[1], (float)RotateManager.lastPitch) < CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incStrike.getValueFloat()) {
                return;
            }
            EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], EntityUtil.mc.field_1724.method_24828()));
        }
    }

    public static void sendLook(PlayerMoveC2SPacket packet) {
        if (!packet.method_36172() || packet.method_12271(114514.0f) == HexTech.ROTATE.lastYaw && packet.method_12270(114514.0f) == RotateManager.lastPitch) {
            return;
        }
        rotating = true;
        HexTech.ROTATE.setRotation(packet.method_12271(0.0f), packet.method_12270(0.0f), true);
        EntityUtil.mc.field_1724.field_3944.method_52787((Packet)packet);
        rotating = false;
    }

    public static void facePosSide(BlockPos pos, Direction side) {
        Vec3d hitVec = pos.method_46558().method_1019(new Vec3d((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5));
        EntityUtil.faceVector(hitVec);
    }

    public static void facePosSideNoStay(BlockPos pos, Direction side) {
        Vec3d hitVec = pos.method_46558().method_1019(new Vec3d((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5));
        EntityUtil.faceVectorNoStay(hitVec);
    }

    public static int getWorldActionId(ClientWorld world) {
        PendingUpdateManager pum = EntityUtil.getUpdateManager(world);
        int p = pum.method_41942();
        pum.close();
        return p;
    }

    public static boolean isElytraFlying() {
        return EntityUtil.mc.field_1724.method_6128();
    }

    static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((IClientWorld)world).acquirePendingUpdateManager();
    }

    public static void swingHand(Hand hand, SwingSide side) {
        switch (EntityUtil_jqZdaOopZvRmVGkTNyyz.$SwitchMap$me$hextech$mod$modules$settings$SwingSide[side.ordinal()]) {
            case 1: {
                EntityUtil.mc.field_1724.method_6104(hand);
                break;
            }
            case 2: {
                EntityUtil.mc.field_1724.method_23667(hand, false);
                break;
            }
            case 3: {
                EntityUtil.mc.field_1724.field_3944.method_52787((Packet)new HandSwingC2SPacket(hand));
            }
        }
    }

    public static void syncInventory() {
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.inventorySync.getValue()) {
            EntityUtil.mc.field_1724.field_3944.method_52787((Packet)new CloseHandledScreenC2SPacket(EntityUtil.mc.field_1724.field_7512.field_7763));
        }
    }
}
