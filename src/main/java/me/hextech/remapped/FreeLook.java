package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.CameraState;
import me.hextech.remapped.EventHandler;
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
        HexTech.EVENT_BUS.subscribe(new _OlDkmefZbssNukXRhXRy());
    }

    public CameraState getCameraState() {
        return this.camera;
    }

    public class _OlDkmefZbssNukXRhXRy {
        @EventHandler
        public void onRender3D(Render3DEvent event) {
            boolean doUnlock;
            CameraState camera = FreeLook.this.getCameraState();
            boolean doLock = FreeLook.this.isOn() && !camera.doLock;
            boolean bl = doUnlock = !FreeLook.this.isOn() && camera.doLock;
            if (doLock) {
                if (!camera.doTransition) {
                    camera.lookYaw = camera.originalYaw();
                    camera.lookPitch = camera.originalPitch();
                }
                camera.doLock = true;
            }
            if (doUnlock) {
                camera.doLock = false;
                camera.doTransition = true;
                camera.transitionInitialYaw = camera.lookYaw;
                camera.transitionInitialPitch = camera.lookPitch;
            }
        }
    }
}
