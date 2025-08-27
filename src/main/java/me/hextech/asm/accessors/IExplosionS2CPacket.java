package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ExplosionS2CPacket.class})
public interface IExplosionS2CPacket {
   @Accessor("playerVelocityX")
   float getX();

   @Mutable
   @Accessor("playerVelocityX")
   void setX(float var1);

   @Accessor("playerVelocityY")
   float getY();

   @Mutable
   @Accessor("playerVelocityY")
   void setY(float var1);

   @Accessor("playerVelocityZ")
   float getZ();

   @Mutable
   @Accessor("playerVelocityZ")
   void setZ(float var1);
}
