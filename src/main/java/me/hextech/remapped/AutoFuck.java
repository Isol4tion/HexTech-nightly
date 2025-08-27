package me.hextech.remapped;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AutoFuck
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 500, 0, 2000));
    private long lastTime = 0L;
    private boolean sneaking = false;

    public AutoFuck() {
        super("AutoFuck", Module_JlagirAibYQgkHtbRnhw.Misc);
    }

    @Override
    public void onEnable() {
        this.lastTime = 0L;
        this.sneaking = false;
    }

    @Override
    public void onUpdate() {
        if (AutoFuck.mc.player == null || mc.method_1562() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if ((double)(now - this.lastTime) >= this.delay.getValue()) {
            this.sneaking = !this.sneaking;
            ClientCommandC2SPacket.Mode mode = this.sneaking ? ClientCommandC2SPacket.Mode.field_12979 : ClientCommandC2SPacket.Mode.field_12984;
            mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)AutoFuck.mc.player, mode));
            this.lastTime = now;
        }
    }
}
