package me.hextech.remapped;

public class Reach extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Reach INSTANCE;
   public final SliderSetting distance = this.add(new SliderSetting("Distance", 5.0, 1.0, 15.0, 0.1));

   public Reach() {
      super("Reach", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
   }
}
