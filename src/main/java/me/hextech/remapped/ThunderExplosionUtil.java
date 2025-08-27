package me.hextech.remapped;

import java.util.Objects;
import me.hextech.asm.accessors.IExplosion;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ThunderExplosionUtil
implements Wrapper {
    public static final Explosion explosion = new Explosion((World)ThunderExplosionUtil.mc.field_1687, null, 0.0, 0.0, 0.0, 6.0f, false, Explosion.DestructionType.field_18687);

    public static float anchorDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.field_23152) {
            CombatUtil.modifyPos = pos;
            CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
            float damage = ThunderExplosionUtil.calculateDamage(pos.method_46558(), target, predict, 5.0f);
            CombatUtil.modifyPos = null;
            return damage;
        }
        return ThunderExplosionUtil.calculateDamage(pos.method_46558(), target, predict, 5.0f);
    }

    public static float calculateDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict, float power) {
        return ThunderExplosionUtil.calculateDamage(pos.method_46558().method_1031(0.0, -0.5, 0.0), target, predict, power);
    }

    public static float calculateDamage(Vec3d explosionPos, PlayerEntity target, PlayerEntity predict, float power) {
        double zDiff;
        double yDiff;
        double xDiff;
        double diff;
        double distExposure;
        if (ThunderExplosionUtil.mc.field_1687.method_8407() == Difficulty.field_5801) {
            return 0.0f;
        }
        if (target.method_31549().field_7477) {
            return 0.0f;
        }
        if (predict == null) {
            predict = target;
        }
        ((IExplosion)explosion).setWorld((World)ThunderExplosionUtil.mc.field_1687);
        ((IExplosion)explosion).setX(explosionPos.field_1352);
        ((IExplosion)explosion).setY(explosionPos.field_1351);
        ((IExplosion)explosion).setZ(explosionPos.field_1350);
        ((IExplosion)explosion).setPower(power);
        if (!new Box((double)MathHelper.method_15357((double)(explosionPos.field_1352 - 11.0)), (double)MathHelper.method_15357((double)(explosionPos.field_1351 - 11.0)), (double)MathHelper.method_15357((double)(explosionPos.field_1350 - 11.0)), (double)MathHelper.method_15357((double)(explosionPos.field_1352 + 13.0)), (double)MathHelper.method_15357((double)(explosionPos.field_1351 + 13.0)), (double)MathHelper.method_15357((double)(explosionPos.field_1350 + 13.0))).method_994(predict.method_5829())) {
            return 0.0f;
        }
        if (!target.method_5659(explosion) && !target.method_5655() && (distExposure = (double)MathHelper.method_15355((float)((float)predict.method_5707(explosionPos))) / 12.0) <= 1.0 && (diff = (double)MathHelper.method_15355((float)((float)((xDiff = predict.method_23317() - explosionPos.field_1352) * xDiff + (yDiff = predict.method_23318() - explosionPos.field_1351) * yDiff + (zDiff = predict.method_23317() - explosionPos.field_1350) * zDiff)))) != 0.0) {
            double exposure = Explosion.method_17752((Vec3d)explosionPos, (Entity)predict);
            double finalExposure = (1.0 - distExposure) * exposure;
            float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
            if (ThunderExplosionUtil.mc.field_1687.method_8407() == Difficulty.field_5805) {
                toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
            } else if (ThunderExplosionUtil.mc.field_1687.method_8407() == Difficulty.field_5807) {
                toDamage = toDamage * 3.0f / 2.0f;
            }
            toDamage = DamageUtil.method_5496((float)toDamage, (float)target.method_6096(), (float)((float)Objects.requireNonNull(target.method_5996(EntityAttributes.field_23725)).method_6194()));
            if (target.method_6059(StatusEffects.field_5907)) {
                int resistance = 25 - (Objects.requireNonNull(target.method_6112(StatusEffects.field_5907)).method_5578() + 1) * 5;
                float resistance_1 = toDamage * (float)resistance;
                toDamage = Math.max(resistance_1 / 25.0f, 0.0f);
            }
            if (toDamage <= 0.0f) {
                toDamage = 0.0f;
            } else {
                int protAmount = EnchantmentHelper.method_8219((Iterable)target.method_5661(), (DamageSource)ThunderExplosionUtil.mc.field_1687.method_48963().method_48807(explosion));
                if (protAmount > 0) {
                    toDamage = DamageUtil.method_5497((float)toDamage, (float)protAmount);
                }
            }
            return toDamage;
        }
        return 0.0f;
    }
}
