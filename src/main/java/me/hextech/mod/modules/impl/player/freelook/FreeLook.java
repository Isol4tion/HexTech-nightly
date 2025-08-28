package me.hextech.mod.modules.impl.player.freelook;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.Render3DEvent;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;

public class FreeLook
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static FreeLook INSTANCE;
    private final CameraState camera = new CameraState();

    public FreeLook() {
        super("FreeLook", Category.Player);
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
