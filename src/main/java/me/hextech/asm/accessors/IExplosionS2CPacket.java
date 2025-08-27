package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ExplosionS2CPacket.class})
public interface IExplosionS2CPacket {
    @Accessor(value="playerVelocityX")
    public float getX();

    @Mutable
    @Accessor(value="playerVelocityX")
    public void setX(float var1);

    @Accessor(value="playerVelocityY")
    public float getY();

    @Mutable
    @Accessor(value="playerVelocityY")
    public void setY(float var1);

    @Accessor(value="playerVelocityZ")
    public float getZ();

    @Mutable
    @Accessor(value="playerVelocityZ")
    public void setZ(float var1);
}
