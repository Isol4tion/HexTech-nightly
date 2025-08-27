package me.hextech.remapped;

import java.util.ArrayList;
import me.hextech.remapped.AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.Vec3;
import me.hextech.remapped.Wrapper;
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
        Block block = PathUtils.mc.field_1687.method_8320(new BlockPos(pos.method_10263(), pos.method_10264(), pos.method_10260())).method_26204();
        return block == Blocks.field_10124 || block instanceof PlantBlock || block == Blocks.field_10597 || block == Blocks.field_9983 || block == Blocks.field_10382 || block == Blocks.field_27097 || block instanceof WallSignBlock;
    }

    public static ArrayList<Vec3> computePath(LivingEntity fromEntity, LivingEntity toEntity) {
        return PathUtils.computePath(new Vec3(fromEntity.method_23317(), fromEntity.method_23318(), fromEntity.method_23321()), new Vec3(toEntity.method_23317(), toEntity.method_23318(), toEntity.method_23321()));
    }

    public static ArrayList<Vec3> computePath(Vec3d vec3d) {
        return PathUtils.computePath(new Vec3(PathUtils.mc.field_1724.method_23317(), PathUtils.mc.field_1724.method_23318(), PathUtils.mc.field_1724.method_23321()), new Vec3(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350));
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
