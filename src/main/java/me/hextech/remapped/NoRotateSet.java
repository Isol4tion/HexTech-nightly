package me.hextech.remapped;

import me.hextech.asm.accessors.IPlayerPositionLookS2CPacket;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class NoRotateSet
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static NoRotateSet INSTANCE;
    public final BooleanSetting rotate = this.add(new BooleanSetting("Apply", false));
    public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 50, 0, 100));
    private final Timer lagTimer = new Timer();

    public NoRotateSet() {
        super("NoRotateSet", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        Object t;
        if (NoRotateSet.nullCheck()) {
            return;
        }
        if (!this.lagTimer.passedMs(this.lagTime.getValueInt())) {
            return;
        }
        if (!this.rotate.getValue() && (t = event.getPacket()) instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket)t;
            if (packet.method_11733().contains(PositionFlag.Y_ROT)) {
                ((IPlayerPositionLookS2CPacket)packet).setYaw(0.0f);
            } else {
                ((IPlayerPositionLookS2CPacket)packet).setYaw(NoRotateSet.mc.player.getYaw());
            }
            if (packet.method_11733().contains(PositionFlag.X_ROT)) {
                ((IPlayerPositionLookS2CPacket)packet).setPitch(0.0f);
            } else {
                ((IPlayerPositionLookS2CPacket)packet).setPitch(NoRotateSet.mc.player.method_36455());
            }
            if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                this.lagTimer.reset();
            }
        }
    }
}
