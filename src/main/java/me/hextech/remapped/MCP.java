package me.hextech.remapped;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.util.Hand;

public class MCP extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static MCP INSTANCE;
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   boolean click = false;

   public MCP() {
      super("MCP", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         if (mc.field_1729.method_35707()) {
            if (!this.click) {
               if (mc.field_1724.method_6047().method_7909() == Items.field_8634) {
                  EntityUtil.sendLook(new LookAndOnGround(mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
                  mc.field_1724.field_3944.method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
               } else {
                  int pearl;
                  if ((pearl = this.findItem(Items.field_8634)) != -1) {
                     int old = mc.field_1724.method_31548().field_7545;
                     this.doSwap(pearl);
                     EntityUtil.sendLook(new LookAndOnGround(mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
                     mc.field_1724.field_3944.method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
                     if (this.inventory.getValue()) {
                        this.doSwap(pearl);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }
                  }
               }

               this.click = true;
            }
         } else {
            this.click = false;
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   public int findItem(Item item) {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(item) : InventoryUtil.findItem(item);
   }
}
