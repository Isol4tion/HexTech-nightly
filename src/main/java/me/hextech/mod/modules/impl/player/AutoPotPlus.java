package me.hextech.mod.modules.impl.player;

import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AutoPotPlus
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoPotPlus INSTANCE;
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5, 0, 10).setSuffix("s"));
    private final BooleanSetting checkRang = this.add(new BooleanSetting("CheckRang", true).setParent());
    private final SliderSetting rang = this.add(new SliderSetting("Rang", 15, 0, 15, v -> this.checkRang.isOpen()).setSuffix("m"));
    private final BooleanSetting strength = this.add(new BooleanSetting("Strength", true));
    private final BooleanSetting speed = this.add(new BooleanSetting("Speed", true));
    private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", true));
    private final BooleanSetting checkFly = this.add(new BooleanSetting("CheckFly", false));
    private final BooleanSetting smartCheckGround = this.add(new BooleanSetting("SmartCheckGround", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final Timer delayTimer = new Timer();
    PlayerEntity target = null;
    BlockPos throwPos = null;
    private boolean throwing = false;

    public AutoPotPlus() {
        super("AutoPot+", Category.Player);
        INSTANCE = this;
    }

    public static int findPotionInventorySlot(StatusEffect targetEffect) {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = AutoPotPlus.mc.player.getInventory().getStack(i);
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
        if (AutoPotPlus.mc.player == null) {
            return;
        }
        this.target = CombatUtil.getClosestEnemy(this.rang.getValue());
        if (this.checkRang.getValue() && this.target == null) {
            return;
        }
        this.throwPos = null;
        this.throwPos = this.findPos();
        if (!(this.checkFly.getValue() && AutoPotPlus.mc.player.isFallFlying() || this.smartCheckGround.getValue() && this.throwPos == null)) {
            if (this.speed.getValue() && !AutoPotPlus.mc.player.hasStatusEffect(StatusEffects.SPEED)) {
                this.throwing = this.checkThrow(StatusEffects.SPEED);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.SPEED);
                    return;
                }
            }
            if (this.resistance.getValue() && (!AutoPotPlus.mc.player.hasStatusEffect(StatusEffects.RESISTANCE) || AutoPotPlus.mc.player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() < 2)) {
                this.throwing = this.checkThrow(StatusEffects.RESISTANCE);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.RESISTANCE);
                }
            }
            if (this.strength.getValue() && !AutoPotPlus.mc.player.hasStatusEffect(StatusEffects.STRENGTH)) {
                this.throwing = this.checkThrow(StatusEffects.STRENGTH);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.STRENGTH);
                }
            }
        }
    }

    public void snapAt(Vec3d directionVec) {
        float[] angle = this.getRotation(directionVec);
        EntityUtil.sendYawAndPitch(angle[0], angle[1]);
    }

    public float[] getRotation(Vec3d vec) {
        Vec3d eyesPos = EntityUtil.getEyesPos();
        return this.getRotation(eyesPos, vec);
    }

    public float[] getRotation(Vec3d eyesPos, Vec3d vec) {
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public void throwPotion(StatusEffect targetEffect) {
        int newSlot;
        int oldSlot = AutoPotPlus.mc.player.getInventory().selectedSlot;
        if (this.inventory.getValue() && (newSlot = AutoPotPlus.findPotionInventorySlot(targetEffect)) != -1) {
            if (this.throwPos != null) {
                this.snapAt(this.throwPos.toCenterPos());
            } else {
                EntityUtil.sendYawAndPitch(AutoPotPlus.mc.player.getYaw(), 90.0f);
            }
            InventoryUtil.inventorySwap(newSlot, AutoPotPlus.mc.player.getInventory().selectedSlot);
            AutoPotPlus.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            InventoryUtil.inventorySwap(newSlot, AutoPotPlus.mc.player.getInventory().selectedSlot);
            EntityUtil.syncInventory();
            this.delayTimer.reset();
        } else {
            newSlot = AutoPotPlus.findPotion(targetEffect);
            if (newSlot != -1) {
                if (this.throwPos != null) {
                    this.snapAt(this.throwPos.toCenterPos());
                } else {
                    EntityUtil.sendYawAndPitch(AutoPotPlus.mc.player.getYaw(), 90.0f);
                }
                InventoryUtil.switchToSlot(newSlot);
                AutoPotPlus.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                InventoryUtil.switchToSlot(oldSlot);
                this.delayTimer.reset();
            }
        }
    }

    public BlockPos findPos() {
        for (float x : new float[]{0.0f, -0.3f, 0.3f}) {
            for (float z : new float[]{0.0f, -0.3f, 0.3f}) {
                BlockPosX pos = new BlockPosX(AutoPotPlus.mc.player.getX() + (double) x, AutoPotPlus.mc.player.getY() - 0.5, AutoPotPlus.mc.player.getZ() + (double) z);
                if (BlockUtil.isAir(pos) || AutoPotPlus.mc.world.getBlockState(pos).getBlock() == Blocks.COBWEB)
                    continue;
                return pos;
            }
        }
        return null;
    }

    public boolean isThrow() {
        return this.throwing;
    }

    public boolean checkThrow(StatusEffect targetEffect) {
        if (this.isOff()) {
            return false;
        }
        if (!(AutoPotPlus.mc.currentScreen == null || AutoPotPlus.mc.currentScreen instanceof ChatScreen || AutoPotPlus.mc.currentScreen instanceof InventoryScreen || AutoPotPlus.mc.currentScreen instanceof ClickGuiScreen || AutoPotPlus.mc.currentScreen instanceof GameMenuScreen)) {
            return false;
        }
        if (this.usingPause.getValue() && AutoPotPlus.mc.player.isUsingItem()) {
            return false;
        }
        return AutoPotPlus.findPotion(targetEffect) != -1 || this.inventory.getValue() && AutoPotPlus.findPotionInventorySlot(targetEffect) != -1;
    }
}
