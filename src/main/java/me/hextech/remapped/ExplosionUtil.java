package me.hextech.remapped;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

public class ExplosionUtil implements Wrapper {
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
      if (entity instanceof PlayerEntity player && player.method_31549().field_7477) {
         return 0.0F;
      }

      if (predict == null) {
         predict = entity;
      }

      float doubleExplosionSize = 2.0F * power;
      double distancedsize = (double)MathHelper.method_15355((float)predict.method_5649(posX, posY, posZ)) / (double)doubleExplosionSize;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = 0.0;

      try {
         blockDensity = (double)getExposure(vec3d, predict);
      } catch (Exception var21) {
      }

      double v = (1.0 - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0));
      double finald = (double)getBlastReduction((LivingEntity)entity, getDamageMultiplied(damage));
      return (float)finald;
   }

   public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
      float f = 2.0F + toughnessAttribute / 4.0F;
      float f1 = MathHelper.method_15363(totalArmor - damage / f, totalArmor * 0.2F, 20.0F);
      return damage * (1.0F - f1 / 25.0F);
   }

   public static float getBlastReduction(LivingEntity entity, float damageI) {
      if (entity instanceof PlayerEntity player) {
         float var6 = getDamageAfterAbsorb(damageI, (float)player.method_6096(), (float)player.method_26825(EntityAttributes.field_23725));
         int k = getProtectionAmount(player.method_5661());
         float f = MathHelper.method_15363((float)k, 0.0F, 20.0F);
         var6 *= 1.0F - f / 25.0F;
         if (player.method_6059(StatusEffects.field_5907)) {
            var6 -= var6 / 4.0F;
         }

         return Math.max(var6, 0.0F);
      } else {
         float damage = getDamageAfterAbsorb(damageI, (float)entity.method_6096(), (float)entity.method_26825(EntityAttributes.field_23725));
         return Math.max(damage, 0.0F);
      }
   }

   public static float getExposure(Vec3d source, Entity entity) {
      Box box = entity.method_5829();
      int miss = 0;
      int hit = 0;

      for (int k = 0; k <= 1; k++) {
         for (int l = 0; l <= 1; l++) {
            for (int m = 0; m <= 1; m++) {
               double n = MathHelper.method_16436((double)k, box.field_1323, box.field_1320);
               double o = MathHelper.method_16436((double)l, box.field_1322, box.field_1325);
               double p = MathHelper.method_16436((double)m, box.field_1321, box.field_1324);
               Vec3d vec3d = new Vec3d(n, o, p);
               if (raycast(vec3d, source) == Type.field_1333) {
                  miss++;
               }

               hit++;
            }
         }
      }

      return (float)miss / (float)hit;
   }

   private static Type raycast(Vec3d start, Vec3d end) {
      return (Type)BlockView.method_17744(start, end, null, (_null, blockPos) -> {
         BlockState blockState = mc.field_1687.method_8320(blockPos);
         if (blockState.method_26204().method_9520() < 600.0F) {
            return null;
         } else {
            BlockHitResult hitResult = blockState.method_26220(mc.field_1687, blockPos).method_1092(start, end, blockPos);
            return hitResult == null ? null : hitResult.method_17783();
         }
      }, _null -> Type.field_1333);
   }

   public static int getProtectionAmount(Iterable<ItemStack> armorItems) {
      int value = 0;

      for (ItemStack itemStack : armorItems) {
         int level = EnchantmentHelper.method_8225(Enchantments.field_9111, itemStack);
         if (level == 0) {
            value += EnchantmentHelper.method_8225(Enchantments.field_9107, itemStack) * 2;
         } else {
            value += level;
         }
      }

      return value;
   }

   public static float getDamageMultiplied(float damage) {
      int diff = mc.field_1687.method_8407().method_5461();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   public static float calculateDamage(BlockPos pos, Entity target) {
      return calculateDamage((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5, target, target, 6.0F);
   }
}
