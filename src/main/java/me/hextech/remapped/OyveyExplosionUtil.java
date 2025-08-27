package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class OyveyExplosionUtil implements Wrapper {
   public static float anchorDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict) {
      if (BlockUtil.getBlock(pos) == Blocks.field_23152) {
         CombatUtil.modifyPos = pos;
         CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
         float damage = calculateDamage(
            pos.method_46558().method_10216(), pos.method_46558().method_10214(), pos.method_46558().method_10215(), target, predict, 5.0F
         );
         CombatUtil.modifyPos = null;
         return damage;
      } else {
         return calculateDamage(pos.method_46558().method_10216(), pos.method_46558().method_10214(), pos.method_46558().method_10215(), target, predict, 5.0F);
      }
   }

   public static float calculateDamage(double posX, double posY, double posZ, Entity entity, Entity predict, float power) {
      if (predict == null) {
         predict = entity;
      }

      float doubleExplosionSize = 2.0F * power;
      double distancedsize = (double)MathHelper.method_15355((float)predict.method_5649(posX, posY, posZ)) / (double)doubleExplosionSize;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = 0.0;

      try {
         blockDensity = (double)Explosion.method_17752(vec3d, predict);
      } catch (Exception var21) {
      }

      double v = (1.0 - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0));
      double finald = 1.0;
      if (entity instanceof LivingEntity) {
         finald = (double)getBlastReduction(
            (LivingEntity)entity,
            getDamageMultiplied(damage),
            new Explosion(mc.field_1687, entity, posX, posY, posZ, power, false, DestructionType.field_18687)
         );
      }

      return (float)finald;
   }

   public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
      float f = 2.0F + toughnessAttribute / 4.0F;
      float f1 = MathHelper.method_15363(totalArmor - damage / f, totalArmor * 0.2F, 20.0F);
      return damage * (1.0F - f1 / 25.0F);
   }

   public static float getBlastReduction(LivingEntity entity, float damageI, Explosion explosion) {
      if (entity instanceof PlayerEntity player) {
         DamageSource ds = mc.field_1687.method_48963().method_48807(explosion);
         float damage = getDamageAfterAbsorb(damageI, (float)player.method_6096(), (float)player.method_26825(EntityAttributes.field_23725));
         int k = EnchantmentHelper.method_8219(player.method_5661(), ds);
         float f = MathHelper.method_15363((float)k, 0.0F, 20.0F);
         damage *= 1.0F - f / 25.0F;
         if (player.method_6059(StatusEffects.field_5907)) {
            damage -= damage / 4.0F;
         }

         return Math.max(damage, 0.0F);
      } else {
         return getDamageAfterAbsorb(damageI, (float)entity.method_6096(), (float)entity.method_26825(EntityAttributes.field_23725));
      }
   }

   public static float getDamageMultiplied(float damage) {
      int diff = mc.field_1687.method_8407().method_5461();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }
}
