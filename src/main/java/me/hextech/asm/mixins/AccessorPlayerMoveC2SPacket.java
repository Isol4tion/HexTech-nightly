package me.hextech.asm.mixins;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PlayerMoveC2SPacket.class})
public interface AccessorPlayerMoveC2SPacket {
    @Accessor(value="pitch")
    public void setPitch(float var1);
}
