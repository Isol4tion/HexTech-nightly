package me.hextech.mod.modules.impl.player;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HitboxDesync
        extends Module_eSdgMXWuzcxgQVaJFmKZ {

    public HitboxDesync() {
        super("HitboxDesync", Category.Player);
    }

    @Override
    public void onUpdate() {
        if (HitboxDesync.nullCheck()) {
            return;
        }
        Direction f = HitboxDesync.mc.player.getHorizontalFacing();
        Box bb = HitboxDesync.mc.player.getBoundingBox();
        Vec3d center = bb.getCenter();
        Vec3d offset = new Vec3d(f.getUnitVector());
        Vec3d fin = this.merge(Vec3d.of(BlockPos.ofFloored(center)).add(0.5, 0.0, 0.5).add(offset.multiply(0.20000996883537)), f);
        HitboxDesync.mc.player.setPosition(fin.x == 0.0 ? HitboxDesync.mc.player.getX() : fin.x, HitboxDesync.mc.player.getY(), fin.z == 0.0 ? HitboxDesync.mc.player.getZ() : fin.z);
        this.disable();
    }

    private Vec3d merge(Vec3d a, Direction facing) {
        return new Vec3d(a.x * (double) Math.abs(facing.getUnitVector().x()), a.y * (double) Math.abs(facing.getUnitVector().y()), a.z * (double) Math.abs(facing.getUnitVector().z()));
    }
}
