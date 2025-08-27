package me.hextech.remapped;

import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import net.minecraft.entity.Entity;

public class SafeWalk
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public SafeWalk() {
        super("SafeWalk", "stop at the edge", Module_JlagirAibYQgkHtbRnhw.Movement);
    }

    @EventHandler(priority=-100)
    public void onMove(MoveEvent event) {
        double x = event.getX();
        double y = event.getY();
        double z = event.getZ();
        if (SafeWalk.mc.player.isOnGround()) {
            double increment = 0.05;
            while (x != 0.0 && this.isOffsetBBEmpty(x, -1.0, 0.0)) {
                if (x < increment && x >= -increment) {
                    x = 0.0;
                    continue;
                }
                if (x > 0.0) {
                    x -= increment;
                    continue;
                }
                x += increment;
            }
            while (z != 0.0 && this.isOffsetBBEmpty(0.0, -1.0, z)) {
                if (z < increment && z >= -increment) {
                    z = 0.0;
                    continue;
                }
                if (z > 0.0) {
                    z -= increment;
                    continue;
                }
                z += increment;
            }
            while (x != 0.0 && z != 0.0 && this.isOffsetBBEmpty(x, -1.0, z)) {
                double d = x < increment && x >= -increment ? 0.0 : (x = x > 0.0 ? x - increment : x + increment);
                if (z < increment && z >= -increment) {
                    z = 0.0;
                    continue;
                }
                if (z > 0.0) {
                    z -= increment;
                    continue;
                }
                z += increment;
            }
        }
        event.setX(x);
        event.setY(y);
        event.setZ(z);
    }

    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        return !SafeWalk.mc.world.canCollide((Entity)SafeWalk.mc.player, SafeWalk.mc.player.getBoundingBox().offset(offsetX, offsetY, offsetZ));
    }
}
