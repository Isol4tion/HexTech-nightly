package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

public class HitLog extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HitLog INSTANCE;
   static double y;
   private final ArrayList<HitLog_STBbGhMCskXcqFYRXyft> logList = new ArrayList();
   public SliderSetting animationSpeed = this.add(new SliderSetting("AnimationSpeed", 0.2, 0.01, 0.5, 0.01));
   public SliderSetting stayTime = this.add(new SliderSetting("StayTime", 1.0, 0.5, 5.0, 0.1));

   public HitLog() {
      super("HitLog", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   public static double animate(double current, double endPoint, double speed) {
      boolean shouldContinueAnimation = endPoint > current;
      double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
      double factor = dif * speed;
      return Math.abs(factor) <= 0.001 ? endPoint : current + (shouldContinueAnimation ? factor : -factor);
   }

   @Override
   public void onRender2D(DrawContext drawContext, float tickDelta) {
      y = (double)mc.method_22683().method_4507() / 4.0 - 40.0;
      this.logList.removeIf(logx -> logx.timer.passed(this.stayTime.getValue() * 1000.0) && logx.alpha <= 10.0);

      for (HitLog_STBbGhMCskXcqFYRXyft log : new ArrayList(this.logList)) {
         boolean end = log.timer.passed(this.stayTime.getValue() * 1000.0);
         log.alpha = animate(log.alpha, end ? 0.0 : 255.0, this.animationSpeed.getValue());
         log.y = animate(log.y, end ? (double)mc.method_22683().method_4507() / 4.0 - 30.0 + 9.0 : y, this.animationSpeed.getValue());
         drawContext.method_25303(mc.field_1772, log.text, (int)log.x, (int)log.y, new Color(255, 255, 255, (int)log.alpha).getRGB());
         if (!end) {
            y -= (double)(9 + 2);
         }
      }
   }

   @EventHandler
   public void onPlayerDeath(DeathEvent event) {
      PlayerEntity player = event.getPlayer();
      if (player != mc.field_1724 && !(player.method_5739(mc.field_1724) > 20.0F)) {
         int popCount = (Integer)me.hextech.HexTech.POP.popContainer.getOrDefault(player.method_5477().getString(), 0);
         this.addLog("§4§m" + player.method_5477().getString() + "§f " + popCount);
      }
   }

   @EventHandler
   public void onTotem(TotemEvent event) {
      PlayerEntity player = event.getPlayer();
      if (player != mc.field_1724 && !(player.method_5739(mc.field_1724) > 20.0F)) {
         int popCount = (Integer)me.hextech.HexTech.POP.popContainer.getOrDefault(player.method_5477().getString(), 1);
         this.addLog("§a" + player.method_5477().getString() + "§f " + popCount);
      }
   }

   public void addLog(String text) {
      this.logList.add(new HitLog_STBbGhMCskXcqFYRXyft(text));
   }
}
