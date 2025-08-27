package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.hextech.HexTech;
import me.hextech.asm.accessors.IClientWorld;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BypassSetting_RInKGmTQYgWFRhsUOiJP;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.Placement;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.SwingSide;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import me.hextech.remapped.Wrapper;
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
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.WorldChunk;

public class BlockUtil
implements Wrapper {
    public static final List<Block> shiftBlocks = new ArrayList<Block>();
    public static final ArrayList<BlockPos> placedPos = new ArrayList();

    public static List<EndCrystalEntity> getEndCrystals(Box box) {
        ArrayList<EndCrystalEntity> list = new ArrayList<EndCrystalEntity>();
        for (Entity entity : BlockUtil.mc.world.method_18112()) {
            EndCrystalEntity crystal;
            if (!(entity instanceof EndCrystalEntity) || !(crystal = (EndCrystalEntity)entity).method_5829().method_994(box)) continue;
            list.add(crystal);
        }
        return list;
    }

    public static List<Entity> getEntities(Box box) {
        ArrayList<Entity> list = new ArrayList<Entity>();
        for (Entity entity : BlockUtil.mc.world.method_18112()) {
            if (entity == null || !entity.method_5829().method_994(box)) continue;
            list.add(entity);
        }
        return list;
    }

    public static boolean isAir(BlockPos pos) {
        return BlockUtil.mc.world.isAir(pos);
    }

    public static boolean isMining(BlockPos pos) {
        return HexTech.BREAK.isMining(pos) || pos.equals((Object)SpeedMine.breakPos);
    }

    public static boolean canPlace(BlockPos pos) {
        return BlockUtil.canPlace(pos, 1000.0);
    }

    public static boolean canPlace(BlockPos pos, double distance) {
        if (BlockUtil.getPlaceSide(pos, distance) == null) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return !BlockUtil.hasEntity(pos, false);
    }

    public static boolean canPlace(BlockPos pos, double distance, boolean ignoreCrystal) {
        if (BlockUtil.getPlaceSide(pos, distance) == null) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return !BlockUtil.hasEntity(pos, ignoreCrystal);
    }

    public static boolean isSafe(Block block) {
        List<Block> safeBlocks = Arrays.asList(Blocks.field_10540, Blocks.field_9987, Blocks.field_10443, Blocks.field_10535);
        return !safeBlocks.contains(block);
    }

    public static boolean clientCanPlace(BlockPos pos) {
        return BlockUtil.clientCanPlace(pos, false);
    }

    public static boolean clientCanPlace(BlockPos pos, boolean ignoreCrystal) {
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return !BlockUtil.hasEntity(pos, ignoreCrystal);
    }

    public static boolean hasEntity(BlockPos pos, boolean ignoreCrystal) {
        for (Entity entity : BlockUtil.mc.world.method_18467(Entity.class, new Box(pos))) {
            if (!entity.method_5805() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || ignoreCrystal && entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    public static boolean hasCrystal(BlockPos pos) {
        for (Entity entity : BlockUtil.mc.world.method_18467(EndCrystalEntity.class, new Box(pos))) {
            if (!entity.method_5805() || !(entity instanceof EndCrystalEntity)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal) {
        for (Entity entity : BlockUtil.mc.world.method_18467(Entity.class, new Box(pos))) {
            if (!entity.method_5805() || ignoreCrystal && entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    public static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        for (Entity entity : BlockUtil.mc.world.method_18467(Entity.class, new Box(pos))) {
            if (!entity.method_5805() || ignoreItem && entity instanceof ItemEntity || ignoreCrystal && entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    public static Direction getBestNeighboring(BlockPos pos, Direction facing) {
        for (Direction i : Direction.values()) {
            if (facing != null && pos.offset(i).equals((Object)pos.method_10079(facing, -1)) || i == Direction.DOWN || BlockUtil.getPlaceSide(pos, false, true) == null) continue;
            return i;
        }
        Direction bestFacing = null;
        double distance = 0.0;
        for (Direction i : Direction.values()) {
            if (facing != null && pos.offset(i).equals((Object)pos.method_10079(facing, -1)) || i == Direction.DOWN || BlockUtil.getPlaceSide(pos) == null || bestFacing != null && !(BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos()) < distance)) continue;
            bestFacing = i;
            distance = BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos());
        }
        return bestFacing;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        BlockPos obsPos = pos.method_10074();
        BlockPos boost = obsPos.up();
        return !(BlockUtil.getBlock(obsPos) != Blocks.field_9987 && BlockUtil.getBlock(obsPos) != Blocks.field_10540 || BlockUtil.getClickSideStrict(obsPos) == null || BlockUtil.getBlock(boost) != Blocks.AIR || BlockUtil.hasEntityBlockCrystal(boost, false) || BlockUtil.hasEntityBlockCrystal(boost.up(), false) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() && BlockUtil.getBlock(boost.up()) != Blocks.AIR);
    }

    public static void placeCrystal(BlockPos pos, boolean rotate) {
        boolean offhand = BlockUtil.mc.player.method_6079().method_7909() == Items.field_8301;
        BlockPos obsPos = pos.method_10074();
        Direction facing = BlockUtil.getClickSide(obsPos);
        Vec3d vec = obsPos.toCenterPos().method_1031((double)facing.method_10163().method_10263() * 0.5, (double)facing.method_10163().method_10264() * 0.5, (double)facing.method_10163().method_10260() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(vec);
        }
        BlockUtil.clickBlock(obsPos, facing, false, offhand ? Hand.field_5810 : Hand.field_5808);
    }

    public static void placeBlock(BlockPos pos, boolean rotate) {
        BlockUtil.placeBlock(pos, rotate, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.packetPlace.getValue());
    }

    public static void placeBlock(BlockPos pos, boolean rotate, boolean packet) {
        if (BlockUtil.airPlace()) {
            placedPos.add(pos);
            BlockUtil.clickBlock(pos, Direction.UP, rotate, Hand.field_5808, packet);
            return;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return;
        }
        placedPos.add(pos);
        BlockUtil.clickBlock(pos.offset(side), side.method_10153(), rotate, Hand.field_5808, packet);
    }

    public static void placeBlock(BlockPos pos, boolean rotate, boolean packet, boolean bypass) {
        Direction side;
        if (BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (!BlockUtil.mc.world.isAir(pos.offset(i))) continue;
                BlockUtil.clickBlock(pos, i, rotate, Hand.field_5808, packet);
                return;
            }
        }
        if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(directionVec);
        }
        EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        placedPos.add(pos);
        boolean sprint = false;
        if (BlockUtil.mc.player != null) {
            sprint = BlockUtil.mc.player.method_5624();
        }
        boolean sneak = false;
        if (BlockUtil.mc.world != null) {
            boolean bl = sneak = BlockUtil.needSneak(BlockUtil.mc.world.getBlockState(result.method_17777()).getBlock()) && !BlockUtil.mc.player.method_5715();
        }
        if (sprint) {
            BlockUtil.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.field_12985));
        }
        if (sneak) {
            BlockUtil.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.field_12979));
        }
        BlockUtil.clickBlock(pos.offset(side), side.method_10153(), rotate, Hand.field_5808, packet);
        if (sneak) {
            BlockUtil.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.field_12984));
        }
        if (sprint) {
            BlockUtil.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.field_12981));
        }
        if (bypass) {
            EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        }
    }

    public static boolean isHole(BlockPos pos) {
        return BlockUtil.isHole(pos, true, false, false);
    }

    public static boolean isHole(BlockPos pos, boolean canStand, boolean checkTrap, boolean anyBlock) {
        int blockProgress = 0;
        for (Direction i : Direction.values()) {
            if (i == Direction.UP || i == Direction.DOWN || (!anyBlock || BlockUtil.mc.world.isAir(pos.offset(i))) && !CombatUtil.isHard(pos.offset(i))) continue;
            ++blockProgress;
        }
        return (!checkTrap || BlockUtil.getBlock(pos) == Blocks.AIR && BlockUtil.getBlock(pos.method_10069(0, 1, 0)) == Blocks.AIR && BlockUtil.getBlock(pos.method_10069(0, 2, 0)) == Blocks.AIR) && blockProgress > 3 && (!canStand || BlockUtil.getState(pos.method_10069(0, -1, 0)).method_51366());
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate) {
        BlockUtil.clickBlock(pos, side, rotate, Hand.field_5808);
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand) {
        BlockUtil.clickBlock(pos, side, rotate, hand, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.packetPlace.getValue());
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, boolean packet) {
        BlockUtil.clickBlock(pos, side, rotate, Hand.field_5808, packet);
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand, boolean packet) {
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(directionVec);
        }
        EntityUtil.swingHand(hand, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        if (packet) {
            BlockUtil.mc.player.field_3944.method_52787((Packet)new PlayerInteractBlockC2SPacket(Hand.field_5808, result, BlockUtil.getWorldActionId(BlockUtil.mc.world)));
        } else {
            BlockUtil.mc.field_1761.method_2896(BlockUtil.mc.player, hand, result);
        }
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand, SwingSide swingSide) {
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(directionVec);
        }
        EntityUtil.swingHand(hand, swingSide);
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        BlockUtil.mc.player.field_3944.method_52787((Packet)new PlayerInteractBlockC2SPacket(hand, result, BlockUtil.getWorldActionId(BlockUtil.mc.world)));
    }

    public static Direction getPlaceSide(BlockPos pos) {
        return BlockUtil.getPlaceSide(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Legit);
    }

    public static Direction getPlaceSide(BlockPos pos, boolean strict, boolean legit) {
        double dis = 114514.0;
        Direction side = null;
        for (Direction i : Direction.values()) {
            if (!BlockUtil.canClick(pos.offset(i)) || BlockUtil.canReplace(pos.offset(i)) || legit && !EntityUtil.canSee(pos.offset(i), i.method_10153()) || strict && !BlockUtil.isStrictDirection(pos.offset(i), i.method_10153())) continue;
            double vecDis = BlockUtil.mc.player.method_5707(pos.toCenterPos().method_1031((double)i.method_10163().method_10263() * 0.5, (double)i.method_10163().method_10264() * 0.5, (double)i.method_10163().method_10260() * 0.5));
            if (side != null && !(vecDis < dis)) continue;
            side = i;
            dis = vecDis;
        }
        if (side == null && BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (!BlockUtil.mc.world.isAir(pos.offset(i))) continue;
                return i;
            }
        }
        return side;
    }

    public static double distanceToXZ(double x, double z) {
        double dx = BlockUtil.mc.player.getX() - x;
        double dz = BlockUtil.mc.player.getZ() - z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static Direction getPlaceSide(BlockPos pos, double distance) {
        double dis = 114514.0;
        Direction side = null;
        for (Direction i : Direction.values()) {
            if (!BlockUtil.canClick(pos.offset(i)) || BlockUtil.canReplace(pos.offset(i)) || (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() != Placement.Legit ? CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && !BlockUtil.isStrictDirection(pos.offset(i), i.method_10153()) : !EntityUtil.canSee(pos.offset(i), i.method_10153()))) continue;
            double vecDis = BlockUtil.mc.player.method_5707(pos.toCenterPos().method_1031((double)i.method_10163().method_10263() * 0.5, (double)i.method_10163().method_10264() * 0.5, (double)i.method_10163().method_10260() * 0.5));
            if ((double)MathHelper.method_15355((float)((float)vecDis)) > distance || side != null && !(vecDis < dis)) continue;
            side = i;
            dis = vecDis;
        }
        if (side == null && BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (!BlockUtil.mc.world.isAir(pos.offset(i))) continue;
                return i;
            }
        }
        return side;
    }

    public static Direction getClickSide(BlockPos pos) {
        if (pos.equals((Object)EntityUtil.getPlayerPos())) {
            return Direction.UP;
        }
        Direction side = null;
        double range = 100.0;
        for (Direction i : Direction.values()) {
            if (!EntityUtil.canSee(pos, i) || (double)MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos())));
        }
        if (side != null) {
            return side;
        }
        side = Direction.UP;
        for (Direction i : Direction.values()) {
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && (!BlockUtil.isStrictDirection(pos, i) || !BlockUtil.isAir(pos.offset(i))) || (double)MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos())));
        }
        return side;
    }

    public static Direction getClickSideStrict(BlockPos pos) {
        Direction side = null;
        double range = 100.0;
        for (Direction i : Direction.values()) {
            if (!EntityUtil.canSee(pos, i) || (double)MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos())));
        }
        if (side != null) {
            return side;
        }
        side = null;
        for (Direction i : Direction.values()) {
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && (!BlockUtil.isStrictDirection(pos, i) || !BlockUtil.isAir(pos.offset(i))) || (double)MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.method_15355((float)((float)BlockUtil.mc.player.method_5707(pos.offset(i).toCenterPos())));
        }
        return side;
    }

    public static boolean isStrictDirection(BlockPos pos, Direction side) {
        BlockState blockState = BlockUtil.mc.world.getBlockState(pos);
        boolean isFullBox = blockState.method_26215() || blockState.method_26234((BlockView)BlockUtil.mc.world, pos) || BlockUtil.getBlock(pos) == Blocks.field_10343;
        return BlockUtil.isStrictDirection(pos, side, isFullBox);
    }

    public static boolean isStrictDirection(BlockPos pos, Direction side, boolean isFullBox) {
        if (EntityUtil.getPlayerPos().method_10264() - pos.method_10264() >= 0 && side == Direction.DOWN) {
            return false;
        }
        if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue() ? side == Direction.UP && (double)(pos.method_10264() + 1) > BlockUtil.mc.player.getEyePos().method_10214() : side == Direction.UP && (double)pos.method_10264() > BlockUtil.mc.player.getEyePos().method_10214()) {
            return false;
        }
        if (BlockUtil.getBlock(pos.offset(side)) == Blocks.field_10540 || BlockUtil.getBlock(pos.offset(side)) == Blocks.field_9987 || BlockUtil.getBlock(pos.offset(side)) == Blocks.field_23152) {
            return false;
        }
        Vec3d eyePos = EntityUtil.getEyesPos();
        Vec3d blockCenter = pos.toCenterPos();
        ArrayList<Direction> validAxis = new ArrayList<Direction>();
        validAxis.addAll(BlockUtil.checkAxis(eyePos.field_1352 - blockCenter.field_1352, Direction.field_11039, Direction.field_11034, !isFullBox));
        validAxis.addAll(BlockUtil.checkAxis(eyePos.field_1351 - blockCenter.field_1351, Direction.DOWN, Direction.UP, true));
        validAxis.addAll(BlockUtil.checkAxis(eyePos.field_1350 - blockCenter.field_1350, Direction.field_11043, Direction.field_11035, !isFullBox));
        return validAxis.contains(side);
    }

    public static ArrayList<Direction> checkAxis(double diff, Direction negativeSide, Direction positiveSide, boolean bothIfInRange) {
        ArrayList<Direction> valid = new ArrayList<Direction>();
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
        PendingUpdateManager pum = BlockUtil.getUpdateManager(world);
        int p = pum.method_41942();
        pum.close();
        return p;
    }

    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((IClientWorld)world).acquirePendingUpdateManager();
    }

    public static ArrayList<BlockEntity> getTileEntities() {
        return BlockUtil.getLoadedChunks().flatMap(chunk -> chunk.method_12214().values().stream()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Stream<WorldChunk> getLoadedChunks() {
        int radius = Math.max(2, BlockUtil.mc.field_1690.method_38521()) + 3;
        int diameter = radius * 2 + 1;
        ChunkPos center = BlockUtil.mc.player.method_31476();
        ChunkPos min = new ChunkPos(center.field_9181 - radius, center.field_9180 - radius);
        ChunkPos max = new ChunkPos(center.field_9181 + radius, center.field_9180 + radius);
        return Stream.iterate(min, pos -> {
            int x = pos.field_9181;
            int z = pos.field_9180;
            if (++x > max.field_9181) {
                x = min.field_9181;
                ++z;
            }
            return new ChunkPos(x, z);
        }).limit((long)diameter * (long)diameter).filter(c -> BlockUtil.mc.world.method_8393(c.field_9181, c.field_9180)).map(c -> BlockUtil.mc.world.method_8497(c.field_9181, c.field_9180)).filter(Objects::nonNull);
    }

    public static ArrayList<BlockPos> getSphere(float range) {
        return BlockUtil.getSphere(range, BlockUtil.mc.player.getEyePos());
    }

    public static ArrayList<BlockPos> getSphere(float range, Vec3d pos) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        for (double x = pos.method_10216() - (double)range; x < pos.method_10216() + (double)range; x += 1.0) {
            for (double y = pos.method_10214() - (double)range; y < pos.method_10214() + (double)range; y += 1.0) {
                for (double z = pos.method_10215() - (double)range; z < pos.method_10215() + (double)range; z += 1.0) {
                    BlockPosX curPos = new BlockPosX(x, y, z);
                    if (list.contains((Object)curPos)) continue;
                    list.add(curPos);
                }
            }
        }
        return list;
    }

    public static BlockState getState(BlockPos pos) {
        return BlockUtil.mc.world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return BlockUtil.getState(pos).getBlock();
    }

    public static boolean canReplace(BlockPos pos) {
        if (pos.method_10264() >= 320) {
            return false;
        }
        if (BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.field_10343 && WebAuraTick_gaIdrzDzsbegzNTtPQoV.ignore && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.replace.getValue()) {
            return true;
        }
        return BlockUtil.getState(pos).method_45474();
    }

    public static boolean canClick(BlockPos pos) {
        return BlockUtil.mc.world.getBlockState(pos).method_51367() && (!shiftBlocks.contains(BlockUtil.getBlock(pos)) && !(BlockUtil.getBlock(pos) instanceof BedBlock) || BlockUtil.mc.player.method_5715());
    }

    public static boolean airPlace() {
        return CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.AirPlace;
    }

    public static boolean canBlockFacing(BlockPos pos) {
        boolean airCheck = false;
        for (Direction side : Direction.values()) {
            if (!BlockUtil.canClick(pos.offset(side))) continue;
            airCheck = true;
        }
        return airCheck;
    }

    public static boolean needSneak(Block in) {
        return shiftBlocks.contains(in);
    }
}
