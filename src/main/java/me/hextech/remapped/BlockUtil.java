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
        for (Entity entity : BlockUtil.mc.world.getEntities()) {
            EndCrystalEntity crystal;
            if (!(entity instanceof EndCrystalEntity) || !(crystal = (EndCrystalEntity)entity).getBoundingBox().intersects(box)) continue;
            list.add(crystal);
        }
        return list;
    }

    public static List<Entity> getEntities(Box box) {
        ArrayList<Entity> list = new ArrayList<Entity>();
        for (Entity entity : BlockUtil.mc.world.getEntities()) {
            if (entity == null || !entity.getBoundingBox().intersects(box)) continue;
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
        List<Block> safeBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);
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
        for (Entity entity : BlockUtil.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (!entity.isAlive() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || ignoreCrystal && entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    public static boolean hasCrystal(BlockPos pos) {
        for (Entity entity : BlockUtil.mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box(pos))) {
            if (!entity.isAlive() || !(entity instanceof EndCrystalEntity)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal) {
        for (Entity entity : BlockUtil.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (!entity.isAlive() || ignoreCrystal && entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    public static boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        for (Entity entity : BlockUtil.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (!entity.isAlive() || ignoreItem && entity instanceof ItemEntity || ignoreCrystal && entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    public static Direction getBestNeighboring(BlockPos pos, Direction facing) {
        for (Direction i : Direction.values()) {
            if (facing != null && pos.offset(i).equals((Object)pos.offset(facing, -1)) || i == Direction.DOWN || BlockUtil.getPlaceSide(pos, false, true) == null) continue;
            return i;
        }
        Direction bestFacing = null;
        double distance = 0.0;
        for (Direction i : Direction.values()) {
            if (facing != null && pos.offset(i).equals((Object)pos.offset(facing, -1)) || i == Direction.DOWN || BlockUtil.getPlaceSide(pos) == null || bestFacing != null && !(BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()) < distance)) continue;
            bestFacing = i;
            distance = BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos());
        }
        return bestFacing;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        return !(BlockUtil.getBlock(obsPos) != Blocks.BEDROCK && BlockUtil.getBlock(obsPos) != Blocks.OBSIDIAN || BlockUtil.getClickSideStrict(obsPos) == null || BlockUtil.getBlock(boost) != Blocks.AIR || BlockUtil.hasEntityBlockCrystal(boost, false) || BlockUtil.hasEntityBlockCrystal(boost.up(), false) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() && BlockUtil.getBlock(boost.up()) != Blocks.AIR);
    }

    public static void placeCrystal(BlockPos pos, boolean rotate) {
        boolean offhand = BlockUtil.mc.player.method_6079().getItem() == Items.END_CRYSTAL;
        BlockPos obsPos = pos.down();
        Direction facing = BlockUtil.getClickSide(obsPos);
        Vec3d vec = obsPos.toCenterPos().add((double)facing.method_10163().getX() * 0.5, (double)facing.method_10163().getY() * 0.5, (double)facing.method_10163().getZ() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(vec);
        }
        BlockUtil.clickBlock(obsPos, facing, false, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND);
    }

    public static void placeBlock(BlockPos pos, boolean rotate) {
        BlockUtil.placeBlock(pos, rotate, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.packetPlace.getValue());
    }

    public static void placeBlock(BlockPos pos, boolean rotate, boolean packet) {
        if (BlockUtil.airPlace()) {
            placedPos.add(pos);
            BlockUtil.clickBlock(pos, Direction.UP, rotate, Hand.MAIN_HAND, packet);
            return;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return;
        }
        placedPos.add(pos);
        BlockUtil.clickBlock(pos.offset(side), side.getOpposite(), rotate, Hand.MAIN_HAND, packet);
    }

    public static void placeBlock(BlockPos pos, boolean rotate, boolean packet, boolean bypass) {
        Direction side;
        if (BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (!BlockUtil.mc.world.isAir(pos.offset(i))) continue;
                BlockUtil.clickBlock(pos, i, rotate, Hand.MAIN_HAND, packet);
                return;
            }
        }
        if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().getX() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().getY() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().getZ() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(directionVec);
        }
        EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        placedPos.add(pos);
        boolean sprint = false;
        if (BlockUtil.mc.player != null) {
            sprint = BlockUtil.mc.player.method_5624();
        }
        boolean sneak = false;
        if (BlockUtil.mc.world != null) {
            boolean bl = sneak = BlockUtil.needSneak(BlockUtil.mc.world.getBlockState(result.getBlockPos()).getBlock()) && !BlockUtil.mc.player.method_5715();
        }
        if (sprint) {
            BlockUtil.mc.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
        if (sneak) {
            BlockUtil.mc.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        }
        BlockUtil.clickBlock(pos.offset(side), side.getOpposite(), rotate, Hand.MAIN_HAND, packet);
        if (sneak) {
            BlockUtil.mc.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        }
        if (sprint) {
            BlockUtil.mc.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)BlockUtil.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
        if (bypass) {
            EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
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
        return (!checkTrap || BlockUtil.getBlock(pos) == Blocks.AIR && BlockUtil.getBlock(pos.add(0, 1, 0)) == Blocks.AIR && BlockUtil.getBlock(pos.add(0, 2, 0)) == Blocks.AIR) && blockProgress > 3 && (!canStand || BlockUtil.getState(pos.add(0, -1, 0)).method_51366());
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate) {
        BlockUtil.clickBlock(pos, side, rotate, Hand.MAIN_HAND);
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand) {
        BlockUtil.clickBlock(pos, side, rotate, hand, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.packetPlace.getValue());
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, boolean packet) {
        BlockUtil.clickBlock(pos, side, rotate, Hand.MAIN_HAND, packet);
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand, boolean packet) {
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().getX() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().getY() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().getZ() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(directionVec);
        }
        EntityUtil.swingHand(hand, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        if (packet) {
            BlockUtil.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, BlockUtil.getWorldActionId(BlockUtil.mc.world)));
        } else {
            BlockUtil.mc.interactionManager.interactBlock(BlockUtil.mc.player, hand, result);
        }
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, Hand hand, SwingSide swingSide) {
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().getX() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().getY() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().getZ() * 0.5);
        if (rotate) {
            EntityUtil.faceVector(directionVec);
        }
        EntityUtil.swingHand(hand, swingSide);
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        BlockUtil.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractBlockC2SPacket(hand, result, BlockUtil.getWorldActionId(BlockUtil.mc.world)));
    }

    public static Direction getPlaceSide(BlockPos pos) {
        return BlockUtil.getPlaceSide(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Legit);
    }

    public static Direction getPlaceSide(BlockPos pos, boolean strict, boolean legit) {
        double dis = 114514.0;
        Direction side = null;
        for (Direction i : Direction.values()) {
            if (!BlockUtil.canClick(pos.offset(i)) || BlockUtil.canReplace(pos.offset(i)) || legit && !EntityUtil.canSee(pos.offset(i), i.getOpposite()) || strict && !BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite())) continue;
            double vecDis = BlockUtil.mc.player.squaredDistanceTo(pos.toCenterPos().add((double)i.method_10163().getX() * 0.5, (double)i.method_10163().getY() * 0.5, (double)i.method_10163().getZ() * 0.5));
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
            if (!BlockUtil.canClick(pos.offset(i)) || BlockUtil.canReplace(pos.offset(i)) || (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() != Placement.Legit ? CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && !BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite()) : !EntityUtil.canSee(pos.offset(i), i.getOpposite()))) continue;
            double vecDis = BlockUtil.mc.player.squaredDistanceTo(pos.toCenterPos().add((double)i.method_10163().getX() * 0.5, (double)i.method_10163().getY() * 0.5, (double)i.method_10163().getZ() * 0.5));
            if ((double)MathHelper.sqrt((float)((float)vecDis)) > distance || side != null && !(vecDis < dis)) continue;
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
            if (!EntityUtil.canSee(pos, i) || (double)MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())));
        }
        if (side != null) {
            return side;
        }
        side = Direction.UP;
        for (Direction i : Direction.values()) {
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && (!BlockUtil.isStrictDirection(pos, i) || !BlockUtil.isAir(pos.offset(i))) || (double)MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())));
        }
        return side;
    }

    public static Direction getClickSideStrict(BlockPos pos) {
        Direction side = null;
        double range = 100.0;
        for (Direction i : Direction.values()) {
            if (!EntityUtil.canSee(pos, i) || (double)MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())));
        }
        if (side != null) {
            return side;
        }
        side = null;
        for (Direction i : Direction.values()) {
            if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && (!BlockUtil.isStrictDirection(pos, i) || !BlockUtil.isAir(pos.offset(i))) || (double)MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()))) > range) continue;
            side = i;
            range = MathHelper.sqrt((float)((float)BlockUtil.mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())));
        }
        return side;
    }

    public static boolean isStrictDirection(BlockPos pos, Direction side) {
        BlockState blockState = BlockUtil.mc.world.getBlockState(pos);
        boolean isFullBox = blockState.method_26215() || blockState.method_26234((BlockView)BlockUtil.mc.world, pos) || BlockUtil.getBlock(pos) == Blocks.COBWEB;
        return BlockUtil.isStrictDirection(pos, side, isFullBox);
    }

    public static boolean isStrictDirection(BlockPos pos, Direction side, boolean isFullBox) {
        if (EntityUtil.getPlayerPos().method_10264() - pos.method_10264() >= 0 && side == Direction.DOWN) {
            return false;
        }
        if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue() ? side == Direction.UP && (double)(pos.method_10264() + 1) > BlockUtil.mc.player.method_33571().method_10214() : side == Direction.UP && (double)pos.method_10264() > BlockUtil.mc.player.method_33571().method_10214()) {
            return false;
        }
        if (BlockUtil.getBlock(pos.offset(side)) == Blocks.OBSIDIAN || BlockUtil.getBlock(pos.offset(side)) == Blocks.BEDROCK || BlockUtil.getBlock(pos.offset(side)) == Blocks.RESPAWN_ANCHOR) {
            return false;
        }
        Vec3d eyePos = EntityUtil.getEyesPos();
        Vec3d blockCenter = pos.toCenterPos();
        ArrayList<Direction> validAxis = new ArrayList<Direction>();
        validAxis.addAll(BlockUtil.checkAxis(eyePos.x - blockCenter.x, Direction.WEST, Direction.EAST, !isFullBox));
        validAxis.addAll(BlockUtil.checkAxis(eyePos.y - blockCenter.y, Direction.DOWN, Direction.UP, true));
        validAxis.addAll(BlockUtil.checkAxis(eyePos.z - blockCenter.z, Direction.NORTH, Direction.SOUTH, !isFullBox));
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
        int p = pum.getSequence();
        pum.close();
        return p;
    }

    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((IClientWorld)world).acquirePendingUpdateManager();
    }

    public static ArrayList<BlockEntity> getTileEntities() {
        return BlockUtil.getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Stream<WorldChunk> getLoadedChunks() {
        int radius = Math.max(2, BlockUtil.mc.options.getClampedViewDistance()) + 3;
        int diameter = radius * 2 + 1;
        ChunkPos center = BlockUtil.mc.player.method_31476();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);
        return Stream.iterate(min, pos -> {
            int x = pos.x;
            int z = pos.z;
            if (++x > max.x) {
                x = min.x;
                ++z;
            }
            return new ChunkPos(x, z);
        }).limit((long)diameter * (long)diameter).filter(c -> BlockUtil.mc.world.method_8393(c.x, c.z)).map(c -> BlockUtil.mc.world.method_8497(c.x, c.z)).filter(Objects::nonNull);
    }

    public static ArrayList<BlockPos> getSphere(float range) {
        return BlockUtil.getSphere(range, BlockUtil.mc.player.method_33571());
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
        if (BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.COBWEB && WebAuraTick_gaIdrzDzsbegzNTtPQoV.ignore && AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.replace.getValue()) {
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
