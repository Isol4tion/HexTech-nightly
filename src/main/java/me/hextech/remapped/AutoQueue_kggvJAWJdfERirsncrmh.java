package me.hextech.remapped;

import java.util.HashMap;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AutoQueue_kggvJAWJdfERirsncrmh extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HashMap<String, String> asks;
   public static boolean inQueue;
   private final BooleanSetting queueCheck = this.add(new BooleanSetting("QueueCheck", true));

   public AutoQueue_kggvJAWJdfERirsncrmh() {
      super("AutoQueue", Module_JlagirAibYQgkHtbRnhw.Misc);
   }

   @Override
   public void onUpdate() {
      if (nullCheck()) {
         inQueue = false;
      } else {
         inQueue = InventoryUtil.findItem(Items.field_8251) != -1;
      }
   }

   @Override
   public void onDisable() {
      inQueue = false;
   }

   @EventHandler
   public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu e) {
      if (inQueue || !this.queueCheck.getValue()) {
         if (e.getPacket() instanceof GameMessageS2CPacket packet) {
            for (String key : asks.keySet()) {
               if (packet.comp_763().getString().contains(key)) {
                  String[] abc = new String[]{"A", "B", "C"};

                  for (String s : abc) {
                     if (packet.comp_763().getString().contains(s + "." + (String)asks.get(key))) {
                        mc.method_1562().method_45729(s.toLowerCase());
                        return;
                     }
                  }
               }
            }
         }
      }
   }
}
