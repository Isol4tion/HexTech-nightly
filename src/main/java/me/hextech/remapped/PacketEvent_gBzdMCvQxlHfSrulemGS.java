package me.hextech.remapped;

import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.network.packet.Packet;

public class PacketEvent_gBzdMCvQxlHfSrulemGS
extends Event_auduwKaxKOWXRtyJkCPb {
    private final Packet<?> packet;

    public PacketEvent_gBzdMCvQxlHfSrulemGS(Packet<?> packet, Stage stage) {
        super(stage);
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }
}
