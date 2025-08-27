package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class OffHand
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final BooleanSetting mainHand = this.add(new BooleanSetting("MainHand", false));
    private final BooleanSetting crystal = this.add(new BooleanSetting("OffHandCrystal", false));
    private final SliderSetting health = this.add(new SliderSetting("Health", 16.0, 0.0, 36.0, 0.1));
    private final Timer timer = new Timer();
    int totems = 0;

    public OffHand() {
        super("OffHand", Module_JlagirAibYQgkHtbRnhw.Combat);
    }

    @Override
    public String getInfo() {
        return String.valueOf(this.totems);
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        this.update();
    }

    @Override
    public void onUpdate() {
        this.update();
    }

    private void update() {
        if (OffHand.nullCheck()) {
            return;
        }
        this.totems = InventoryUtil.getItemCount(Items.field_8288);
        if (!(OffHand.mc.field_1755 == null || OffHand.mc.field_1755 instanceof ChatScreen || OffHand.mc.field_1755 instanceof InventoryScreen || OffHand.mc.field_1755 instanceof ClickGuiScreen || OffHand.mc.field_1755 instanceof GameMenuScreen)) {
            return;
        }
        if (!this.timer.passedMs(200L)) {
            return;
        }
        if ((double)(OffHand.mc.player.method_6032() + OffHand.mc.player.method_6067()) > this.health.getValue()) {
            int itemSlot;
            if (!this.mainHand.getValue() && this.crystal.getValue() && OffHand.mc.player.method_6079().method_7909() != Items.field_8301 && (itemSlot = InventoryUtil.findItemInventorySlot(Items.field_8301)) != -1) {
                OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, 45, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                EntityUtil.syncInventory();
                this.timer.reset();
            }
            return;
        }
        if (OffHand.mc.player.method_6047().method_7909() == Items.field_8288 || OffHand.mc.player.method_6079().method_7909() == Items.field_8288) {
            return;
        }
        int itemSlot = InventoryUtil.findItemInventorySlot(Items.field_8288);
        if (itemSlot != -1) {
            if (this.mainHand.getValue()) {
                InventoryUtil.switchToSlot(0);
                if (OffHand.mc.player.method_31548().method_5438(0).method_7909() != Items.field_8288) {
                    OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                    OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, 36, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                    OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                    EntityUtil.syncInventory();
                }
            } else {
                OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, 45, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                OffHand.mc.field_1761.method_2906(OffHand.mc.player.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, (PlayerEntity)OffHand.mc.player);
                EntityUtil.syncInventory();
            }
            this.timer.reset();
        }
    }
}
