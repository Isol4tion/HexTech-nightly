package me.hextech.remapped;

public class SendMessageEvent extends Event_auduwKaxKOWXRtyJkCPb {
   public final String defaultMessage;
   public String message;

   public SendMessageEvent(String message) {
      super(Event.Pre);
      this.defaultMessage = message;
      this.message = message;
   }
}
