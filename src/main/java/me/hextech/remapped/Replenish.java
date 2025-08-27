package me.hextech.remapped;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class Replenish extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 2.0, 0.0, 5.0, 0.01).setSuffix("s"));
   private final SliderSetting min = this.add(new SliderSetting("Min", 50, 1, 64));
   private final SliderSetting forceDelay = this.add(new SliderSetting("ForceDelay", 0.2, 0.0, 4.0, 0.01).setSuffix("s"));
   private final SliderSetting forceMin = this.add(new SliderSetting("ForceMin", 16, 1, 64));
   private final Timer timer = new Timer();

   public Replenish() {
      super("Replenish", Module_JlagirAibYQgkHtbRnhw.Player);
   }

   @Override
   public void onUpdate() {
      for (int i = 0; i < 9; i++) {
         if (this.replenish(i)) {
            this.timer.reset();
            return;
         }
      }
   }

   private boolean replenish(int slot) {
      ItemStack stack = mc.field_1724.method_31548().method_5438(slot);
      if (stack.method_7960()) {
         return false;
      } else if (!stack.method_7946()) {
         return false;
      } else if ((double)stack.method_7947() > this.min.getValue()) {
         return false;
      } else if (stack.method_7947() == stack.method_7914()) {
         return false;
      } else {
         for (int i = 9; i < 36; i++) {
            ItemStack item = mc.field_1724.method_31548().method_5438(i);
            if (!item.method_7960() && this.canMerge(stack, item)) {
               if ((float)stack.method_7947() > this.forceMin.getValueFloat()) {
                  if (!this.timer.passedS(this.delay.getValue())) {
                     return false;
                  }
               } else if (!this.timer.passedS(this.forceDelay.getValue())) {
                  return false;
               }

               mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, i, 0, SlotActionType.field_7794, mc.field_1724);
               return true;
            }
         }

         return false;
      }
   }

   private boolean canMerge(ItemStack source, ItemStack stack) {
      return source.method_7909() == stack.method_7909() && source.method_7964().equals(stack.method_7964());
   }
}
