package me.hextech.remapped;

import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.SliderSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting distance = this.add(new SliderSetting("Distance", 3.0, 0.0, 8.0, 0.1));

    public NoFall() {
        super("NoFall", Module_JlagirAibYQgkHtbRnhw.Player);
        this.setDescription("Prevents fall damage.");
    }

    @Override
    public String getInfo() {
        return "SpoofGround";
    }

    @EventHandler
    public void onPacketSend(PacketEvent event) {
        if (NoFall.nullCheck()) {
            return;
        }
        for (ItemStack is : NoFall.mc.field_1724.method_5661()) {
            if (is.method_7909() != Items.field_8833) continue;
            return;
        }
        Object t = event.getPacket();
        if (t instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket)t;
            if (NoFall.mc.field_1724.field_6017 >= (float)this.distance.getValue()) {
                ((IPlayerMoveC2SPacket)packet).setOnGround(true);
            }
        }
    }
}
