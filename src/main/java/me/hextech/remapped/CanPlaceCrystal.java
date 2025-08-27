package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class CanPlaceCrystal implements Wrapper {
   public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         BlockPos obsPos = pos.method_10074();
         BlockPos boost = obsPos.method_10084();
         BlockPos boost2 = boost.method_10084();
         return (BlockUtil.getBlock(obsPos) == Blocks.field_9987 || BlockUtil.getBlock(obsPos) == Blocks.field_10540)
            && BlockUtil.getClickSideStrict(obsPos) != null
            && hasEntityBlockCrystal(boost, ignoreCrystal, ignoreItem)
            && hasEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem)
            && (BlockUtil.isAir(boost) || BlockUtil.hasCrystal(boost) && BlockUtil.getBlock(boost) == Blocks.field_10036)
            && (!CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() || BlockUtil.isAir(boost2));
      } else {
         return false;
      }
   }

   private static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
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
                     && !(mc.field_1724.method_33571().method_1022(entity.method_19538()) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue())) {
                  return false;
               }
            }
         }
      }

      return true;
   }
}
