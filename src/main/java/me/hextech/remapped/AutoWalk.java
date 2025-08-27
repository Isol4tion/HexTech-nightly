package me.hextech.remapped;

public class AutoWalk extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoWalk INSTANCE;

   public AutoWalk() {
      super("AutoWalk", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @Override
   public void onDisable() {
      mc.field_1690.field_1894.method_23481(false);
   }

   @Override
   public void onUpdate() {
      mc.field_1690.field_1894.method_23481(true);
   }
}
