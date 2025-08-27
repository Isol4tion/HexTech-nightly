package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class ListenerHelperUtil implements Wrapper {
   public static boolean canTouch(BlockPos pos) {
      Direction side = BlockUtil.getClickSideStrict(pos);
      return mc.field_1724 == null
         ? false
         : side != null
            && pos.method_46558()
                  .method_1019(
                     new Vec3d(
                        (double)side.method_10163().method_10263() * 0.5,
                        (double)side.method_10163().method_10264() * 0.5,
                        (double)side.method_10163().method_10260() * 0.5
                     )
                  )
                  .method_1022(mc.field_1724.method_33571())
               <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue();
   }

   public static float calculateBase(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
      return calculateObsidian(
         pos.method_10074(), new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5), player, predict
      );
   }

   public static float calculateObsidian(BlockPos obs, Vec3d pos, PlayerEntity player, PlayerEntity predict) {
      CombatUtil.modifyPos = obs;
      CombatUtil.modifyBlockState = Blocks.field_10540.method_9564();
      float damage = ExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
      CombatUtil.modifyPos = null;
      CombatUtil.terrainIgnore = false;
      return damage;
   }

   public static boolean behindWall(BlockPos pos) {
      Vec3d testVec = new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
      HitResult result = null;
      if (mc.field_1687 != null && mc.field_1724 != null) {
         result = mc.field_1687
            .method_17742(new RaycastContext(EntityUtil.getEyesPos(), testVec, ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724));
      }

      return result != null && result.method_17783() != Type.field_1333
         ? mc.field_1724.method_33571().method_1022(pos.method_46558().method_1031(0.0, -0.5, 0.0))
            > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue()
         : false;
   }

   public static boolean canSee(Vec3d from, Vec3d to) {
      HitResult result = null;
      if (mc.field_1687 != null && mc.field_1724 != null) {
         result = mc.field_1687.method_17742(new RaycastContext(from, to, ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724));
      }

      return result == null || result.method_17783() == Type.field_1333;
   }

   public static boolean liteCheck(Vec3d from, Vec3d to) {
      return !canSee(from, to) && !canSee(from, to.method_1031(0.0, 1.8, 0.0));
   }

   public static boolean canBasePlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      BlockPos obsPos = pos.method_10074();
      BlockPos boost = obsPos.method_10084();
      BlockPos boost2 = boost.method_10084();
      return BlockUtil.canPlace(obsPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())
         && BlockUtil.getClickSideStrict(obsPos) != null
         && noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem)
         && noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem)
         && (BlockUtil.isAir(boost) || BlockUtil.hasCrystal(boost) && ListenerHelper.getBlock(boost) == Blocks.field_10036)
         && (!CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() || BlockUtil.isAir(boost2))
         && !BlockUtil.isAir(obsPos);
   }

   public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      BlockPos obsPos = pos.method_10074();
      BlockPos boost = obsPos.method_10084();
      BlockPos boost2 = boost.method_10084();
      return (
            ListenerHelper.getBlock(obsPos) == Blocks.field_9987
               || ListenerHelper.getBlock(obsPos) == Blocks.field_10540
               || BlockUtil.canPlace(obsPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())
         )
         && BlockUtil.getClickSideStrict(obsPos) != null
         && noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem)
         && noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem)
         && (BlockUtil.isAir(boost) || BlockUtil.hasCrystal(boost) && ListenerHelper.getBlock(boost) == Blocks.field_10036)
         && (!CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() || BlockUtil.isAir(boost2));
   }

   private static boolean noEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      if (mc.field_1687 != null) {
         for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
            if (entity.method_5805()
               && (!ignoreItem || !(entity instanceof ItemEntity))
               && (!(entity instanceof ArmorStandEntity) || !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue())) {
               if (!(entity instanceof EndCrystalEntity)) {
                  return false;
               }

               if (!ignoreCrystal) {
                  return false;
               }

               if (mc.field_1724 == null
                  || !mc.field_1724.method_6057(entity)
                     && !(mc.field_1724.method_33571().method_1022(entity.method_19538()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())) {
                  return false;
               }
            }
         }
      }

      return true;
   }
}
