package me.hextech.remapped;

import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.CrystalDamage;
import me.hextech.remapped.ExplosionUtil;
import me.hextech.remapped.MeteorExplosionUtil;
import me.hextech.remapped.MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY;
import me.hextech.remapped.OyveyExplosionUtil;
import me.hextech.remapped.ThunderExplosionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalDamage_eJITUTNYpCPnjaYYZUHH {
    public static float calculateDamage(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
        return CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateCrystalDamage(new Vec3d((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5), player, predict);
    }

    public static float calculateCrystalDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTerrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        float damage = 0.0f;
        switch (CrystalDamage.$SwitchMap$me$hextech$mod$modules$impl$combat$autocrystal$mode$Enum$CalcMode[AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.calcMode.getValue().ordinal()]) {
            case 1: {
                damage = (float)MeteorExplosionUtil.explosionDamage(player, pos, predict, 6.0f);
                break;
            }
            case 2: {
                damage = ThunderExplosionUtil.calculateDamage(pos, player, predict, 6.0f);
                break;
            }
            case 3: {
                damage = OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), (Entity)player, (Entity)predict, 6.0f);
                break;
            }
            case 4: {
                damage = ExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), (Entity)player, (Entity)predict, 6.0f);
                break;
            }
            case 5: {
                damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.calculateDamage(pos, player, predict, 6.0f);
            }
        }
        CombatUtil.terrainIgnore = false;
        return damage;
    }
}
