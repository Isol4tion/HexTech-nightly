package me.hextech.remapped;

import me.hextech.asm.accessors.IClientWorld;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class EntityUtil implements Wrapper {
   public static boolean rotating;

   public static boolean isHoldingWeapon(PlayerEntity player) {
      return player.method_6047().method_7909() instanceof SwordItem || player.method_6047().method_7909() instanceof AxeItem;
   }

   public static boolean isUsing() {
      return mc.field_1724.method_6115();
   }

   public static boolean isInsideBlock() {
      return BlockUtil.getBlock(getPlayerPos(true)) == Blocks.field_10443 ? true : mc.field_1687.method_39454(mc.field_1724, mc.field_1724.method_5829());
   }

   public static int getDamagePercent(ItemStack stack) {
      return stack.method_7919() == stack.method_7936()
         ? 100
         : (int)((double)(stack.method_7936() - stack.method_7919()) / Math.max(0.1, (double)stack.method_7936()) * 100.0);
   }

   public static boolean isArmorLow(PlayerEntity player, int durability) {
      for (ItemStack piece : player.method_5661()) {
         if (piece == null || piece.method_7960()) {
            return true;
         }

         if (getDamagePercent(piece) < durability) {
            return true;
         }
      }

      return false;
   }

   public static float[] getLegitRotations(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.field_1352 - eyesPos.field_1352;
      double diffY = vec.field_1351 - eyesPos.field_1351;
      double diffZ = vec.field_1350 - eyesPos.field_1350;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{MathHelper.method_15393(yaw), MathHelper.method_15393(pitch)};
   }

   public static float getHealth(Entity entity) {
      if (entity.method_5709()) {
         LivingEntity livingBase = (LivingEntity)entity;
         return livingBase.method_6032() + livingBase.method_6067();
      } else {
         return 0.0F;
      }
   }

   public static BlockPos getPlayerPos() {
      return new BlockPosX(mc.field_1724.method_19538());
   }

   public static BlockPos getEntityPos(Entity entity) {
      return new BlockPosX(entity.method_19538());
   }

   public static BlockPos getPlayerPos(boolean fix) {
      return new BlockPosX(mc.field_1724.method_19538(), fix);
   }

   public static BlockPos getEntityPos(Entity entity, boolean fix) {
      return new BlockPosX(entity.method_19538(), fix);
   }

   public static Vec3d getEyesPos() {
      return mc.field_1724.method_33571();
   }

   public static boolean canSee(BlockPos pos, Direction side) {
      Vec3d testVec = pos.method_46558()
         .method_1031(
            (double)side.method_10163().method_10263() * 0.5,
            (double)side.method_10163().method_10264() * 0.5,
            (double)side.method_10163().method_10260() * 0.5
         );
      HitResult result = mc.field_1687.method_17742(new RaycastContext(getEyesPos(), testVec, ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724));
      return result == null || result.method_17783() == Type.field_1333;
   }

   public static void sendYawAndPitch(float yaw, float pitch) {
      sendLook(new LookAndOnGround(yaw, pitch, mc.field_1724.method_24828()));
   }

   public static void faceVector(Vec3d directionVec) {
      RotateManager.TrueVec3d(directionVec);
      faceVectorNoStay(directionVec);
   }

   public static void faceVectorNoStay(Vec3d directionVec) {
      float[] angle = getLegitRotations(directionVec);
      if (angle[0] != me.hextech.HexTech.ROTATE.lastYaw || angle[1] != RotateManager.lastPitch) {
         if (ComboBreaks.INSTANCE.isOff()) {
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incRotate.getValue()
               && MathHelper.method_15356(angle[0], me.hextech.HexTech.ROTATE.lastYaw) < CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incStrike.getValueFloat()
               && MathHelper.method_15356(angle[1], RotateManager.lastPitch) < CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.incStrike.getValueFloat()) {
               return;
            }

            sendLook(new LookAndOnGround(angle[0], angle[1], mc.field_1724.method_24828()));
         }
      }
   }

   public static void sendLook(PlayerMoveC2SPacket packet) {
      if (packet.method_36172()
         && (packet.method_12271(114514.0F) != me.hextech.HexTech.ROTATE.lastYaw || packet.method_12270(114514.0F) != RotateManager.lastPitch)) {
         rotating = true;
         me.hextech.HexTech.ROTATE.setRotation(packet.method_12271(0.0F), packet.method_12270(0.0F), true);
         mc.field_1724.field_3944.method_52787(packet);
         rotating = false;
      }
   }

   public static void facePosSide(BlockPos pos, Direction side) {
      Vec3d hitVec = pos.method_46558()
         .method_1019(
            new Vec3d(
               (double)side.method_10163().method_10263() * 0.5,
               (double)side.method_10163().method_10264() * 0.5,
               (double)side.method_10163().method_10260() * 0.5
            )
         );
      faceVector(hitVec);
   }

   public static void facePosSideNoStay(BlockPos pos, Direction side) {
      Vec3d hitVec = pos.method_46558()
         .method_1019(
            new Vec3d(
               (double)side.method_10163().method_10263() * 0.5,
               (double)side.method_10163().method_10264() * 0.5,
               (double)side.method_10163().method_10260() * 0.5
            )
         );
      faceVectorNoStay(hitVec);
   }

   public static int getWorldActionId(ClientWorld world) {
      PendingUpdateManager pum = getUpdateManager(world);
      int p = pum.method_41942();
      pum.close();
      return p;
   }

   public static boolean isElytraFlying() {
      return mc.field_1724.method_6128();
   }

   static PendingUpdateManager getUpdateManager(ClientWorld world) {
      return ((IClientWorld)world).acquirePendingUpdateManager();
   }

   public static void swingHand(Hand hand, SwingSide side) {
      switch (side) {
         case All:
            mc.field_1724.method_6104(hand);
            break;
         case Client:
            mc.field_1724.method_23667(hand, false);
            break;
         case Server:
            mc.field_1724.field_3944.method_52787(new HandSwingC2SPacket(hand));
      }
   }

   public static void syncInventory() {
      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.inventorySync.getValue()) {
         mc.field_1724.field_3944.method_52787(new CloseHandledScreenC2SPacket(mc.field_1724.field_7512.field_7763));
      }
   }
}
