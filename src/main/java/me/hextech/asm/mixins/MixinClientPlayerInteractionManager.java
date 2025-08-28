package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.api.events.impl.ClickBlockEvent;
import me.hextech.remapped.MineTweak;
import me.hextech.remapped.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerInteractionManager.class})
public class MixinClientPlayerInteractionManager {
//    @Shadow
//    private ItemStack selectedStack;
//
//    @ModifyVariable(method={"isCurrentlyBreaking"}, at=@At(value="STORE"))
//    private ClientPlayerInteractionManager stack(ClientPlayerInteractionManager value) {
//        return MineTweak.INSTANCE.noReset() ? this.selectedStack : value.getReachDistance();
//    }todo

    @ModifyConstant(method={"updateBlockBreakingProgress"}, constant={@Constant(intValue=5)})
    private int MiningCooldownFix(int value) {
        return MineTweak.INSTANCE.noDelay() ? 0 : value;
    }

    @Inject(method={"cancelBlockBreaking"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookCancelBlockBreaking(CallbackInfo callbackInfo) {
        if (MineTweak.INSTANCE.noAbort()) {
            callbackInfo.cancel();
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"getReachDistance()F"}, cancellable=true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> ci) {
        Reach reachHack = Reach.INSTANCE;
        if (reachHack.isOn()) {
            ci.setReturnValue(Float.valueOf(reachHack.distance.getValueFloat()));
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"hasExtendedReach()Z"}, cancellable=true)
    private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        Reach reachHack = Reach.INSTANCE;
        if (reachHack.isOn()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method={"attackBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        ClickBlockEvent event = new ClickBlockEvent(pos, direction);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}
