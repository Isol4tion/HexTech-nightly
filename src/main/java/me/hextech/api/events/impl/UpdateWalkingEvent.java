package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;

public class UpdateWalkingEvent
        extends Event_auduwKaxKOWXRtyJkCPb {
    private boolean cancelRotate = false;

    public UpdateWalkingEvent(Stage stage) {
        super(stage);
    }

    public void cancelRotate() {
        this.cancelRotate = true;
    }

    public boolean isCancelRotate() {
        return this.cancelRotate;
    }

    public void setCancelRotate(boolean cancelRotate) {
        this.cancelRotate = cancelRotate;
    }
}
