package me.hextech.remapped;

import java.util.HashMap;
import java.util.Map;
import java.util.Map$Entry;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoArmor INSTANCE;
   private final BooleanSetting noMove = this.add(new BooleanSetting("NoMove", false));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 3.0, 0.0, 10.0, 1.0));
   private final BooleanSetting autoElytra = this.add(new BooleanSetting("AutoElytra", true));
   private int tickDelay = 0;

   public AutoArmor() {
      super("AutoArmor", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (mc.field_1755 == null || mc.field_1755 instanceof ChatScreen || mc.field_1755 instanceof InventoryScreen || mc.field_1755 instanceof ClickGuiScreen) {
         if (mc.field_1724.field_7498 == mc.field_1724.field_7512) {
            if (!MovementUtil.isMoving() || !this.noMove.getValue()) {
               if (this.tickDelay > 0) {
                  this.tickDelay--;
               } else {
                  this.tickDelay = this.delay.getValueInt();
                  Map<EquipmentSlot, int[]> armorMap = new HashMap(4);
                  armorMap.put(EquipmentSlot.field_6166, new int[]{36, this.getProtection(mc.field_1724.method_31548().method_5438(36)), -1, -1});
                  armorMap.put(EquipmentSlot.field_6172, new int[]{37, this.getProtection(mc.field_1724.method_31548().method_5438(37)), -1, -1});
                  armorMap.put(EquipmentSlot.field_6174, new int[]{38, this.getProtection(mc.field_1724.method_31548().method_5438(38)), -1, -1});
                  armorMap.put(EquipmentSlot.field_6169, new int[]{39, this.getProtection(mc.field_1724.method_31548().method_5438(39)), -1, -1});

                  for (int s = 0; s < 36; s++) {
                     if (mc.field_1724.method_31548().method_5438(s).method_7909() instanceof ArmorItem
                        || mc.field_1724.method_31548().method_5438(s).method_7909() == Items.field_8833) {
                        int protection = this.getProtection(mc.field_1724.method_31548().method_5438(s));
                        EquipmentSlot slot = mc.field_1724.method_31548().method_5438(s).method_7909() instanceof ElytraItem
                           ? EquipmentSlot.field_6174
                           : ((ArmorItem)mc.field_1724.method_31548().method_5438(s).method_7909()).method_7685();

                        for (Map$Entry<EquipmentSlot, int[]> e : armorMap.entrySet()) {
                           if (this.autoElytra.getValue() && ElytraFly.INSTANCE.isOn() && e.getKey() == EquipmentSlot.field_6174) {
                              if ((
                                    mc.field_1724.method_31548().method_5438(38).method_7960()
                                       || !(mc.field_1724.method_31548().method_5438(38).method_7909() instanceof ElytraItem)
                                       || !ElytraItem.method_7804(mc.field_1724.method_31548().method_5438(38))
                                 )
                                 && (
                                    e.getValue()[2] == -1
                                       || mc.field_1724.method_31548().method_5438(e.getValue()[2]).method_7960()
                                       || !(mc.field_1724.method_31548().method_5438(e.getValue()[2]).method_7909() instanceof ElytraItem)
                                       || !ElytraItem.method_7804(mc.field_1724.method_31548().method_5438(e.getValue()[2]))
                                 )
                                 && !mc.field_1724.method_31548().method_5438(s).method_7960()
                                 && mc.field_1724.method_31548().method_5438(s).method_7909() instanceof ElytraItem
                                 && ElytraItem.method_7804(mc.field_1724.method_31548().method_5438(s))) {
                                 e.getValue()[2] = s;
                              }
                           } else if (protection > 0 && e.getKey() == slot && protection > e.getValue()[1] && protection > e.getValue()[3]) {
                              e.getValue()[2] = s;
                              e.getValue()[3] = protection;
                           }
                        }
                     }
                  }

                  for (Map$Entry<EquipmentSlot, int[]> equipmentSlotEntry : armorMap.entrySet()) {
                     if (equipmentSlotEntry.getValue()[2] != -1) {
                        if (equipmentSlotEntry.getValue()[1] == -1 && equipmentSlotEntry.getValue()[2] < 9) {
                           if (equipmentSlotEntry.getValue()[2] != mc.field_1724.method_31548().field_7545) {
                              mc.field_1724.method_31548().field_7545 = equipmentSlotEntry.getValue()[2];
                              mc.field_1724.field_3944.method_52787(new UpdateSelectedSlotC2SPacket(equipmentSlotEntry.getValue()[2]));
                           }

                           mc.field_1761
                              .method_2906(
                                 mc.field_1724.field_7512.field_7763, 36 + equipmentSlotEntry.getValue()[2], 1, SlotActionType.field_7794, mc.field_1724
                              );
                           EntityUtil.syncInventory();
                        } else if (mc.field_1724.field_7498 == mc.field_1724.field_7512) {
                           int armorSlot = equipmentSlotEntry.getValue()[0] - 34 + (39 - equipmentSlotEntry.getValue()[0]) * 2;
                           int newArmorSlot = ((int[])equipmentSlotEntry.getValue())[2] < 9
                              ? 36 + equipmentSlotEntry.getValue()[2]
                              : equipmentSlotEntry.getValue()[2];
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, newArmorSlot, 0, SlotActionType.field_7790, mc.field_1724);
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, armorSlot, 0, SlotActionType.field_7790, mc.field_1724);
                           if (equipmentSlotEntry.getValue()[1] != -1) {
                              mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, newArmorSlot, 0, SlotActionType.field_7790, mc.field_1724);
                           }

                           EntityUtil.syncInventory();
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private int getProtection(ItemStack is) {
      if (is.method_7909() instanceof ArmorItem || is.method_7909() == Items.field_8833) {
         int prot = 0;
         if (is.method_7909() instanceof ElytraItem) {
            if (!ElytraItem.method_7804(is)) {
               return 0;
            }

            prot = 1;
         }

         if (is.method_7942()) {
            for (Map$Entry<Enchantment, Integer> e : EnchantmentHelper.method_8222(is).entrySet()) {
               if (e.getKey() instanceof ProtectionEnchantment) {
                  prot += e.getValue();
               }
            }
         }

         return (is.method_7909() instanceof ArmorItem ? ((ArmorItem)is.method_7909()).method_7687() : 0) + prot;
      } else {
         return !is.method_7960() ? 0 : -1;
      }
   }
}
