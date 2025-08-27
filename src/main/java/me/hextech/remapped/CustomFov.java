package me.hextech.remapped;

public class CustomFov extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static CustomFov INSTANCE;
   public final BooleanSetting usefov = this.add(new BooleanSetting("CustomFov", true));
   public final SliderSetting fov = this.add(new SliderSetting("Fov", 120, 0, 160));
   public final BooleanSetting itemFov = this.add(new BooleanSetting("itemFov", true));
   public final SliderSetting itemFovModifier = this.add(new SliderSetting("ItemModifier", 120, 0, 358));

   public CustomFov() {
      super("CustomFov", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }
}
