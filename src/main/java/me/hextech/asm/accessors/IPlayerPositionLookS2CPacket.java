package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PlayerPositionLookS2CPacket.class})
public interface IPlayerPositionLookS2CPacket {
    @Mutable
    @Accessor(value="yaw")
    void setYaw(float var1);

    @Mutable
    @Accessor(value="pitch")
    void setPitch(float var1);
}
