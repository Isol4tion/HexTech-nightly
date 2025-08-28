package me.hextech.asm.accessors;

import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Vec3d.class})
public interface IVec3d {
    @Mutable
    @Accessor(value="x")
    void setX(double var1);

    @Mutable
    @Accessor(value="y")
    void setY(double var1);

    @Mutable
    @Accessor(value="z")
    void setZ(double var1);
}
