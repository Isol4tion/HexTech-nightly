package me.hextech.remapped;

@Beta
public class NoInterp extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static NoInterp INSTANCE;
   public final SliderSetting tickDelta = this.add(new SliderSetting("TickDelta", 0.0, 0.0, 10.0, 0.1));

   public NoInterp() {
      super("NoInterp", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }
}
