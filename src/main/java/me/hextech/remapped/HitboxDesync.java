package me.hextech.remapped;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HitboxDesync
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final double MAGIC_OFFSET;

    public HitboxDesync() {
        super("HitboxDesync", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @Override
    public void onUpdate() {
        if (HitboxDesync.nullCheck()) {
            return;
        }
        Direction f = HitboxDesync.mc.player.method_5735();
        Box bb = HitboxDesync.mc.player.method_5829();
        Vec3d center = bb.getCenter();
        Vec3d offset = new Vec3d(f.getUnitVector());
        Vec3d fin = this.merge(Vec3d.of((Vec3i)BlockPos.ofFloored((Position)center)).add(0.5, 0.0, 0.5).add(offset.multiply(0.20000996883537)), f);
        HitboxDesync.mc.player.method_5814(fin.x == 0.0 ? HitboxDesync.mc.player.method_23317() : fin.x, HitboxDesync.mc.player.method_23318(), fin.z == 0.0 ? HitboxDesync.mc.player.method_23321() : fin.z);
        this.disable();
    }

    private Vec3d merge(Vec3d a, Direction facing) {
        return new Vec3d(a.x * (double)Math.abs(facing.getUnitVector().x()), a.y * (double)Math.abs(facing.getUnitVector().y()), a.z * (double)Math.abs(facing.getUnitVector().z()));
    }
}
