package me.hextech.remapped;

public class Sprint extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Sprint INSTANCE;
   public static boolean shouldSprint;
   public final EnumSetting<Sprint_kIBjeDSbfTeuMDPgEQgD> mode = this.add(new EnumSetting("Mode", Sprint_kIBjeDSbfTeuMDPgEQgD.Normal));

   public Sprint() {
      super("Sprint", Module_JlagirAibYQgkHtbRnhw.Movement);
      this.setDescription("Permanently keeps player in sprinting mode.");
      INSTANCE = this;
   }

   @Override
   public String getInfo() {
      return ((Sprint_kIBjeDSbfTeuMDPgEQgD)this.mode.getValue()).name();
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         switch ((Sprint_kIBjeDSbfTeuMDPgEQgD)this.mode.getValue()) {
            case Legit:
               mc.field_1690.field_1867.method_23481(true);
               shouldSprint = false;
               break;
            case Normal:
               mc.field_1690.field_1867.method_23481(true);
               shouldSprint = false;
               if (mc.field_1724.method_7344().method_7586() <= 6 && !mc.field_1724.method_7337()) {
                  return;
               }

               mc.field_1724.method_5728(MovementUtil.isMoving() && !mc.field_1724.method_5715());
               break;
            case Rage:
               shouldSprint = (mc.field_1724.method_7344().method_7586() > 6 || mc.field_1724.method_7337()) && !mc.field_1724.method_5715();
               mc.field_1724.method_5728(shouldSprint);
         }
      }
   }
}
