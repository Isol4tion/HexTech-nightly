package me.hextech.remapped;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.util.Hand;

public class PearlUtil implements Wrapper {
   public static void doPearl(float yaw, float pitch, boolean inv) {
      if (mc.field_1724.method_6047().method_7909() == Items.field_8634) {
         EntityUtil.sendLook(new LookAndOnGround(mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
         mc.field_1724.field_3944.method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
      } else {
         int pearl;
         if ((pearl = findItem(Items.field_8634, inv)) != -1) {
            int old = mc.field_1724.method_31548().field_7545;
            doSwap(pearl, inv);
            EntityUtil.sendLook(new LookAndOnGround(mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
            if (inv) {
               doSwap(pearl, true);
               EntityUtil.syncInventory();
            } else {
               doSwap(old, false);
            }
         }
      }
   }

   private static void doSwap(int slot, boolean inv) {
      if (inv) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   public static int findItem(Item item, boolean inv) {
      return inv ? InventoryUtil.findItemInventorySlot(item) : InventoryUtil.findItem(item);
   }
}
