package me.hextech.remapped;

import java.util.ArrayList;
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

public class AStarCustomPathFinder_VSltHecsGAqplnRtLzRQ implements Wrapper {
   private static final Vec3[] flatCardinalDirections;
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
      return checkPositionValidity((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), checkGround);
   }

   public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
      BlockPos block1 = new BlockPos(x, y, z);
      BlockPos block2 = new BlockPos(x, y + 1, z);
      BlockPos block3 = new BlockPos(x, y - 1, z);
      return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3);
   }

   private static boolean isBlockSolid(BlockPos block) {
      return mc.field_1687.method_8320(block).field_23166 != null && mc.field_1687.method_8320(block).field_23166.field_20337
         || mc.field_1687.method_8320(block).method_26204() instanceof SlabBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof StairsBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof CactusBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof ChestBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof EnderChestBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof SkullBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof PaneBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof FenceBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof WallBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof StainedGlassBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof PistonBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof PistonExtensionBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof PistonHeadBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof StainedGlassBlock
         || mc.field_1687.method_8320(block).method_26204() instanceof TrapdoorBlock;
   }

   private static boolean isSafeToWalkOn(BlockPos block) {
      return !(mc.field_1687.method_8320(block).method_26204() instanceof FenceBlock)
         && !(mc.field_1687.method_8320(block).method_26204() instanceof WallBlock);
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
      ArrayList<Vec3> initPath = new ArrayList();
      initPath.add(this.startVec3);
      this.hubsToWork
         .add(new AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl(this.startVec3, null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));

      label51:
      for (int i = 0; i < loops; i++) {
         this.hubsToWork.sort(new AStarCustomPathFinder());
         int j = 0;
         if (this.hubsToWork.size() == 0) {
            break;
         }

         for (AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o : new ArrayList(this.hubsToWork)) {
            if (++j <= depth) {
               this.hubsToWork.remove(o);
               this.hubs.add(o);

               for (Vec3 direction : flatCardinalDirections) {
                  Vec3 loc = o.getLoc().add(direction).floor();
                  if (checkPositionValidity(loc, false) && this.addHub(o, loc, 0.0)) {
                     break label51;
                  }
               }

               Vec3 loc1 = o.getLoc().addVector(0.0, 1.0, 0.0).floor();
               if (checkPositionValidity(loc1, false) && this.addHub(o, loc1, 0.0)) {
                  break label51;
               }

               Vec3 loc2 = o.getLoc().addVector(0.0, -1.0, 0.0).floor();
               if (checkPositionValidity(loc2, false) && this.addHub(o, loc2, 0.0)) {
                  break label51;
               }
               continue;
            }
         }
      }

      this.hubs.sort(new AStarCustomPathFinder());
      this.path = ((AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl)this.hubs.get(0)).getPath();
   }

   public AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl isHubExisting(Vec3 loc) {
      for (AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl hub : this.hubs) {
         if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ()) {
            return hub;
         }
      }

      for (AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl hubx : this.hubsToWork) {
         if (hubx.getLoc().getX() == loc.getX() && hubx.getLoc().getY() == loc.getY() && hubx.getLoc().getZ() == loc.getZ()) {
            return hubx;
         }
      }

      return null;
   }

   public boolean addHub(AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl parent, Vec3 loc, double cost) {
      AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl existingHub = this.isHubExisting(loc);
      double totalCost = cost;
      if (parent != null) {
         totalCost = cost + parent.getTotalCost();
      }

      if (existingHub == null) {
         double minDistanceSquared = 9.0;
         if (loc.getX() == this.endVec3.getX() && loc.getY() == this.endVec3.getY() && loc.getZ() == this.endVec3.getZ()
            || loc.squareDistanceTo(this.endVec3) <= minDistanceSquared) {
            this.path.clear();
            this.path = parent.getPath();
            this.path.add(loc);
            return true;
         }

         ArrayList<Vec3> path = new ArrayList(parent.getPath());
         path.add(loc);
         this.hubsToWork.add(new AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
      } else if (existingHub.getCost() > cost) {
         ArrayList<Vec3> path = new ArrayList(parent.getPath());
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
