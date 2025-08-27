package me.hextech.asm.mixins;

import me.hextech.remapped.NoSlow_PaVUKKxFbWGbplzMaucl;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Block.class})
public abstract class MixinBlock
implements ItemConvertible {
    @Inject(at={@At(value="HEAD")}, method={"getVelocityMultiplier()F"}, cancellable=true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (!NoSlow_PaVUKKxFbWGbplzMaucl.INSTANCE.isOn()) {
            return;
        }
        if (cir.getReturnValueF() < 1.0f) {
            cir.setReturnValue((Object)Float.valueOf(1.0f));
        }
    }
}
