package me.hextech.mod.modules.impl.combat;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.UpdateWalkingEvent;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
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
        super("OffHand", Category.Combat);
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
        this.totems = InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        if (!(OffHand.mc.currentScreen == null || OffHand.mc.currentScreen instanceof ChatScreen || OffHand.mc.currentScreen instanceof InventoryScreen || OffHand.mc.currentScreen instanceof ClickGuiScreen || OffHand.mc.currentScreen instanceof GameMenuScreen)) {
            return;
        }
        if (!this.timer.passedMs(200L)) {
            return;
        }
        if ((double) (OffHand.mc.player.getHealth() + OffHand.mc.player.getAbsorptionAmount()) > this.health.getValue()) {
            int itemSlot;
            if (!this.mainHand.getValue() && this.crystal.getValue() && OffHand.mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL && (itemSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL)) != -1) {
                OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, OffHand.mc.player);
                OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, OffHand.mc.player);
                OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, OffHand.mc.player);
                EntityUtil.syncInventory();
                this.timer.reset();
            }
            return;
        }
        if (OffHand.mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING || OffHand.mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        int itemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING);
        if (itemSlot != -1) {
            if (this.mainHand.getValue()) {
                InventoryUtil.switchToSlot(0);
                if (OffHand.mc.player.getInventory().getStack(0).getItem() != Items.TOTEM_OF_UNDYING) {
                    OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, OffHand.mc.player);
                    OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, 36, 0, SlotActionType.PICKUP, OffHand.mc.player);
                    OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, OffHand.mc.player);
                    EntityUtil.syncInventory();
                }
            } else {
                OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, OffHand.mc.player);
                OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, OffHand.mc.player);
                OffHand.mc.interactionManager.clickSlot(OffHand.mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, OffHand.mc.player);
                EntityUtil.syncInventory();
            }
            this.timer.reset();
        }
    }
}
