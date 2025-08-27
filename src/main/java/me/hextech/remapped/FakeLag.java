package me.hextech.remapped;

import me.hextech.remapped.FakeLag_pNelqtbEdFyayuoaPLch;
import me.hextech.remapped.Timer;
import me.hextech.remapped.Wrapper;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

/*
 * Exception performing whole class analysis ignored.
 */
private static class FakeLag {
    final Timer timer;
    final int delay;
    Packet pp;
    final /* synthetic */ FakeLag_pNelqtbEdFyayuoaPLch this$0;

    public FakeLag(FakeLag_pNelqtbEdFyayuoaPLch fakeLag_pNelqtbEdFyayuoaPLch, Packet p) {
        this.this$0 = fakeLag_pNelqtbEdFyayuoaPLch;
        this.pp = p;
        this.timer = new Timer();
        this.delay = fakeLag_pNelqtbEdFyayuoaPLch.spoof.getValueInt();
    }

    public boolean send() {
        if (this.timer.passedMs(this.delay)) {
            this.apply();
            return true;
        }
        return false;
    }

    public void apply() {
        if (this.pp != null) {
            this.pp.method_11054((PacketListener)Wrapper.mc.player.field_3944);
        }
    }
}
