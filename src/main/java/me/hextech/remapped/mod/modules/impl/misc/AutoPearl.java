package me.hextech.remapped.mod.modules.impl.misc;

import me.hextech.HexTech;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
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
        this.throwPearl(AutoPearl.mc.player.getYaw(), AutoPearl.mc.player.getPitch());
        this.disable();
    }

    @Override
    public void onUpdate() {
        if (this.rotation.getValue() && HexTech.ROTATE.inFov(AutoPearl.mc.player.getYaw(), AutoPearl.mc.player.getPitch(), this.fov.getValueFloat())) {
            int pearl;
            throwing = true;
            if (AutoPearl.mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            } else if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlot(Items.ENDER_PEARL)) != -1) {
                InventoryUtil.inventorySwap(pearl, AutoPearl.mc.player.getInventory().selectedSlot);
                AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                InventoryUtil.inventorySwap(pearl, AutoPearl.mc.player.getInventory().selectedSlot);
                EntityUtil.syncInventory();
            } else {
                pearl = InventoryUtil.findItem(Items.ENDER_PEARL);
                if (pearl != -1) {
                    int old = AutoPearl.mc.player.getInventory().selectedSlot;
                    InventoryUtil.switchToSlot(pearl);
                    AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
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
            event.setRotation(AutoPearl.mc.player.getYaw(), AutoPearl.mc.player.getPitch(), this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    public void throwPearl(float yaw, float pitch) {
        int pearl;
        throwing = true;
        if (AutoPearl.mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
            EntityUtil.sendYawAndPitch(yaw, pitch);
            AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
        } else if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlot(Items.ENDER_PEARL)) != -1) {
            InventoryUtil.inventorySwap(pearl, AutoPearl.mc.player.getInventory().selectedSlot);
            EntityUtil.sendYawAndPitch(yaw, pitch);
            AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            InventoryUtil.inventorySwap(pearl, AutoPearl.mc.player.getInventory().selectedSlot);
            EntityUtil.syncInventory();
        } else {
            pearl = InventoryUtil.findItem(Items.ENDER_PEARL);
            if (pearl != -1) {
                int old = AutoPearl.mc.player.getInventory().selectedSlot;
                InventoryUtil.switchToSlot(pearl);
                EntityUtil.sendYawAndPitch(yaw, pitch);
                AutoPearl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
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
            this.throwPearl(AutoPearl.mc.player.getYaw(), AutoPearl.mc.player.getPitch());
        }
    }
}
