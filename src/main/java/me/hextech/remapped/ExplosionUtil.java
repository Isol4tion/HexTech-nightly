package me.hextech.remapped;

import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

public class ExplosionUtil
implements Wrapper {
    public static float anchorDamage(BlockPos pos, PlayerEntity target, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) {
            CombatUtil.modifyPos = pos;
            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
            float damage = ExplosionUtil.calculateDamage(pos.toCenterPos().getX(), pos.toCenterPos().getY(), pos.toCenterPos().getZ(), (Entity)target, (Entity)predict, 5.0f);
            CombatUtil.modifyPos = null;
            return damage;
        }
        return ExplosionUtil.calculateDamage(pos.toCenterPos().getX(), pos.toCenterPos().getY(), pos.toCenterPos().getZ(), (Entity)target, (Entity)predict, 5.0f);
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, Entity predict, float power) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            if (player.getAbilities().creativeMode) {
                return 0.0f;
            }
        }
        if (predict == null) {
            predict = entity;
        }
        float doubleExplosionSize = 2.0f * power;
        double distancedsize = (double)MathHelper.sqrt((float)((float)predict.squaredDistanceTo(posX, posY, posZ))) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = ExplosionUtil.getExposure(vec3d, predict);
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = ExplosionUtil.getBlastReduction((LivingEntity)entity, ExplosionUtil.getDamageMultiplied(damage));
        return (float)finald;
    }

    public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
        float f = 2.0f + toughnessAttribute / 4.0f;
        float f1 = MathHelper.clamp((float)(totalArmor - damage / f), (float)(totalArmor * 0.2f), (float)20.0f);
        return damage * (1.0f - f1 / 25.0f);
    }

    public static float getBlastReduction(LivingEntity entity, float damageI) {
        float damage = damageI;
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            damage = ExplosionUtil.getDamageAfterAbsorb(damage, player.method_6096(), (float)player.getAttributeValue(EntityAttributes.field_23725));
            int k = ExplosionUtil.getProtectionAmount(player.method_5661());
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (player.method_6059(StatusEffects.field_5907)) {
                damage -= damage / 4.0f;
            }
            return Math.max(damage, 0.0f);
        }
        damage = ExplosionUtil.getDamageAfterAbsorb(damage, entity.getArmor(), (float)entity.getAttributeValue(EntityAttributes.field_23725));
        return Math.max(damage, 0.0f);
    }

    public static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        int miss = 0;
        int hit = 0;
        for (int k = 0; k <= 1; ++k) {
            for (int l = 0; l <= 1; ++l) {
                for (int m = 0; m <= 1; ++m) {
                    double p;
                    double o;
                    double n = MathHelper.lerp((double)k, (double)box.minX, (double)box.maxX);
                    Vec3d vec3d = new Vec3d(n, o = MathHelper.lerp((double)l, (double)box.minY, (double)box.maxY), p = MathHelper.lerp((double)m, (double)box.minZ, (double)box.maxZ));
                    if (ExplosionUtil.raycast(vec3d, source) == HitResult.Type.MISS) {
                        ++miss;
                    }
                    ++hit;
                }
            }
        }
        return (float)miss / (float)hit;
    }

    private static HitResult.Type raycast(Vec3d start, Vec3d end) {
        return (HitResult.Type)BlockView.raycast((Vec3d)start, (Vec3d)end, null, (_null, blockPos) -> {
            BlockState blockState = ExplosionUtil.mc.world.getBlockState(blockPos);
            if (blockState.getBlock().getBlastResistance() < 600.0f) {
                return null;
            }
            BlockHitResult hitResult = blockState.method_26220((BlockView)ExplosionUtil.mc.world, blockPos).raycast(start, end, blockPos);
            return hitResult == null ? null : hitResult.method_17783();
        }, _null -> HitResult.Type.MISS);
    }

    public static int getProtectionAmount(Iterable<ItemStack> armorItems) {
        int value = 0;
        for (ItemStack itemStack : armorItems) {
            int level = EnchantmentHelper.method_8225((Enchantment)Enchantments.field_9111, (ItemStack)itemStack);
            if (level == 0) {
                value += EnchantmentHelper.method_8225((Enchantment)Enchantments.field_9107, (ItemStack)itemStack) * 2;
                continue;
            }
            value += level;
        }
        return value;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = ExplosionUtil.mc.world.method_8407().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(BlockPos pos, Entity target) {
        return ExplosionUtil.calculateDamage((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, target, target, 6.0f);
    }
}
