package me.hextech.remapped;

import java.util.HashMap;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.Blocker_mEBqWazfEhCLEwVSYEFP;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FadeUtils;
import me.hextech.remapped.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.Render3DEvent;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.Timer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/*
 * Exception performing whole class analysis ignored.
 */
public static class Blocker {
    public static final HashMap<BlockPos, _FijMAnLaeintSRYqwSXS> renderMap = new HashMap();
    final /* synthetic */ Blocker_mEBqWazfEhCLEwVSYEFP this$0;

    public Blocker(Blocker_mEBqWazfEhCLEwVSYEFP this$0) {
        this.this$0 = this$0;
    }

    public static void addBlock(BlockPos pos) {
        renderMap.put(pos, new _FijMAnLaeintSRYqwSXS(pos));
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (!Blocker_mEBqWazfEhCLEwVSYEFP.INSTANCE.render.getValue()) {
            return;
        }
        if (renderMap.isEmpty()) {
            return;
        }
        boolean shouldClear = true;
        for (_FijMAnLaeintSRYqwSXS placePosition : renderMap.values()) {
            if (!BlockUtil.clientCanPlace(placePosition.pos, true)) {
                placePosition.isAir = false;
            }
            if (!placePosition.timer.passedMs((long)(this.this$0.delay.getValue() + 100.0)) && placePosition.isAir) {
                placePosition.firstFade.reset();
            }
            if (placePosition.firstFade.getQuad(FadeUtils.In2) == 1.0) continue;
            shouldClear = false;
            MatrixStack matrixStack = event.getMatrixStack();
            if (Blocker_mEBqWazfEhCLEwVSYEFP.INSTANCE.fill.booleanValue) {
                Render3DUtil.drawFill(matrixStack, new Box(placePosition.pos), ColorUtil.injectAlpha(Blocker_mEBqWazfEhCLEwVSYEFP.INSTANCE.fill.getValue(), (int)((double)this.this$0.fill.getValue().getAlpha() * (1.0 - placePosition.firstFade.getQuad(FadeUtils.In2)))));
            }
            if (!Blocker_mEBqWazfEhCLEwVSYEFP.INSTANCE.box.booleanValue) continue;
            Render3DUtil.drawBox(matrixStack, new Box(placePosition.pos), ColorUtil.injectAlpha(Blocker_mEBqWazfEhCLEwVSYEFP.INSTANCE.box.getValue(), (int)((double)this.this$0.box.getValue().getAlpha() * (1.0 - placePosition.firstFade.getQuad(FadeUtils.In2)))));
        }
        if (shouldClear) {
            renderMap.clear();
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class _FijMAnLaeintSRYqwSXS {
        public final FadeUtils_DPfHthPqEJdfXfNYhDbG firstFade;
        public final BlockPos pos;
        public final Timer timer;
        public boolean isAir;

        public _FijMAnLaeintSRYqwSXS(BlockPos blockPos) {
        }
    }
}
