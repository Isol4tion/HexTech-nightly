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
        Vec3d center = bb.method_1005();
        Vec3d offset = new Vec3d(f.method_23955());
        Vec3d fin = this.merge(Vec3d.method_24954((Vec3i)BlockPos.method_49638((Position)center)).method_1031(0.5, 0.0, 0.5).method_1019(offset.method_1021(0.20000996883537)), f);
        HitboxDesync.mc.player.method_5814(fin.field_1352 == 0.0 ? HitboxDesync.mc.player.getX() : fin.field_1352, HitboxDesync.mc.player.getY(), fin.field_1350 == 0.0 ? HitboxDesync.mc.player.getZ() : fin.field_1350);
        this.disable();
    }

    private Vec3d merge(Vec3d a, Direction facing) {
        return new Vec3d(a.field_1352 * (double)Math.abs(facing.method_23955().x()), a.field_1351 * (double)Math.abs(facing.method_23955().y()), a.field_1350 * (double)Math.abs(facing.method_23955().z()));
    }
}
