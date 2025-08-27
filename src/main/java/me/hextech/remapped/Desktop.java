package me.hextech.remapped;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FriendManager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.TotemEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class Desktop
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final Image image;
    private static final InputStream inputStream;
    final TrayIcon icon = new TrayIcon(image, "NullPoint");
    private final BooleanSetting onlyTabbed = this.add(new BooleanSetting("OnlyTabbed", false));
    private final BooleanSetting visualRange = this.add(new BooleanSetting("VisualRange", true));
    private final BooleanSetting selfPop = this.add(new BooleanSetting("TotemPop", true));
    private final BooleanSetting mention = this.add(new BooleanSetting("Mention", true));
    private final BooleanSetting dm = this.add(new BooleanSetting("DM", true));
    private final List<Entity> knownPlayers = new ArrayList<Entity>();
    private List<Entity> players;

    public Desktop() {
        super("Desktop", "Desktop notifications", Module_JlagirAibYQgkHtbRnhw.Client);
    }

    @Override
    public void onDisable() {
        this.knownPlayers.clear();
        this.removeIcon();
    }

    @Override
    public void onEnable() {
        this.addIcon();
    }

    @Override
    public void onUpdate() {
        if (Desktop.nullCheck() || !this.visualRange.getValue()) {
            return;
        }
        try {
            if (this.onlyTabbed.getValue()) {
                return;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.players = Desktop.mc.world.method_18456().stream().filter(Objects::nonNull).collect(Collectors.toList());
        try {
            for (Entity entity2 : this.players) {
                if (!(entity2 instanceof PlayerEntity) || entity2.method_5477().equals((Object)Desktop.mc.player.method_5477()) || this.knownPlayers.contains(entity2) || FriendManager.isFriend(entity2.method_5477().getString())) continue;
                this.knownPlayers.add(entity2);
                this.icon.displayMessage("NullPoint", String.valueOf(entity2.method_5477()) + " has entered your visual range!", TrayIcon.MessageType.INFO);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            this.knownPlayers.removeIf(entity -> entity instanceof PlayerEntity && !entity.method_5477().equals((Object)Desktop.mc.player.method_5477()) && !this.players.contains(entity));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventHandler
    public void onTotemPop(TotemEvent event) {
        if (Desktop.nullCheck() || event.getPlayer() != Desktop.mc.player || !this.selfPop.getValue()) {
            return;
        }
        this.icon.displayMessage("NullPoint", "You are popping!", TrayIcon.MessageType.WARNING);
    }

    @EventHandler
    public void onClientChatReceived(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (Desktop.nullCheck()) {
            return;
        }
        Object t = event.getPacket();
        if (t instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket e = (GameMessageS2CPacket)t;
            String message = String.valueOf(e.content());
            if (message.contains(Desktop.mc.player.method_5477().getString()) && this.mention.getValue()) {
                this.icon.displayMessage("NullPoint", "New chat mention!", TrayIcon.MessageType.INFO);
            }
            if (message.contains("whispers:") && this.dm.getValue()) {
                this.icon.displayMessage("NullPoint", "New direct message!", TrayIcon.MessageType.INFO);
            }
        }
    }

    private void addIcon() {
        SystemTray tray = SystemTray.getSystemTray();
        this.icon.setImageAutoSize(true);
        this.icon.setToolTip("NullPoint8");
        try {
            tray.add(this.icon);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void removeIcon() {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(this.icon);
    }
}
