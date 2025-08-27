package me.hextech.remapped;

import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class PacketEat
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PacketEat INSTANCE;
    private final BooleanSetting deSync = this.add(new BooleanSetting("noSync", false));

    public PacketEat() {
        super("PacketEat", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.deSync.getValue() && PacketEat.mc.player.method_6115() && PacketEat.mc.player.method_6030().getItem().method_19263()) {
            PacketEat.mc.player.networkHandler.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, BlockUtil.getWorldActionId(PacketEat.mc.world)));
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        PlayerActionC2SPacket packet;
        Object t = event.getPacket();
        if (t instanceof PlayerActionC2SPacket && (packet = (PlayerActionC2SPacket)t).getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM && PacketEat.mc.player.method_6030().getItem().method_19263()) {
            event.cancel();
        }
    }
}
