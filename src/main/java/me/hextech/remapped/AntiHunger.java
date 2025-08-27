package me.hextech.remapped;

import io.netty.buffer.Unpooled;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.jetbrains.annotations.NotNull;

public class AntiHunger
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AntiHunger INSTANCE;
    public final BooleanSetting sprint = this.add(new BooleanSetting("Sprint", true));
    public final BooleanSetting ground = this.add(new BooleanSetting("Ground", true));

    public AntiHunger() {
        super("AntiHunger", "lol", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    public static Entity getEntity(@NotNull PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        return AntiHunger.mc.world.getEntityById(packetBuf.readVarInt());
    }

    public static AntiHunger_zYbEBAOiuFfDBojQHScp getInteractType(@NotNull PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        packetBuf.readVarInt();
        return (AntiHunger_zYbEBAOiuFfDBojQHScp)packetBuf.readEnumConstant(AntiHunger_zYbEBAOiuFfDBojQHScp.class);
    }

    @EventHandler(priority=-100)
    public void onPacketSend(PacketEvent event) {
        Object t = event.getPacket();
        if (t instanceof ClientCommandC2SPacket) {
            ClientCommandC2SPacket packet = (ClientCommandC2SPacket)t;
            if (this.sprint.getValue() && packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
                event.cancel();
            }
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket && this.ground.getValue() && AntiHunger.mc.player.fallDistance <= 0.0f && !AntiHunger.mc.interactionManager.isBreakingBlock()) {
            ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(false);
        }
    }

    public enum AntiHunger_zYbEBAOiuFfDBojQHScp {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }
}
