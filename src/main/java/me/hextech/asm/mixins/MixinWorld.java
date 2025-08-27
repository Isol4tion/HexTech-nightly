package me.hextech.asm.mixins;

import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.MineTweak;
import me.hextech.remapped.Wrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={World.class})
public abstract class MixinWorld {
    @Inject(method={"getBlockState"}, at={@At(value="HEAD")}, cancellable=true)
    public void blockStateHook(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (Wrapper.mc.world != null && Wrapper.mc.world.method_24794(pos)) {
            WorldChunk worldChunk;
            BlockState tempState;
            if (CombatUtil.terrainIgnore || CombatUtil.modifyPos != null) {
                WorldChunk worldChunk2 = Wrapper.mc.world.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4);
                BlockState tempState2 = worldChunk2.getBlockState(pos);
                if (CombatUtil.modifyPos != null && pos.equals((Object)CombatUtil.modifyPos)) {
                    cir.setReturnValue((Object)CombatUtil.modifyBlockState);
                    return;
                }
                if (CombatUtil.terrainIgnore) {
                    if (tempState2.getBlock() == Blocks.field_10540 || tempState2.getBlock() == Blocks.field_9987 || tempState2.getBlock() == Blocks.field_10443 || tempState2.getBlock() == Blocks.field_23152 || tempState2.getBlock() == Blocks.field_22108) {
                        return;
                    }
                    cir.setReturnValue((Object)Blocks.field_10124.method_9564());
                }
            } else if (MineTweak.INSTANCE.isActive && (tempState = (worldChunk = Wrapper.mc.world.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4)).getBlockState(pos)).getBlock() == Blocks.field_9987) {
                cir.setReturnValue((Object)Blocks.field_10124.method_9564());
            }
        }
    }
}
