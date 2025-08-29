package me.hextech.mod.modules.impl.misc;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.HashMap;

public class AutoQueue_kggvJAWJdfERirsncrmh
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HashMap<String, String> asks;
    public static boolean inQueue;
    private final BooleanSetting queueCheck = this.add(new BooleanSetting("QueueCheck", true));

    public AutoQueue_kggvJAWJdfERirsncrmh() {
        super("AutoQueue", Category.Misc);
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
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive e) {
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
