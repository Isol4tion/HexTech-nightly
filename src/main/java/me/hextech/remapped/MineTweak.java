package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;

public class MineTweak
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static MineTweak INSTANCE;
    public final BooleanSetting noEntityTrace = this.add(new BooleanSetting("NoEntityTrace", true).setParent());
    public final BooleanSetting onlyPickaxe = this.add(new BooleanSetting("OnlyPickaxe", true, v -> this.noEntityTrace.isOpen()));
    public final BooleanSetting multiTask = this.add(new BooleanSetting("MultiTask", true));
    public final BooleanSetting ghostHand = this.add(new BooleanSetting("GhostHand", true));
    private final BooleanSetting noAbort = this.add(new BooleanSetting("NoAbort", true));
    private final BooleanSetting noReset = this.add(new BooleanSetting("NoReset", true));
    private final BooleanSetting noDelay = this.add(new BooleanSetting("NoDelay", true));
    private final BooleanSetting pickaxeSwitch = this.add(new BooleanSetting("SwitchEat", true));
    public boolean isActive;
    boolean swapped = false;
    int lastSlot = 0;

    public MineTweak() {
        super("MineTweak", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.pickaxeSwitch.getValue()) {
            if (!(MineTweak.mc.player.method_6047().method_7909() instanceof PickaxeItem) && MineTweak.mc.player.method_6047().method_7909() != Items.field_8367) {
                this.swapped = false;
                return;
            }
            int gapple = InventoryUtil.findItem(Items.field_8367);
            if (gapple == -1) {
                if (this.swapped) {
                    InventoryUtil.switchToSlot(this.lastSlot);
                    this.swapped = false;
                }
                return;
            }
            if (MineTweak.mc.field_1690.field_1904.method_1434()) {
                if (MineTweak.mc.player.method_6047().method_7909() instanceof PickaxeItem && MineTweak.mc.player.method_6079().method_7909() != Items.field_8367) {
                    this.lastSlot = MineTweak.mc.player.method_31548().field_7545;
                    InventoryUtil.switchToSlot(gapple);
                    this.swapped = true;
                }
            } else if (this.swapped) {
                InventoryUtil.switchToSlot(this.lastSlot);
                this.swapped = false;
            }
        }
    }

    @Override
    public void onDisable() {
        this.isActive = false;
    }

    public boolean noAbort() {
        return this.isOn() && this.noAbort.getValue() && !MineTweak.mc.field_1690.field_1904.method_1434();
    }

    public boolean noReset() {
        return this.isOn() && this.noReset.getValue();
    }

    public boolean noDelay() {
        return this.isOn() && this.noDelay.getValue();
    }

    public boolean multiTask() {
        return this.isOn() && this.multiTask.getValue();
    }

    public boolean noEntityTrace() {
        if (this.isOff() || !this.noEntityTrace.getValue()) {
            return false;
        }
        if (this.onlyPickaxe.getValue()) {
            return MineTweak.mc.player.method_6047().method_7909() instanceof PickaxeItem || MineTweak.mc.player.method_6115() && !(MineTweak.mc.player.method_6047().method_7909() instanceof SwordItem);
        }
        return true;
    }

    public boolean ghostHand() {
        return this.isOn() && this.ghostHand.getValue() && !MineTweak.mc.field_1690.field_1904.method_1434() && !MineTweak.mc.field_1690.field_1832.method_1434();
    }
}
