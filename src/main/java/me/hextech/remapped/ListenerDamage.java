package me.hextech.remapped;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ListenerDamage implements Wrapper {
   public static double getDamage(PlayerEntity target) {
      if (SpeedMine.INSTANCE.obsidian.isPressed()
         || !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.slowPlace.getValue()
         || !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastBreakTimer.passedMs((long)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.slowDelay.getValue())
         || BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.isOn() && BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.getBed() != -1) {
         if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forcePlace.getValue()
            && (double)EntityUtil.getHealth(target) <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceMaxHealth.getValue()
            && !SpeedMine.INSTANCE.obsidian.isPressed()
            && !PistonCrystal.INSTANCE.isOn()) {
            return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceMin.getValue();
         } else {
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.armorBreaker.getValue()) {
               for (ItemStack armor : target.method_31548().field_7548) {
                  if (!armor.method_7960() && !((double)EntityUtil.getDamagePercent(armor) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.maxDurable.getValue())) {
                     return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.armorBreakerDamage.getValue();
                  }
               }
            }

            return PistonCrystal.INSTANCE.isOn()
               ? (double)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.ingionPiston.getValueFloat()
               : AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.minDamage.getValue();
         }
      } else {
         return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.slowMinDamage.getValue();
      }
   }
}
