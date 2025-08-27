package me.hextech.remapped;

import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MineManager_aMxFbgVZCMGgbqNPBFpw implements Wrapper {
   public final ConcurrentHashMap<Integer, MineManager> breakMap = new ConcurrentHashMap();

   public MineManager_aMxFbgVZCMGgbqNPBFpw() {
      me.hextech.HexTech.EVENT_BUS.subscribe(this);
   }

   @EventHandler
   public void onPacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (event.getPacket() instanceof BlockBreakingProgressS2CPacket packet) {
         if (packet.method_11277() == null) {
            return;
         }

         MineManager breakData = new MineManager(packet.method_11277(), packet.method_11280());
         if (this.breakMap.containsKey(packet.method_11280()) && ((MineManager)this.breakMap.get(packet.method_11280())).pos.equals(packet.method_11277())) {
            return;
         }

         if (breakData.getEntity() == null) {
            return;
         }

         if (MathHelper.method_15355((float)breakData.getEntity().method_33571().method_1025(packet.method_11277().method_46558())) > 8.0F) {
            return;
         }

         this.breakMap.put(packet.method_11280(), breakData);
      }
   }

   public boolean isMining(BlockPos pos) {
      for (MineManager breakData : this.breakMap.values()) {
         if (breakData.getEntity() != null && !(breakData.getEntity().method_33571().method_1022(pos.method_46558()) > 7.0) && breakData.pos.equals(pos)) {
            return true;
         }
      }

      return false;
   }
}
