package me.hextech.remapped;

public class UpdateWalkingEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private boolean cancelRotate = false;

   public UpdateWalkingEvent(Event stage) {
      super(stage);
   }

   public void cancelRotate() {
      this.cancelRotate = true;
   }

   public boolean isCancelRotate() {
      return this.cancelRotate;
   }

   public void setCancelRotate(boolean cancelRotate) {
      this.cancelRotate = cancelRotate;
   }
}
