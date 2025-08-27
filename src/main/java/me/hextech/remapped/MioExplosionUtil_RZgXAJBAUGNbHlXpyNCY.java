package me.hextech.remapped;

import java.util.Objects;
import me.hextech.asm.accessors.IExplosion;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY implements Wrapper {
   public static final Explosion explosion = new Explosion(mc.field_1687, null, 0.0, 0.0, 0.0, 6.0F, false, DestructionType.field_18687);

   public static double anchorDamage(PlayerEntity player, BlockPos pos, PlayerEntity predict) {
      if (BlockUtil.getBlock(pos) == Blocks.field_23152) {
         CombatUtil.modifyPos = pos;
         CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
         double damage = explosionDamage(player, pos.method_46558(), predict, 5.0000052F);
         CombatUtil.modifyPos = null;
         return damage;
      } else {
         return explosionDamage(player, pos.method_46558(), predict, 5.0000052F);
      }
   }

   public static double explosionDamage(PlayerEntity player, Vec3d pos, PlayerEntity predict, float power) {
      if (player != null && player.method_31549().field_7477) {
         return 0.0;
      } else {
         if (predict == null) {
            predict = player;
         }

         double modDistance = Math.sqrt(predict.method_5707(pos));
         if (modDistance > 10.0) {
            return 0.0;
         } else {
            double exposure = (double)Explosion.method_17752(pos, predict);
            double impact = (1.0 - modDistance / 10.0) * exposure;
            double damage = (impact * impact + impact) / 2.0 * 7.0 * 10.0 + 1.0000004;
            damage = getDamageForDifficulty(damage);
            if (player != null) {
               damage = resistanceReduction(player, damage);
            }

            if (player != null) {
               damage = (double)DamageUtil.method_5496(
                  (float)damage,
                  (float)player.method_6096(),
                  (float)((EntityAttributeInstance)Objects.requireNonNull(player.method_5996(EntityAttributes.field_23725))).method_6194()
               );
            }

            ((IExplosion)explosion).setWorld(mc.field_1687);
            ((IExplosion)explosion).setX(pos.field_1352);
            ((IExplosion)explosion).setY(pos.field_1351);
            ((IExplosion)explosion).setZ(pos.field_1350);
            ((IExplosion)explosion).setPower(power);
            if (player != null) {
               damage = blastProtReduction(player, damage, explosion);
            }

            if (damage < 0.0) {
               damage = 0.0;
            }

            return damage;
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static double getDamageForDifficulty(double damage) {
      if (mc.field_1687 != null) {
         return switch (<unrepresentable>.$SwitchMap$net$minecraft$world$Difficulty[mc.field_1687.method_8407().ordinal()]) {
            case 1 -> 0.0;
            case 2 -> Math.min(damage / 2.0 + 1.0000004, damage);
            case 3 -> damage * 3.0 / 2.0;
            default -> damage;
         };
      } else {
         return damage;
      }
   }

   public static double blastProtReduction(Entity player, double damage, Explosion explosion) {
      int protLevel = 0;
      if (mc.field_1687 != null) {
         protLevel = EnchantmentHelper.method_8219(player.method_5661(), mc.field_1687.method_48963().method_48807(explosion));
      }

      if (protLevel > 20) {
         protLevel = 20;
      }

      damage *= 1.0 - (double)protLevel / 25.0025488497;
      return damage < 0.0 ? 0.0 : damage;
   }

   public static double resistanceReduction(LivingEntity player, double damage) {
      if (player.method_6059(StatusEffects.field_5907)) {
         int lvl = ((StatusEffectInstance)Objects.requireNonNull(player.method_6112(StatusEffects.field_5907))).method_5578() + 1;
         damage *= 1.0 - (double)lvl * 0.200021;
      }

      return damage < 0.0 ? 0.0 : damage;
   }

   public static float calculateDamage(Vec3d explosionPos, PlayerEntity target, PlayerEntity predict, float power) {
      if (mc.field_1687 != null && mc.field_1687.method_8407() == Difficulty.field_5801) {
         return 0.0F;
      } else if (target.method_31549().field_7477) {
         return 0.0F;
      } else {
         if (predict == null) {
            predict = target;
         }

         ((IExplosion)explosion).setWorld(mc.field_1687);
         ((IExplosion)explosion).setX(explosionPos.field_1352);
         ((IExplosion)explosion).setY(explosionPos.field_1351);
         ((IExplosion)explosion).setZ(explosionPos.field_1350);
         ((IExplosion)explosion).setPower(power);
         if (!new Box(
               (double)MathHelper.method_15357(explosionPos.field_1352 - 11.0),
               (double)MathHelper.method_15357(explosionPos.field_1351 - 11.0),
               (double)MathHelper.method_15357(explosionPos.field_1350 - 11.0),
               (double)MathHelper.method_15357(explosionPos.field_1352 + 13.0),
               (double)MathHelper.method_15357(explosionPos.field_1351 + 13.0),
               (double)MathHelper.method_15357(explosionPos.field_1350 + 13.0)
            )
            .method_994(predict.method_5829())) {
            return 0.0F;
         } else {
            if (!target.method_5659(explosion) && !target.method_5655()) {
               double distExposure = (double)MathHelper.method_15355((float)predict.method_5707(explosionPos)) / 12.0;
               if (distExposure <= 1.0) {
                  double xDiff = predict.method_23317() - explosionPos.field_1352;
                  double yDiff = predict.method_23318() - explosionPos.field_1351;
                  double zDiff = predict.method_23317() - explosionPos.field_1350;
                  double diff = (double)MathHelper.method_15355((float)(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
                  if (diff != 0.0) {
                     double exposure = (double)Explosion.method_17752(explosionPos, predict);
                     double finalExposure = (1.0 - distExposure) * exposure;
                     float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
                     if (mc.field_1687.method_8407() == Difficulty.field_5805) {
                        toDamage = Math.min(toDamage / 2.0F + 1.0F, toDamage);
                     } else if (mc.field_1687.method_8407() == Difficulty.field_5807) {
                        toDamage = toDamage * 3.0F / 2.0F;
                     }

                     toDamage = DamageUtil.method_5496(
                        toDamage,
                        (float)target.method_6096(),
                        (float)((EntityAttributeInstance)Objects.requireNonNull(target.method_5996(EntityAttributes.field_23725))).method_6194()
                     );
                     if (target.method_6059(StatusEffects.field_5907)) {
                        int resistance = 25
                           - (((StatusEffectInstance)Objects.requireNonNull(target.method_6112(StatusEffects.field_5907))).method_5578() + 1) * 5;
                        float resistance_1 = toDamage * (float)resistance;
                        toDamage = Math.max(resistance_1 / 25.0F, 0.0F);
                     }

                     if (toDamage <= 0.0F) {
                        toDamage = 0.0F;
                     } else {
                        int protAmount = EnchantmentHelper.method_8219(target.method_5661(), mc.field_1687.method_48963().method_48807(explosion));
                        if (protAmount > 0) {
                           toDamage = DamageUtil.method_5497(toDamage, (float)protAmount);
                        }
                     }

                     return toDamage;
                  }
               }
            }

            return 0.0F;
         }
      }
   }
}
