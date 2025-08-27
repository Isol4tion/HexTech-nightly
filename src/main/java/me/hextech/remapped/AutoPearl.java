package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.SliderSetting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class AutoPearl
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoPearl INSTANCE;
    public static boolean throwing;
    public final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", false));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 10, 0, 50));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 100, 0, 100));
    boolean shouldThrow = false;

    public AutoPearl() {
        super("AutoPearl", Module_JlagirAibYQgkHtbRnhw.Misc);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (AutoPearl.nullCheck()) {
            this.disable();
            return;
        }
        if (this.rotation.getValue()) {
            return;
        }
        if (this.getBind().isHoldEnable()) {
            this.shouldThrow = true;
            return;
        }
        this.throwPearl(AutoPearl.mc.field_1724.method_36454(), AutoPearl.mc.field_1724.method_36455());
        this.disable();
    }

    @Override
    public void onUpdate() {
        if (this.rotation.getValue() && HexTech.ROTATE.inFov(AutoPearl.mc.field_1724.method_36454(), AutoPearl.mc.field_1724.method_36455(), this.fov.getValueFloat())) {
            int pearl;
            throwing = true;
            if (AutoPearl.mc.field_1724.method_6047().method_7909() == Items.field_8634) {
                AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
            } else if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlot(Items.field_8634)) != -1) {
                InventoryUtil.inventorySwap(pearl, AutoPearl.mc.field_1724.method_31548().field_7545);
                AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
                InventoryUtil.inventorySwap(pearl, AutoPearl.mc.field_1724.method_31548().field_7545);
                EntityUtil.syncInventory();
            } else {
                pearl = InventoryUtil.findItem(Items.field_8634);
                if (pearl != -1) {
                    int old = AutoPearl.mc.field_1724.method_31548().field_7545;
                    InventoryUtil.switchToSlot(pearl);
                    AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
                    InventoryUtil.switchToSlot(old);
                }
            }
            throwing = false;
            this.disable();
        }
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.rotation.getValue()) {
            event.setRotation(AutoPearl.mc.field_1724.method_36454(), AutoPearl.mc.field_1724.method_36455(), this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    public void throwPearl(float yaw, float pitch) {
        int pearl;
        throwing = true;
        if (AutoPearl.mc.field_1724.method_6047().method_7909() == Items.field_8634) {
            EntityUtil.sendYawAndPitch(yaw, pitch);
            AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
        } else if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlot(Items.field_8634)) != -1) {
            InventoryUtil.inventorySwap(pearl, AutoPearl.mc.field_1724.method_31548().field_7545);
            EntityUtil.sendYawAndPitch(yaw, pitch);
            AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
            InventoryUtil.inventorySwap(pearl, AutoPearl.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
        } else {
            pearl = InventoryUtil.findItem(Items.field_8634);
            if (pearl != -1) {
                int old = AutoPearl.mc.field_1724.method_31548().field_7545;
                InventoryUtil.switchToSlot(pearl);
                EntityUtil.sendYawAndPitch(yaw, pitch);
                AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
                InventoryUtil.switchToSlot(old);
            }
        }
        throwing = false;
    }

    @Override
    public void onDisable() {
        if (AutoPearl.nullCheck()) {
            return;
        }
        if (this.shouldThrow && this.getBind().isHoldEnable()) {
            this.shouldThrow = false;
            this.throwPearl(AutoPearl.mc.field_1724.method_36454(), AutoPearl.mc.field_1724.method_36455());
        }
    }
}
