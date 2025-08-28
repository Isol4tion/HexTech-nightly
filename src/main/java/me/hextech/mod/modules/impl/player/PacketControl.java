package me.hextech.mod.modules.impl.player;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketControl
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PacketControl INSTANCE;
    private final SliderSetting fullPackets = this.add(new SliderSetting("FullPackets", 15, 1, 50));
    private final SliderSetting positionPackets = this.add(new SliderSetting("PositionPackets", 15, 1, 50));
    public boolean full;
    private int fullPacket;
    private int positionPacket;

    public PacketControl() {
        super("PacketControl", Category.Player);
        INSTANCE = this;
    }

    @EventHandler
    public final void onPacketSend(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket.PositionAndOnGround && !this.full) {
            ++this.positionPacket;
            if ((double)this.positionPacket >= this.positionPackets.getValue()) {
                this.positionPacket = 0;
                this.full = true;
            }
        } else if (event.getPacket() instanceof PlayerMoveC2SPacket.Full && this.full) {
            ++this.fullPacket;
            if ((double)this.fullPacket > this.fullPackets.getValue()) {
                this.fullPacket = 0;
                this.full = false;
            }
        }
    }
}
