package me.hextech.remapped;

import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class Quiver extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Quiver INSTANCE;
   private static int slot;
   private static int oldSlot;
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));

   public Quiver() {
      super("Quiver", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onEnable() {
      if (!nullCheck()) {
         oldSlot = mc.field_1724.method_31548().field_7545;
         slot = this.findItem(Items.field_8102);
         if (slot != -1) {
            this.doSwap(slot);
            mc.field_1690.field_1904.method_23481(true);
            mc.field_1761.method_2919(mc.field_1724, Hand.field_5808);
         }
      }
   }

   @Override
   public void onDisable() {
      if (mc.field_1690.field_1904.method_1434()) {
         mc.field_1690.field_1904.method_23481(false);
         mc.field_1761.method_2897(mc.field_1724);
         if (this.inventory.getValue()) {
            this.doSwap(slot);
            EntityUtil.syncInventory();
            if (this.inventory.getValue()) {
               this.doSwap(oldSlot);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(oldSlot);
            }
         }
      }
   }

   @Override
   public void onUpdate() {
      if ((double)BowItem.method_7722(mc.field_1724.method_6048()) >= 0.13) {
         mc.field_1690.field_1904.method_23481(false);
         mc.field_1761.method_2897(mc.field_1724);
         if (this.inventory.getValue()) {
            this.doSwap(slot);
            EntityUtil.syncInventory();
         } else {
            this.doSwap(oldSlot);
         }

         this.disable();
      }
   }

   @EventHandler(
      priority = -101
   )
   public void onRotate(RotateEvent event) {
      if (mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909() instanceof BowItem) {
         event.setPitch(-90.0F);
      }
   }

   public int findItem(Item itemIn) {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(itemIn) : InventoryUtil.findItem(itemIn);
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }
}
