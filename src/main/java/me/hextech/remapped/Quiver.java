package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.RotateEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class Quiver
extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
        if (Quiver.nullCheck()) {
            return;
        }
        oldSlot = Quiver.mc.player.method_31548().field_7545;
        slot = this.findItem(Items.field_8102);
        if (slot == -1) {
            return;
        }
        this.doSwap(slot);
        Quiver.mc.field_1690.field_1904.method_23481(true);
        Quiver.mc.field_1761.method_2919((PlayerEntity)Quiver.mc.player, Hand.field_5808);
    }

    @Override
    public void onDisable() {
        if (Quiver.mc.field_1690.field_1904.method_1434()) {
            Quiver.mc.field_1690.field_1904.method_23481(false);
            Quiver.mc.field_1761.method_2897((PlayerEntity)Quiver.mc.player);
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
        if ((double)BowItem.method_7722((int)Quiver.mc.player.method_6048()) >= 0.13) {
            Quiver.mc.field_1690.field_1904.method_23481(false);
            Quiver.mc.field_1761.method_2897((PlayerEntity)Quiver.mc.player);
            if (this.inventory.getValue()) {
                this.doSwap(slot);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(oldSlot);
            }
            this.disable();
        }
    }

    @EventHandler(priority=-101)
    public void onRotate(RotateEvent event) {
        if (Quiver.mc.player.method_6115() && Quiver.mc.player.method_6030().method_7909() instanceof BowItem) {
            event.setPitch(-90.0f);
        }
    }

    public int findItem(Item itemIn) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findItemInventorySlot(itemIn);
        }
        return InventoryUtil.findItem(itemIn);
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, Quiver.mc.player.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }
}
