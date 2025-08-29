package me.hextech.mod.modules.impl.combat;

import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

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
        super("AutoPot", Category.Combat);
        INSTANCE = this;
    }

    public static int findPotionInventorySlot(StatusEffect targetEffect) {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = AutoPot.mc.player.getInventory().getStack(i);
            if (Item.getRawId(itemStack.getItem()) != Item.getRawId(Items.SPLASH_POTION)) continue;
            List<StatusEffectInstance> effects = new ArrayList<>(PotionUtil.getPotionEffects(itemStack));
            for (StatusEffectInstance effect : effects) {
                if (effect.getEffectType() != targetEffect) continue;
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int findPotion(StatusEffect targetEffect) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.getStackInSlot(i);
            if (Item.getRawId(itemStack.getItem()) != Item.getRawId(Items.SPLASH_POTION)) continue;
            List<StatusEffectInstance> effects = new ArrayList<>(PotionUtil.getPotionEffects(itemStack));
            for (StatusEffectInstance effect : effects) {
                if (effect.getEffectType() != targetEffect) continue;
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
        if (!this.onlyGround.getValue() || AutoPot.mc.player.isOnGround() && !AutoPot.mc.world.isAir(new BlockPosX(AutoPot.mc.player.getPos().add(0.0, -1.0, 0.0)))) {
            if (this.speed.getValue() && !AutoPot.mc.player.hasStatusEffect(StatusEffects.SPEED)) {
                this.throwing = this.checkThrow(StatusEffects.SPEED);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.SPEED);
                    return;
                }
            }
            if (this.resistance.getValue() && (!AutoPot.mc.player.hasStatusEffect(StatusEffects.RESISTANCE) || AutoPot.mc.player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() < 2)) {
                this.throwing = this.checkThrow(StatusEffects.RESISTANCE);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.RESISTANCE);
                }
            }
        }
    }

    public void throwPotion(StatusEffect targetEffect) {
        int newSlot;
        int oldSlot = AutoPot.mc.player.getInventory().selectedSlot;
        if (this.inventory.getValue() && (newSlot = AutoPot.findPotionInventorySlot(targetEffect)) != -1) {
            EntityUtil.sendYawAndPitch(AutoPot.mc.player.getYaw(), 90.0f);
            InventoryUtil.inventorySwap(newSlot, AutoPot.mc.player.getInventory().selectedSlot);
            AutoPot.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            InventoryUtil.inventorySwap(newSlot, AutoPot.mc.player.getInventory().selectedSlot);
            EntityUtil.syncInventory();
            this.delayTimer.reset();
        } else {
            newSlot = AutoPot.findPotion(targetEffect);
            if (newSlot != -1) {
                EntityUtil.sendYawAndPitch(AutoPot.mc.player.getYaw(), 90.0f);
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
