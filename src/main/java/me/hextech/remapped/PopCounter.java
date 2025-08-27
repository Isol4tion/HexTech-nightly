package me.hextech.remapped;

import net.minecraft.entity.player.PlayerEntity;

public class PopCounter extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static PopCounter INSTANCE;
   public final BooleanSetting unPop = this.add(new BooleanSetting("Dead", true));
   private final EnumSetting<PopCounter_jJlsegqwuwGfJNTYElis> notitype = this.add(new EnumSetting("Type", PopCounter_jJlsegqwuwGfJNTYElis.Notify));

   public PopCounter() {
      super("PopCounter", "Counts players totem pops", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   @EventHandler
   public void onPlayerDeath(DeathEvent event) {
      PlayerEntity player = event.getPlayer();
      if (me.hextech.HexTech.POP.popContainer.containsKey(player.method_5477().getString())) {
         int l_Count = (Integer)me.hextech.HexTech.POP.popContainer.get(player.method_5477().getString());
         if (l_Count == 1) {
            if (player.equals(mc.field_1724)) {
               this.sendMessage("§f你§r已经失去了§f" + l_Count + "§r 图腾", player.method_5628());
            } else {
               this.sendMessage("§f" + player.method_5477().getString() + "§r击破敌人§f" + l_Count + "§r 图腾", player.method_5628());
            }
         } else if (player.equals(mc.field_1724)) {
            this.sendMessage("§f你§r已经失去了 §f" + l_Count + "§r 图腾", player.method_5628());
         } else {
            this.sendMessage("§f" + player.method_5477().getString() + "§r击破敌人§f" + l_Count + "§r 图腾", player.method_5628());
         }
      } else if (this.unPop.getValue()) {
         if (player.equals(mc.field_1724)) {
            this.sendMessage("§f你§r[确认死亡]", player.method_5628());
         } else {
            this.sendMessage("§f" + player.method_5477().getString() + "§r[确认死亡]", player.method_5628());
         }
      }
   }

   @EventHandler
   public void onTotem(TotemEvent event) {
      PlayerEntity player = event.getPlayer();
      int l_Count = 1;
      if (me.hextech.HexTech.POP.popContainer.containsKey(player.method_5477().getString())) {
         l_Count = (Integer)me.hextech.HexTech.POP.popContainer.get(player.method_5477().getString());
      }

      if (l_Count == 1) {
         if (player.equals(mc.field_1724)) {
            this.sendMessage("§f你§r正在丢失 §f" + l_Count + "§r 图腾", player.method_5628());
         } else {
            this.sendMessage("§f" + player.method_5477().getString() + " §r击破敌人 §f" + l_Count + "§r 图腾", player.method_5628());
         }
      } else if (player.equals(mc.field_1724)) {
         this.sendMessage("§f你§r失去了§f" + l_Count + "§r 图腾", player.method_5628());
      } else {
         this.sendMessage("§f" + player.method_5477().getString() + " §r击破敌人 §f" + l_Count + "§r 图腾", player.method_5628());
      }
   }

   public void sendMessage(String message, int id) {
      if (!nullCheck()) {
         if (this.notitype.getValue() == PopCounter_jJlsegqwuwGfJNTYElis.Notify) {
            sendNotify("§6[!] " + message);
         } else if (this.notitype.getValue() == PopCounter_jJlsegqwuwGfJNTYElis.Both) {
            CommandManager.sendChatMessageWidthId("§6[!] " + message, id);
            sendNotify("§6[!] " + message);
         } else if (this.notitype.getValue() == PopCounter_jJlsegqwuwGfJNTYElis.Chat) {
            CommandManager.sendChatMessageWidthId("§6[!] " + message, id);
         }
      }
   }
}
