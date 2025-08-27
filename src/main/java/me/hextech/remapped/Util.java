package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Util {
   public static boolean isBurrowed(PlayerEntity entity, boolean Echest) {
      return doesBoxTouchBlock(entity.method_5829(), Echest);
   }

   public static boolean doesBoxTouchBlock(Box box, boolean Echest) {
      for (int x = (int)Math.floor(box.field_1323); (double)x < Math.ceil(box.field_1320); x++) {
         for (int y = (int)Math.floor(box.field_1322); (double)y < Math.ceil(box.field_1325); y++) {
            for (int z = (int)Math.floor(box.field_1321); (double)z < Math.ceil(box.field_1324); z++) {
               if (Wrapper.mc.field_1687.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.field_10540
                  || Wrapper.mc.field_1687.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.field_9987
                  || Wrapper.mc.field_1687.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.field_10443 && Echest
                  || Wrapper.mc.field_1687.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.field_23152) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
