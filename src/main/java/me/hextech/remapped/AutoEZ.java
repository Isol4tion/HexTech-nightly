package me.hextech.remapped;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;

public class AutoEZ extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final String CHARACTERS;
   private final EnumSetting<AutoEZ_TjwHGYKUAEgXsMpgUbfm> type = this.add(new EnumSetting("Type", AutoEZ_TjwHGYKUAEgXsMpgUbfm.HEXTECH));
   private final SliderSetting range = this.add(new SliderSetting("Range", 10.0, 0.0, 20.0, 1.0));
   private final StringSetting msg = this.add(
      new StringSetting("Custom", "EZ %player%-BY Hextech-nightly", v -> this.type.getValue() == AutoEZ_TjwHGYKUAEgXsMpgUbfm.Custom)
   );
   public List<String> ALEXJONNY = List.of("SO EASY!", "BEST CLIENT BUY+2353761389", "Power By Hex_Tech -Nightly", "YOU ARE AI?");
   public List<String> HEXTECH = List.of("Power By Hex_Tech -Nightly", "YOU ARE AI?", "SO EASY!", "BEST CLIENT BUY+2353761389");
   public List<String> GUAZIGEGE = List.of("Killed by HexTech-Nightly", "Only HexTech-Nightly can do", "Hex-Tech You need it");
   Random random = new Random();

   public AutoEZ() {
      super("AutoEZ", Module_JlagirAibYQgkHtbRnhw.Misc);
   }

   @EventHandler
   public void onDeath(DeathEvent event) {
      PlayerEntity player = event.getPlayer();
      if (mc.field_1724 != null && player != mc.field_1724 && !me.hextech.HexTech.FRIEND.isFriend(player)) {
         if (this.range.getValue() > 0.0 && (double)mc.field_1724.method_5739(player) > this.range.getValue()) {
            return;
         }

         switch ((AutoEZ_TjwHGYKUAEgXsMpgUbfm)this.type.getValue()) {
            case HEXTECH:
               mc.field_1724
                  .field_3944
                  .method_45729((String)this.HEXTECH.get(this.random.nextInt(this.HEXTECH.size() - 1)) + " " + player.method_5477().getString());
               break;
            case ALEXJONNY:
               mc.field_1724.field_3944.method_45729((String)this.ALEXJONNY.get(this.random.nextInt(this.ALEXJONNY.size() - 1)) + " " + player.method_5477());
               break;
            case GUAZIGEGE:
               mc.field_1724
                  .field_3944
                  .method_45729(player.method_5477().getString() + " " + (String)this.GUAZIGEGE.get(this.random.nextInt(this.GUAZIGEGE.size() - 1)));
               break;
            case Custom:
               mc.field_1724.field_3944.method_45729(this.msg.getValue().replaceAll("%player%", player.method_5477().getString()));
         }
      }
   }

   private String generateRandomString(int LENGTH) {
      StringBuilder sb = new StringBuilder(LENGTH);

      for (int i = 0; i < LENGTH; i++) {
         int index = this.random.nextInt("qwertyugjndshgshighewsnflksanffafskanx".length());
         sb.append("qwertyugjndshgshighewsnflksanffafskanx".charAt(index));
      }

      return sb.toString();
   }
}
