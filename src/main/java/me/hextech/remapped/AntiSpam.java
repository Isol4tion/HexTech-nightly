package me.hextech.remapped;

import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.StringSetting;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AntiSpam
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final StringSetting name = this.add(new StringSetting("Name", "zhuan_gan_"));

    public AntiSpam() {
        super("AntiSpam", Module_JlagirAibYQgkHtbRnhw.Misc);
    }

    @EventHandler
    private void PacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu receive) {
        GameMessageS2CPacket e;
        Object t = receive.getPacket();
        if (t instanceof GameMessageS2CPacket && (e = (GameMessageS2CPacket)t).content().getString().contains(this.name.getValue())) {
            receive.cancel();
        }
    }
}
