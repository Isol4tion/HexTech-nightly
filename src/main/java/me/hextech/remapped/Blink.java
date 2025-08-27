package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

public class Blink extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Blink INSTANCE;
   public static OtherClientPlayerEntity fakePlayer;
   private final ArrayList<Packet> packetsList = new ArrayList();
   private final BooleanSetting allPacket = this.add(new BooleanSetting("AllPacket", true));
   public BooleanSetting bur = this.add(new BooleanSetting("DoneBurrow", false));
   public BooleanSetting exp = this.add(new BooleanSetting("Exp", false));
   public BooleanSetting sur = this.add(new BooleanSetting("DoneSurround", false));

   public Blink() {
      super("Blink", "Fake lag", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }

   @Override
   public void onEnable() {
      this.packetsList.clear();
      if (nullCheck()) {
         this.disable();
      } else {
         fakePlayer = new OtherClientPlayerEntity(
            mc.field_1687, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666601"), mc.field_1724.method_5477().getString())
         );
         fakePlayer.method_5719(mc.field_1724);
         fakePlayer.method_31548().method_7377(mc.field_1724.method_31548());
         mc.field_1687.method_53875(fakePlayer);
         if (this.exp.getValue()) {
            AutoEXP.INSTANCE.enable();
         }
      }
   }

   @Override
   public void onUpdate() {
      if (mc.field_1724.method_29504()) {
         this.packetsList.clear();
         this.disable();
      }
   }

   @Override
   public void onLogin() {
      if (this.isOn()) {
         this.packetsList.clear();
         this.disable();
      }
   }

   @EventHandler
   public void onSendPacket(PacketEvent event) {
      Packet<?> t = event.getPacket();
      if (!(t instanceof ChatMessageC2SPacket)) {
         if (!(t instanceof RequestCommandCompletionsC2SPacket)) {
            if (!(t instanceof CommandExecutionC2SPacket)) {
               if (!(t instanceof TeleportConfirmC2SPacket)) {
                  if (!(t instanceof KeepAliveC2SPacket)) {
                     if (!(t instanceof AdvancementTabC2SPacket)) {
                        if (!(t instanceof ClientStatusC2SPacket)) {
                           if (!(t instanceof ClickSlotC2SPacket)) {
                              if (t instanceof PlayerMoveC2SPacket || this.allPacket.getValue()) {
                                 this.packetsList.add(event.getPacket());
                                 event.cancel();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (nullCheck()) {
         this.packetsList.clear();
         this.disable();
      } else {
         if (fakePlayer != null) {
            fakePlayer.method_5768();
            fakePlayer.method_31745(RemovalReason.field_26998);
            fakePlayer.method_36209();
            fakePlayer = null;
         }

         for (Packet packet : this.packetsList) {
            mc.field_1724.field_3944.method_52787(packet);
         }

         if (this.bur.getValue() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
         }

         if (this.sur.getValue() && !Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn()) {
            Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.enable();
         }
      }
   }

   @Override
   public String getInfo() {
      return String.valueOf(this.packetsList.size());
   }
}
