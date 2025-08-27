package me.hextech.remapped;

public class FreeLook extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static FreeLook INSTANCE;
   private final CameraState camera = new CameraState();

   public FreeLook() {
      super("FreeLook", Module_JlagirAibYQgkHtbRnhw.Player);
      INSTANCE = this;
      me.hextech.HexTech.EVENT_BUS.subscribe(new FreeLook_OlDkmefZbssNukXRhXRy(this));
   }

   public CameraState getCameraState() {
      return this.camera;
   }
}
