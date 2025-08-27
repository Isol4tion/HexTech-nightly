package me.hextech.asm.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.hextech.asm.accessors.IPostProcessShader;
import me.hextech.remapped.IShaderEffect;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PostEffectProcessor.class})
public abstract class MixinShaderEffect implements IShaderEffect {
   @Unique
   private final List<String> fakedBufferNames = new ArrayList();
   @Shadow
   @Final
   private Map<String, Framebuffer> field_1495;
   @Shadow
   @Final
   private List<PostEffectPass> field_1497;

   @Override
   public void nullpoint_nextgen_master$addFakeTargetHook(String name, Framebuffer buffer) {
      Framebuffer previousFramebuffer = (Framebuffer)this.field_1495.get(name);
      if (previousFramebuffer != buffer) {
         if (previousFramebuffer != null) {
            for (PostEffectPass pass : this.field_1497) {
               if (pass.field_1536 == previousFramebuffer) {
                  ((IPostProcessShader)pass).setInput(buffer);
               }

               if (pass.field_1538 == previousFramebuffer) {
                  ((IPostProcessShader)pass).setOutput(buffer);
               }
            }

            this.field_1495.remove(name);
            this.fakedBufferNames.remove(name);
         }

         this.field_1495.put(name, buffer);
         this.fakedBufferNames.add(name);
      }
   }

   @Inject(
      method = {"close"},
      at = {@At("HEAD")}
   )
   void deleteFakeBuffersHook(CallbackInfo ci) {
      for (String fakedBufferName : this.fakedBufferNames) {
         this.field_1495.remove(fakedBufferName);
      }
   }
}
