package me.hextech.mod.modules.impl.player;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.math.Timer;
import me.hextech.asm.accessors.IPlayerPositionLookS2CPacket;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class NoRotateSet
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static NoRotateSet INSTANCE;
    public final BooleanSetting rotate = this.add(new BooleanSetting("Apply", false));
    public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 50, 0, 100));
    private final Timer lagTimer = new Timer();

    public NoRotateSet() {
        super("NoRotateSet", Category.Player);
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        Object t;
        if (NoRotateSet.nullCheck()) {
            return;
        }
        if (!this.lagTimer.passedMs(this.lagTime.getValueInt())) {
            return;
        }
        if (!this.rotate.getValue() && (t = event.getPacket()) instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket) t;
            if (packet.getFlags().contains(PositionFlag.Y_ROT)) {
                ((IPlayerPositionLookS2CPacket) packet).setYaw(0.0f);
            } else {
                ((IPlayerPositionLookS2CPacket) packet).setYaw(NoRotateSet.mc.player.getYaw());
            }
            if (packet.getFlags().contains(PositionFlag.X_ROT)) {
                ((IPlayerPositionLookS2CPacket) packet).setPitch(0.0f);
            } else {
                ((IPlayerPositionLookS2CPacket) packet).setPitch(NoRotateSet.mc.player.getPitch());
            }
            if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                this.lagTimer.reset();
            }
        }
    }
}
