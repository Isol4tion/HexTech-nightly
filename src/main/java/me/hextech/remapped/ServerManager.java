package me.hextech.remapped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class ServerManager implements Wrapper {
   private final Timer timeDelay = new Timer();
   private final ArrayDeque<Float> tpsResult = new ArrayDeque(20);
   boolean worldNull = true;
   private long time;
   private long tickTime;
   private float tps;

   public ServerManager() {
      me.hextech.HexTech.EVENT_BUS.subscribe(this);
   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   public float getTPS() {
      return round2((double)this.tps);
   }

   public float getCurrentTPS() {
      return round2((double)(20.0F * ((float)this.tickTime / 1000.0F)));
   }

   public float getTPSFactor() {
      return (float)this.tickTime / 1000.0F;
   }

   @EventHandler
   public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
         if (this.time != 0L) {
            this.tickTime = System.currentTimeMillis() - this.time;
            if (this.tpsResult.size() > 20) {
               this.tpsResult.poll();
            }

            this.tpsResult.add(20.0F * (1000.0F / (float)this.tickTime));
            float average = 0.0F;

            for (Float value : this.tpsResult) {
               average += MathUtil.clamp(value, 0.0F, 20.0F);
            }

            this.tps = average / (float)this.tpsResult.size();
         }

         this.time = System.currentTimeMillis();
      }
   }

   public long serverRespondingTime() {
      return this.timeDelay.getPassedTimeMs();
   }

   public boolean isServerNotResponding() {
      return this.timeDelay.passedMs(HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.lagTime.getValue());
   }

   public int getPing() {
      PlayerListEntry playerListEntry = mc.method_1562().method_2871(mc.field_1724.method_5667());
      int ping;
      if (playerListEntry == null) {
         ping = 0;
      } else {
         ping = playerListEntry.method_2959();
      }

      return ping;
   }

   public void onUpdate() {
      JelloUtil.updateJello();
      if (this.worldNull && mc.field_1687 != null) {
         me.hextech.HexTech.MODULE.onLogin();
         this.worldNull = false;
      } else if (!this.worldNull && mc.field_1687 == null) {
         me.hextech.HexTech.save();
         me.hextech.HexTech.MODULE.onLogout();
         this.worldNull = true;
      }
   }

   public void run() {
      JelloUtil.updateJello();
      if (this.worldNull && mc.field_1687 != null) {
         me.hextech.HexTech.MODULE.onLogin();
         this.worldNull = false;
      } else if (!this.worldNull && mc.field_1687 == null) {
         me.hextech.HexTech.MODULE.onLogout();
         this.worldNull = true;
      }
   }
}
