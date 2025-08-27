package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.AutoTrap;
import me.hextech.remapped.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.Timer;
import net.minecraft.util.math.BlockPos;

/*
 * Exception performing whole class analysis ignored.
 */
public static class AutoTrap_RYPZUKNZXVloqcMUfNgc {
    public final FadeUtils_DPfHthPqEJdfXfNYhDbG firstFade;
    public final BlockPos pos;
    public final Color posColor;
    public final Timer timer;
    public boolean isAir;

    public AutoTrap_RYPZUKNZXVloqcMUfNgc(BlockPos placePos) {
        this.firstFade = new FadeUtils_DPfHthPqEJdfXfNYhDbG((long)AutoTrap.INSTANCE.fadeTime.getValue());
        this.pos = placePos;
        this.posColor = AutoTrap.INSTANCE.color.getValue();
        this.timer = new Timer();
        this.isAir = true;
        this.timer.reset();
    }
}
