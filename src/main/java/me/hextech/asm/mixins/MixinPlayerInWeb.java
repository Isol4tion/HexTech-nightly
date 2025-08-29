package me.hextech.asm.mixins;

import me.hextech.api.utils.Wrapper;
import me.hextech.mod.modules.impl.movement.FastWeb_dehcwwTxEbDSnkFtZvNl;
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

@Mixin(value = {CobwebBlock.class})
public class MixinPlayerInWeb {
    @Inject(at = {@At(value = "HEAD")}, method = {"onEntityCollision"}, cancellable = true)
    private void onGetVelocityMultiplier(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.isOn() && (Wrapper.mc.options.sneakKey.isPressed() || !FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.onlySneak.getValue())) {
            if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.groundcheck.getValue() && Wrapper.mc.player.isOnGround()) {
                return;
            }
            if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.mode.is(FastWeb_dehcwwTxEbDSnkFtZvNl.Mode.Ignore)) {
                ci.cancel();
            } else if (FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.mode.is(FastWeb_dehcwwTxEbDSnkFtZvNl.Mode.Custom)) {
                ci.cancel();
                entity.slowMovement(state, new Vec3d(FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.xZSlow.getValue() / 100.0, FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.ySlow.getValue() / 100.0, FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.xZSlow.getValue() / 100.0));
            }
        }
    }
}
