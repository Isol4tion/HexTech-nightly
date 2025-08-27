package me.hextech.remapped;

import me.hextech.asm.accessors.IUpdateSelectedSlotS2CPacket;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.BypassSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

public class BypassSetting_RInKGmTQYgWFRhsUOiJP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static BypassSetting_RInKGmTQYgWFRhsUOiJP INSTANCE;
    public final EnumSetting<BypassSetting> page = this.add(new EnumSetting<BypassSetting>("Page", BypassSetting.NCP));
    public final BooleanSetting grim = this.add(new BooleanSetting("GrimAC[!Test]", false, v -> this.page.getValue() == BypassSetting.Grim));
    public final BooleanSetting grimvelocity = this.add(new BooleanSetting("inVeloCity", false, v -> this.page.getValue() == BypassSetting.Grim));
    public final BooleanSetting ongroundsync = this.add(new BooleanSetting("OnGroundSync", false, v -> this.page.getValue() == BypassSetting.NCP));
    public final BooleanSetting SwapSync = this.add(new BooleanSetting("SwapSync", true, v -> this.page.getValue() == BypassSetting.NCP));
    public final BooleanSetting rotatepacket = this.add(new BooleanSetting("RotatePacket", false, v -> this.page.getValue() == BypassSetting.NCP));
    public final BooleanSetting timerPacket = this.add(new BooleanSetting("TimerPacket", false, v -> this.page.getValue() == BypassSetting.NCP));
    public final BooleanSetting positionPacket = this.add(new BooleanSetting("PositionPacket", false, v -> this.page.getValue() == BypassSetting.NCP));

    public BypassSetting_RInKGmTQYgWFRhsUOiJP() {
        super("BypassSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (this.SwapSync.getValue()) {
            if (BypassSetting_RInKGmTQYgWFRhsUOiJP.nullCheck()) {
                return;
            }
            Object t = event.getPacket();
            if (t instanceof UpdateSelectedSlotS2CPacket) {
                UpdateSelectedSlotS2CPacket packet = (UpdateSelectedSlotS2CPacket)t;
                int slot = BypassSetting_RInKGmTQYgWFRhsUOiJP.mc.player.method_31548().selectedSlot;
                if (packet.method_11803() != slot) {
                    ((IUpdateSelectedSlotS2CPacket)packet).setslot(slot);
                    InventoryUtil.switchToSlot(slot);
                    EntityUtil.syncInventory();
                }
            }
        }
    }
}
