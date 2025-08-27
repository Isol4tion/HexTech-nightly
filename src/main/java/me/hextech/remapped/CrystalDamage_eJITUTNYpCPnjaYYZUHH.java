package me.hextech.remapped;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalDamage_eJITUTNYpCPnjaYYZUHH {
   public static float calculateDamage(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
      return calculateCrystalDamage(new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5), player, predict);
   }

   public static float calculateCrystalDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
      if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTerrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      float damage = 0.0F;
      switch ((Enum_IKgLeKHCELPvcpdGlLhV)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.calcMode.getValue()) {
         case OyVey:
            damage = (float)MeteorExplosionUtil.explosionDamage(player, pos, predict, 6.0F);
            break;
         case Meteor:
            damage = ThunderExplosionUtil.calculateDamage(pos, player, predict, 6.0F);
            break;
         case Thunder:
            damage = OyveyExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
            break;
         case EditionHex:
            damage = ExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
            break;
         case Mio:
            damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.calculateDamage(pos, player, predict, 6.0F);
      }

      CombatUtil.terrainIgnore = false;
      return damage;
   }
}
