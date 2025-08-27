package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.hextech.asm.accessors.IClientWorld;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

public class BlockUtil implements Wrapper {
   public static final List<Block> shiftBlocks = new ArrayList();
   public static final ArrayList<BlockPos> placedPos = new ArrayList();

   public static List<EndCrystalEntity> getEndCrystals(Box box) {
      List<EndCrystalEntity> list = new ArrayList();

      for (Entity entity : mc.field_1687.method_18112()) {
         if (entity instanceof EndCrystalEntity) {
            EndCrystalEntity crystal = (EndCrystalEntity)entity;
            if (crystal.method_5829().method_994(box)) {
               list.add(crystal);
            }
         }
      }

      return list;
   }

   public static List<Entity> getEntities(Box box) {
      List<Entity> list = new ArrayList();

      for (Entity entity : mc.field_1687.method_18112()) {
         if (entity != null && entity.method_5829().method_994(box)) {
            list.add(entity);
         }
      }

      return list;
   }

   public static boolean isAir(BlockPos pos) {
      return mc.field_1687.method_22347(pos);
   }

   public static boolean isMining(BlockPos pos) {
      return me.hextech.HexTech.BREAK.isMining(pos) || pos.equals(SpeedMine.breakPos);
   }

   public static boolean canPlace(BlockPos pos) {
      return canPlace(pos, 1000.0);
   }

   public static boolean canPlace(BlockPos pos, double distance) {
      if (getPlaceSide(pos, distance) == null) {
         return false;
      } else {
         return !canReplace(pos) ? false : !hasEntity(pos, false);
      }
   }

   public static boolean canPlace(BlockPos pos, double distance, boolean ignoreCrystal) {
      if (getPlaceSide(pos, distance) == null) {
         return false;
      } else {
         return !canReplace(pos) ? false : !hasEntity(pos, ignoreCrystal);
      }
   }

   public static boolean isSafe(Block block) {
      List<Block> safeBlocks = Arrays.asList(Blocks.field_10540, Blocks.field_9987, Blocks.field_10443, Blocks.field_10535);
      return !safeBlocks.contains(block);
   }

   public static boolean clientCanPlace(BlockPos pos) {
      return clientCanPlace(pos, false);
   }

   public static boolean clientCanPlace(BlockPos pos, boolean ignoreCrystal) {
      return !canReplace(pos) ? false : !hasEntity(pos, ignoreCrystal);
   }

   public static boolean hasEntity(BlockPos pos, boolean ignoreCrystal) {
      for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
         if (entity.method_5805()
            && !(entity instanceof ItemEntity)
            && !(entity instanceof ExperienceOrbEntity)
            && !(entity instanceof ExperienceBottleEntity)
            && !(entity instanceof ArrowEntity)
            && (!ignoreCrystal || !(entity instanceof EndCrystalEntity))
            && (!(entity instanceof ArmorStandEntity) || !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue())) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasCrystal(BlockPos pos) {
      for (Entity entity : mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos))) {
         if (entity.method_5805() && entity instanceof EndCrystalEntity) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal) {
      for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
         if (entity.method_5805()
            && (!ignoreCrystal || !(entity instanceof EndCrystalEntity))
            && (!(entity instanceof ArmorStandEntity) || !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue())) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
         if (entity.method_5805()
            && (!ignoreItem || !(entity instanceof ItemEntity))
            && (!ignoreCrystal || !(entity instanceof EndCrystalEntity))
            && (!(entity instanceof ArmorStandEntity) || !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue())) {
            return true;
         }
      }

