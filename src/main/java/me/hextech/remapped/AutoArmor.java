package me.hextech.remapped;

import java.util.HashMap;
import java.util.Map;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.ElytraFly;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor
extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
        if (!(AutoArmor.mc.field_1755 == null || AutoArmor.mc.field_1755 instanceof ChatScreen || AutoArmor.mc.field_1755 instanceof InventoryScreen || AutoArmor.mc.field_1755 instanceof ClickGuiScreen)) {
            return;
        }
        if (AutoArmor.mc.field_1724.field_7498 != AutoArmor.mc.field_1724.field_7512) {
            return;
        }
        if (MovementUtil.isMoving() && this.noMove.getValue()) {
            return;
        }
        if (this.tickDelay > 0) {
            --this.tickDelay;
            return;
        }
        this.tickDelay = this.delay.getValueInt();
        HashMap<EquipmentSlot, int[]> armorMap = new HashMap<EquipmentSlot, int[]>(4);
        armorMap.put(EquipmentSlot.field_6166, new int[]{36, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(36)), -1, -1});
        armorMap.put(EquipmentSlot.field_6172, new int[]{37, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(37)), -1, -1});
        armorMap.put(EquipmentSlot.field_6174, new int[]{38, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(38)), -1, -1});
        armorMap.put(EquipmentSlot.field_6169, new int[]{39, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(39)), -1, -1});
        for (int s = 0; s < 36; ++s) {
            if (!(AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() instanceof ArmorItem) && AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() != Items.field_8833) continue;
            int protection = this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(s));
            EquipmentSlot slot = AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() instanceof ElytraItem ? EquipmentSlot.field_6174 : ((ArmorItem)AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909()).method_7685();
            for (Map.Entry e : armorMap.entrySet()) {
                if (this.autoElytra.getValue() && ElytraFly.INSTANCE.isOn() && e.getKey() == EquipmentSlot.field_6174) {
                    if (!AutoArmor.mc.field_1724.method_31548().method_5438(38).method_7960() && AutoArmor.mc.field_1724.method_31548().method_5438(38).method_7909() instanceof ElytraItem && ElytraItem.method_7804((ItemStack)AutoArmor.mc.field_1724.method_31548().method_5438(38)) || ((int[])e.getValue())[2] != -1 && !AutoArmor.mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]).method_7960() && AutoArmor.mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]).method_7909() instanceof ElytraItem && ElytraItem.method_7804((ItemStack)AutoArmor.mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2])) || AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7960() || !(AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() instanceof ElytraItem) || !ElytraItem.method_7804((ItemStack)AutoArmor.mc.field_1724.method_31548().method_5438(s))) continue;
                    ((int[])e.getValue())[2] = s;
                    continue;
                }
                if (protection <= 0 || e.getKey() != slot || protection <= ((int[])e.getValue())[1] || protection <= ((int[])e.getValue())[3]) continue;
                ((int[])e.getValue())[2] = s;
                ((int[])e.getValue())[3] = protection;
            }
        }
        for (Map.Entry equipmentSlotEntry : armorMap.entrySet()) {
            if (((int[])equipmentSlotEntry.getValue())[2] == -1) continue;
            if (((int[])equipmentSlotEntry.getValue())[1] == -1 && ((int[])equipmentSlotEntry.getValue())[2] < 9) {
                if (((int[])equipmentSlotEntry.getValue())[2] != AutoArmor.mc.field_1724.method_31548().field_7545) {
                    AutoArmor.mc.field_1724.method_31548().field_7545 = ((int[])equipmentSlotEntry.getValue())[2];
                    AutoArmor.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(((int[])equipmentSlotEntry.getValue())[2]));
                }
                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, 36 + ((int[])equipmentSlotEntry.getValue())[2], 1, SlotActionType.field_7794, (PlayerEntity)AutoArmor.mc.field_1724);
                EntityUtil.syncInventory();
            } else if (AutoArmor.mc.field_1724.field_7498 == AutoArmor.mc.field_1724.field_7512) {
                int armorSlot = ((int[])equipmentSlotEntry.getValue())[0] - 34 + (39 - ((int[])equipmentSlotEntry.getValue())[0]) * 2;
                int newArmorSlot = ((int[])equipmentSlotEntry.getValue())[2] < 9 ? 36 + ((int[])equipmentSlotEntry.getValue())[2] : ((int[])equipmentSlotEntry.getValue())[2];
                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, newArmorSlot, 0, SlotActionType.field_7790, (PlayerEntity)AutoArmor.mc.field_1724);
                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, armorSlot, 0, SlotActionType.field_7790, (PlayerEntity)AutoArmor.mc.field_1724);
                if (((int[])equipmentSlotEntry.getValue())[1] != -1) {
                    AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, newArmorSlot, 0, SlotActionType.field_7790, (PlayerEntity)AutoArmor.mc.field_1724);
                }
                EntityUtil.syncInventory();
            }
            return;
        }
    }

    private int getProtection(ItemStack is) {
        if (is.method_7909() instanceof ArmorItem || is.method_7909() == Items.field_8833) {
            int prot = 0;
            if (is.method_7909() instanceof ElytraItem) {
                if (!ElytraItem.method_7804((ItemStack)is)) {
                    return 0;
                }
                prot = 1;
            }
            if (is.method_7942()) {
                for (Map.Entry e : EnchantmentHelper.method_8222((ItemStack)is).entrySet()) {
                    if (!(e.getKey() instanceof ProtectionEnchantment)) continue;
                    prot += ((Integer)e.getValue()).intValue();
                }
            }
            return (is.method_7909() instanceof ArmorItem ? ((ArmorItem)is.method_7909()).method_7687() : 0) + prot;
        }
        if (!is.method_7960()) {
            return 0;
        }
        return -1;
    }
}
