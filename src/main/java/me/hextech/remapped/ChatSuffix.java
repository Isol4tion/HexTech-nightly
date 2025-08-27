package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SendMessageEvent;
import me.hextech.remapped.StringSetting;

public class ChatSuffix
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ChatSuffix INSTANCE;
    public final BooleanSetting green = this.add(new BooleanSetting("Green", false));
    private final StringSetting msg = this.add(new StringSetting("append", "\u1d04\u1d1b\ud835\udc0e\ud835\udc12"));

    public ChatSuffix() {
        super("ChatSuffix", Module_JlagirAibYQgkHtbRnhw.Misc);
        INSTANCE = this;
    }

    @EventHandler
    public void onSendMessage(SendMessageEvent event) {
        if (ChatSuffix.nullCheck() || event.isCancelled()) {
            return;
        }
        Object message = event.message;
        if (((String)message).startsWith("/") || ((String)message).startsWith("!") || ((String)message).startsWith("+") || ((String)message).startsWith("-") || ((String)message).startsWith("@") || ((String)message).endsWith(this.msg.getValue())) {
            return;
        }
        String suffix = this.msg.getValue();
        event.message = message = (String)message + " " + suffix;
    }

    public String getSuffix() {
        return null;
    }
}
