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
        if (this.deSync.getValue() && PacketEat.mc.field_1724.method_6115() && PacketEat.mc.field_1724.method_6030().method_7909().method_19263()) {
            PacketEat.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.field_5808, BlockUtil.getWorldActionId(PacketEat.mc.field_1687)));
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        PlayerActionC2SPacket packet;
        Object t = event.getPacket();
        if (t instanceof PlayerActionC2SPacket && (packet = (PlayerActionC2SPacket)t).method_12363() == PlayerActionC2SPacket.Action.field_12974 && PacketEat.mc.field_1724.method_6030().method_7909().method_19263()) {
            event.cancel();
        }
    }
}
