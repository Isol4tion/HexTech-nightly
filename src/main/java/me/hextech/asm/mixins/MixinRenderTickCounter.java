package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.TimerEvent;
import net.minecraft.class_317;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_317.class})
public class MixinRenderTickCounter {
    @Shadow
    public float field_1969;

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J", opcode=181, ordinal=0)}, method={"beginRenderTick(J)I"})
    public void onBeginRenderTick(long long_1, CallbackInfoReturnable<Integer> cir) {
        TimerEvent event = new TimerEvent();
        HexTech.EVENT_BUS.post(event);
        if (!event.isCancelled()) {
            this.field_1969 = event.isModified() ? (this.field_1969 *= event.get()) : (this.field_1969 *= HexTech.TIMER.get());
        }
    }
}
