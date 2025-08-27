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
        if (!(CleanInventory.mc.field_1755 == null || CleanInventory.mc.field_1755 instanceof ChatScreen || CleanInventory.mc.field_1755 instanceof InventoryScreen || CleanInventory.mc.field_1755 instanceof ClickGuiScreen || CleanInventory.mc.field_1755 instanceof GameMenuScreen)) {
            return;
        }
        if (this.stack.getValue()) {
            for (slot1 = 9; slot1 < 36; ++slot1) {
                ItemStack stack = CleanInventory.mc.field_1724.method_31548().method_5438(slot1);
                if (stack.method_7960() || !stack.method_7946() || stack.method_7947() == stack.method_7914()) continue;
                for (int slot2 = 35; slot2 >= 9; --slot2) {
                    ItemStack stack2;
                    if (slot1 == slot2 || (stack2 = CleanInventory.mc.field_1724.method_31548().method_5438(slot2)).method_7947() == stack2.method_7914() || !this.canMerge(stack, stack2)) continue;
                    CleanInventory.mc.field_1761.method_2906(CleanInventory.mc.field_1724.field_7498.field_7763, slot1, 0, SlotActionType.field_7790, (PlayerEntity)CleanInventory.mc.field_1724);
                    CleanInventory.mc.field_1761.method_2906(CleanInventory.mc.field_1724.field_7498.field_7763, slot2, 0, SlotActionType.field_7790, (PlayerEntity)CleanInventory.mc.field_1724);
                    CleanInventory.mc.field_1761.method_2906(CleanInventory.mc.field_1724.field_7498.field_7763, slot1, 0, SlotActionType.field_7790, (PlayerEntity)CleanInventory.mc.field_1724);
                    this.timer.reset();
                    return;
                }
            }
        }
        if (this.sort.getValue()) {
            for (slot1 = 9; slot1 < 36; ++slot1) {
                int minId;
                int id = Item.method_7880((Item)CleanInventory.mc.field_1724.method_31548().method_5438(slot1).method_7909());
                if (CleanInventory.mc.field_1724.method_31548().method_5438(slot1).method_7960()) {
                    id = 114514;
                }
                if ((minId = this.getMinId(slot1, id)) >= id) continue;
                for (int slot2 = 35; slot2 > slot1; --slot2) {
                    int itemID;
                    ItemStack stack = CleanInventory.mc.field_1724.method_31548().method_5438(slot2);
                    if (stack.method_7960() || (itemID = Item.method_7880((Item)stack.method_7909())) != minId) continue;
                    CleanInventory.mc.field_1761.method_2906(CleanInventory.mc.field_1724.field_7498.field_7763, slot1, 0, SlotActionType.field_7790, (PlayerEntity)CleanInventory.mc.field_1724);
                    CleanInventory.mc.field_1761.method_2906(CleanInventory.mc.field_1724.field_7498.field_7763, slot2, 0, SlotActionType.field_7790, (PlayerEntity)CleanInventory.mc.field_1724);
                    CleanInventory.mc.field_1761.method_2906(CleanInventory.mc.field_1724.field_7498.field_7763, slot1, 0, SlotActionType.field_7790, (PlayerEntity)CleanInventory.mc.field_1724);
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
            ItemStack stack = CleanInventory.mc.field_1724.method_31548().method_5438(slot1);
            if (stack.method_7960() || (itemID = Item.method_7880((Item)stack.method_7909())) >= id) continue;
            id = itemID;
        }
        return id;
    }

    private boolean canMerge(ItemStack source, ItemStack stack) {
        return source.method_7909() == stack.method_7909() && source.method_7964().equals((Object)stack.method_7964());
    }
}
