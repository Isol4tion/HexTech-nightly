package me.hextech.remapped;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;

public class PacketEat extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static PacketEat INSTANCE;
   private final BooleanSetting deSync = this.add(new BooleanSetting("noSync", false));

   public PacketEat() {
      super("PacketEat", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (this.deSync.getValue() && mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909().method_19263()) {
         mc.field_1724.field_3944.method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, BlockUtil.getWorldActionId(mc.field_1687)));
      }
   }

   @EventHandler
   public void onPacket(PacketEvent event) {
      if (event.getPacket() instanceof PlayerActionC2SPacket packet
         && packet.method_12363() == Action.field_12974
         && mc.field_1724.method_6030().method_7909().method_19263()) {
         event.cancel();
      }
   }
}
