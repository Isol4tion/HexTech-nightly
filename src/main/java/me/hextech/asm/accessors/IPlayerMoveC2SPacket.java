package me.hextech.asm.accessors;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = {PlayerMoveC2SPacket.class})
public interface IPlayerMoveC2SPacket {
    @Mutable
    @Accessor(value = "onGround")
    void setOnGround(boolean var1);

    @Mutable
    @Accessor(value = "pitch")
    void setPitch(float var1);

    @Mutable
    @Accessor(value = "yaw")
    void setYaw(float var1);
}
