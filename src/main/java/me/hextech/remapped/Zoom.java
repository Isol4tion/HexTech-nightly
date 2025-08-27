package me.hextech.remapped;

import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Render3DEvent;
import me.hextech.remapped.Zoom_qxASoURSmqLSKrnTPdNq;

/*
 * Exception performing whole class analysis ignored.
 */
public static class Zoom {
    final /* synthetic */ Zoom_qxASoURSmqLSKrnTPdNq this$0;

    public Zoom(Zoom_qxASoURSmqLSKrnTPdNq this$0) {
        this.this$0 = this$0;
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (this.this$0.isOn()) {
            this.this$0.currentFov = AnimateUtil.animate(this.this$0.currentFov, this.this$0.fov.getValue(), this.this$0.animSpeed.getValue());
            Zoom_qxASoURSmqLSKrnTPdNq.on = true;
        } else if (Zoom_qxASoURSmqLSKrnTPdNq.on) {
            this.this$0.currentFov = AnimateUtil.animate(this.this$0.currentFov, 0.0, this.this$0.animSpeed.getValue());
            if ((int)this.this$0.currentFov == 0) {
                Zoom_qxASoURSmqLSKrnTPdNq.on = false;
            }
        }
    }
}
