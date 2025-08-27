package me.hextech.remapped;

import me.hextech.remapped.BreakESP;
import me.hextech.remapped.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.Timer;
import me.hextech.remapped.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/*
 * Exception performing whole class analysis ignored.
 */
public static class MineManager {
    public final BlockPos pos;
    public final int entityId;
    public final FadeUtils_DPfHthPqEJdfXfNYhDbG fade;
    public final Timer timer;

    public MineManager(BlockPos pos, int entityId) {
        this.pos = pos;
        this.entityId = entityId;
        this.fade = new FadeUtils_DPfHthPqEJdfXfNYhDbG((long)BreakESP.INSTANCE.animationTime.getValue());
        this.timer = new Timer();
    }

    public Entity getEntity() {
        if (Wrapper.mc.world == null) {
            return null;
        }
        Entity entity = Wrapper.mc.world.method_8469(this.entityId);
        return entity instanceof PlayerEntity ? entity : null;
    }
}
