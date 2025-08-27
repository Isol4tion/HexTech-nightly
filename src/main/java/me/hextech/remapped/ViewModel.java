package me.hextech.remapped;

import me.hextech.asm.accessors.IHeldItemRenderer;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.HeldItemRendererEvent;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

public class ViewModel
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ViewModel INSTANCE;
    public final BooleanSetting swingAnimation = this.add(new BooleanSetting("SwingAnimation", false));
    public final BooleanSetting eatAnimation = this.add(new BooleanSetting("EatAnimation", false));
    public final BooleanSetting mainhandSwap = this.add(new BooleanSetting("MainhandSwap", true));
    public final BooleanSetting offhandSwap = this.add(new BooleanSetting("OffhandSwap", true));
    public final SliderSetting scaleMainX = this.add(new SliderSetting("ScaleMainX", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleMainY = this.add(new SliderSetting("ScaleMainY", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleMainZ = this.add(new SliderSetting("ScaleMainZ", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting positionMainX = this.add(new SliderSetting("PositionMainX", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionMainY = this.add(new SliderSetting("PositionMainY", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionMainZ = this.add(new SliderSetting("PositionMainZ", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting rotationMainX = this.add(new SliderSetting("RotationMainX", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationMainY = this.add(new SliderSetting("RotationMainY", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationMainZ = this.add(new SliderSetting("RotationMainZ", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting scaleOffX = this.add(new SliderSetting("ScaleOffX", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleOffY = this.add(new SliderSetting("ScaleOffY", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleOffZ = this.add(new SliderSetting("ScaleOffZ", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting positionOffX = this.add(new SliderSetting("PositionOffX", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionOffY = this.add(new SliderSetting("PositionOffY", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionOffZ = this.add(new SliderSetting("PositionOffZ", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting rotationOffX = this.add(new SliderSetting("RotationOffX", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationOffY = this.add(new SliderSetting("RotationOffY", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationOffZ = this.add(new SliderSetting("RotationOffZ", 0.0, -180.0, 180.0, 0.01));
    public final BooleanSetting slowAnimation = this.add(new BooleanSetting("SlowAnimation", true));
    public final SliderSetting slowAnimationVal = this.add(new SliderSetting("SlowValue", 6, 1, 50));
    public final SliderSetting eatX = this.add(new SliderSetting("EatX", 1.0, -1.0, 2.0, 0.01));
    public final SliderSetting eatY = this.add(new SliderSetting("EatY", 1.0, -1.0, 2.0, 0.01));
    public final BooleanSetting setdeaflu = this.add(new BooleanSetting("Setdefault", false));

    public ViewModel() {
        super("ViewModel", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.setdeaflu.getValue()) {
            this.positionMainX.setValue(0.0);
            this.positionMainY.setValue(0.0);
            this.positionMainZ.setValue(0.0);
            this.positionOffX.setValue(0.0);
            this.positionOffY.setValue(0.0);
            this.positionOffZ.setValue(0.0);
            this.setdeaflu.setValue(false);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (!this.mainhandSwap.getValue() && ((IHeldItemRenderer)mc.getEntityRenderDispatcher().getHeldItemRenderer()).getEquippedProgressMainHand() <= 1.0f) {
            ((IHeldItemRenderer)mc.getEntityRenderDispatcher().getHeldItemRenderer()).setEquippedProgressMainHand(1.0f);
            ((IHeldItemRenderer)mc.getEntityRenderDispatcher().getHeldItemRenderer()).setItemStackMainHand(ViewModel.mc.player.method_6047());
        }
        if (!this.offhandSwap.getValue() && ((IHeldItemRenderer)mc.getEntityRenderDispatcher().getHeldItemRenderer()).getEquippedProgressOffHand() <= 1.0f) {
            ((IHeldItemRenderer)mc.getEntityRenderDispatcher().getHeldItemRenderer()).setEquippedProgressOffHand(1.0f);
            ((IHeldItemRenderer)mc.getEntityRenderDispatcher().getHeldItemRenderer()).setItemStackOffHand(ViewModel.mc.player.method_6079());
        }
    }

    @EventHandler
    private void onHeldItemRender(HeldItemRendererEvent event) {
        if (event.getHand() == Hand.MAIN_HAND) {
            event.getStack().translate(this.positionMainX.getValueFloat(), this.positionMainY.getValueFloat(), this.positionMainZ.getValueFloat());
            event.getStack().scale(this.scaleMainX.getValueFloat(), this.scaleMainY.getValueFloat(), this.scaleMainZ.getValueFloat());
            event.getStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.rotationMainX.getValueFloat()));
            event.getStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.rotationMainY.getValueFloat()));
            event.getStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.rotationMainZ.getValueFloat()));
        } else {
            event.getStack().translate(this.positionOffX.getValueFloat(), this.positionOffY.getValueFloat(), this.positionOffZ.getValueFloat());
            event.getStack().scale(this.scaleOffX.getValueFloat(), this.scaleOffY.getValueFloat(), this.scaleOffZ.getValueFloat());
            event.getStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.rotationOffX.getValueFloat()));
            event.getStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.rotationOffY.getValueFloat()));
            event.getStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.rotationOffZ.getValueFloat()));
        }
    }
}
