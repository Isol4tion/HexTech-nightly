package me.hextech.remapped;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon$MessageType;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class Desktop extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final Image image;
   private static final InputStream inputStream;
   final TrayIcon icon;
   private final BooleanSetting onlyTabbed;
   private final BooleanSetting visualRange;
   private final BooleanSetting selfPop;
   private final BooleanSetting mention;
   private final BooleanSetting dm;
   private final List<Entity> knownPlayers;
   private List<Entity> players;

   public Desktop() {
      super("Desktop", "Desktop notifications", Module_JlagirAibYQgkHtbRnhw.Client);
      this.icon = new TrayIcon(image, "NullPoint");
      this.onlyTabbed = this.add(new BooleanSetting("OnlyTabbed", false));
      this.visualRange = this.add(new BooleanSetting("VisualRange", true));
      this.selfPop = this.add(new BooleanSetting("TotemPop", true));
      this.mention = this.add(new BooleanSetting("Mention", true));
      this.dm = this.add(new BooleanSetting("DM", true));
      this.knownPlayers = new ArrayList();
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
      if (!nullCheck() && this.visualRange.getValue()) {
         try {
            if (this.onlyTabbed.getValue()) {
               return;
            }
         } catch (Exception var4) {
         }

         this.players = (List<Entity>)mc.field_1687.method_18456().stream().filter(Objects::nonNull).collect(Collectors.toList());

         try {
            for (Entity entity : this.players) {
               if (entity instanceof PlayerEntity
                  && !entity.method_5477().equals(mc.field_1724.method_5477())
                  && !this.knownPlayers.contains(entity)
                  && !FriendManager.isFriend(entity.method_5477().getString())) {
                  this.knownPlayers.add(entity);
                  this.icon.displayMessage("NullPoint", entity.method_5477() + " has entered your visual range!", TrayIcon$MessageType.INFO);
               }
            }
         } catch (Exception var5) {
         }

         try {
            this.knownPlayers
               .removeIf(
                  entityx -> entityx instanceof PlayerEntity && !entityx.method_5477().equals(mc.field_1724.method_5477()) && !this.players.contains(entityx)
               );
         } catch (Exception var3) {
         }
      }
   }

   @EventHandler
   public void onTotemPop(TotemEvent event) {
      if (!nullCheck() && event.getPlayer() == mc.field_1724 && this.selfPop.getValue()) {
         this.icon.displayMessage("NullPoint", "You are popping!", TrayIcon$MessageType.WARNING);
      }
   }

   @EventHandler
   public void onClientChatReceived(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (!nullCheck()) {
         if (event.getPacket() instanceof GameMessageS2CPacket e) {
            String message = String.valueOf(e.comp_763());
            if (message.contains(mc.field_1724.method_5477().getString()) && this.mention.getValue()) {
               this.icon.displayMessage("NullPoint", "New chat mention!", TrayIcon$MessageType.INFO);
            }

            if (message.contains("whispers:") && this.dm.getValue()) {
               this.icon.displayMessage("NullPoint", "New direct message!", TrayIcon$MessageType.INFO);
            }
         }
      }
   }

   private void addIcon() {
      SystemTray tray = SystemTray.getSystemTray();
      this.icon.setImageAutoSize(true);
      this.icon.setToolTip("NullPoint8");

      try {
         tray.add(this.icon);
      } catch (AWTException var3) {
         var3.printStackTrace();
      }
   }

   private void removeIcon() {
      SystemTray tray = SystemTray.getSystemTray();
      tray.remove(this.icon);
   }
}
