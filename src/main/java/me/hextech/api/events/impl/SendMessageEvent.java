package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;

public class SendMessageEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public final String defaultMessage;
    public String message;

    public SendMessageEvent(String message) {
        super(Stage.Pre);
        this.defaultMessage = message;
        this.message = message;
    }
}
