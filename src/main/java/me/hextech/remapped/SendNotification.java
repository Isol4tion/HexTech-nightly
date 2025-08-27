package me.hextech.remapped;

import java.io.IOException;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Notification_UtrzOkjpnGqedJBDTCjT;
import me.hextech.remapped.Notification_lQoZqJolJVHgxQLLpwsm;

public class SendNotification
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static SendNotification INSTANCE;

    public SendNotification() {
        super("SendNotification", "send Notification", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable() throws IOException {
        Notification_lQoZqJolJVHgxQLLpwsm.notifyList.add(new Notification_UtrzOkjpnGqedJBDTCjT("test by cutemic"));
    }
}
