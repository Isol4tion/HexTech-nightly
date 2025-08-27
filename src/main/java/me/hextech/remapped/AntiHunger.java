package me.hextech.remapped;

import io.netty.buffer.Unpooled;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import org.jetbrains.annotations.NotNull;

public class AntiHunger extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AntiHunger INSTANCE;
   public final BooleanSetting sprint = this.add(new BooleanSetting("Sprint", true));
   public final BooleanSetting ground = this.add(new BooleanSetting("Ground", true));

   public AntiHunger() {
      super("AntiHunger", "lol", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   public static Entity getEntity(@NotNull PlayerInteractEntityC2SPacket packet) {
      PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
      packet.method_11052(packetBuf);
      return mc.field_1687.method_8469(packetBuf.method_10816());
   }

   public static AntiHunger_zYbEBAOiuFfDBojQHScp getInteractType(@NotNull PlayerInteractEntityC2SPacket packet) {
      PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
      packet.method_11052(packetBuf);
      packetBuf.method_10816();
      return (AntiHunger_zYbEBAOiuFfDBojQHScp)packetBuf.method_10818(AntiHunger_zYbEBAOiuFfDBojQHScp.class);
   }

   @EventHandler(
      priority = -100
   )
   public void onPacketSend(PacketEvent event) {
      if (event.getPacket() instanceof ClientCommandC2SPacket packet && this.sprint.getValue() && packet.method_12365() == Mode.field_12981) {
         event.cancel();
      }

      if (event.getPacket() instanceof PlayerMoveC2SPacket && this.ground.getValue() && mc.field_1724.field_6017 <= 0.0F && !mc.field_1761.method_2923()) {
         ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(false);
      }
   }
}
