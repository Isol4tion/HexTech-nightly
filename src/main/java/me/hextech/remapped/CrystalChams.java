package me.hextech.remapped;

import java.awt.Color;

public class CrystalChams extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static CrystalChams INSTANCE;
   public final ColorSetting core = this.add(new ColorSetting("Core", new Color(255, 255, 255, 255)).injectBoolean(true));
   public final BooleanSetting sync = this.add(new BooleanSetting("sync", true));
   public final ColorSetting outerFrame = this.add(new ColorSetting("OuterFrame", new Color(255, 255, 255, 255)).injectBoolean(true));
   public final ColorSetting innerFrame = this.add(new ColorSetting("InnerFrame", new Color(255, 255, 255, 255)).injectBoolean(true));
   public final BooleanSetting texture = this.add(new BooleanSetting("Texture", true));
   public final SliderSetting scale = this.add(new SliderSetting("Scale", 1.0, 0.0, 3.0, 0.01));
   public final SliderSetting spinValue = this.add(new SliderSetting("SpinSpeed", 1.0, 0.0, 10.0, 0.01));
   public final SliderSetting bounceHeight = this.add(new SliderSetting("BounceHeight", 1.0, 0.0, 3.0, 0.01));
   public final SliderSetting floatValue = this.add(new SliderSetting("BounceSpeed", 1.0, 0.0, 10.0, 0.01));
   public final SliderSetting floatOffset = this.add(new SliderSetting("YOffset", 0.0, -1.0, 1.0, 0.01));

   public CrystalChams() {
      super("CrystalChams", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }
}
