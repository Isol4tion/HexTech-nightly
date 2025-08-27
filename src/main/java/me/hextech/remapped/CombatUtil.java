package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class CombatUtil implements Wrapper {
   public static final Timer breakTimer = new Timer();
   public static boolean terrainIgnore;
   public static BlockPos modifyPos;
   public static BlockState modifyBlockState;

   public static List<PlayerEntity> getEnemies(double range) {
      List<PlayerEntity> list = new ArrayList();
      if (mc.field_1687 != null) {
         for (PlayerEntity player : mc.field_1687.method_18456()) {
            if (isValid(player, range)) {
               list.add(player);
            }
         }
      }

      return list;
   }

   public static void attackCrystal(BlockPos pos, boolean rotate, boolean eatingPause) {
      if (mc.field_1687 != null) {
         Iterator var3 = mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos)).iterator();
         if (var3.hasNext()) {
            EndCrystalEntity entity = (EndCrystalEntity)var3.next();
            attackCrystal(entity, rotate, eatingPause);
         }
      }
   }

   public static void attackCrystal(Box box, boolean rotate, boolean eatingPause) {
      if (mc.field_1687 != null) {
         Iterator var3 = mc.field_1687.method_18467(EndCrystalEntity.class, box).iterator();
         if (var3.hasNext()) {
            EndCrystalEntity entity = (EndCrystalEntity)var3.next();
            attackCrystal(entity, rotate, eatingPause);
         }
      }
   }

   public static void attackCrystal(Entity crystal, boolean rotate, boolean usingPause) {
      if (breakTimer.passedMs((long)(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackDelay.getValue() * 1000.0))) {
         if (!usingPause || !EntityUtil.isUsing()) {
            if (crystal != null) {
               breakTimer.reset();
               if (rotate && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue()) {
                  EntityUtil.faceVector(new Vec3d(crystal.method_23317(), crystal.method_23318() + 0.25, crystal.method_23321()));
               }

               if (mc.field_1724 != null) {
                  mc.field_1724.field_3944.method_52787(PlayerInteractEntityC2SPacket.method_34206(crystal, mc.field_1724.method_5715()));
               }

               if (mc.field_1724 != null) {
                  mc.field_1724.method_7350();
               }

               EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
            }
         }
      }
   }

   public static boolean isValid(Entity entity, double range) {
      boolean invalid = entity == null
         || !entity.method_5805()
         || entity.equals(mc.field_1724)
         || entity instanceof PlayerEntity && FriendManager.isFriend(entity.method_5477().getString())
         || mc.field_1724.method_5858(entity) > MathUtil.square(range);
      return !invalid;
   }

   public static BlockPos getHole(float range, boolean doubleHole, boolean any) {
      BlockPos bestPos = null;
      double bestDistance = (double)(range + 1.0F);

      for (BlockPos pos : BlockUtil.getSphere(range)) {
         if (mc.field_1724 != null
            && (pos.method_10263() != mc.field_1724.method_31477() || pos.method_10260() != mc.field_1724.method_31479())
            && (BlockUtil.isHole(pos, true, true, any) || doubleHole && isDoubleHole(pos))
            && pos.method_10264() - mc.field_1724.method_31478() <= 1) {
            double distance = (double)MathHelper.method_15355(
               (float)mc.field_1724.method_5649((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 0.5, (double)pos.method_10260() + 0.5)
            );
            if (bestPos == null || distance < bestDistance) {
               bestPos = pos;
               bestDistance = distance;
            }
         }
      }

      return bestPos;
   }

   public static boolean isDoubleHole(BlockPos pos) {
      Direction unHardFacing = is3Block(pos);
      if (unHardFacing != null) {
         pos = pos.method_10093(unHardFacing);
         unHardFacing = is3Block(pos);
         return unHardFacing != null;
      } else {
         return false;
      }
   }

   public static Direction is3Block(BlockPos pos) {
      if (!isHard(pos.method_10074())) {
         return null;
      } else if (BlockUtil.isAir(pos) && BlockUtil.isAir(pos.method_10084()) && BlockUtil.isAir(pos.method_10086(2))) {
         int progress = 0;
         Direction unHardFacing = null;

         for (Direction facing : Direction.values()) {
            if (facing != Direction.field_11036 && facing != Direction.field_11033) {
               if (isHard(pos.method_10093(facing))) {
                  progress++;
               } else {
                  int progress2 = 0;

                  for (Direction facing2 : Direction.values()) {
                     if (facing2 != Direction.field_11033 && facing2 != facing.method_10153() && isHard(pos.method_10093(facing).method_10093(facing2))) {
                        progress2++;
                     }
                  }

                  if (progress2 == 4) {
                     progress++;
                  } else {
                     unHardFacing = facing;
                  }
               }
            }
         }

         return progress == 3 ? unHardFacing : null;
      } else {
         return null;
      }
   }

   public static PlayerEntity getClosestEnemy(double distance) {
      PlayerEntity closest = null;

      for (PlayerEntity player : getEnemies(distance)) {
         if (closest == null) {
            closest = player;
         } else if (mc.field_1724.method_33571().method_1025(player.method_19538()) < mc.field_1724.method_5858(closest)) {
            closest = player;
         }
      }

      return closest;
   }

   public static Vec3d getEntityPosVec(PlayerEntity entity, int ticks) {
      return ticks <= 0 ? entity.method_19538() : entity.method_19538().method_1019(getMotionVec(entity, (float)ticks, true));
   }

   public static Vec3d getMotionVec(Entity entity, float ticks, boolean collision) {
      double dX = entity.method_23317() - entity.field_6014;
      double dZ = entity.method_23321() - entity.field_5969;
      double entityMotionPosX = 0.0;
      double entityMotionPosZ = 0.0;
      if (collision) {
         for (double i = 1.0;
            i <= (double)ticks && !mc.field_1687.method_39454(entity, entity.method_5829().method_997(new Vec3d(dX * i, 0.0, dZ * i)));
            i += 0.5
         ) {
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
      return ticks <= 0 ? entity.method_19538() : entity.method_19538().method_1019(getMotionVecWithY(entity, ticks, true));
   }

   public static Vec3d getMotionVecWithY(Entity entity, int ticks, boolean collision) {
      double dX = entity.method_23317() - entity.field_6014;
      double dY = entity.method_23318() - entity.field_6036;
      double dZ = entity.method_23321() - entity.field_5969;
      double entityMotionPosX = 0.0;
      double entityMotionPosY = 0.0;
      double entityMotionPosZ = 0.0;
      if (collision) {
         for (double i = 1.0;
            i <= (double)ticks && !mc.field_1687.method_39454(entity, entity.method_5829().method_997(new Vec3d(dX * i, dY * i, dZ * i)));
            i += 0.5
         ) {
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
      Block block = BlockUtil.getState(pos).method_26204();
      return block == Blocks.field_10540
         || block == Blocks.field_22108
         || block == Blocks.field_10443
         || block == Blocks.field_9987
         || block == Blocks.field_10535;
   }
}
