package me.hextech.mod.modules.impl.client;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;

import java.io.IOException;

public class SendNotification
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static SendNotification INSTANCE;

    public SendNotification() {
        super("SendNotification", "send Notification", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable() throws IOException {
        Notification_lQoZqJolJVHgxQLLpwsm.notifyList.add(new Notification_lQoZqJolJVHgxQLLpwsm.NotificationInfo("test by cutemic"));
    }
}
