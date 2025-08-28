package me.hextech.remapped.mod.modules.impl.misc;

import java.util.HashMap;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AutoQueue_kggvJAWJdfERirsncrmh
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HashMap<String, String> asks;
    public static boolean inQueue;
    private final BooleanSetting queueCheck = this.add(new BooleanSetting("QueueCheck", true));

    public AutoQueue_kggvJAWJdfERirsncrmh() {
        super("AutoQueue", Module_JlagirAibYQgkHtbRnhw.Misc);
    }

    @Override
    public void onUpdate() {
        if (AutoQueue_kggvJAWJdfERirsncrmh.nullCheck()) {
            inQueue = false;
            return;
        }
        inQueue = InventoryUtil.findItem(Items.COMPASS) != -1;
    }

    @Override
    public void onDisable() {
        inQueue = false;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu e) {
        if (!inQueue && this.queueCheck.getValue()) {
            return;
        }
        Object object = e.getPacket();
        if (object instanceof GameMessageS2CPacket packet) {
            for (String key : asks.keySet()) {
                String[] abc;
                if (!packet.content().getString().contains(key)) continue;
                for (String s : abc = new String[]{"A", "B", "C"}) {
                    if (!packet.content().getString().contains(s + "." + asks.get(key))) continue;
                    mc.getNetworkHandler().sendChatMessage(s.toLowerCase());
                    return;
                }
            }
        }
    }
}
