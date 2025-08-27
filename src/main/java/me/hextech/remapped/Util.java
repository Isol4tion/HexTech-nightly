package me.hextech.remapped;

import me.hextech.remapped.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Util {
    public static boolean isBurrowed(PlayerEntity entity, boolean Echest) {
        return Util.doesBoxTouchBlock(entity.method_5829(), Echest);
    }

    public static boolean doesBoxTouchBlock(Box box, boolean Echest) {
        int x = (int)Math.floor(box.field_1323);
        while ((double)x < Math.ceil(box.field_1320)) {
            int y = (int)Math.floor(box.field_1322);
            while ((double)y < Math.ceil(box.field_1325)) {
                int z = (int)Math.floor(box.field_1321);
                while ((double)z < Math.ceil(box.field_1324)) {
                    if (Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.field_10540 || Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.field_9987 || Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.field_10443 && Echest || Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.field_23152) {
                        return true;
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }
}
