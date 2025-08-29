package me.hextech.api.utils.combat;

import me.hextech.api.utils.world.*;
import me.hextech.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalDamage_eJITUTNYpCPnjaYYZUHH {
    public static float calculateDamage(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
        return CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateCrystalDamage(new Vec3d((double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5), player, predict);
    }

    public static float calculateCrystalDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTerrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        float damage = 0.0f;
        switch (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.calcMode.getValue()) {
            case Meteor: {
                damage = (float) MeteorExplosionUtil.explosionDamage(player, pos, predict, 6.0f);
                break;
            }
            case Thunder: {
                damage = ThunderExplosionUtil.calculateDamage(pos, player, predict, 6.0f);
                break;
            }
            case OyVey: {
                damage = OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), player, predict, 6.0f);
                break;
            }
            case EditionHex: {
                damage = ExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), player, predict, 6.0f);
                break;
            }
            case Mio: {
                damage = MioExplosionUtil_RZgXAJBAUGNbHlXpyNCY.calculateDamage(pos, player, predict, 6.0f);
            }
        }
        CombatUtil.terrainIgnore = false;
        return damage;
    }
}
