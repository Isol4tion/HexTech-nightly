package me.hextech.asm.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Explosion.class})
public interface IExplosion {
    @Mutable
    @Accessor(value="x")
    void setX(double var1);

    @Mutable
    @Accessor(value="y")
    void setY(double var1);

    @Mutable
    @Accessor(value="z")
    void setZ(double var1);

    @Mutable
    @Accessor(value="power")
    void setPower(float var1);

    @Mutable
    @Accessor(value="entity")
    void setEntity(Entity var1);

    @Mutable
    @Accessor(value="world")
    World getWorld();

    @Mutable
    @Accessor(value="world")
    void setWorld(World var1);
}
