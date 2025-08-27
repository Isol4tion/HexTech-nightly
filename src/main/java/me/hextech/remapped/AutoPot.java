package me.hextech.remapped;

import java.util.List;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AutoPot
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoPot INSTANCE;
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5, 0, 10).setSuffix("s"));
    private final BooleanSetting speed = this.add(new BooleanSetting("Speed", true));
    private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", true));
    private final BooleanSetting slowFalling = this.add(new BooleanSetting("SlowFalling", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final Timer delayTimer = new Timer();
    private boolean throwing = false;

    public AutoPot() {
        super("AutoPot", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static int findPotionInventorySlot(StatusEffect targetEffect) {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = AutoPot.mc.player.getInventory().method_5438(i);
            if (Item.getRawId((Item)itemStack.getItem()) != Item.getRawId((Item)Items.SPLASH_POTION)) continue;
            List effects = PotionContentsComponent.method_8067((ItemStack)itemStack);
            for (StatusEffectInstance effect : effects) {
                if (effect.method_5579() != targetEffect) continue;
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int findPotion(StatusEffect targetEffect) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.getStackInSlot(i);
            if (Item.getRawId((Item)itemStack.getItem()) != Item.getRawId((Item)Items.SPLASH_POTION)) continue;
            List effects = PotionContentsComponent.method_8067((ItemStack)itemStack);
            for (StatusEffectInstance effect : effects) {
                if (effect.method_5579() != targetEffect) continue;
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDisable() {
        this.throwing = false;
    }

    @Override
    public void onUpdate() {
        if (!this.onlyGround.getValue() || AutoPot.mc.player.isOnGround() && !AutoPot.mc.world.isAir((BlockPos)new BlockPosX(AutoPot.mc.player.method_19538().add(0.0, -1.0, 0.0)))) {
            if (this.speed.getValue() && !AutoPot.mc.player.method_6059(StatusEffects.field_5904)) {
                this.throwing = this.checkThrow(StatusEffects.field_5904);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.field_5904);
                    return;
                }
            }
            if (this.resistance.getValue() && (!AutoPot.mc.player.method_6059(StatusEffects.field_5907) || AutoPot.mc.player.method_6112(StatusEffects.field_5907).getAmplifier() < 2)) {
                this.throwing = this.checkThrow(StatusEffects.field_5907);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.field_5907);
                }
            }
        }
    }

    public void throwPotion(StatusEffect targetEffect) {
        int newSlot;
        int oldSlot = AutoPot.mc.player.getInventory().selectedSlot;
        if (this.inventory.getValue() && (newSlot = AutoPot.findPotionInventorySlot(targetEffect)) != -1) {
            EntityUtil.sendYawAndPitch(AutoPot.mc.player.method_36454(), 90.0f);
            InventoryUtil.inventorySwap(newSlot, AutoPot.mc.player.getInventory().selectedSlot);
            AutoPot.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            InventoryUtil.inventorySwap(newSlot, AutoPot.mc.player.getInventory().selectedSlot);
            EntityUtil.syncInventory();
            this.delayTimer.reset();
        } else {
            newSlot = AutoPot.findPotion(targetEffect);
            if (newSlot != -1) {
                EntityUtil.sendYawAndPitch(AutoPot.mc.player.method_36454(), 90.0f);
                InventoryUtil.switchToSlot(newSlot);
                AutoPot.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                InventoryUtil.switchToSlot(oldSlot);
                this.delayTimer.reset();
            }
        }
    }

    public boolean isThrow() {
        return this.throwing;
    }

    public boolean checkThrow(StatusEffect targetEffect) {
        if (this.isOff()) {
            return false;
        }
        if (!(AutoPot.mc.currentScreen == null || AutoPot.mc.currentScreen instanceof ChatScreen || AutoPot.mc.currentScreen instanceof InventoryScreen || AutoPot.mc.currentScreen instanceof ClickGuiScreen || AutoPot.mc.currentScreen instanceof GameMenuScreen)) {
            return false;
        }
        if (this.usingPause.getValue() && AutoPot.mc.player.isUsingItem()) {
            return false;
        }
        return AutoPot.findPotion(targetEffect) != -1 || this.inventory.getValue() && AutoPot.findPotionInventorySlot(targetEffect) != -1;
    }
}
