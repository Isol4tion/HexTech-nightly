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

@Mixin({World.class})
public abstract class MixinWorld {
   @Inject(
      method = {"getBlockState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void blockStateHook(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
      if (Wrapper.mc.field_1687 != null && Wrapper.mc.field_1687.method_24794(pos)) {
         if (CombatUtil.terrainIgnore || CombatUtil.modifyPos != null) {
            WorldChunk worldChunk = Wrapper.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4);
            BlockState tempState = worldChunk.method_8320(pos);
            if (CombatUtil.modifyPos != null && pos.equals(CombatUtil.modifyPos)) {
               cir.setReturnValue(CombatUtil.modifyBlockState);
               return;
            }

            if (CombatUtil.terrainIgnore) {
               if (tempState.method_26204() == Blocks.field_10540
                  || tempState.method_26204() == Blocks.field_9987
                  || tempState.method_26204() == Blocks.field_10443
                  || tempState.method_26204() == Blocks.field_23152
                  || tempState.method_26204() == Blocks.field_22108) {
                  return;
               }

               cir.setReturnValue(Blocks.field_10124.method_9564());
            }
         } else if (MineTweak.INSTANCE.isActive) {
            WorldChunk worldChunkx = Wrapper.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4);
            BlockState tempStatex = worldChunkx.method_8320(pos);
            if (tempStatex.method_26204() == Blocks.field_9987) {
               cir.setReturnValue(Blocks.field_10124.method_9564());
            }
         }
      }
   }
}
