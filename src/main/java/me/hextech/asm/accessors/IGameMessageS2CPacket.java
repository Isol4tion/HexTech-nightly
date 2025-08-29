package me.hextech.asm.accessors;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = {GameMessageS2CPacket.class})
public interface IGameMessageS2CPacket {
    @Mutable
    @Accessor(value = "content")
    void setContent(Text var1);
}
