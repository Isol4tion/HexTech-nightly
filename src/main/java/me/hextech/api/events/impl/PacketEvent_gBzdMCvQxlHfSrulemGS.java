package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.network.packet.Packet;

public class PacketEvent_gBzdMCvQxlHfSrulemGS
        extends Event_auduwKaxKOWXRtyJkCPb {
    private final Packet<?> packet;

    public PacketEvent_gBzdMCvQxlHfSrulemGS(Packet<?> packet, Stage stage) {
        super(stage);
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class Send
            extends PacketEvent_gBzdMCvQxlHfSrulemGS {
        public Send(Packet<?> packet) {
            super(packet, Stage.Pre);
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class SendPost
            extends PacketEvent_gBzdMCvQxlHfSrulemGS {
        public SendPost(Packet<?> packet) {
            super(packet, Stage.Post);
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class Receive
            extends PacketEvent_gBzdMCvQxlHfSrulemGS {
        public Receive(Packet<?> packet) {
            super(packet, Stage.Pre);
        }
    }
}
