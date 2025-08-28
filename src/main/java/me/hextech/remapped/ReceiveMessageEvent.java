package me.hextech.remapped;

import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;

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
