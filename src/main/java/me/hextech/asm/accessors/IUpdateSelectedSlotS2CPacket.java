package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={UpdateSelectedSlotS2CPacket.class})
public interface IUpdateSelectedSlotS2CPacket {
    @Mutable
    @Accessor(value="slot")
    public void setslot(int var1);
}
