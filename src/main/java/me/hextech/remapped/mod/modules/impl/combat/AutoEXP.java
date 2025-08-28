package me.hextech.remapped.mod.modules.impl.combat;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public class AutoEXP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoEXP INSTANCE;
    public final BooleanSetting down = this.add(new BooleanSetting("Down", true));
    public final BooleanSetting onlyBroken = this.add(new BooleanSetting("OnlyBroken", true));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 3, 0, 5));
    private final SliderSetting downpitch = this.add(new SliderSetting("Patch", 88, 0, 90));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final Timer delayTimer = new Timer();
    int exp = 0;
    private boolean throwing = false;

    public AutoEXP() {
        super("AutoEXP", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.throwing = false;
    }

    @Override
    public void onUpdate() {
        if (!this.getBind().isPressed()) {
            this.disable();
            return;
        }
        this.throwing = this.checkThrow();
        if (this.isThrow() && this.delayTimer.passedMs((long)this.delay.getValueInt() * 20L) && (!this.onlyGround.getValue() || AutoEXP.mc.player.isOnGround())) {
            this.exp = InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE) - 1;
            this.throwExp();
        }
    }

    @Override
    public void onEnable() {
        if (AutoEXP.nullCheck()) {
            this.disable();
            return;
        }
        this.exp = InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE);
    }

    @Override
    public String getInfo() {
        return String.valueOf(this.exp);
    }

    public void throwExp() {
        int newSlot;
        int oldSlot = AutoEXP.mc.player.getInventory().selectedSlot;
        if (this.inventory.getValue() && (newSlot = InventoryUtil.findItemInventorySlot(Items.EXPERIENCE_BOTTLE)) != -1) {
            InventoryUtil.inventorySwap(newSlot, AutoEXP.mc.player.getInventory().selectedSlot);
            AutoEXP.mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, EntityUtil.getWorldActionId(AutoEXP.mc.world)));
            InventoryUtil.inventorySwap(newSlot, AutoEXP.mc.player.getInventory().selectedSlot);
            EntityUtil.syncInventory();
            this.delayTimer.reset();
        } else {
            newSlot = InventoryUtil.findItem(Items.EXPERIENCE_BOTTLE);
            if (newSlot != -1) {
                InventoryUtil.switchToSlot(newSlot);
                AutoEXP.mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, EntityUtil.getWorldActionId(AutoEXP.mc.world)));
                InventoryUtil.switchToSlot(oldSlot);
                this.delayTimer.reset();
            }
        }
    }

    @EventHandler(priority=-200)
    public void RotateEvent(RotateEvent event) {
        if (!this.down.getValue()) {
            return;
        }
        if (this.isThrow()) {
            event.setPitch(this.downpitch.getValueFloat());
        }
    }

    public boolean isThrow() {
        return this.throwing;
    }

    public boolean checkThrow() {
        if (this.isOff()) {
            return false;
        }
        if (AutoEXP.mc.currentScreen instanceof ChatScreen) {
            return false;
        }
        if (AutoEXP.mc.currentScreen != null) {
            return false;
        }
        if (this.usingPause.getValue() && AutoEXP.mc.player.isUsingItem()) {
            return false;
        }
        if (!(InventoryUtil.findItem(Items.EXPERIENCE_BOTTLE) != -1 || this.inventory.getValue() && InventoryUtil.findItemInventorySlot(Items.EXPERIENCE_BOTTLE) != -1)) {
            return false;
        }
        if (this.onlyBroken.getValue()) {
            DefaultedList<ItemStack> armors = AutoEXP.mc.player.getInventory().armor;
            for (ItemStack armor : armors) {
                if (armor.isEmpty() || EntityUtil.getDamagePercent(armor) >= 100) continue;
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
