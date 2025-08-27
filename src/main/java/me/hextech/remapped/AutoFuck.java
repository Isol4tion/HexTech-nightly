package me.hextech.remapped;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;

public class AutoFuck extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
      if (mc.field_1724 != null && mc.method_1562() != null) {
         long now = System.currentTimeMillis();
         if ((double)(now - this.lastTime) >= this.delay.getValue()) {
            this.sneaking = !this.sneaking;
            Mode mode = this.sneaking ? Mode.field_12979 : Mode.field_12984;
            mc.method_1562().method_52787(new ClientCommandC2SPacket(mc.field_1724, mode));
            this.lastTime = now;
         }
      }
   }
}
