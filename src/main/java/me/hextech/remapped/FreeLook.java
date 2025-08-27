package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.CameraState;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DEvent;

public class FreeLook
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static FreeLook INSTANCE;
    private final CameraState camera = new CameraState();

    public FreeLook() {
        super("FreeLook", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
        HexTech.EVENT_BUS.subscribe(new _OlDkmefZbssNukXRhXRy(this));
    }

    public CameraState getCameraState() {
        return this.camera;
    }

    public class _OlDkmefZbssNukXRhXRy {
        final /* synthetic */ FreeLook this$0;

        public _OlDkmefZbssNukXRhXRy(FreeLook freeLook) {
        }

        public void onRender3D(Render3DEvent render3DEvent) {
        }
    }
}
