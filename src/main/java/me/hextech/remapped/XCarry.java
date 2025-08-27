package me.hextech.remapped;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public final BooleanSetting Move = this.add(new BooleanSetting("MoveItem", true));
   Screen lastScreen = null;

   public XCarry() {
      super("XCarry", Module_JlagirAibYQgkHtbRnhw.Player);
   }

   @Override
   public void onUpdate() {
      if (mc.field_1755 != null
         && !(mc.field_1755 instanceof GameMenuScreen)
         && !(mc.field_1755 instanceof ChatScreen)
         && !(mc.field_1755 instanceof ClickGuiScreen)) {
         this.lastScreen = mc.field_1755;
      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent event) {
      if (this.lastScreen instanceof InventoryScreen && event.getPacket() instanceof CloseHandledScreenC2SPacket) {
         event.cancel();
      }
   }
}
