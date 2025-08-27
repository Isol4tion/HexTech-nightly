package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map$Entry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtil implements Wrapper {
   static int lastSlot;
   static int lastSelect;

   public static void inventorySwap(int slot, int selectedSlot) {
      if (slot == lastSlot) {
         switchToSlot(lastSelect);
         lastSlot = -1;
         lastSelect = -1;
      } else if (slot - 36 != selectedSlot) {
         if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.invSwapBypass.getValue()) {
            if (slot - 36 >= 0) {
               lastSlot = slot;
               lastSelect = selectedSlot;
               switchToSlot(slot - 36);
            } else {
               mc.method_1562().method_52787(new PickFromInventoryC2SPacket(slot));
            }
         } else {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, selectedSlot, SlotActionType.field_7791, mc.field_1724);
         }
      }
   }

   public static void doSwap(int slot) {
      inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      switchToSlot(slot);
   }

   public static void switchToSlot(int slot) {
      mc.field_1724.method_31548().field_7545 = slot;
      mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(slot));
   }

   public static boolean holdingItem(Class clazz) {
      ItemStack stack = mc.field_1724.method_6047();
      boolean result = isInstanceOf(stack, clazz);
      if (!result) {
         result = isInstanceOf(stack, clazz);
      }

      return result;
   }

   public static boolean isInstanceOf(ItemStack stack, Class clazz) {
      if (stack == null) {
         return false;
      } else {
         Item item = stack.method_7909();
         if (clazz.isInstance(item)) {
            return true;
         } else if (item instanceof BlockItem) {
            Block block = Block.method_9503(item);
            return clazz.isInstance(block);
         } else {
            return false;
         }
      }
   }

   public static ItemStack getStackInSlot(int i) {
      return mc.field_1724.method_31548().method_5438(i);
   }

   public static int findItem(Item input) {
      for (int i = 0; i < 9; i++) {
         Item item = getStackInSlot(i).method_7909();
         if (Item.method_7880(item) == Item.method_7880(input)) {
            return i;
         }
      }

      return -1;
   }

   public static int getItemCount(Item item) {
      int count = 0;

      for (Map$Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
         if (entry.getValue().method_7909() == item) {
            count += entry.getValue().method_7947();
         }
      }

      return count;
   }

   public static int getEmptySlotCount() {
      int count = 0;

      for (Map$Entry<Integer, ItemStack> entry : getNoArmorInventoryAndHotbarSlots().entrySet()) {
         if (entry.getValue() == ItemStack.field_8037) {
            count++;
         }
      }

      return count;
   }

   public static int findClass(Class clazz) {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = getStackInSlot(i);
         if (stack != ItemStack.field_8037) {
            if (clazz.isInstance(stack.method_7909())) {
               return i;
            }

            if (stack.method_7909() instanceof BlockItem && clazz.isInstance(((BlockItem)stack.method_7909()).method_7711())) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int getClassCount(Class clazz) {
      int count = 0;

      for (Map$Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
         if (entry.getValue() != ItemStack.field_8037) {
            if (clazz.isInstance(entry.getValue().method_7909())) {
               count += entry.getValue().method_7947();
            }

            if (entry.getValue().method_7909() instanceof BlockItem && clazz.isInstance(((BlockItem)entry.getValue().method_7909()).method_7711())) {
               count += entry.getValue().method_7947();
            }
         }
      }

      return count;
   }

   public static int findClassInventorySlot(Class clazz) {
      for (int i = 0; i < 45; i++) {
         ItemStack stack = mc.field_1724.method_31548().method_5438(i);
         if (stack != ItemStack.field_8037) {
            if (clazz.isInstance(stack.method_7909())) {
               return i < 9 ? i + 36 : i;
            }

            if (stack.method_7909() instanceof BlockItem && clazz.isInstance(((BlockItem)stack.method_7909()).method_7711())) {
               return i < 9 ? i + 36 : i;
            }
         }
      }

      return -1;
   }

   public static int findBlock(Block blockIn) {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = getStackInSlot(i);
         if (stack != ItemStack.field_8037 && stack.method_7909() instanceof BlockItem && ((BlockItem)stack.method_7909()).method_7711() == blockIn) {
            return i;
         }
      }

      return -1;
   }

   public static int getPotCount(StatusEffect potion) {
      int count = 0;

      for (Map$Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
         if (entry.getValue().method_7909() instanceof SplashPotionItem) {
            for (StatusEffectInstance potionEffect : new ArrayList(PotionUtil.method_8067(entry.getValue()))) {
               if (potionEffect.method_5579() == potion) {
                  count += entry.getValue().method_7947();
                  break;
               }
            }
         }
      }

      return count;
   }

   public static int getArmorCount(Type type) {
      int count = 0;

      for (Map$Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
         if (entry.getValue().method_7909() instanceof ArmorItem && ((ArmorItem)entry.getValue().method_7909()).method_48398() == type) {
            count += entry.getValue().method_7947();
         }
      }

      return count;
   }

   public static boolean CheckArmorType(Item item, Type type) {
      return !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.checkArmor.getValue() ? false : item instanceof ArmorItem && ((ArmorItem)item).method_48398() == type;
   }

   public static int findPot(StatusEffect potion) {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = getStackInSlot(i);
         if (stack != ItemStack.field_8037 && stack.method_7909() instanceof SplashPotionItem) {
            for (StatusEffectInstance potionEffect : new ArrayList(PotionUtil.method_8067(stack))) {
               if (potionEffect.method_5579() == potion) {
                  return i;
               }
            }
         }
      }

      return -1;
   }

   public static int findUnBlock() {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = getStackInSlot(i);
         if (!(stack.method_7909() instanceof BlockItem)) {
            return i;
         }
      }

      return -1;
   }

   public static int findBlock() {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = getStackInSlot(i);
         if (stack.method_7909() instanceof BlockItem
            && !BlockUtil.shiftBlocks.contains(Block.method_9503(stack.method_7909()))
            && ((BlockItem)stack.method_7909()).method_7711() != Blocks.field_10343) {
            return i;
         }
      }

      return -1;
   }

   public static int findBlockInventorySlot(Block block) {
      return findItemInventorySlot(Item.method_7867(block));
   }

   public static int findItemInventorySlot(Item item) {
      for (int i = 0; i < 45; i++) {
         ItemStack stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() == item) {
            return i < 9 ? i + 36 : i;
         }
      }

      return -1;
   }

   public static int findPotInventorySlot(StatusEffect potion) {
      for (int i = 0; i < 45; i++) {
         ItemStack stack = mc.field_1724.method_31548().method_5438(i);
         if (stack != ItemStack.field_8037 && stack.method_7909() instanceof SplashPotionItem) {
            for (StatusEffectInstance potionEffect : new ArrayList(PotionUtil.method_8067(stack))) {
               if (potionEffect.method_5579() == potion) {
                  return i < 9 ? i + 36 : i;
               }
            }
         }
      }

      return -1;
   }

   public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
      HashMap<Integer, ItemStack> fullInventorySlots = new HashMap();

      for (int current = 0; current <= 44; current++) {
         fullInventorySlots.put(current, mc.field_1724.method_31548().method_5438(current));
      }

      return fullInventorySlots;
   }

   public static Map<Integer, ItemStack> getNoArmorInventoryAndHotbarSlots() {
      HashMap<Integer, ItemStack> fullInventorySlots = new HashMap();

      for (int current = 0; current <= 35; current++) {
         fullInventorySlots.put(current, mc.field_1724.method_31548().method_5438(current));
      }

      return fullInventorySlots;
   }
}
