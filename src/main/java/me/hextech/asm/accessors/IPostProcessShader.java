package me.hextech.asm.accessors;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PostEffectPass.class})
public interface IPostProcessShader {
    @Mutable
    @Accessor(value="input")
    public void setInput(Framebuffer var1);

    @Mutable
    @Accessor(value="output")
    public void setOutput(Framebuffer var1);
}
