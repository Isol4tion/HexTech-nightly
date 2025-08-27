package me.hextech.remapped;

public class Event_auduwKaxKOWXRtyJkCPb {
   private final Event stage;
   private boolean cancel = false;

   public Event_auduwKaxKOWXRtyJkCPb(Event stage) {
      this.stage = stage;
   }

   public void cancel() {
      this.setCancelled(true);
   }

   public boolean isCancel() {
      return this.cancel;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }

   public Event getStage() {
      return this.stage;
   }

   public boolean isPost() {
      return this.stage == Event.Post;
   }

   public boolean isPre() {
      return this.stage == Event.Pre;
   }
}
