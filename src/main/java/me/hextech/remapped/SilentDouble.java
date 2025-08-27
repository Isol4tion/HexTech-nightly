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
            if (this.pause.getValue() && SilentDouble.mc.player.isUsingItem()) {
                return;
            }
            if (this.groundcheck.getValue() && !SilentDouble.mc.player.isOnGround()) {
                return;
            }
            if (SpeedMine.secondPos != null && !SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos) == -1 ? SilentDouble.mc.player.getInventory().selectedSlot : this.getTool(SpeedMine.secondPos), this.dmg.getValueFloat()))) {
                slotMain = SilentDouble.mc.player.getInventory().selectedSlot;
            }
            if (SpeedMine.secondPos != null && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos), this.lastdmg.getValueFloat()))) {
                if (SilentDouble.mc.player.getMainHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                    if (!SilentDouble.mc.options.useKey.isPressed()) {
                        SilentDouble.mc.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(this.getTool(SpeedMine.secondPos)));
                        swithc2 = 1;
                    } else if (swithc2 == 1) {
                        SilentDouble.mc.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(slotMain));
                        if (this.syncinv.getValue()) {
                            EntityUtil.syncInventory();
                        }
                    }
                } else {
                    SilentDouble.mc.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(this.getTool(SpeedMine.secondPos)));
                    swithc2 = 1;
                }
            }
            if (SpeedMine.secondPos != null && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos), this.enddmg.getValueFloat())) && swithc2 == 1) {
                SilentDouble.mc.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(slotMain));
                if (this.syncinv.getValue()) {
                    EntityUtil.syncInventory();
                }
            }
        }
        if (this.mode.getValue().equals((Object)_JPSoqiNZyGTsyFDufNku.Switch)) {
            this.sendPacket((Packet<?>)new UpdateSelectedSlotC2SPacket(SilentDouble.mc.player.getInventory().selectedSlot));
        } else if (this.mode.getValue() == _JPSoqiNZyGTsyFDufNku.Switch && SpeedMine.INSTANCE.lastSlot != -1) {
            SilentDouble.mc.player.getInventory().selectedSlot = SpeedMine.INSTANCE.lastSlot;
            ((IClientPlayerInteractionManager)SilentDouble.mc.interactionManager).syncSelected();
            SpeedMine.INSTANCE.lastSlot = -1;
        }
        boolean canUpdate = false;
    }

    public int getTool(BlockPos pos) {
        return SpeedMine.INSTANCE.getTool(pos) == -1 ? SilentDouble.mc.player.getInventory().selectedSlot : SpeedMine.INSTANCE.getTool(pos);
    }

    public static enum _JPSoqiNZyGTsyFDufNku {
        SilentDouble,
        Switch,
        None;

    }
}
