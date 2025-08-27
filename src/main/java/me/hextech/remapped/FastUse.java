package me.hextech.remapped;

public class FastUse extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.0, 0.0, 4.0, 1.0));

   public FastUse() {
      super("FastUse", Module_JlagirAibYQgkHtbRnhw.Player);
   }

   @Override
   public void onUpdate() {
      if (mc.field_1752 <= 4 - this.delay.getValueInt()) {
         mc.field_1752 = 0;
      }
   }
}
