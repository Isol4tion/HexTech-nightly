package me.hextech.remapped;

import me.hextech.asm.accessors.IExplosion;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.MeteorExplosionUtil_zRIRZRqrriuJQOCbEfNs;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class MeteorExplosionUtil
implements Wrapper {
    public static final Explosion explosion = new Explosion((World)MeteorExplosionUtil.mc.world, null, 0.0, 0.0, 0.0, 6.0f, false, Explosion.DestructionType.DESTROY);

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
        if ((modDistance = Math.sqrt(predict.method_5707(pos))) > 10.0) {
            return 0.0;
        }
        double exposure = Explosion.method_17752((Vec3d)pos, (Entity)predict);
        double impact = (1.0 - modDistance / 10.0) * exposure;
        double damage = (impact * impact + impact) / 2.0 * 7.0 * 10.0 + 1.0;
        damage = MeteorExplosionUtil.getDamageForDifficulty(damage);
        damage = MeteorExplosionUtil.resistanceReduction((LivingEntity)player, damage);
        damage = DamageUtil.method_5496((float)((float)damage), (float)player.method_6096(), (float)((float)player.method_5996(EntityAttributes.field_23725).getValue()));
        ((IExplosion)explosion).setWorld((World)MeteorExplosionUtil.mc.world);
        ((IExplosion)explosion).setX(pos.x);
        ((IExplosion)explosion).setY(pos.y);
        ((IExplosion)explosion).setZ(pos.z);
        ((IExplosion)explosion).setPower(power);
        damage = MeteorExplosionUtil.blastProtReduction((Entity)player, damage, explosion);
        if (damage < 0.0) {
            damage = 0.0;
        }
        return damage;
    }

    private static double getDamageForDifficulty(double damage) {
        return switch (MeteorExplosionUtil_zRIRZRqrriuJQOCbEfNs.$SwitchMap$net$minecraft$world$Difficulty[MeteorExplosionUtil.mc.world.method_8407().ordinal()]) {
            case 1 -> 0.0;
            case 2 -> Math.min(damage / 2.0 + 1.0, damage);
            case 3 -> damage * 3.0 / 2.0;
            default -> damage;
        };
    }

    private static double blastProtReduction(Entity player, double damage, Explosion explosion) {
        int protLevel = EnchantmentHelper.method_8219((Iterable)player.method_5661(), (DamageSource)MeteorExplosionUtil.mc.world.method_48963().explosion(explosion));
        if (protLevel > 20) {
            protLevel = 20;
        }
        return (damage *= 1.0 - (double)protLevel / 25.0) < 0.0 ? 0.0 : damage;
    }

    private static double resistanceReduction(LivingEntity player, double damage) {
        if (player.method_6059(StatusEffects.field_5907)) {
            int lvl = player.method_6112(StatusEffects.field_5907).getAmplifier() + 1;
            damage *= 1.0 - (double)lvl * 0.2;
        }
        return damage < 0.0 ? 0.0 : damage;
    }
}
