package me.hextech.mod.modules.impl.movement;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting distance = this.add(new SliderSetting("Distance", 3.0, 0.0, 8.0, 0.1));

    public NoFall() {
        super("NoFall", Category.Player);
        this.setDescription("Prevents fall damage.");
    }

    @Override
    public String getInfo() {
        return "SpoofGround";
    }

    @EventHandler
    public void onPacketSend(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        if (NoFall.nullCheck()) {
            return;
        }
        for (ItemStack is : NoFall.mc.player.getArmorItems()) {
            if (is.getItem() != Items.ELYTRA) continue;
            return;
        }
        Object t = event.getPacket();
        if (t instanceof PlayerMoveC2SPacket packet) {
            if (NoFall.mc.player.fallDistance >= (float)this.distance.getValue()) {
                ((IPlayerMoveC2SPacket)packet).setOnGround(true);
            }
        }
    }
}
