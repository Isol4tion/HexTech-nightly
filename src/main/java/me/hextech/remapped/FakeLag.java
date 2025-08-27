package me.hextech.remapped;

import net.minecraft.network.packet.Packet;

class FakeLag {
   final Timer timer;
   final int delay;
   Packet pp;

   public FakeLag(final FakeLag_pNelqtbEdFyayuoaPLch param1, Packet p) {
      this.this$0 = var1;
      this.pp = p;
      this.timer = new Timer();
      this.delay = var1.spoof.getValueInt();
   }

   public boolean send() {
      if (this.timer.passedMs((long)this.delay)) {
         this.apply();
         return true;
      } else {
         return false;
      }
   }

   public void apply() {
      if (this.pp != null) {
         this.pp.method_11054(Wrapper.mc.field_1724.field_3944);
      }
   }
}
