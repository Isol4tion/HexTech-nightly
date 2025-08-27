package me.hextech.remapped;

import me.hextech.asm.mixins.IClientPlayerInteractionManager;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

public class SilentDouble
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static int slotMain;
    public static int swithc2;
    public static SilentDouble INSTANCE;
    public final EnumSetting<_JPSoqiNZyGTsyFDufNku> mode = this.add(new EnumSetting<_JPSoqiNZyGTsyFDufNku>("SwitchMode", _JPSoqiNZyGTsyFDufNku.SilentDouble));
    public final SliderSetting dmg = this.add(new SliderSetting("Switch", 0.89, 0.0, 1.5, 0.01));
    public final SliderSetting lastdmg = this.add(new SliderSetting("LastSwitch", 0.9, 0.0, 1.5, 0.01));
    public final SliderSetting enddmg = this.add(new SliderSetting("EndSwitch", 1.2, 0.0, 2.0, 0.1));
    public BooleanSetting syncinv = this.add(new BooleanSetting("InvSync", true));
    public BooleanSetting pause = this.add(new BooleanSetting("NoUsingMine", true));
    public BooleanSetting groundcheck = this.add(new BooleanSetting("GroundCheck", true));

    public SilentDouble() {
        super("SilentDouble", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        this.update();
    }

    public void update() {
        if (this.mode.getValue().equals((Object)_JPSoqiNZyGTsyFDufNku.SilentDouble)) {
            if (this.pause.getValue() && SilentDouble.mc.field_1724.method_6115()) {
                return;
            }
            if (this.groundcheck.getValue() && !SilentDouble.mc.field_1724.method_24828()) {
                return;
            }
            if (SpeedMine.secondPos != null && !SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos) == -1 ? SilentDouble.mc.field_1724.method_31548().field_7545 : this.getTool(SpeedMine.secondPos), this.dmg.getValueFloat()))) {
                slotMain = SilentDouble.mc.field_1724.method_31548().field_7545;
            }
            if (SpeedMine.secondPos != null && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos), this.lastdmg.getValueFloat()))) {
                if (SilentDouble.mc.field_1724.method_6047().method_7909() == Items.field_8367) {
                    if (!SilentDouble.mc.field_1690.field_1904.method_1434()) {
                        SilentDouble.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(this.getTool(SpeedMine.secondPos)));
                        swithc2 = 1;
                    } else if (swithc2 == 1) {
                        SilentDouble.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(slotMain));
                        if (this.syncinv.getValue()) {
                            EntityUtil.syncInventory();
                        }
                    }
                } else {
                    SilentDouble.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(this.getTool(SpeedMine.secondPos)));
                    swithc2 = 1;
                }
            }
            if (SpeedMine.secondPos != null && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos), this.enddmg.getValueFloat())) && swithc2 == 1) {
                SilentDouble.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(slotMain));
                if (this.syncinv.getValue()) {
                    EntityUtil.syncInventory();
                }
            }
        }
        if (this.mode.getValue().equals((Object)_JPSoqiNZyGTsyFDufNku.Switch)) {
            this.sendPacket((Packet<?>)new UpdateSelectedSlotC2SPacket(SilentDouble.mc.field_1724.method_31548().field_7545));
        } else if (this.mode.getValue() == _JPSoqiNZyGTsyFDufNku.Switch && SpeedMine.INSTANCE.lastSlot != -1) {
            SilentDouble.mc.field_1724.method_31548().field_7545 = SpeedMine.INSTANCE.lastSlot;
            ((IClientPlayerInteractionManager)SilentDouble.mc.field_1761).syncSelected();
            SpeedMine.INSTANCE.lastSlot = -1;
        }
        boolean canUpdate = false;
    }

    public int getTool(BlockPos pos) {
        return SpeedMine.INSTANCE.getTool(pos) == -1 ? SilentDouble.mc.field_1724.method_31548().field_7545 : SpeedMine.INSTANCE.getTool(pos);
    }

    public static final class _JPSoqiNZyGTsyFDufNku
    extends Enum<_JPSoqiNZyGTsyFDufNku> {
        public static final /* enum */ _JPSoqiNZyGTsyFDufNku SilentDouble;
        public static final /* enum */ _JPSoqiNZyGTsyFDufNku Switch;
        public static final /* enum */ _JPSoqiNZyGTsyFDufNku None;

        public static _JPSoqiNZyGTsyFDufNku[] values() {
            return null;
        }

        public static _JPSoqiNZyGTsyFDufNku valueOf(String string) {
            return null;
        }
    }
}
