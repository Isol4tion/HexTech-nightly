package me.hextech.remapped.mod.modules.impl.render;

import me.hextech.remapped.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;

public class CameraClip
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static CameraClip INSTANCE;
    public final SliderSetting distance = this.add(new SliderSetting("Distance", 4.0, 1.0, 20.0));
    public final SliderSetting animateTime = this.add(new SliderSetting("AnimationTime", 200, 0, 1000));
    private final BooleanSetting noFront = this.add(new BooleanSetting("NoFront", false));
    private final FadeUtils_DPfHthPqEJdfXfNYhDbG animation = new FadeUtils_DPfHthPqEJdfXfNYhDbG(300L);
    boolean first = false;

    public CameraClip() {
        super("CameraClip", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (CameraClip.mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT && this.noFront.getValue()) {
            CameraClip.mc.options.setPerspective(Perspective.FIRST_PERSON);
        }
        this.animation.setLength(this.animateTime.getValueInt());
        if (CameraClip.mc.options.getPerspective() == Perspective.FIRST_PERSON) {
            if (!this.first) {
                this.first = true;
                this.animation.reset();
            }
        } else if (this.first) {
            this.first = false;
            this.animation.reset();
        }
    }

    public double getDistance() {
        double quad = CameraClip.mc.options.getPerspective() == Perspective.FIRST_PERSON ? 1.0 - this.animation.easeOutQuad() : this.animation.easeOutQuad();
        return 1.0 + (this.distance.getValue() - 1.0) * quad;
    }
}
