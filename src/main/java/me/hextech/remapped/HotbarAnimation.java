package me.hextech.remapped;

public class HotbarAnimation extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HotbarAnimation INSTANCE;
   public final EnumSetting<AnimateUtil_AcLZzRdHWZkNeKEYTOwI> animMode = this.add(new EnumSetting("AnimMode", AnimateUtil_AcLZzRdHWZkNeKEYTOwI.Mio));
   public final SliderSetting hotbarSpeed = this.add(new SliderSetting("HotbarSpeed", 0.2, 0.01, 1.0, 0.01));

   public HotbarAnimation() {
      super("HotbarAnimation", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }
}
