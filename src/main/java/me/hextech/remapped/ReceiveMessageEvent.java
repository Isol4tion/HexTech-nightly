package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;

public class ReceiveMessageEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public String message;

    public ReceiveMessageEvent(String message) {
        super(Event.Pre);
        this.message = message;
    }

    public String getString() {
        return this.message;
    }
}
