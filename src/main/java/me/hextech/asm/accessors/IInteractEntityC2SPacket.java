package me.hextech.asm.accessors;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PlayerInteractEntityC2SPacket.class})
public interface IInteractEntityC2SPacket {
    @Accessor(value="entityId")
    @Final
    @Mutable
    public void setId(int var1);

    @Accessor(value="entityId")
    public int getId();
}
