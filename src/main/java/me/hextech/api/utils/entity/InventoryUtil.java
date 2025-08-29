package me.hextech.api.utils.entity;

import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            mc.getNetworkHandler().sendPacket(new PickFromInventoryC2SPacket(slot));
            return;
        }
        InventoryUtil.mc.interactionManager.clickSlot(InventoryUtil.mc.player.currentScreenHandler.syncId, slot, selectedSlot, SlotActionType.SWAP, InventoryUtil.mc.player);
    }

    public static void doSwap(int slot) {
        InventoryUtil.inventorySwap(slot, InventoryUtil.mc.player.getInventory().selectedSlot);
        InventoryUtil.switchToSlot(slot);
    }

    public static void switchToSlot(int slot) {
        InventoryUtil.mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static boolean holdingItem(Class clazz) {
        ItemStack stack = InventoryUtil.mc.player.getMainHandStack();
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
        Item item = stack.getItem();
        if (clazz.isInstance(item)) {
            return true;
        }
        if (item instanceof BlockItem) {
            Block block = Block.getBlockFromItem(item);
            return clazz.isInstance(block);
        }
        return false;
    }

    public static ItemStack getStackInSlot(int i) {
        return InventoryUtil.mc.player.getInventory().getStack(i);
    }

    public static int findItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = InventoryUtil.getStackInSlot(i).getItem();
            if (Item.getRawId(item) != Item.getRawId(input)) continue;
            return i;
        }
        return -1;
    }

    public static int getItemCount(Item item) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item) continue;
            count += entry.getValue().getCount();
        }
        return count;
    }

    public static int getEmptySlotCount() {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getNoArmorInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue() != ItemStack.EMPTY) continue;
            ++count;
        }
        return count;
    }

    public static int findClass(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof BlockItem) || !clazz.isInstance(((BlockItem) stack.getItem()).getBlock()))
                continue;
            return i;
        }
        return -1;
    }

    public static int getClassCount(Class clazz) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue() == ItemStack.EMPTY) continue;
            if (clazz.isInstance(entry.getValue().getItem())) {
                count += entry.getValue().getCount();
            }
            if (!(entry.getValue().getItem() instanceof BlockItem) || !clazz.isInstance(((BlockItem) entry.getValue().getItem()).getBlock()))
                continue;
            count += entry.getValue().getCount();
        }
        return count;
    }

    public static int findClassInventorySlot(Class clazz) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = InventoryUtil.mc.player.getInventory().getStack(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i < 9 ? i + 36 : i;
            }
            if (!(stack.getItem() instanceof BlockItem) || !clazz.isInstance(((BlockItem) stack.getItem()).getBlock()))
                continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof BlockItem) || ((BlockItem) stack.getItem()).getBlock() != blockIn)
                continue;
            return i;
        }
        return -1;
    }

    public static int getPotCount(StatusEffect potion) {
        int count = 0;
        block0:
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().getItem() instanceof SplashPotionItem)) continue;
            List<StatusEffectInstance> effects = new ArrayList<>(PotionUtil.getPotionEffects(entry.getValue()));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.getEffectType() != potion) continue;
                count += entry.getValue().getCount();
                continue block0;
            }
        }
        return count;
    }

    public static int getArmorCount(ArmorItem.Type type) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().getItem() instanceof ArmorItem) || ((ArmorItem) entry.getValue().getItem()).getType() != type)
                continue;
            count += entry.getValue().getCount();
        }
        return count;
    }

    public static boolean CheckArmorType(Item item, ArmorItem.Type type) {
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.checkArmor.getValue()) {
            return item instanceof ArmorItem && ((ArmorItem) item).getType() == type;
        }
        return false;
    }

    public static int findPot(StatusEffect potion) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof SplashPotionItem)) continue;
            List<StatusEffectInstance> effects = new ArrayList<>(PotionUtil.getPotionEffects(stack));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.getEffectType() != potion) continue;
                return i;
            }
        }
        return -1;
    }

    public static int findUnBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (stack.getItem() instanceof BlockItem) continue;
            return i;
        }
        return -1;
    }

    public static int findBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.getStackInSlot(i);
            if (!(stack.getItem() instanceof BlockItem) || BlockUtil.shiftBlocks.contains(Block.getBlockFromItem(stack.getItem())) || ((BlockItem) stack.getItem()).getBlock() == Blocks.COBWEB)
                continue;
            return i;
        }
        return -1;
    }

    public static int findBlockInventorySlot(Block block) {
        return InventoryUtil.findItemInventorySlot(Item.fromBlock(block));
    }

    public static int findItemInventorySlot(Item item) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = InventoryUtil.mc.player.getInventory().getStack(i);
            if (stack.getItem() != item) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findPotInventorySlot(StatusEffect potion) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = InventoryUtil.mc.player.getInventory().getStack(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof SplashPotionItem)) continue;
            List<StatusEffectInstance> effects = new ArrayList<>(PotionUtil.getPotionEffects(stack));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.getEffectType() != potion) continue;
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = 0; current <= 44; ++current) {
            fullInventorySlots.put(current, InventoryUtil.mc.player.getInventory().getStack(current));
        }
        return fullInventorySlots;
    }

    public static Map<Integer, ItemStack> getNoArmorInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = 0; current <= 35; ++current) {
            fullInventorySlots.put(current, InventoryUtil.mc.player.getInventory().getStack(current));
        }
        return fullInventorySlots;
    }
}