      return false;
   }

   public static Direction getBestNeighboring(BlockPos pos, Direction facing) {
      for (Direction i : Direction.values()) {
         if ((facing == null || !pos.method_10093(i).equals(pos.method_10079(facing, -1)))
            && i != Direction.field_11033
            && getPlaceSide(pos, false, true) != null) {
            return i;
         }
      }

      Direction bestFacing = null;
      double distance = 0.0;

      for (Direction ix : Direction.values()) {
         if ((facing == null || !pos.method_10093(ix).equals(pos.method_10079(facing, -1)))
            && ix != Direction.field_11033
            && getPlaceSide(pos) != null
            && (bestFacing == null || mc.field_1724.method_5707(pos.method_10093(ix).method_46558()) < distance)) {
            bestFacing = ix;
            distance = mc.field_1724.method_5707(pos.method_10093(ix).method_46558());
         }
      }

      return bestFacing;
   }

   public static boolean canPlaceCrystal(BlockPos pos) {
      BlockPos obsPos = pos.method_10074();
      BlockPos boost = obsPos.method_10084();
      return (getBlock(obsPos) == Blocks.field_9987 || getBlock(obsPos) == Blocks.field_10540)
         && getClickSideStrict(obsPos) != null
         && getBlock(boost) == Blocks.field_10124
         && !hasEntityBlockCrystal(boost, false)
         && !hasEntityBlockCrystal(boost.method_10084(), false)
         && (!CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() || getBlock(boost.method_10084()) == Blocks.field_10124);
   }

   public static void placeCrystal(BlockPos pos, boolean rotate) {
      boolean offhand = mc.field_1724.method_6079().method_7909() == Items.field_8301;
      BlockPos obsPos = pos.method_10074();
      Direction facing = getClickSide(obsPos);
      Vec3d vec = obsPos.method_46558()
         .method_1031(
            (double)facing.method_10163().method_10263() * 0.5,
            (double)facing.method_10163().method_10264() * 0.5,
            (double)facing.method_10163().method_10260() * 0.5
         );
      if (rotate) {
         EntityUtil.faceVector(vec);
      }

      clickBlock(obsPos, facing, false, offhand ? Hand.field_5810 : Hand.field_5808);
   }

   public static void placeBlock(BlockPos pos, boolean rotate) {
      placeBlock(pos, rotate, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.packetPlace.getValue());
   }

   public static void placeBlock(BlockPos pos, boolean rotate, boolean packet) {
      if (airPlace()) {
         placedPos.add(pos);
         clickBlock(pos, Direction.field_11036, rotate, Hand.field_5808, packet);
      } else {
         Direction side = getPlaceSide(pos);
         if (side != null) {
            placedPos.add(pos);
            clickBlock(pos.method_10093(side), side.method_10153(), rotate, Hand.field_5808, packet);
         }
      }
   }

   public static void placeBlock(BlockPos pos, boolean rotate, boolean packet, boolean bypass) {
      if (airPlace()) {
         for (Direction i : Direction.values()) {
            if (mc.field_1687.method_22347(pos.method_10093(i))) {
               clickBlock(pos, i, rotate, Hand.field_5808, packet);
               return;
            }
         }
      }

      Direction side = getPlaceSide(pos);
      if (side != null) {
         Vec3d directionVec = new Vec3d(
            (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
            (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
            (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
         );
         if (rotate) {
            EntityUtil.faceVector(directionVec);
         }

         EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
         placedPos.add(pos);
         boolean sprint = false;
         if (mc.field_1724 != null) {
            sprint = mc.field_1724.method_5624();
         }

         boolean sneak = false;
         if (mc.field_1687 != null) {
            sneak = needSneak(mc.field_1687.method_8320(result.method_17777()).method_26204()) && !mc.field_1724.method_5715();
         }

         if (sprint) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12985));
         }

         if (sneak) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12979));
         }

         clickBlock(pos.method_10093(side), side.method_10153(), rotate, Hand.field_5808, packet);
         if (sneak) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12984));
         }

         if (sprint) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12981));
         }

         if (bypass) {
            EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         }
      }
   }

   public static boolean isHole(BlockPos pos) {
      return isHole(pos, true, false, false);
   }

   public static boolean isHole(BlockPos pos, boolean canStand, boolean checkTrap, boolean anyBlock) {
      int blockProgress = 0;

      for (Direction i : Direction.values()) {
         if (i != Direction.field_11036
            && i != Direction.field_11033
            && (anyBlock && !mc.field_1687.method_22347(pos.method_10093(i)) || CombatUtil.isHard(pos.method_10093(i)))) {
            blockProgress++;
         }
      }

      return (
            !checkTrap
               || getBlock(pos) == Blocks.field_10124
                  && getBlock(pos.method_10069(0, 1, 0)) == Blocks.field_10124
                  && getBlock(pos.method_10069(0, 2, 0)) == Blocks.field_10124
         )
         && blockProgress > 3
         && (!canStand || getState(pos.method_10069(0, -1, 0)).method_51366());
   }

   public static void clickBlock(BlockPos pos, Direction side, boolean rotate) {
      clickBlock(pos, side, rotate, Hand.field_5808);
   }

   public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand) {
      clickBlock(pos, side, rotate, hand, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.packetPlace.getValue());
   }

   public static void clickBlock(BlockPos pos, Direction side, boolean rotate, boolean packet) {
      clickBlock(pos, side, rotate, Hand.field_5808, packet);
   }

   public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand, boolean packet) {
      Vec3d directionVec = new Vec3d(
         (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
         (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
         (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
      );
      if (rotate) {
         EntityUtil.faceVector(directionVec);
      }

      EntityUtil.swingHand(hand, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
      BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
      if (packet) {
         mc.field_1724.field_3944.method_52787(new PlayerInteractBlockC2SPacket(Hand.field_5808, result, getWorldActionId(mc.field_1687)));
      } else {
         mc.field_1761.method_2896(mc.field_1724, hand, result);
      }
   }

   public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand, SwingSide swingSide) {
      Vec3d directionVec = new Vec3d(
         (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
         (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
         (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
      );
      if (rotate) {
         EntityUtil.faceVector(directionVec);
      }

      EntityUtil.swingHand(hand, swingSide);
      BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
      mc.field_1724.field_3944.method_52787(new PlayerInteractBlockC2SPacket(hand, result, getWorldActionId(mc.field_1687)));
   }

   public static Direction getPlaceSide(BlockPos pos) {
      return getPlaceSide(
         pos,
         CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict,
         CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Legit
      );
   }

   public static Direction getPlaceSide(BlockPos pos, boolean strict, boolean legit) {
      double dis = 114514.0;
      Direction side = null;

      for (Direction i : Direction.values()) {
         if (canClick(pos.method_10093(i))
            && !canReplace(pos.method_10093(i))
            && (!legit || EntityUtil.canSee(pos.method_10093(i), i.method_10153()))
            && (!strict || isStrictDirection(pos.method_10093(i), i.method_10153()))) {
            double vecDis = mc.field_1724
               .method_5707(
                  pos.method_46558()
                     .method_1031(
                        (double)i.method_10163().method_10263() * 0.5,
                        (double)i.method_10163().method_10264() * 0.5,
                        (double)i.method_10163().method_10260() * 0.5
                     )
               );
            if (side == null || vecDis < dis) {
               side = i;
               dis = vecDis;
            }
         }
      }

      if (side == null && airPlace()) {
         for (Direction ix : Direction.values()) {
            if (mc.field_1687.method_22347(pos.method_10093(ix))) {
               return ix;
            }
         }
      }

      return side;
   }

   public static double distanceToXZ(double x, double z) {
      double dx = mc.field_1724.method_23317() - x;
      double dz = mc.field_1724.method_23321() - z;
      return Math.sqrt(dx * dx + dz * dz);
   }

   public static Direction getPlaceSide(BlockPos pos, double distance) {
      double dis = 114514.0;
      Direction side = null;

      for (Direction i : Direction.values()) {
         if (canClick(pos.method_10093(i))
            && !canReplace(pos.method_10093(i))
            && (
               CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Legit
                  ? EntityUtil.canSee(pos.method_10093(i), i.method_10153())
                  : CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() != Placement.Strict
                     || isStrictDirection(pos.method_10093(i), i.method_10153())
            )) {
            double vecDis = mc.field_1724
               .method_5707(
                  pos.method_46558()
                     .method_1031(
                        (double)i.method_10163().method_10263() * 0.5,
                        (double)i.method_10163().method_10264() * 0.5,
                        (double)i.method_10163().method_10260() * 0.5
                     )
               );
            if (!((double)MathHelper.method_15355((float)vecDis) > distance) && (side == null || vecDis < dis)) {
               side = i;
               dis = vecDis;
            }
         }
      }

      if (side == null && airPlace()) {
         for (Direction ix : Direction.values()) {
            if (mc.field_1687.method_22347(pos.method_10093(ix))) {
               return ix;
            }
         }
      }

      return side;
   }

   public static Direction getClickSide(BlockPos pos) {
      if (pos.equals(EntityUtil.getPlayerPos())) {
         return Direction.field_11036;
      } else {
         Direction side = null;
         double range = 100.0;

         for (Direction i : Direction.values()) {
            if (EntityUtil.canSee(pos, i) && !((double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(i).method_46558())) > range)) {
               side = i;
               range = (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(i).method_46558()));
            }
         }

         if (side != null) {
            return side;
         } else {
            side = Direction.field_11036;

            for (Direction ix : Direction.values()) {
               if ((
                     CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() != Placement.Strict
                        || isStrictDirection(pos, ix) && isAir(pos.method_10093(ix))
                  )
                  && !((double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(ix).method_46558())) > range)) {
                  side = ix;
                  range = (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(ix).method_46558()));
               }
            }

            return side;
         }
      }
   }

   public static Direction getClickSideStrict(BlockPos pos) {
      Direction side = null;
      double range = 100.0;

      for (Direction i : Direction.values()) {
         if (EntityUtil.canSee(pos, i) && !((double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(i).method_46558())) > range)) {
            side = i;
            range = (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(i).method_46558()));
         }
      }

      if (side != null) {
         return side;
      } else {
         side = null;

         for (Direction ix : Direction.values()) {
            if ((
                  CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() != Placement.Strict
                     || isStrictDirection(pos, ix) && isAir(pos.method_10093(ix))
               )
               && !((double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(ix).method_46558())) > range)) {
               side = ix;
               range = (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_10093(ix).method_46558()));
            }
         }

         return side;
      }
   }

   public static boolean isStrictDirection(BlockPos pos, Direction side) {
      BlockState blockState = mc.field_1687.method_8320(pos);
      boolean isFullBox = blockState.method_26215() || blockState.method_26234(mc.field_1687, pos) || getBlock(pos) == Blocks.field_10343;
      return isStrictDirection(pos, side, isFullBox);
   }

   public static boolean isStrictDirection(BlockPos pos, Direction side, boolean isFullBox) {
      if (EntityUtil.getPlayerPos().method_10264() - pos.method_10264() >= 0 && side == Direction.field_11033) {
         return false;
      } else {
         if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue()) {
            if (side == Direction.field_11036 && (double)(pos.method_10264() + 1) > mc.field_1724.method_33571().method_10214()) {
               return false;
            }
         } else if (side == Direction.field_11036 && (double)pos.method_10264() > mc.field_1724.method_33571().method_10214()) {
            return false;
         }

         if (getBlock(pos.method_10093(side)) != Blocks.field_10540
            && getBlock(pos.method_10093(side)) != Blocks.field_9987
            && getBlock(pos.method_10093(side)) != Blocks.field_23152) {
            Vec3d eyePos = EntityUtil.getEyesPos();
            Vec3d blockCenter = pos.method_46558();
            ArrayList<Direction> validAxis = new ArrayList();
            validAxis.addAll(checkAxis(eyePos.field_1352 - blockCenter.field_1352, Direction.field_11039, Direction.field_11034, !isFullBox));
            validAxis.addAll(checkAxis(eyePos.field_1351 - blockCenter.field_1351, Direction.field_11033, Direction.field_11036, true));
            validAxis.addAll(checkAxis(eyePos.field_1350 - blockCenter.field_1350, Direction.field_11043, Direction.field_11035, !isFullBox));
            return validAxis.contains(side);
         } else {
            return false;
         }
      }
   }

   public static ArrayList<Direction> checkAxis(double diff, Direction negativeSide, Direction positiveSide, boolean bothIfInRange) {
      ArrayList<Direction> valid = new ArrayList();
      if (diff < -0.5) {
         valid.add(negativeSide);
      }

      if (diff > 0.5) {
         valid.add(positiveSide);
      }

      if (bothIfInRange) {
         if (!valid.contains(negativeSide)) {
            valid.add(negativeSide);
         }

         if (!valid.contains(positiveSide)) {
            valid.add(positiveSide);
         }
      }

      return valid;
   }

   public static int getWorldActionId(ClientWorld world) {
      PendingUpdateManager pum = getUpdateManager(world);
      int p = pum.method_41942();
      pum.close();
      return p;
   }

   public static PendingUpdateManager getUpdateManager(ClientWorld world) {
      return ((IClientWorld)world).acquirePendingUpdateManager();
   }

   public static ArrayList<BlockEntity> getTileEntities() {
      return (ArrayList<BlockEntity>)getLoadedChunks()
         .flatMap(chunk -> chunk.method_12214().values().stream())
         .collect(Collectors.toCollection(ArrayList::new));
   }

   public static Stream<WorldChunk> getLoadedChunks() {
      int radius = Math.max(2, mc.field_1690.method_38521()) + 3;
      int diameter = radius * 2 + 1;
      ChunkPos center = mc.field_1724.method_31476();
      ChunkPos min = new ChunkPos(center.field_9181 - radius, center.field_9180 - radius);
      ChunkPos max = new ChunkPos(center.field_9181 + radius, center.field_9180 + radius);
      return Stream.iterate(min, pos -> {
            int x = pos.field_9181;
            int z = pos.field_9180;
            if (++x > max.field_9181) {
               x = min.field_9181;
               z++;
            }

            return new ChunkPos(x, z);
         })
         .limit((long)diameter * (long)diameter)
         .filter(c -> mc.field_1687.method_8393(c.field_9181, c.field_9180))
         .map(c -> mc.field_1687.method_8497(c.field_9181, c.field_9180))
         .filter(Objects::nonNull);
   }

   public static ArrayList<BlockPos> getSphere(float range) {
      return getSphere(range, mc.field_1724.method_33571());
   }

   public static ArrayList<BlockPos> getSphere(float range, Vec3d pos) {
      ArrayList<BlockPos> list = new ArrayList();

      for (double x = pos.method_10216() - (double)range; x < pos.method_10216() + (double)range; x++) {
         for (double y = pos.method_10214() - (double)range; y < pos.method_10214() + (double)range; y++) {
            for (double z = pos.method_10215() - (double)range; z < pos.method_10215() + (double)range; z++) {
               BlockPos curPos = new BlockPosX(x, y, z);
               if (!list.contains(curPos)) {
                  list.add(curPos);
               }
            }
         }
      }

      return list;
   }

   public static BlockState getState(BlockPos pos) {
      return mc.field_1687.method_8320(pos);
   }

   public static Block getBlock(BlockPos pos) {
      return getState(pos).method_26204();
   }

   public static boolean canReplace(BlockPos pos) {
      if (pos.method_10264() >= 320) {
         return false;
      } else {
         return mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10343
               && WebAuraTick_gaIdrzDzsbegzNTtPQoV.ignore
               && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.replace.getValue()
            ? true
            : getState(pos).method_45474();
      }
   }

   public static boolean canClick(BlockPos pos) {
      return mc.field_1687.method_8320(pos).method_51367()
         && (!shiftBlocks.contains(getBlock(pos)) && !(getBlock(pos) instanceof BedBlock) || mc.field_1724.method_5715());
   }

   public static boolean airPlace() {
      return CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.AirPlace;
   }

   public static boolean canBlockFacing(BlockPos pos) {
      boolean airCheck = false;

      for (Direction side : Direction.values()) {
         if (canClick(pos.method_10093(side))) {
            airCheck = true;
         }
      }

      return airCheck;
   }

   public static boolean needSneak(Block in) {
      return shiftBlocks.contains(in);
   }
}
