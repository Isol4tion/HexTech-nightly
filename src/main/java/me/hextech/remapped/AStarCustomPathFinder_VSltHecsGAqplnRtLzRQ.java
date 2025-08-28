package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Iterator;

import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.util.math.BlockPos;

public class AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ
implements Wrapper {
    private static final Vec3[] flatCardinalDirections = new Vec3[]{new Vec3(1.0F, 0.0F, 0.0F), new Vec3(-1.0F, 0.0F, 0.0F), new Vec3(0.0F, 0.0F, 1.0F), new Vec3(0.0F, 0.0F, -1.0F)};
    private final Vec3 startVec3;
    private final Vec3 endVec3;
    private final ArrayList<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl> hubs = new ArrayList();
    private final ArrayList<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl> hubsToWork = new ArrayList();
    private ArrayList<Vec3> path = new ArrayList();

    public AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ(Vec3 startVec3, Vec3 endVec3) {
        this.startVec3 = startVec3.addVector(0.0, 0.0, 0.0).floor();
        this.endVec3 = endVec3.addVector(0.0, 0.0, 0.0).floor();
    }

    public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
        return AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.checkPositionValidity((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.isBlockSolid(block1) && !AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.isBlockSolid(block2) && (AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.isBlockSolid(block3) || !checkGround) && AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.isSafeToWalkOn(block3);
    }

    private static boolean isBlockSolid(BlockPos block) {
        return AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).shapeCache != null && AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).shapeCache.isFullCube || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof SlabBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof StairsBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof CactusBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof ChestBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof EnderChestBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof SkullBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof PaneBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof FenceBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof WallBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof StainedGlassBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof PistonBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof PistonExtensionBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof PistonHeadBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof StainedGlassBlock || AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof TrapdoorBlock;
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        return !(AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof FenceBlock) && !(AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.mc.world.getBlockState(block).getBlock() instanceof WallBlock);
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void compute() {
        this.compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        this.path.clear();
        this.hubsToWork.clear();
        ArrayList<Vec3> initPath = new ArrayList<Vec3>();
        initPath.add(this.startVec3);
        this.hubsToWork.add(new AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl(this.startVec3, null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));
        block0: for (int i = 0; i < loops; ++i) {
            this.hubsToWork.sort(new AStarCustomPathFinder());
            int j = 0;
            if (this.hubsToWork.size() == 0) break;
            for (AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o : new ArrayList<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl>(this.hubsToWork)) {
                Vec3 loc2;
                if (++j > depth) continue block0;
                this.hubsToWork.remove(o);
                this.hubs.add(o);
                for (Vec3 direction : flatCardinalDirections) {
                    Vec3 loc = o.getLoc().add(direction).floor();
                    if (AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.checkPositionValidity(loc, false) && this.addHub(o, loc, 0.0)) break block0;
                }
                Vec3 loc1 = o.getLoc().addVector(0.0, 1.0, 0.0).floor();
                if ((!AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.checkPositionValidity(loc1, false) || !this.addHub(o, loc1, 0.0)) && (!AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ.checkPositionValidity(loc2 = o.getLoc().addVector(0.0, -1.0, 0.0).floor(), false) || !this.addHub(o, loc2, 0.0))) continue;
                break block0;
            }
        }
        this.hubs.sort(new AStarCustomPathFinder());
        this.path = this.hubs.get(0).getPath();
    }

    public AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl isHubExisting(Vec3 loc) {
        AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl hub;
        Iterator<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl> var2 = this.hubs.iterator();
        do {
            if (var2.hasNext()) continue;
            var2 = this.hubsToWork.iterator();
            do {
                if (var2.hasNext()) continue;
                return null;
            } while ((hub = var2.next()).getLoc().getX() != loc.getX() || hub.getLoc().getY() != loc.getY() || hub.getLoc().getZ() != loc.getZ());
            return hub;
        } while ((hub = var2.next()).getLoc().getX() != loc.getX() || hub.getLoc().getY() != loc.getY() || hub.getLoc().getZ() != loc.getZ());
        return hub;
    }

    public boolean addHub(AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl parent, Vec3 loc, double cost) {
        AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl existingHub = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost = cost + parent.getTotalCost();
        }
        if (existingHub == null) {
            double minDistanceSquared = 9.0;
            if (loc.getX() == this.endVec3.getX() && loc.getY() == this.endVec3.getY() && loc.getZ() == this.endVec3.getZ() || loc.squareDistanceTo(this.endVec3) <= minDistanceSquared) {
                this.path.clear();
                this.path = parent.getPath();
                this.path.add(loc);
                return true;
            }
            ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            this.hubsToWork.add(new AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(this.endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }
}
