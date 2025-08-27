package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtil
implements Wrapper {
    static int lastSlot;
    static int lastSelect;

    public static void inventorySwap(int slot, int selectedSlot) {
        if (slot == lastSlot) {
            InventoryUtil.switchToSlot(lastSelect);
            lastSlot = -1;
            lastSelect = -1;
            return;
        }
        if (slot - 36 == selectedSlot) {
            return;
        }
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.invSwapBypass.getValue()) {
            if (slot - 36 >= 0) {
                lastSlot = slot;
                lastSelect = selectedSlot;
                InventoryUtil.switchToSlot(slot - 36);
                return;
            }
            mc.method_1562().method_52787((Packet)new PickFromInventoryC2SPacket(slot));
            return;
        }
        InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, slot, selectedSlot, SlotActionType.field_7791, (PlayerEntity)InventoryUtil.mc.field_1724);
    }

    public static void doSwap(int slot) {
        InventoryUtil.inventorySwap(slot, InventoryUtil.mc.field_1724.method_31548().field_7545);
        InventoryUtil.switchToSlot(slot);
    }

    public static void switchToSlot(int slot) {
        InventoryUtil.mc.field_1724.method_31548().field_7545 = slot;
        mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(slot));
    }

    public static boolean holdingItem(Class clazz) {
        ItemStack stack = InventoryUtil.mc.field_1724.method_6047();
        boolean result = InventoryUtil.isInstanceOf(stack, clazz);
        if (!result) {
            result = InventoryUtil.isInstanceOf(stack, clazz);
        }
        return result;
    }

    public static boolean isInstanceOf(ItemStack stack, Class clazz) {
        if (stack == null) {
            return false;
        }
        Item item = stack.method_7909();
        if (clazz.isInstance(item)) {
            return true;
        }
        if (item instanceof BlockItem) {
            Block block = Block.method_9503((Item)item);
            return clazz.isInstance(block);
        }
        return false;
    }

    public static ItemStack getStackInSlot(int i) {
        return InventoryUtil.mc.field_1724.method_31548().method_5438(i);
    }

    public static int findItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = InventoryUtil.getStackInSlot(i).method_7909();
            if (Item.method_7880((Item)item) != Item.method_7880((Item)input)) continue;
            return i;
        }
        return -1;
    }

    public static int getItemCount(Item item) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().method_7909() != item) continue;
            count += entry.getValue().method_7947();
        }
        return count;
    }

    public static int getEmptySlotCount() {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getNoArmorInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue() != ItemStack.field_8037) continue;
            ++count;
        }
        return count;
    }

    public static int findClass(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack == ItemStack.field_8037) continue;
            if (clazz.isInstance(stack.method_7909())) {
                return i;
            }
            if (!(stack.method_7909() instanceof BlockItem) || !clazz.isInstance(((BlockItem)stack.method_7909()).method_7711())) continue;
            return i;
        }
        return -1;
    }

    public static int getClassCount(Class clazz) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue() == ItemStack.field_8037) continue;
            if (clazz.isInstance(entry.getValue().method_7909())) {
                count += entry.getValue().method_7947();
            }
            if (!(entry.getValue().method_7909() instanceof BlockItem) || !clazz.isInstance(((BlockItem)entry.getValue().method_7909()).method_7711())) continue;
            count += entry.getValue().method_7947();
        }
        return count;
    }

    public static int findClassInventorySlot(Class clazz) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack == ItemStack.field_8037) continue;
            if (clazz.isInstance(stack.method_7909())) {
                return i < 9 ? i + 36 : i;
            }
            if (!(stack.method_7909() instanceof BlockItem) || !clazz.isInstance(((BlockItem)stack.method_7909()).method_7711())) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack == ItemStack.field_8037 || !(stack.method_7909() instanceof BlockItem) || ((BlockItem)stack.method_7909()).method_7711() != blockIn) continue;
            return i;
        }
        return -1;
    }

    public static int getPotCount(StatusEffect potion) {
        int count = 0;
        block0: for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().method_7909() instanceof SplashPotionItem)) continue;
            ArrayList effects = new ArrayList(PotionUtil.method_8067((ItemStack)entry.getValue()));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.method_5579() != potion) continue;
                count += entry.getValue().method_7947();
                continue block0;
            }
        }
        return count;
    }

    public static int getArmorCount(ArmorItem.Type type) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().method_7909() instanceof ArmorItem) || ((ArmorItem)entry.getValue().method_7909()).method_48398() != type) continue;
            count += entry.getValue().method_7947();
        }
        return count;
    }

    public static boolean CheckArmorType(Item item, ArmorItem.Type type) {
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.checkArmor.getValue()) {
            return item instanceof ArmorItem && ((ArmorItem)item).method_48398() == type;
        }
        return false;
    }

    public static int findPot(StatusEffect potion) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack == ItemStack.field_8037 || !(stack.method_7909() instanceof SplashPotionItem)) continue;
            ArrayList effects = new ArrayList(PotionUtil.method_8067((ItemStack)stack));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.method_5579() != potion) continue;
                return i;
            }
        }
        return -1;
    }

    public static int findUnBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack.method_7909() instanceof BlockItem) continue;
            return i;
        }
        return -1;
    }

    public static int findBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (!(stack.method_7909() instanceof BlockItem) || BlockUtil.shiftBlocks.contains(Block.method_9503((Item)stack.method_7909())) || ((BlockItem)stack.method_7909()).method_7711() == Blocks.field_10343) continue;
            return i;
        }
        return -1;
    }

    public static int findBlockInventorySlot(Block block) {
        return InventoryUtil.findItemInventorySlot(Item.method_7867((Block)block));
    }

    public static int findItemInventorySlot(Item item) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() != item) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findPotInventorySlot(StatusEffect potion) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack == ItemStack.field_8037 || !(stack.method_7909() instanceof SplashPotionItem)) continue;
            ArrayList effects = new ArrayList(PotionUtil.method_8067((ItemStack)stack));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.method_5579() != potion) continue;
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = 0; current <= 44; ++current) {
            fullInventorySlots.put(current, InventoryUtil.mc.field_1724.method_31548().method_5438(current));
        }
        return fullInventorySlots;
    }

    public static Map<Integer, ItemStack> getNoArmorInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = 0; current <= 35; ++current) {
            fullInventorySlots.put(current, InventoryUtil.mc.field_1724.method_31548().method_5438(current));
        }
        return fullInventorySlots;
    }
}
