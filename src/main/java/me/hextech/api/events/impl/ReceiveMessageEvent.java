package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;

public class ReceiveMessageEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public String message;

    public ReceiveMessageEvent(String message) {
        super(Stage.Pre);
        this.message = message;
    }

    public String getString() {
        return this.message;
    }
}
