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
    public static final Explosion explosion = new Explosion((World)ThunderExplosionUtil.mc.world, null, 0.0, 0.0, 0.0, 6.0f, false, Explosion.DestructionType.DESTROY);

    public static float anchorDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) {
            CombatUtil.modifyPos = pos;
            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
            float damage = ThunderExplosionUtil.calculateDamage(pos.toCenterPos(), target, predict, 5.0f);
            CombatUtil.modifyPos = null;
            return damage;
        }
        return ThunderExplosionUtil.calculateDamage(pos.toCenterPos(), target, predict, 5.0f);
    }

    public static float calculateDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict, float power) {
        return ThunderExplosionUtil.calculateDamage(pos.toCenterPos().add(0.0, -0.5, 0.0), target, predict, power);
    }

    public static float calculateDamage(Vec3d explosionPos, PlayerEntity target, PlayerEntity predict, float power) {
        double zDiff;
        double yDiff;
        double xDiff;
        double diff;
        double distExposure;
        if (ThunderExplosionUtil.mc.world.getDifficulty() == Difficulty.PEACEFUL) {
            return 0.0f;
        }
        if (target.getAbilities().creativeMode) {
            return 0.0f;
        }
        if (predict == null) {
            predict = target;
        }
        ((IExplosion)explosion).setWorld((World)ThunderExplosionUtil.mc.world);
        ((IExplosion)explosion).setX(explosionPos.x);
        ((IExplosion)explosion).setY(explosionPos.y);
        ((IExplosion)explosion).setZ(explosionPos.z);
        ((IExplosion)explosion).setPower(power);
        if (!new Box((double)MathHelper.floor((double)(explosionPos.x - 11.0)), (double)MathHelper.floor((double)(explosionPos.y - 11.0)), (double)MathHelper.floor((double)(explosionPos.z - 11.0)), (double)MathHelper.floor((double)(explosionPos.x + 13.0)), (double)MathHelper.floor((double)(explosionPos.y + 13.0)), (double)MathHelper.floor((double)(explosionPos.z + 13.0))).intersects(predict.getBoundingBox())) {
            return 0.0f;
        }
        if (!target.isImmuneToExplosion(explosion) && !target.isInvulnerable() && (distExposure = (double)MathHelper.sqrt((float)((float)predict.squaredDistanceTo(explosionPos))) / 12.0) <= 1.0 && (diff = (double)MathHelper.sqrt((float)((float)((xDiff = predict.getX() - explosionPos.x) * xDiff + (yDiff = predict.getY() - explosionPos.y) * yDiff + (zDiff = predict.getX() - explosionPos.z) * zDiff)))) != 0.0) {
            double exposure = Explosion.getExposure((Vec3d)explosionPos, (Entity)predict);
            double finalExposure = (1.0 - distExposure) * exposure;
            float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
            if (ThunderExplosionUtil.mc.world.getDifficulty() == Difficulty.EASY) {
                toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
            } else if (ThunderExplosionUtil.mc.world.getDifficulty() == Difficulty.HARD) {
                toDamage = toDamage * 3.0f / 2.0f;
            }
            toDamage = DamageUtil.getDamageLeft((float)toDamage, (float)target.getArmor(), (float)((float)Objects.requireNonNull(target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue()));
            if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
                int resistance = 25 - (Objects.requireNonNull(target.getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1) * 5;
                float resistance_1 = toDamage * (float)resistance;
                toDamage = Math.max(resistance_1 / 25.0f, 0.0f);
            }
            if (toDamage <= 0.0f) {
                toDamage = 0.0f;
            } else {
                int protAmount = EnchantmentHelper.getProtectionAmount((Iterable)target.getArmorItems(), (DamageSource)ThunderExplosionUtil.mc.world.getDamageSources().explosion(explosion));
                if (protAmount > 0) {
                    toDamage = DamageUtil.getInflictedDamage((float)toDamage, (float)protAmount);
                }
            }
            return toDamage;
        }
        return 0.0f;
    }
}
