package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.TimerEvent;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={RenderTickCounter.class})
public class MixinRenderTickCounter {
    @Shadow
    public float lastFrameDuration;

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J", opcode=181, ordinal=0)}, method={"beginRenderTick(J)I"})
    public void onBeginRenderTick(long long_1, CallbackInfoReturnable<Integer> cir) {
        TimerEvent event = new TimerEvent();
        HexTech.EVENT_BUS.post(event);
        if (!event.isCancelled()) {
            this.lastFrameDuration = event.isModified() ? (this.lastFrameDuration *= event.get()) : (this.lastFrameDuration *= HexTech.TIMER.get());
        }
    }
}
