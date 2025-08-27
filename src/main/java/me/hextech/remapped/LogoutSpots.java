package me.hextech.remapped;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import me.hextech.asm.accessors.IEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.util.math.Vec3d;

public class LogoutSpots extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
   private final BooleanSetting box = this.add(new BooleanSetting("Box", true));
   private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true));
   private final BooleanSetting text = this.add(new BooleanSetting("Text", true));
   private final BooleanSetting message = this.add(new BooleanSetting("Message", true));
   private final BooleanSetting notify = this.add(new BooleanSetting("Notify", true));
   private final Map<UUID, PlayerEntity> playerCache = Maps.newConcurrentMap();
   private final Map<UUID, PlayerEntity> logoutCache = Maps.newConcurrentMap();

   public LogoutSpots() {
      super("LogoutSpots", Module_JlagirAibYQgkHtbRnhw.Render);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (event.getPacket() instanceof PlayerListS2CPacket packet) {
         if (packet.method_46327().contains(Action.field_29136)) {
            for (Entry addedPlayer : packet.method_46330()) {
               for (UUID uuid : this.logoutCache.keySet()) {
                  if (uuid.equals(addedPlayer.comp_1107().getId())) {
                     PlayerEntity player = (PlayerEntity)this.logoutCache.get(uuid);
                     if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
                        && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Both) {
                        if (this.notify.getValue()) {
                           sendNotify(
                              "§e[!] §b"
                                 + player.method_5477().getString()
                                 + " §alogged back at X: "
                                 + (int)player.method_23317()
                                 + " Y: "
                                 + (int)player.method_23318()
                                 + " Z: "
                                 + (int)player.method_23321()
                           );
                        }

                        if (this.message.getValue()) {
                           CommandManager.sendChatMessage(
                              "§e[!] §b"
                                 + player.method_5477().getString()
                                 + " §alogged back at X: "
                                 + (int)player.method_23317()
                                 + " Y: "
                                 + (int)player.method_23318()
                                 + " Z: "
                                 + (int)player.method_23321()
                           );
                        }
                     } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
                        && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Notify) {
                        if (this.notify.getValue()) {
                           sendNotify(
                              "§e[!] §b"
                                 + player.method_5477().getString()
                                 + " §alogged back at X: "
                                 + (int)player.method_23317()
                                 + " Y: "
                                 + (int)player.method_23318()
                                 + " Z: "
                                 + (int)player.method_23321()
                           );
                        }
                     } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
                        && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Chat
                        && this.message.getValue()) {
                        CommandManager.sendChatMessage(
                           "§e[!] §b"
                              + player.method_5477().getString()
                              + " §alogged back at X: "
                              + (int)player.method_23317()
                              + " Y: "
                              + (int)player.method_23318()
                              + " Z: "
                              + (int)player.method_23321()
                        );
                     }

                     this.logoutCache.remove(uuid);
                  }
               }
            }
         }

         this.playerCache.clear();
      } else if (event.getPacket() instanceof PlayerRemoveS2CPacket packet) {
         for (UUID uuid2 : packet.comp_1105()) {
            for (UUID uuidx : this.playerCache.keySet()) {
               if (uuidx.equals(uuid2)) {
                  PlayerEntity player = (PlayerEntity)this.playerCache.get(uuidx);
                  if (!this.logoutCache.containsKey(uuidx)) {
                     if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
                        && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Both) {
                        if (this.notify.getValue()) {
                           sendNotify(
                              "§e[!] §b"
                                 + player.method_5477().getString()
                                 + " §alogged back at X: "
                                 + (int)player.method_23317()
                                 + " Y: "
                                 + (int)player.method_23318()
                                 + " Z: "
                                 + (int)player.method_23321()
                           );
                        }

                        if (this.message.getValue()) {
                           CommandManager.sendChatMessage(
                              "§e[!] §b"
                                 + player.method_5477().getString()
                                 + " §clogged out at X: "
                                 + (int)player.method_23317()
                                 + " Y: "
                                 + (int)player.method_23318()
                                 + " Z: "
                                 + (int)player.method_23321()
                           );
                        }
                     } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
                        && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Notify) {
                        if (this.notify.getValue()) {
                           sendNotify(
                              "§e[!] §b"
                                 + player.method_5477().getString()
                                 + " §alogged back at X: "
                                 + (int)player.method_23317()
                                 + " Y: "
                                 + (int)player.method_23318()
                                 + " Z: "
                                 + (int)player.method_23321()
                           );
                        }
                     } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
                        && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Chat
                        && this.message.getValue()) {
                        CommandManager.sendChatMessage(
                           "§e[!] §b"
                              + player.method_5477().getString()
                              + " §clogged out at X: "
                              + (int)player.method_23317()
                              + " Y: "
                              + (int)player.method_23318()
                              + " Z: "
                              + (int)player.method_23321()
                        );
                     }

                     this.logoutCache.put(uuidx, player);
                  }
               }
            }
         }

         this.playerCache.clear();
      }
   }

   @Override
   public void onEnable() {
      this.playerCache.clear();
      this.logoutCache.clear();
   }

   @Override
   public void onUpdate() {
      for (PlayerEntity player : mc.field_1687.method_18456()) {
         if (player != null && !player.equals(mc.field_1724)) {
            this.playerCache.put(player.method_7334().getId(), player);
         }
      }
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      for (UUID uuid : this.logoutCache.keySet()) {
         PlayerEntity data = (PlayerEntity)this.logoutCache.get(uuid);
         if (data != null) {
            Render3DUtil.draw3DBox(
               matrixStack,
               ((IEntity)data).getDimensions().method_30757(data.method_19538()),
               this.color.getValue(),
               this.outline.getValue(),
               this.box.getValue()
            );
            if (this.text.getValue()) {
               Render3DUtil.drawText3D(
                  data.method_5477().getString(),
                  new Vec3d(data.method_23317(), ((IEntity)data).getDimensions().method_30757(data.method_19538()).field_1325 + 0.5, data.method_23321()),
                  ColorUtil.injectAlpha(this.color.getValue(), 255)
               );
            }
         }
      }
   }
}
