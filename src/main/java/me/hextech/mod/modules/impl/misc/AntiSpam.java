package me.hextech.mod.modules.impl.misc;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.StringSetting;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AntiSpam
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final StringSetting name = this.add(new StringSetting("Name", "zhuan_gan_"));

    public AntiSpam() {
        super("AntiSpam", Category.Misc);
    }

    @EventHandler
    private void PacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive receive) {
        GameMessageS2CPacket e;
        Object t = receive.getPacket();
        if (t instanceof GameMessageS2CPacket && (e = (GameMessageS2CPacket) t).content().getString().contains(this.name.getValue())) {
            receive.cancel();
        }
    }
}
