package me.hextech.remapped;

import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Render3DEvent;
import me.hextech.remapped.Render3DUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/*
 * Exception performing whole class analysis ignored.
 */
public static class AutoAnchor_fWvHjchZKtWCdDnpHPYc {
    final /* synthetic */ AutoAnchor_MDcwoWYRcPYheLZJWRZK this$0;

    public AutoAnchor_fWvHjchZKtWCdDnpHPYc(AutoAnchor_MDcwoWYRcPYheLZJWRZK this$0) {
        this.this$0 = this$0;
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null) {
            this.this$0.noPosTimer.reset();
            AutoAnchor_MDcwoWYRcPYheLZJWRZK.placeVec3d = AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos.toCenterPos();
        }
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.placeVec3d == null) {
            return;
        }
        this.this$0.fade = this.this$0.fadeSpeed.getValue() >= 1.0 ? (this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5) : AnimateUtil.animate(this.this$0.fade, this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5, this.this$0.fadeSpeed.getValue() / 10.0);
        if (this.this$0.fade == 0.0) {
            AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d = null;
            return;
        }
        AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d = AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d == null || this.this$0.sliderSpeed.getValue() >= 1.0 ? AutoAnchor_MDcwoWYRcPYheLZJWRZK.placeVec3d : new Vec3d(AnimateUtil.animate(AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d.field_1352, AutoAnchor_MDcwoWYRcPYheLZJWRZK.placeVec3d.field_1352, this.this$0.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d.field_1351, AutoAnchor_MDcwoWYRcPYheLZJWRZK.placeVec3d.field_1351, this.this$0.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d.field_1350, AutoAnchor_MDcwoWYRcPYheLZJWRZK.placeVec3d.field_1350, this.this$0.sliderSpeed.getValue() / 10.0));
        if (this.this$0.render.getValue()) {
            Box cbox = new Box(AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d, AutoAnchor_MDcwoWYRcPYheLZJWRZK.curVec3d);
            cbox = this.this$0.shrink.getValue() ? cbox.method_1014(this.this$0.fade) : cbox.method_1014(0.5);
            MatrixStack matrixStack = event.getMatrixStack();
            if (this.this$0.fill.booleanValue) {
                Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(this.this$0.fill.getValue(), (int)((double)this.this$0.fill.getValue().getAlpha() * this.this$0.fade * 2.0)));
            }
            if (this.this$0.box.booleanValue) {
                if (!this.this$0.bold.getValue()) {
                    Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(this.this$0.box.getValue(), (int)((double)this.this$0.box.getValue().getAlpha() * this.this$0.fade * 2.0)));
                } else {
                    Render3DUtil.drawLine(cbox, ColorUtil.injectAlpha(this.this$0.box.getValue(), (int)((double)this.this$0.box.getValue().getAlpha() * this.this$0.fade * 2.0)), this.this$0.lineWidth.getValueInt());
                }
            }
        }
    }
}
