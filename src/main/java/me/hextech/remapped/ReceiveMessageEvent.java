package me.hextech.remapped;

public class ReceiveMessageEvent extends Event_auduwKaxKOWXRtyJkCPb {
   public String message;

   public ReceiveMessageEvent(String message) {
      super(Event.Pre);
      this.message = message;
   }

   public String getString() {
      return this.message;
   }
}
