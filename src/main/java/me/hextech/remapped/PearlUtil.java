package me.hextech.remapped;

import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class PearlUtil
implements Wrapper {
    public static void doPearl(float yaw, float pitch, boolean inv) {
        if (PearlUtil.mc.field_1724.method_6047().method_7909() == Items.field_8634) {
            EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(PearlUtil.mc.field_1724.method_36454(), PearlUtil.mc.field_1724.method_36455(), PearlUtil.mc.field_1724.method_24828()));
            PearlUtil.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
        } else {
            int pearl = PearlUtil.findItem(Items.field_8634, inv);
            if (pearl != -1) {
                int old = PearlUtil.mc.field_1724.method_31548().field_7545;
                PearlUtil.doSwap(pearl, inv);
                EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(PearlUtil.mc.field_1724.method_36454(), PearlUtil.mc.field_1724.method_36455(), PearlUtil.mc.field_1724.method_24828()));
                PearlUtil.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
                if (inv) {
                    PearlUtil.doSwap(pearl, true);
                    EntityUtil.syncInventory();
                } else {
                    PearlUtil.doSwap(old, false);
                }
            }
        }
    }

    private static void doSwap(int slot, boolean inv) {
        if (inv) {
            InventoryUtil.inventorySwap(slot, PearlUtil.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public static int findItem(Item item, boolean inv) {
        if (inv) {
            return InventoryUtil.findItemInventorySlot(item);
        }
        return InventoryUtil.findItem(item);
    }
}
