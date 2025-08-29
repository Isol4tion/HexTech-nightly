package me.hextech.api.utils.world;

import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.asm.accessors.IExplosion;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;

public class MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY
        implements Wrapper {
    public static final Explosion explosion = new Explosion(MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world, null, 0.0, 0.0, 0.0, 6.0f, false, Explosion.DestructionType.DESTROY);

    public static double anchorDamage(PlayerEntity player, BlockPos pos, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) {
            CombatUtil.modifyPos = pos;
            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
            double damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.explosionDamage(player, pos.toCenterPos(), predict, 5.0000052f);
            CombatUtil.modifyPos = null;
            return damage;
        }
        return MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.explosionDamage(player, pos.toCenterPos(), predict, 5.0000052f);
    }

    public static double explosionDamage(PlayerEntity player, Vec3d pos, PlayerEntity predict, float power) {
        double modDistance;
        if (player != null && player.getAbilities().creativeMode) {
            return 0.0;
        }
        if (predict == null) {
            predict = player;
        }
        if ((modDistance = Math.sqrt(predict.squaredDistanceTo(pos))) > 10.0) {
            return 0.0;
        }
        double exposure = Explosion.getExposure(pos, predict);
        double impact = (1.0 - modDistance / 10.0) * exposure;
        double damage = (impact * impact + impact) / 2.0 * 7.0 * 10.0 + 1.0000004;
        damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.getDamageForDifficulty(damage);
        if (player != null) {
            damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.resistanceReduction(player, damage);
        }
        if (player != null) {
            damage = DamageUtil.getDamageLeft((float) damage, (float) player.getArmor(), (float) Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue());
        }
        ((IExplosion) explosion).setWorld(MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world);
        ((IExplosion) explosion).setX(pos.x);
        ((IExplosion) explosion).setY(pos.y);
        ((IExplosion) explosion).setZ(pos.z);
        ((IExplosion) explosion).setPower(power);
        if (player != null) {
            damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.blastProtReduction(player, damage, explosion);
        }
        if (damage < 0.0) {
            damage = 0.0;
        }
        return damage;
    }

    public static double getDamageForDifficulty(double damage) {
        if (MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world != null) {
            return switch (mc.world.getDifficulty()) {
                case PEACEFUL -> 0.0;
                case EASY -> Math.min(damage / 2.0 + 1.0000004, damage);
                case NORMAL -> damage * 3.0 / 2.0;
                default -> damage;
            };
        }
        return damage;
    }

    public static double blastProtReduction(Entity player, double damage, Explosion explosion) {
        int protLevel = 0;
        if (MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world != null) {
            protLevel = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world.getDamageSources().explosion(explosion));
        }
        if (protLevel > 20) {
            protLevel = 20;
        }
        return (damage *= 1.0 - (double) protLevel / 25.0025488497) < 0.0 ? 0.0 : damage;
    }

    public static double resistanceReduction(LivingEntity player, double damage) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int lvl = Objects.requireNonNull(player.getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1;
            damage *= 1.0 - (double) lvl * 0.200021;
        }
        return damage < 0.0 ? 0.0 : damage;
    }

    public static float calculateDamage(Vec3d explosionPos, PlayerEntity target, PlayerEntity predict, float power) {
        double zDiff;
        double yDiff;
        double xDiff;
        double diff;
        double distExposure;
        if (MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world != null && MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world.getDifficulty() == Difficulty.PEACEFUL) {
            return 0.0f;
        }
        if (target.getAbilities().creativeMode) {
            return 0.0f;
        }
        if (predict == null) {
            predict = target;
        }
        ((IExplosion) explosion).setWorld(MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world);
        ((IExplosion) explosion).setX(explosionPos.x);
        ((IExplosion) explosion).setY(explosionPos.y);
        ((IExplosion) explosion).setZ(explosionPos.z);
        ((IExplosion) explosion).setPower(power);
        if (!new Box(MathHelper.floor(explosionPos.x - 11.0), MathHelper.floor(explosionPos.y - 11.0), MathHelper.floor(explosionPos.z - 11.0), MathHelper.floor(explosionPos.x + 13.0), MathHelper.floor(explosionPos.y + 13.0), MathHelper.floor(explosionPos.z + 13.0)).intersects(predict.getBoundingBox())) {
            return 0.0f;
        }
        if (!target.isImmuneToExplosion(explosion) && !target.isInvulnerable() && (distExposure = (double) MathHelper.sqrt((float) predict.squaredDistanceTo(explosionPos)) / 12.0) <= 1.0 && (diff = MathHelper.sqrt((float) ((xDiff = predict.getX() - explosionPos.x) * xDiff + (yDiff = predict.getY() - explosionPos.y) * yDiff + (zDiff = predict.getX() - explosionPos.z) * zDiff))) != 0.0) {
            double exposure = Explosion.getExposure(explosionPos, predict);
            double finalExposure = (1.0 - distExposure) * exposure;
            float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
            if (MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world.getDifficulty() == Difficulty.EASY) {
                toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
            } else if (MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world.getDifficulty() == Difficulty.HARD) {
                toDamage = toDamage * 3.0f / 2.0f;
            }
            toDamage = DamageUtil.getDamageLeft(toDamage, (float) target.getArmor(), (float) Objects.requireNonNull(target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue());
            if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
                int resistance = 25 - (Objects.requireNonNull(target.getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1) * 5;
                float resistance_1 = toDamage * (float) resistance;
                toDamage = Math.max(resistance_1 / 25.0f, 0.0f);
            }
            if (toDamage <= 0.0f) {
                toDamage = 0.0f;
            } else {
                int protAmount = EnchantmentHelper.getProtectionAmount(target.getArmorItems(), MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.mc.world.getDamageSources().explosion(explosion));
                if (protAmount > 0) {
                    toDamage = DamageUtil.getInflictedDamage(toDamage, (float) protAmount);
                }
            }
            return toDamage;
        }
        return 0.0f;
    }
}
