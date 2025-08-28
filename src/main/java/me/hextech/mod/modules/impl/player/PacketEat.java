package me.hextech.mod.modules.impl.player;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class PacketEat
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PacketEat INSTANCE;
    private final BooleanSetting deSync = this.add(new BooleanSetting("noSync", false));

    public PacketEat() {
        super("PacketEat", Category.Player);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.deSync.getValue() && PacketEat.mc.player.isUsingItem() && PacketEat.mc.player.getActiveItem().getItem().isFood()) {
            PacketEat.mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, BlockUtil.getWorldActionId(PacketEat.mc.world)));
        }
    }

    @EventHandler
    public void onPacket(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        PlayerActionC2SPacket packet;
        Object t = event.getPacket();
        if (t instanceof PlayerActionC2SPacket && (packet = (PlayerActionC2SPacket)t).getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM && PacketEat.mc.player.getActiveItem().getItem().isFood()) {
            event.cancel();
        }
    }
}
