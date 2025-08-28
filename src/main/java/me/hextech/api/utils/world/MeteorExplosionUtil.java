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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;

public class MeteorExplosionUtil
implements Wrapper {
    public static final Explosion explosion = new Explosion(MeteorExplosionUtil.mc.world, null, 0.0, 0.0, 0.0, 6.0f, false, Explosion.DestructionType.DESTROY);

    public static double crystalDamage(PlayerEntity player, BlockPos pos, PlayerEntity predict) {
        return MeteorExplosionUtil.explosionDamage(player, pos.toCenterPos().add(0.0, -0.5, 0.0), predict, 6.0f);
    }

    public static double crystalDamage(PlayerEntity player, Vec3d pos, PlayerEntity predict) {
        return MeteorExplosionUtil.explosionDamage(player, pos, predict, 6.0f);
    }

    public static double anchorDamage(PlayerEntity player, BlockPos pos, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) {
            CombatUtil.modifyPos = pos;
            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
            double damage = MeteorExplosionUtil.explosionDamage(player, pos.toCenterPos(), predict, 5.0f);
            CombatUtil.modifyPos = null;
            return damage;
        }
        return MeteorExplosionUtil.explosionDamage(player, pos.toCenterPos(), predict, 5.0f);
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
        double damage = (impact * impact + impact) / 2.0 * 7.0 * 10.0 + 1.0;
        damage = MeteorExplosionUtil.getDamageForDifficulty(damage);
        damage = MeteorExplosionUtil.resistanceReduction(player, damage);
        damage = DamageUtil.getDamageLeft((float)damage, (float)player.getArmor(), (float)player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());
        ((IExplosion)explosion).setWorld(MeteorExplosionUtil.mc.world);
        ((IExplosion)explosion).setX(pos.x);
        ((IExplosion)explosion).setY(pos.y);
        ((IExplosion)explosion).setZ(pos.z);
        ((IExplosion)explosion).setPower(power);
        damage = MeteorExplosionUtil.blastProtReduction(player, damage, explosion);
        if (damage < 0.0) {
            damage = 0.0;
        }
        return damage;
    }

    private static double getDamageForDifficulty(double damage) {
        return switch (MeteorExplosionUtil.mc.world.getDifficulty()) {
            case PEACEFUL -> 0.0;
            case EASY -> Math.min(damage / 2.0 + 1.0, damage);
            case NORMAL -> damage * 3.0 / 2.0;
            default -> damage;
        };
    }

    private static double blastProtReduction(Entity player, double damage, Explosion explosion) {
        int protLevel = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), MeteorExplosionUtil.mc.world.getDamageSources().explosion(explosion));
        if (protLevel > 20) {
            protLevel = 20;
        }
        return (damage *= 1.0 - (double)protLevel / 25.0) < 0.0 ? 0.0 : damage;
    }

    private static double resistanceReduction(LivingEntity player, double damage) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int lvl = player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1;
            damage *= 1.0 - (double)lvl * 0.2;
        }
        return damage < 0.0 ? 0.0 : damage;
    }
}
