package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ExplosionS2CPacket.class})
public interface IExplosionS2CPacket {
    @Accessor(value="playerVelocityX")
    float getX();

    @Mutable
    @Accessor(value="playerVelocityX")
    void setX(float var1);

    @Accessor(value="playerVelocityY")
    float getY();

    @Mutable
    @Accessor(value="playerVelocityY")
    void setY(float var1);

    @Accessor(value="playerVelocityZ")
    float getZ();

    @Mutable
    @Accessor(value="playerVelocityZ")
    void setZ(float var1);
}
