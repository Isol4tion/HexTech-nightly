package me.hextech.remapped;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;

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
        String message = event.message;
        if (message.startsWith("/") || message.startsWith("!") || message.startsWith("+") || message.startsWith("-") || message.startsWith("@") || message.endsWith(this.msg.getValue())) {
            return;
        }
        String suffix = this.msg.getValue();
        event.message = message + " " + suffix;
    }

    public String getSuffix() {
        return null;
    }
}
