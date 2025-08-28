package me.hextech.remapped;

import me.hextech.remapped.api.utils.Wrapper;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.mod.modules.impl.combat.BedAura_BzCWaQEhnpenizjBqrRp;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class ListenerDamage
implements Wrapper {
    public static double getDamage(PlayerEntity target) {
        if (!SpeedMine.INSTANCE.obsidian.isPressed() && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.slowPlace.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastBreakTimer.passedMs((long)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.slowDelay.getValue()) && (!BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.isOn() || BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.getBed() == -1)) {
            return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.slowMinDamage.getValue();
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forcePlace.getValue() && (double) EntityUtil.getHealth(target) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceMaxHealth.getValue() && !SpeedMine.INSTANCE.obsidian.isPressed() && !PistonCrystal.INSTANCE.isOn()) {
            return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceMin.getValue();
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.armorBreaker.getValue()) {
            DefaultedList<ItemStack> armors = target.getInventory().armor;
            for (ItemStack armor : armors) {
                if (armor.isEmpty() || (double)EntityUtil.getDamagePercent(armor) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.maxDurable.getValue()) continue;
                return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.armorBreakerDamage.getValue();
            }
        }
        if (PistonCrystal.INSTANCE.isOn()) {
            return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.ingionPiston.getValueFloat();
        }
        return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.minDamage.getValue();
    }
}
