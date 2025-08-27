package me.hextech.remapped;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class IncreasesTime extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static IncreasesTime INSTANCE;
   public final SliderSetting multiplier = this.add(new SliderSetting("Speed", 1.0, 0.1, 5.0, 0.5));
   public final BindSetting activekey = this.add(new BindSetting("KeyBind", -1));
   public final SliderSetting active = this.add(new SliderSetting("ActiveSpeed", 1.0, 0.1, 10.0, 0.01));
   public final SliderSetting longTime = this.add(new SliderSetting("LongTime", 0, 0, 150));
   long lastMs = (long)this.longTime.getValueInt();

   public IncreasesTime() {
      super("IncreasesTime", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   @Override
   public void onDisable() {
      me.hextech.HexTech.TIMER.reset();
   }

   @Override
   public void onUpdate() {
      me.hextech.HexTech.TIMER.tryReset();
   }

   @Override
   public void onEnable() {
      me.hextech.HexTech.TIMER.reset();
   }

   @EventHandler
   public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
         this.lastMs = (long)this.longTime.getValueInt();
      }
   }
}
