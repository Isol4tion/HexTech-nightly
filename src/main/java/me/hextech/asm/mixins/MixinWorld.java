package me.hextech.asm.mixins;

import me.hextech.remapped.api.utils.combat.CombatUtil;
import me.hextech.remapped.MineTweak;
import me.hextech.remapped.api.utils.Wrapper;
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
        if (Wrapper.mc.world != null && Wrapper.mc.world.isInBuildLimit(pos)) {
            WorldChunk worldChunk;
            BlockState tempState;
            if (CombatUtil.terrainIgnore || CombatUtil.modifyPos != null) {
                WorldChunk worldChunk2 = Wrapper.mc.world.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
                BlockState tempState2 = worldChunk2.getBlockState(pos);
                if (pos.equals(CombatUtil.modifyPos)) {
                    cir.setReturnValue(CombatUtil.modifyBlockState);
                    return;
                }
                if (CombatUtil.terrainIgnore) {
                    if (tempState2.getBlock() == Blocks.OBSIDIAN || tempState2.getBlock() == Blocks.BEDROCK || tempState2.getBlock() == Blocks.ENDER_CHEST || tempState2.getBlock() == Blocks.RESPAWN_ANCHOR || tempState2.getBlock() == Blocks.NETHERITE_BLOCK) {
                        return;
                    }
                    cir.setReturnValue(Blocks.AIR.getDefaultState());
                }
            } else if (MineTweak.INSTANCE.isActive && (tempState = (worldChunk = Wrapper.mc.world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)).getBlockState(pos)).getBlock() == Blocks.BEDROCK) {
                cir.setReturnValue(Blocks.AIR.getDefaultState());
            }
        }
    }
}
