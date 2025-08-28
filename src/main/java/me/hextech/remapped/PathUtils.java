package me.hextech.remapped;

import java.util.ArrayList;

import me.hextech.remapped.api.utils.Wrapper;
import me.hextech.remapped.api.utils.path.AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ;
import me.hextech.remapped.api.utils.world.BlockPosX;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PathUtils
implements Wrapper {
    private static boolean canPassThrough(BlockPos pos) {
        Block block = PathUtils.mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block == Blocks.AIR || block instanceof PlantBlock || block == Blocks.VINE || block == Blocks.LADDER || block == Blocks.WATER || block == Blocks.WATER_CAULDRON || block instanceof WallSignBlock;
    }

    public static ArrayList<Vec3> computePath(LivingEntity fromEntity, LivingEntity toEntity) {
        return PathUtils.computePath(new Vec3(fromEntity.getX(), fromEntity.getY(), fromEntity.getZ()), new Vec3(toEntity.getX(), toEntity.getY(), toEntity.getZ()));
    }

    public static ArrayList<Vec3> computePath(Vec3d vec3d) {
        return PathUtils.computePath(new Vec3(PathUtils.mc.player.getX(), PathUtils.mc.player.getY(), PathUtils.mc.player.getZ()), new Vec3(vec3d.x, vec3d.y, vec3d.z));
    }

    public static ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!PathUtils.canPassThrough(new BlockPosX(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ pathfinder = new AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i != 0 && i != pathFinderPath.size() - 1) {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.tp.getValue()) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    int x = (int)smallX;
                    block1: while ((double)x <= bigX) {
                        int y = (int)smallY;
                        while ((double)y <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y;
                        }
                        ++x;
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            } else {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }
}
