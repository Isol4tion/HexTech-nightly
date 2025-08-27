package me.hextech.remapped;

public class ChatSuffix extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static ChatSuffix INSTANCE;
   public final BooleanSetting green = this.add(new BooleanSetting("Green", false));
   private final StringSetting msg = this.add(new StringSetting("append", "ᴄᴛ\ud835\udc0e\ud835\udc12"));

   public ChatSuffix() {
      super("ChatSuffix", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   @EventHandler
   public void onSendMessage(SendMessageEvent event) {
      if (!nullCheck() && !event.isCancelled()) {
         String message = event.message;
         if (!message.startsWith("/")
            && !message.startsWith("!")
            && !message.startsWith("+")
            && !message.startsWith("-")
            && !message.startsWith("@")
            && !message.endsWith(this.msg.getValue())) {
            String suffix = this.msg.getValue();
            message = message + " " + suffix;
            event.message = message;
         }
      }
   }

   public String getSuffix() {
      return null;
   }
}
