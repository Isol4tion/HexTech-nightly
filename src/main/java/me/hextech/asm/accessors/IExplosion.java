package me.hextech.asm.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Explosion.class})
public interface IExplosion {
   @Mutable
   @Accessor("x")
   void setX(double var1);

   @Mutable
   @Accessor("y")
   void setY(double var1);

   @Mutable
   @Accessor("z")
   void setZ(double var1);

   @Mutable
   @Accessor("power")
   void setPower(float var1);

   @Mutable
   @Accessor("entity")
   void setEntity(Entity var1);

   @Mutable
   @Accessor("world")
   World getWorld();

   @Mutable
   @Accessor("world")
   void setWorld(World var1);
}
