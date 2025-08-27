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
        oldSlot = Quiver.mc.player.method_31548().selectedSlot;
        slot = this.findItem(Items.BOW);
        if (slot == -1) {
            return;
        }
        this.doSwap(slot);
        Quiver.mc.options.useKey.setPressed(true);
        Quiver.mc.interactionManager.interactItem((PlayerEntity)Quiver.mc.player, Hand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        if (Quiver.mc.options.useKey.isPressed()) {
            Quiver.mc.options.useKey.setPressed(false);
            Quiver.mc.interactionManager.stopUsingItem((PlayerEntity)Quiver.mc.player);
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
        if ((double)BowItem.getPullProgress((int)Quiver.mc.player.method_6048()) >= 0.13) {
            Quiver.mc.options.useKey.setPressed(false);
            Quiver.mc.interactionManager.stopUsingItem((PlayerEntity)Quiver.mc.player);
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
        if (Quiver.mc.player.method_6115() && Quiver.mc.player.method_6030().getItem() instanceof BowItem) {
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
            InventoryUtil.inventorySwap(slot, Quiver.mc.player.method_31548().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }
}
