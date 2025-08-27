package me.hextech.remapped;

import java.util.List;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        super("AutoPot+", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    public static int findPotionInventorySlot(StatusEffect targetEffect) {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = AutoPotPlus.mc.player.getInventory().method_5438(i);
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
        if (AutoPotPlus.mc.player == null) {
            return;
        }
        this.target = CombatUtil.getClosestEnemy(this.rang.getValue());
        if (this.checkRang.getValue() && this.target == null) {
            return;
        }
        this.throwPos = null;
        this.throwPos = this.findPos();
        if (!(this.checkFly.getValue() && AutoPotPlus.mc.player.method_6128() || this.smartCheckGround.getValue() && this.throwPos == null)) {
            if (this.speed.getValue() && !AutoPotPlus.mc.player.method_6059(StatusEffects.field_5904)) {
                this.throwing = this.checkThrow(StatusEffects.field_5904);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.field_5904);
                    return;
                }
            }
            if (this.resistance.getValue() && (!AutoPotPlus.mc.player.method_6059(StatusEffects.field_5907) || AutoPotPlus.mc.player.method_6112(StatusEffects.field_5907).getAmplifier() < 2)) {
                this.throwing = this.checkThrow(StatusEffects.field_5907);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.field_5907);
                }
            }
            if (this.strength.getValue() && !AutoPotPlus.mc.player.method_6059(StatusEffects.field_5910)) {
                this.throwing = this.checkThrow(StatusEffects.field_5910);
                if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                    this.throwPotion(StatusEffects.field_5910);
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
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapDegrees((float)yaw), MathHelper.wrapDegrees((float)pitch)};
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
                BlockPosX pos = new BlockPosX(AutoPotPlus.mc.player.getX() + (double)x, AutoPotPlus.mc.player.getY() - 0.5, AutoPotPlus.mc.player.getZ() + (double)z);
                if (BlockUtil.isAir(pos) || AutoPotPlus.mc.world.getBlockState((BlockPos)pos).getBlock() == Blocks.COBWEB) continue;
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
