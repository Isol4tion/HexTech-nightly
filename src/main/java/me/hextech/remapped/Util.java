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
        int x = (int)Math.floor(box.minX);
        while ((double)x < Math.ceil(box.maxX)) {
            int y = (int)Math.floor(box.minY);
            while ((double)y < Math.ceil(box.maxY)) {
                int z = (int)Math.floor(box.minZ);
                while ((double)z < Math.ceil(box.maxZ)) {
                    if (Wrapper.mc.world.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.OBSIDIAN || Wrapper.mc.world.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.BEDROCK || Wrapper.mc.world.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.ENDER_CHEST && Echest || Wrapper.mc.world.method_8320(new BlockPos(x, y, z)).method_26204() == Blocks.RESPAWN_ANCHOR) {
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
