package me.hextech.asm.accessors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = {MinecraftClient.class})
public interface IMinecraftClient {
    @Mutable
    @Accessor(value = "session")
    void setSession(Session var1);
}
