package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={EntityVelocityUpdateS2CPacket.class})
public interface IEntityVelocityUpdateS2CPacket {
    @Mutable
    @Accessor(value="velocityX")
    void setX(int var1);

    @Mutable
    @Accessor(value="velocityY")
    void setY(int var1);

    @Mutable
    @Accessor(value="velocityZ")
    void setZ(int var1);
}
