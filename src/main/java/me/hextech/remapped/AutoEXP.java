package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
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
        if (this.isThrow() && this.delayTimer.passedMs((long)this.delay.getValueInt() * 20L) && (!this.onlyGround.getValue() || AutoEXP.mc.field_1724.method_24828())) {
            this.exp = InventoryUtil.getItemCount(Items.field_8287) - 1;
            this.throwExp();
        }
    }

    @Override
    public void onEnable() {
        if (AutoEXP.nullCheck()) {
            this.disable();
            return;
        }
        this.exp = InventoryUtil.getItemCount(Items.field_8287);
    }

    @Override
    public String getInfo() {
        return String.valueOf(this.exp);
    }

    public void throwExp() {
        int newSlot;
        int oldSlot = AutoEXP.mc.field_1724.method_31548().field_7545;
        if (this.inventory.getValue() && (newSlot = InventoryUtil.findItemInventorySlot(Items.field_8287)) != -1) {
            InventoryUtil.inventorySwap(newSlot, AutoEXP.mc.field_1724.method_31548().field_7545);
            AutoEXP.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, EntityUtil.getWorldActionId(AutoEXP.mc.field_1687)));
            InventoryUtil.inventorySwap(newSlot, AutoEXP.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
            this.delayTimer.reset();
        } else {
            newSlot = InventoryUtil.findItem(Items.field_8287);
            if (newSlot != -1) {
                InventoryUtil.switchToSlot(newSlot);
                AutoEXP.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, EntityUtil.getWorldActionId(AutoEXP.mc.field_1687)));
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
        if (AutoEXP.mc.field_1755 instanceof ChatScreen) {
            return false;
        }
        if (AutoEXP.mc.field_1755 != null) {
            return false;
        }
        if (this.usingPause.getValue() && AutoEXP.mc.field_1724.method_6115()) {
            return false;
        }
        if (!(InventoryUtil.findItem(Items.field_8287) != -1 || this.inventory.getValue() && InventoryUtil.findItemInventorySlot(Items.field_8287) != -1)) {
            return false;
        }
        if (this.onlyBroken.getValue()) {
            DefaultedList armors = AutoEXP.mc.field_1724.method_31548().field_7548;
            for (ItemStack armor : armors) {
                if (armor.method_7960() || EntityUtil.getDamagePercent(armor) >= 100) continue;
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
