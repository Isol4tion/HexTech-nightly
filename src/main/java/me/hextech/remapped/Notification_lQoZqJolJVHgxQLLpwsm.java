package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;

public class Notification_lQoZqJolJVHgxQLLpwsm extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static final ArrayList<Notification_UtrzOkjpnGqedJBDTCjT> notifyList = new ArrayList();
   public static Notification_lQoZqJolJVHgxQLLpwsm INSTANCE;
   public final EnumSetting<Notification_PrCPUuiCqVywIosJdRuW> type = this.add(new EnumSetting("Type", Notification_PrCPUuiCqVywIosJdRuW.Chat));
   public final EnumSetting<Notification> mode = this.add(new EnumSetting("Type", Notification.Fill));
   public final SliderSetting notifyX = this.add(new SliderSetting("notifyX", 256, 18, 500));
   private final SliderSetting notifyY = this.add(new SliderSetting("Y", 18, -50, 500));
   private final ColorSetting typeColor = this.add(new ColorSetting("TypeColor", new Color(20, 20, 20, 100)));
   private final ColorSetting fillcolor = this.add(new ColorSetting("FillColor", new Color(20, 20, 20, 100)));
   private final ColorSetting linecolor = this.add(new ColorSetting("LineColor", new Color(140, 140, 250, 225)));

   public Notification_lQoZqJolJVHgxQLLpwsm() {
      super("Notification", "Notification", Module_JlagirAibYQgkHtbRnhw.Client);
      INSTANCE = this;
   }

   @Override
   public void onRender2D(DrawContext drawContext, float tickDelta) {
      boolean bl = true;
      int n = (int)(379.0 - this.notifyY.getValue());
      int n2 = this.notifyX.getValueInt() + 500;

      for (Notification_UtrzOkjpnGqedJBDTCjT notifys : notifyList) {
         if (notifys != null && notifys.first != null && notifys.firstFade != null && notifys.delayed >= 1) {
            bl = false;
            if (notifys.delayed < 5 && !notifys.end) {
               notifys.end = true;
               notifys.endFade.reset();
            }

            n = (int)((double)n - 18.0 * notifys.yFade.easeOutQuad());
            String string = notifys.first;
            double d = notifys.delayed < 5
               ? (double)n2 - (double)(mc.field_1772.method_1727(string) + 10) * (1.0 - notifys.endFade.easeOutQuad())
               : (double)n2 - (double)(mc.field_1772.method_1727(string) + 10) * notifys.firstFade.easeOutQuad();
            Render2DUtil.drawRound(drawContext.method_51448(), (float)((int)d), (float)n, 5.0F, 50.0F, 0.0F, this.typeColor.getValue());
            Render2DUtil.drawRound(
               drawContext.method_51448(),
               (float)((int)d + 5),
               (float)n,
               (float)(5 + mc.field_1772.method_1727(string)),
               50.0F,
               0.0F,
               this.fillcolor.getValue()
            );
            drawContext.method_51433(mc.field_1772, string, 10 + (int)d, 4 + n, new Color(255, 255, 255, 255).getRGB(), true);
            if (notifys.delayed < 5) {
               n = (int)((double)n + 18.0 * notifys.yFade.easeOutQuad() - 18.0 * (1.0 - notifys.endFade.easeOutQuad()));
            } else {
               Render2DUtil.drawRect(
                  drawContext.method_51448(),
                  (float)((int)d) + 2.0F,
                  (float)(n + 14),
                  (float)((10 + mc.field_1772.method_1727(string)) * (notifys.delayed - 4) - 2) / 62.0F,
                  1.0F,
                  this.linecolor.getValue()
               );
            }
         }
      }

      if (bl) {
         notifyList.clear();
      }
   }

   @Override
   public void onUpdate() {
      if (!HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.state) {
         for (Notification_UtrzOkjpnGqedJBDTCjT notifys : notifyList) {
            if (notifys != null && notifys.first != null && notifys.firstFade != null) {
               notifys.delayed--;
            }
         }
      }
   }

   @Override
   public void onDisable() {
      notifyList.clear();
   }
}
