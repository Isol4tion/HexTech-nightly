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
        if (PearlUtil.mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
            EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(PearlUtil.mc.player.method_36454(), PearlUtil.mc.player.method_36455(), PearlUtil.mc.player.isOnGround()));
            PearlUtil.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
        } else {
            int pearl = PearlUtil.findItem(Items.ENDER_PEARL, inv);
            if (pearl != -1) {
                int old = PearlUtil.mc.player.getInventory().selectedSlot;
                PearlUtil.doSwap(pearl, inv);
                EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(PearlUtil.mc.player.method_36454(), PearlUtil.mc.player.method_36455(), PearlUtil.mc.player.isOnGround()));
                PearlUtil.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
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
            InventoryUtil.inventorySwap(slot, PearlUtil.mc.player.getInventory().selectedSlot);
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
