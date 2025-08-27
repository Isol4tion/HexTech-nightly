package me.hextech.remapped;

import java.util.HashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class SpeedManager implements Wrapper {
   public final HashMap<PlayerEntity, Double> playerSpeeds = new HashMap();
   public double speedometerCurrentSpeed;

   public SpeedManager() {
      me.hextech.HexTech.EVENT_BUS.subscribe(this);
   }

   @EventHandler
   public void updateWalking(UpdateWalkingEvent event) {
      this.updateValues();
   }

   public void updateValues() {
      double distTraveledLastTickX = mc.field_1724.method_23317() - mc.field_1724.field_6014;
      double distTraveledLastTickZ = mc.field_1724.method_23321() - mc.field_1724.field_5969;
      this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
      this.updatePlayers();
   }

   public void updatePlayers() {
      for (PlayerEntity player : mc.field_1687.method_18456()) {
         if ((double)mc.field_1724.method_5739(player) < 400.0) {
            double distTraveledLastTickX = player.method_23317() - player.field_6014;
            double distTraveledLastTickZ = player.method_23321() - player.field_5969;
            double playerSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
            this.playerSpeeds.put(player, playerSpeed);
         }
      }
   }

   public double getPlayerSpeed(PlayerEntity player) {
      return this.playerSpeeds.get(player) == null ? 0.0 : this.turnIntoKpH((Double)this.playerSpeeds.get(player));
   }

   public double getSpeedKpH() {
      double speedometerkphdouble = this.turnIntoKpH(this.speedometerCurrentSpeed);
      return (double)Math.round(10.0 * speedometerkphdouble) / 10.0;
   }

   public double turnIntoKpH(double input) {
      return (double)MathHelper.method_15355((float)input) * 71.2729367892;
   }
}
