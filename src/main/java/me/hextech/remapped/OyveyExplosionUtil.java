package me.hextech.remapped;

import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.Wrapper;
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
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class OyveyExplosionUtil
implements Wrapper {
    public static float anchorDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) {
            CombatUtil.modifyPos = pos;
            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
            float damage = OyveyExplosionUtil.calculateDamage(pos.toCenterPos().method_10216(), pos.toCenterPos().method_10214(), pos.toCenterPos().method_10215(), (Entity)target, (Entity)predict, 5.0f);
            CombatUtil.modifyPos = null;
            return damage;
        }
        return OyveyExplosionUtil.calculateDamage(pos.toCenterPos().method_10216(), pos.toCenterPos().method_10214(), pos.toCenterPos().method_10215(), (Entity)target, (Entity)predict, 5.0f);
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, Entity predict, float power) {
        if (predict == null) {
            predict = entity;
        }
        float doubleExplosionSize = 2.0f * power;
        double distancedsize = (double)MathHelper.sqrt((float)((float)predict.squaredDistanceTo(posX, posY, posZ))) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = Explosion.method_17752((Vec3d)vec3d, (Entity)predict);
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof LivingEntity) {
            finald = OyveyExplosionUtil.getBlastReduction((LivingEntity)entity, OyveyExplosionUtil.getDamageMultiplied(damage), new Explosion((World)OyveyExplosionUtil.mc.world, entity, posX, posY, posZ, power, false, Explosion.DestructionType.DESTROY));
        }
        return (float)finald;
    }

    public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
        float f = 2.0f + toughnessAttribute / 4.0f;
        float f1 = MathHelper.clamp((float)(totalArmor - damage / f), (float)(totalArmor * 0.2f), (float)20.0f);
        return damage * (1.0f - f1 / 25.0f);
    }

    public static float getBlastReduction(LivingEntity entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            DamageSource ds = OyveyExplosionUtil.mc.world.method_48963().explosion(explosion);
            damage = OyveyExplosionUtil.getDamageAfterAbsorb(damage, player.method_6096(), (float)player.getAttributeValue(EntityAttributes.field_23725));
            int k = EnchantmentHelper.method_8219((Iterable)player.method_5661(), (DamageSource)ds);
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (player.method_6059(StatusEffects.field_5907)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = OyveyExplosionUtil.getDamageAfterAbsorb(damage, entity.getArmor(), (float)entity.getAttributeValue(EntityAttributes.field_23725));
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = OyveyExplosionUtil.mc.world.method_8407().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }
}
