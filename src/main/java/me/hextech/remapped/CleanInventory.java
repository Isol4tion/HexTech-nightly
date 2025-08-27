package me.hextech.remapped;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class CleanInventory extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final BooleanSetting stack = this.add(new BooleanSetting("Stack", true));
   private final BooleanSetting sort = this.add(new BooleanSetting("Sort", true));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.1, 0.0, 5.0, 0.01).setSuffix("s"));
   private final Timer timer = new Timer();

   public CleanInventory() {
      super("CleanInventory", Module_JlagirAibYQgkHtbRnhw.Player);
   }

   @Override
   public void onUpdate() {
      if (this.timer.passedS(this.delay.getValue())) {
         if (mc.field_1755 == null
            || mc.field_1755 instanceof ChatScreen
            || mc.field_1755 instanceof InventoryScreen
            || mc.field_1755 instanceof ClickGuiScreen
            || mc.field_1755 instanceof GameMenuScreen) {
            if (this.stack.getValue()) {
               for (int slot1 = 9; slot1 < 36; slot1++) {
                  ItemStack stack = mc.field_1724.method_31548().method_5438(slot1);
                  if (!stack.method_7960() && stack.method_7946() && stack.method_7947() != stack.method_7914()) {
                     for (int slot2 = 35; slot2 >= 9; slot2--) {
                        if (slot1 != slot2) {
                           ItemStack stack2 = mc.field_1724.method_31548().method_5438(slot2);
                           if (stack2.method_7947() != stack2.method_7914() && this.canMerge(stack, stack2)) {
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 0, SlotActionType.field_7790, mc.field_1724);
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot2, 0, SlotActionType.field_7790, mc.field_1724);
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 0, SlotActionType.field_7790, mc.field_1724);
                              this.timer.reset();
                              return;
                           }
                        }
                     }
                  }
               }
            }

            if (this.sort.getValue()) {
               for (int slot1x = 9; slot1x < 36; slot1x++) {
                  int id = Item.method_7880(mc.field_1724.method_31548().method_5438(slot1x).method_7909());
                  if (mc.field_1724.method_31548().method_5438(slot1x).method_7960()) {
                     id = 114514;
                  }

                  int minId = this.getMinId(slot1x, id);
                  if (minId < id) {
                     for (int slot2x = 35; slot2x > slot1x; slot2x--) {
                        ItemStack stack = mc.field_1724.method_31548().method_5438(slot2x);
                        if (!stack.method_7960()) {
                           int itemID = Item.method_7880(stack.method_7909());
                           if (itemID == minId) {
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1x, 0, SlotActionType.field_7790, mc.field_1724);
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot2x, 0, SlotActionType.field_7790, mc.field_1724);
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1x, 0, SlotActionType.field_7790, mc.field_1724);
                              this.timer.reset();
                              return;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private int getMinId(int slot, int currentId) {
      int id = currentId;

      for (int slot1 = slot + 1; slot1 < 36; slot1++) {
         ItemStack stack = mc.field_1724.method_31548().method_5438(slot1);
         if (!stack.method_7960()) {
            int itemID = Item.method_7880(stack.method_7909());
            if (itemID < id) {
               id = itemID;
            }
         }
      }

      return id;
   }

   private boolean canMerge(ItemStack source, ItemStack stack) {
      return source.method_7909() == stack.method_7909() && source.method_7964().equals(stack.method_7964());
   }
}
