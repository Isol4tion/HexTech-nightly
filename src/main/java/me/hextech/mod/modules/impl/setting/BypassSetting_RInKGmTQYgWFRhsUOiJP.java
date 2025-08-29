package me.hextech.mod.modules.impl.setting;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.asm.accessors.IUpdateSelectedSlotS2CPacket;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

public class BypassSetting_RInKGmTQYgWFRhsUOiJP
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static BypassSetting_RInKGmTQYgWFRhsUOiJP INSTANCE;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.NCP));
    public final BooleanSetting grim = this.add(new BooleanSetting("GrimAC[!Test]", false, v -> this.page.getValue() == Page.Grim));
    public final BooleanSetting grimvelocity = this.add(new BooleanSetting("inVeloCity", false, v -> this.page.getValue() == Page.Grim));
    public final BooleanSetting ongroundsync = this.add(new BooleanSetting("OnGroundSync", false, v -> this.page.getValue() == Page.NCP));
    public final BooleanSetting SwapSync = this.add(new BooleanSetting("SwapSync", true, v -> this.page.getValue() == Page.NCP));
    public final BooleanSetting rotatepacket = this.add(new BooleanSetting("RotatePacket", false, v -> this.page.getValue() == Page.NCP));
    public final BooleanSetting timerPacket = this.add(new BooleanSetting("TimerPacket", false, v -> this.page.getValue() == Page.NCP));
    public final BooleanSetting positionPacket = this.add(new BooleanSetting("PositionPacket", false, v -> this.page.getValue() == Page.NCP));

    public BypassSetting_RInKGmTQYgWFRhsUOiJP() {
        super("BypassSetting", Category.Setting);
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        if (this.SwapSync.getValue()) {
            if (BypassSetting_RInKGmTQYgWFRhsUOiJP.nullCheck()) {
                return;
            }
            Object t = event.getPacket();
            if (t instanceof UpdateSelectedSlotS2CPacket packet) {
                int slot = BypassSetting_RInKGmTQYgWFRhsUOiJP.mc.player.getInventory().selectedSlot;
                if (packet.getSlot() != slot) {
                    ((IUpdateSelectedSlotS2CPacket) packet).setslot(slot);
                    InventoryUtil.switchToSlot(slot);
                    EntityUtil.syncInventory();
                }
            }
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Page {
        NCP,
        Grim

    }
}
