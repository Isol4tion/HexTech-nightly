package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class CleanInventory
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final BooleanSetting stack = this.add(new BooleanSetting("Stack", true));
    private final BooleanSetting sort = this.add(new BooleanSetting("Sort", true));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.1, 0.0, 5.0, 0.01).setSuffix("s"));
    private final Timer timer = new Timer();

    public CleanInventory() {
        super("CleanInventory", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @Override
    public void onUpdate() {
        int slot1;
        if (!this.timer.passedS(this.delay.getValue())) {
            return;
        }
        if (!(CleanInventory.mc.currentScreen == null || CleanInventory.mc.currentScreen instanceof ChatScreen || CleanInventory.mc.currentScreen instanceof InventoryScreen || CleanInventory.mc.currentScreen instanceof ClickGuiScreen || CleanInventory.mc.currentScreen instanceof GameMenuScreen)) {
            return;
        }
        if (this.stack.getValue()) {
            for (slot1 = 9; slot1 < 36; ++slot1) {
                ItemStack stack = CleanInventory.mc.player.getInventory().getStack(slot1);
                if (stack.isEmpty() || !stack.isStackable() || stack.getCount() == stack.getMaxCount()) continue;
                for (int slot2 = 35; slot2 >= 9; --slot2) {
                    ItemStack stack2;
                    if (slot1 == slot2 || (stack2 = CleanInventory.mc.player.getInventory().getStack(slot2)).getCount() == stack2.getMaxCount() || !this.canMerge(stack, stack2)) continue;
                    CleanInventory.mc.interactionManager.clickSlot(CleanInventory.mc.player.playerScreenHandler.field_7763, slot1, 0, SlotActionType.PICKUP, (PlayerEntity)CleanInventory.mc.player);
                    CleanInventory.mc.interactionManager.clickSlot(CleanInventory.mc.player.playerScreenHandler.field_7763, slot2, 0, SlotActionType.PICKUP, (PlayerEntity)CleanInventory.mc.player);
                    CleanInventory.mc.interactionManager.clickSlot(CleanInventory.mc.player.playerScreenHandler.field_7763, slot1, 0, SlotActionType.PICKUP, (PlayerEntity)CleanInventory.mc.player);
                    this.timer.reset();
                    return;
                }
            }
        }
        if (this.sort.getValue()) {
            for (slot1 = 9; slot1 < 36; ++slot1) {
                int minId;
                int id = Item.getRawId((Item)CleanInventory.mc.player.getInventory().getStack(slot1).getItem());
                if (CleanInventory.mc.player.getInventory().getStack(slot1).isEmpty()) {
                    id = 114514;
                }
                if ((minId = this.getMinId(slot1, id)) >= id) continue;
                for (int slot2 = 35; slot2 > slot1; --slot2) {
                    int itemID;
                    ItemStack stack = CleanInventory.mc.player.getInventory().getStack(slot2);
                    if (stack.isEmpty() || (itemID = Item.getRawId((Item)stack.getItem())) != minId) continue;
                    CleanInventory.mc.interactionManager.clickSlot(CleanInventory.mc.player.playerScreenHandler.field_7763, slot1, 0, SlotActionType.PICKUP, (PlayerEntity)CleanInventory.mc.player);
                    CleanInventory.mc.interactionManager.clickSlot(CleanInventory.mc.player.playerScreenHandler.field_7763, slot2, 0, SlotActionType.PICKUP, (PlayerEntity)CleanInventory.mc.player);
                    CleanInventory.mc.interactionManager.clickSlot(CleanInventory.mc.player.playerScreenHandler.field_7763, slot1, 0, SlotActionType.PICKUP, (PlayerEntity)CleanInventory.mc.player);
                    this.timer.reset();
                    return;
                }
            }
        }
    }

    private int getMinId(int slot, int currentId) {
        int id = currentId;
        for (int slot1 = slot + 1; slot1 < 36; ++slot1) {
            int itemID;
            ItemStack stack = CleanInventory.mc.player.getInventory().getStack(slot1);
            if (stack.isEmpty() || (itemID = Item.getRawId((Item)stack.getItem())) >= id) continue;
            id = itemID;
        }
        return id;
    }

    private boolean canMerge(ItemStack source, ItemStack stack) {
        return source.getItem() == stack.getItem() && source.getName().equals((Object)stack.getName());
    }
}
