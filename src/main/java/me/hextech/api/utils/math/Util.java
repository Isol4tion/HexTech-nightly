package me.hextech.api.utils.math;

import me.hextech.api.utils.Wrapper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Util {
    public static boolean isBurrowed(PlayerEntity entity, boolean Echest) {
        return Util.doesBoxTouchBlock(entity.getBoundingBox(), Echest);
    }

    public static boolean doesBoxTouchBlock(Box box, boolean Echest) {
        int x = (int)Math.floor(box.minX);
        while ((double)x < Math.ceil(box.maxX)) {
            int y = (int)Math.floor(box.minY);
            while ((double)y < Math.ceil(box.maxY)) {
                int z = (int)Math.floor(box.minZ);
                while ((double)z < Math.ceil(box.maxZ)) {
                    if (Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.OBSIDIAN || Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.BEDROCK || Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.ENDER_CHEST && Echest || Wrapper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.RESPAWN_ANCHOR) {
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
