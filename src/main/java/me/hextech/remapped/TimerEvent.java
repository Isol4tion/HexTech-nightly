package me.hextech.remapped;

public class TimerEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private float timer = 1.0F;
   private boolean modified;

   public TimerEvent() {
      super(Event.Pre);
   }

   public float get() {
      return this.timer;
   }

   public void set(float timer) {
      this.modified = true;
      this.timer = timer;
   }

   public boolean isModified() {
      return this.modified;
   }
}
