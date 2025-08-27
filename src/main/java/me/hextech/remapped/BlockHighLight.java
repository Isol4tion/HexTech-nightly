package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class BlockHighLight
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    static Vec3d placeVec3d;
    static Vec3d curVec3d;
    final BooleanSetting center = this.add(new BooleanSetting("Center", true));
    final BooleanSetting shrink = this.add(new BooleanSetting("Shrink", true));
    final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255)).injectBoolean(true));
    final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100)).injectBoolean(true));
    final SliderSetting sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.01, 1.0, 0.01));
    final SliderSetting startFadeTime = this.add(new SliderSetting("StartFade", 0.3, 0.0, 2.0, 0.01).setSuffix("s"));
    final SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01));
    final Timer noPosTimer = new Timer();
    double fade = 0.0;

    public BlockHighLight() {
        super("BlockHighLight", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        HitResult hitResult;
        if (BlockHighLight.mc.crosshairTarget == null || !((hitResult = BlockHighLight.mc.crosshairTarget) instanceof BlockHitResult)) {
            return;
        }
        BlockHitResult hitResult2 = (BlockHitResult)hitResult;
        if (BlockHighLight.mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            this.noPosTimer.reset();
            Vec3d vec3d = placeVec3d = this.center.getValue() ? hitResult2.getBlockPos().toCenterPos() : BlockHighLight.mc.crosshairTarget.getPos();
        }
        if (placeVec3d == null) {
            return;
        }
        this.fade = this.fadeSpeed.getValue() >= 1.0 ? (this.noPosTimer.passedMs((long)(this.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5) : AnimateUtil.animate(this.fade, this.noPosTimer.passedMs((long)(this.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5, this.fadeSpeed.getValue() / 10.0);
        if (this.fade == 0.0) {
            curVec3d = null;
            return;
        }
        curVec3d = curVec3d == null || this.sliderSpeed.getValue() >= 1.0 ? placeVec3d : new Vec3d(AnimateUtil.animate(BlockHighLight.curVec3d.x, BlockHighLight.placeVec3d.x, this.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(BlockHighLight.curVec3d.y, BlockHighLight.placeVec3d.y, this.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(BlockHighLight.curVec3d.z, BlockHighLight.placeVec3d.z, this.sliderSpeed.getValue() / 10.0));
        Box box = new Box(curVec3d, curVec3d);
        box = this.shrink.getValue() ? box.expand(this.fade) : box.expand(0.5);
        if (this.fill.booleanValue) {
            Render3DUtil.drawFill(matrixStack, box, ColorUtil.injectAlpha(this.fill.getValue(), (int)((double)this.fill.getValue().getAlpha() * this.fade * 2.0)));
        }
        if (this.box.booleanValue) {
            Render3DUtil.drawBox(matrixStack, box, ColorUtil.injectAlpha(this.box.getValue(), (int)((double)this.box.getValue().getAlpha() * this.fade * 2.0)));
        }
    }
}
