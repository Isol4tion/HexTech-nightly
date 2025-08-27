package me.hextech.remapped;

import net.minecraft.entity.vehicle.BoatEntity;

public class BoatMoveEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private final BoatEntity boat;

   public BoatMoveEvent(BoatEntity boat) {
      super(Event.Pre);
      this.boat = boat;
   }

   public BoatEntity getBoat() {
      return this.boat;
   }
}
