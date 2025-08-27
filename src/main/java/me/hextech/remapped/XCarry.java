package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final BooleanSetting Move = this.add(new BooleanSetting("MoveItem", true));
    Screen lastScreen = null;

    public XCarry() {
        super("XCarry", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @Override
    public void onUpdate() {
        if (!(XCarry.mc.currentScreen == null || XCarry.mc.currentScreen instanceof GameMenuScreen || XCarry.mc.currentScreen instanceof ChatScreen || XCarry.mc.currentScreen instanceof ClickGuiScreen)) {
            this.lastScreen = XCarry.mc.currentScreen;
        }
    }

    @EventHandler
    public void onPacketSend(PacketEvent event) {
        if (this.lastScreen instanceof InventoryScreen && event.getPacket() instanceof CloseHandledScreenC2SPacket) {
            event.cancel();
        }
    }
}
