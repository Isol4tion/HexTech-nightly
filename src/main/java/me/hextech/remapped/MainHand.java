package me.hextech.remapped;

import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OyveyExplosionUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MainHand
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static MainHand INSTANCE;
    public final BooleanSetting mineSlot = this.add(new BooleanSetting("MineSlot", true).setParent());
    public final BooleanSetting pauseEat = this.add(new BooleanSetting("PauseEat", false, v -> this.mineSlot.isOpen()));
    public final BooleanSetting totemPause = this.add(new BooleanSetting("TotemPause", false, v -> this.mineSlot.isOpen()));
    public final BooleanSetting totem = this.add(new BooleanSetting("TotemSlot", true).setParent());
    public final BooleanSetting minePause = this.add(new BooleanSetting("MinePause", false, v -> this.totem.isOpen()));
    private final EnumSetting<_TQxYHQLcjCDwbwqUjIlv> mode = this.add(new EnumSetting<_TQxYHQLcjCDwbwqUjIlv>("Mode", _TQxYHQLcjCDwbwqUjIlv.Double, v -> this.mineSlot.isOpen()));
    private final SliderSetting damage = this.add(new SliderSetting("Damage", 0.9, 0.0, 1.0, 0.01, v -> this.mineSlot.isOpen()));
    private final SliderSetting time = this.add(new SliderSetting("Time", 1.0, 0.0, 10.0, 1.0, v -> this.mineSlot.isOpen()));
    private final SliderSetting forceHealth = this.add(new SliderSetting("SlotHealth", 3.0, 0.0, 36.0, 0.1, v -> this.totem.isOpen()));
    private final SliderSetting slotPosition = this.add(new SliderSetting("SlotPosition", 1.0, 1.0, 9.0, 1.0, v -> this.totem.isOpen()));
    private final Timer timer = new Timer();
    boolean needSwitch = false;
    int old = -1;
    int slot = -1;
    int slot2 = -1;
    boolean handSlot = false;

    public MainHand() {
        super("MainHand", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static int findItemInventorySlot(Item item) {
        for (int i = 35; i >= 0; --i) {
            ItemStack stack = MainHand.mc.player.getInventory().getStack(i);
            if (stack.getItem() != item) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    @Override
    public String getInfo() {
        if (this.mode.getValue() == _TQxYHQLcjCDwbwqUjIlv.Double) {
            return "Double";
        }
        return "All";
    }

    public void updateTotem() {
        if (!this.totem.getValue()) {
            return;
        }
        boolean bl = this.handSlot = (double)(MainHand.mc.player.getAbsorptionAmount() + MainHand.mc.player.getHealth()) - this.getCrystal() <= this.forceHealth.getValue();
        if (this.minePause.getValue() && this.needSwitch && !MainHand.mc.player.isUsingItem()) {
            return;
        }
        if (MainHand.mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        if (this.handSlot) {
            int item = MainHand.findItemInventorySlot(Items.TOTEM_OF_UNDYING);
            InventoryUtil.switchToSlot(this.slotPosition.getValueInt() - 1);
            this.doSwap(item);
            EntityUtil.syncInventory();
        }
    }

    public void updateMine() {
        if (MainHand.mc.player.isDead()) {
            this.needSwitch = false;
        }
        if (!this.mineSlot.getValue()) {
            return;
        }
        if (this.handSlot && this.totemPause.getValue()) {
            return;
        }
        if (this.pauseEat.getValue() && MainHand.mc.player.isUsingItem() && MainHand.mc.player.getActiveHand() == Hand.MAIN_HAND) {
            return;
        }
        if (this.mode.getValue() == _TQxYHQLcjCDwbwqUjIlv.All) {
            if (SpeedMine.INSTANCE.isOn()) {
                this.slot = SpeedMine.INSTANCE.getTool(SpeedMine.breakPos);
                this.slot2 = SpeedMine.INSTANCE.getTool(SpeedMine.secondPos);
            }
            if (this.slot == -1 && this.slot2 == -1) {
                return;
            }
            if (!this.needSwitch) {
                this.old = MainHand.mc.player.getInventory().selectedSlot;
            }
            if (this.needSwitch && this.timer.passedMs(this.time.getValue() * 50.0)) {
                MainHand.mc.player.getInventory().selectedSlot = this.old;
                this.needSwitch = false;
            }
            if (SpeedMine.breakPos != null && (double)MathHelper.sqrt((float)((float)EntityUtil.getEyesPos().squaredDistanceTo(SpeedMine.breakPos.toCenterPos()))) <= SpeedMine.INSTANCE.range.getValue() && (!BlockUtil.isAir(SpeedMine.breakPos) || SpeedMine.secondPos != null) && (SpeedMine.INSTANCE.done || SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.slot, this.damage.getValue())))) {
                this.needSwitch = true;
                if (MainHand.mc.player.getInventory().selectedSlot != this.slot) {
                    MainHand.mc.player.getInventory().selectedSlot = this.slot;
                }
                this.timer.reset();
            } else if (SpeedMine.breakPos == null && SpeedMine.secondPos != null && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.slot, this.damage.getValue()))) {
                this.needSwitch = true;
                if (MainHand.mc.player.getInventory().selectedSlot != this.slot2) {
                    MainHand.mc.player.getInventory().selectedSlot = this.slot2;
                }
                this.timer.reset();
            }
        } else {
            this.slot2 = SpeedMine.INSTANCE.getTool(SpeedMine.secondPos);
            if (this.slot2 == -1) {
                return;
            }
            if (!this.needSwitch) {
                this.old = MainHand.mc.player.getInventory().selectedSlot;
            }
            if (this.needSwitch && this.timer.passedMs(this.time.getValue() * 50.0)) {
                MainHand.mc.player.getInventory().selectedSlot = this.old;
                this.needSwitch = false;
            }
            if (SpeedMine.INSTANCE.isOn() && SpeedMine.secondPos != null && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.slot2, this.damage.getValue()))) {
                this.needSwitch = true;
                if (MainHand.mc.player.getInventory().selectedSlot != this.slot2) {
                    MainHand.mc.player.getInventory().selectedSlot = this.slot2;
                }
                this.timer.reset();
            }
        }
    }

    public double getCrystal() {
        double maxDMG = 0.0;
        for (Entity entity : MainHand.mc.world.getEntities()) {
            double dmg;
            EndCrystalEntity endCrystal;
            if (!(entity instanceof EndCrystalEntity) || (endCrystal = (EndCrystalEntity)entity).squaredDistanceTo(MainHand.mc.player.getEyePos()) > 25.0 || !((dmg = (double)this.calculateDamage(endCrystal.getBlockPos().toCenterPos(), (PlayerEntity)MainHand.mc.player, (PlayerEntity)MainHand.mc.player)) > maxDMG)) continue;
            maxDMG = dmg;
        }
        return maxDMG;
    }

    private void doSwap(int slot) {
        InventoryUtil.inventorySwap(slot, MainHand.mc.player.getInventory().selectedSlot);
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        float damage = OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), (Entity)player, (Entity)predict, 6.0f);
        return damage;
    }

    @Override
    public void onUpdate() {
        if (MainHand.nullCheck()) {
            return;
        }
        this.updateTotem();
        this.updateMine();
    }

    public static enum _TQxYHQLcjCDwbwqUjIlv {
        All,
        Double;

    }

    public static enum _zejtTTXMdfbxiOFVgdxG {
        Mine,
        Totem;

    }
}
