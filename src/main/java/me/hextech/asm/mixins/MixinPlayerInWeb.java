package me.hextech.asm.mixins;

import me.hextech.remapped.FastWeb;
import me.hextech.remapped.FastWeb_dehcwwTxEbDSnkFtZvNl;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={CobwebBlock.class})
public class MixinPlayerInWeb {
    @Inject(at={@At(value="HEAD")}, method={"onEntityCollision"}, cancellable=true)
    private void onGetVelocityMultiplier(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.isOn() && (Wrapper.mc.field_1690.field_1832.method_1434() || !FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.onlySneak.getValue())) {
            if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.groundcheck.getValue() && Wrapper.mc.player.method_24828()) {
                return;
            }
            if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.mode.is(FastWeb.Ignore)) {
                ci.cancel();
            } else if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.mode.is(FastWeb.Custom)) {
                ci.cancel();
                entity.method_5844(state, new Vec3d(FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.xZSlow.getValue() / 100.0, FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.ySlow.getValue() / 100.0, FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.xZSlow.getValue() / 100.0));
            }
        }
    }
}
