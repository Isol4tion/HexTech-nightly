package me.hextech.remapped;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class MCP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static MCP INSTANCE;
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    boolean click = false;

    public MCP() {
        super("MCP", Module_JlagirAibYQgkHtbRnhw.Misc);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (MCP.nullCheck()) {
            return;
        }
        if (MCP.mc.mouse.wasMiddleButtonClicked()) {
            if (!this.click) {
                if (MCP.mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                    EntityUtil.sendLook(new PlayerMoveC2SPacket.LookAndOnGround(MCP.mc.player.getYaw(), MCP.mc.player.getPitch(), MCP.mc.player.isOnGround()));
                    MCP.mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
                } else {
                    int pearl = this.findItem(Items.ENDER_PEARL);
                    if (pearl != -1) {
                        int old = MCP.mc.player.getInventory().selectedSlot;
                        this.doSwap(pearl);
                        EntityUtil.sendLook(new PlayerMoveC2SPacket.LookAndOnGround(MCP.mc.player.getYaw(), MCP.mc.player.getPitch(), MCP.mc.player.isOnGround()));
                        MCP.mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
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

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, MCP.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public int findItem(Item item) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findItemInventorySlot(item);
        }
        return InventoryUtil.findItem(item);
    }
}
