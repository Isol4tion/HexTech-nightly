package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
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
        if (MCP.mc.field_1729.method_35707()) {
            if (!this.click) {
                if (MCP.mc.field_1724.method_6047().method_7909() == Items.field_8634) {
                    EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(MCP.mc.field_1724.method_36454(), MCP.mc.field_1724.method_36455(), MCP.mc.field_1724.method_24828()));
                    MCP.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
                } else {
                    int pearl = this.findItem(Items.field_8634);
                    if (pearl != -1) {
                        int old = MCP.mc.field_1724.method_31548().field_7545;
                        this.doSwap(pearl);
                        EntityUtil.sendLook((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.LookAndOnGround(MCP.mc.field_1724.method_36454(), MCP.mc.field_1724.method_36455(), MCP.mc.field_1724.method_24828()));
                        MCP.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
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
            InventoryUtil.inventorySwap(slot, MCP.mc.field_1724.method_31548().field_7545);
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
