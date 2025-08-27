package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.WorldBreakEvent;
import net.minecraft.client.render.BlockBreakingInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BlockBreakingInfo.class})
public class MixinBlockBreakingInfo {
    @Inject(method={"compareTo"}, at={@At(value="HEAD")})
    public void onCompareTo(BlockBreakingInfo blockBreakingInfo, CallbackInfoReturnable<Integer> cir) {
        HexTech.EVENT_BUS.post(new WorldBreakEvent(blockBreakingInfo));
    }
}
