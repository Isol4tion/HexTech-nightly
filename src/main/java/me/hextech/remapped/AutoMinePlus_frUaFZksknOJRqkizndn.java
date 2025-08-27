package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoMinePlus_frUaFZksknOJRqkizndn extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoMinePlus_frUaFZksknOJRqkizndn INSTANCE;
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
    private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
    private final BooleanSetting face = this.add(new BooleanSetting("Face", true));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false).setParent());
    public final BooleanSetting smart = this.add(new BooleanSetting("Smart", false, v -> this.down.isOpen()));
    public final SliderSetting yandy = this.add(new SliderSetting("Y", 2, 1, 3, v -> this.down.isOpen() && this.smart.getValue()).setSuffix("m"));
    private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true).setParent());
    private final EnumSetting<AutoMinePlus> mineMode = this.add(new EnumSetting("MineMode", AutoMinePlus.Normal, v -> this.surround.isOpen()));
    private final BooleanSetting second = this.add(new BooleanSetting("DoubleMine", false).setParent());
    private final EnumSetting<AutoMinePlus_iBVeFXhOwamRbRZuxxcN> coverMode = this.add(
            new EnumSetting("CoverMode", AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Normal, v -> this.second.isOpen())
    );
    private final BooleanSetting checkBedrock = this.add(new BooleanSetting("CheckBedrock", true));
    boolean mineBur = false;
    boolean mineBlocker = false;

    public AutoMinePlus_frUaFZksknOJRqkizndn() {
        super("AutoMine+", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        PlayerEntity player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
        if (player != null) {
            if (this.coverMode.getValue() != AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Sync
                    || SpeedMine.secondPos == null
                    || SpeedMine.secondPos.equals(SpeedMine.breakPos)) {
                if (!Blink.INSTANCE.isOn()) {
                    BlockPos pos = EntityUtil.getEntityPos(player);
                    if (mc.world.getBlockState(pos).getBlock() != Blocks.field_9987 || !this.checkBedrock.getValue()) {
                        this.doBreak(player);
                    }
                }
            }
        }
    }

    private void doBreak(PlayerEntity player) {
        this.mineBur = false;
        this.mineBlocker = false;
        BlockPos pos = EntityUtil.getEntityPos(player, true);
        double[] yOffset = new double[]{-0.8, 0.5, 1.1};
        double[] xzOffset = new double[]{0.3, -0.3};

        for (PlayerEntity entity : CombatUtil.getEnemies(this.targetRange.getValue())) {
            for (double y : yOffset) {
                for (double x : xzOffset) {
                    for (double z : xzOffset) {
                        BlockPos offsetPos = new BlockPosX(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                        if (this.canBreak(offsetPos) && offsetPos.equals(SpeedMine.getBreakPos())) {
                            return;
                        }
                    }
                }
            }
        }

        List<Float> yList = new ArrayList();
        if (this.down.getValue()) {
            if (this.smart.getValue() && (double)(player.method_24515().method_10264() - mc.player.method_24515().method_10264()) > this.yandy.getValue()) {
                yList.add(-0.8F);
            } else if (!this.smart.getValue()) {
                yList.add(-0.8F);
            }
        }

        if (this.burrow.getValue()) {
            yList.add(0.15F);
        }

        if (this.face.getValue()) {
            yList.add(1.1F);
        }

        Iterator var55 = yList.iterator();

        while (var55.hasNext()) {
            double y = (double)((Float)var55.next()).floatValue();

            for (double offset : xzOffset) {
                BlockPos offsetPos = new BlockPosX(player.getX() + offset, player.getY() + y, player.getZ() + offset);
                if (this.canBreak(offsetPos)) {
                    SpeedMine.INSTANCE.mine(offsetPos);
                    this.mineBur = true;
                    return;
                }
            }
        }

        var55 = yList.iterator();

        while (var55.hasNext()) {
            double y = (double)((Float)var55.next()).floatValue();

            for (double offsetx : xzOffset) {
                for (double offset2 : xzOffset) {
                    BlockPos offsetPos = new BlockPosX(player.getX() + offset2, player.getY() + y, player.getZ() + offsetx);
                    if (this.canBreak(offsetPos)) {
                        SpeedMine.INSTANCE.mine(offsetPos);
                        this.mineBur = true;
                        return;
                    }
                }
            }
        }

        if (this.surround.getValue()) {
            if (this.mineMode.getValue() == AutoMinePlus.Normal) {
                for (Direction i : Direction.values()) {
                    if (i != Direction.UP
                            && i != Direction.DOWN
                            && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue())
                            && (mc.world.isAir(pos.offset(i)) || pos.offset(i).equals(SpeedMine.getBreakPos()))
                            && this.canPlaceCrystal(pos.offset(i), false)) {
                        return;
                    }
                }

                ArrayList<BlockPos> list = new ArrayList();

                for (Direction ix : Direction.values()) {
                    if (ix != Direction.UP
                            && ix != Direction.DOWN
                            && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ix).toCenterPos())) > this.range.getValue())
                            && this.canBreak(pos.offset(ix))
                            && this.canPlaceCrystal(pos.offset(ix), true)) {
                        list.add(pos.offset(ix));
                    }
                }

                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(mc.player.getEyePos()))).get());
                } else {
                    for (Direction ixx : Direction.values()) {
                        if (ixx != Direction.UP
                                && ixx != Direction.DOWN
                                && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ixx).toCenterPos())) > this.range.getValue())
                                && this.canBreak(pos.offset(ixx))
                                && this.canPlaceCrystal(pos.offset(ixx), false)) {
                            list.add(pos.offset(ixx));
                        }
                    }

                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(mc.player.getEyePos()))).get());
                    }
                }
            } else if (this.mineMode.getValue() == AutoMinePlus.Always) {
                ArrayList<BlockPos> list = new ArrayList();

                for (Direction ixxx : Direction.values()) {
                    if (ixxx != Direction.UP
                            && ixxx != Direction.DOWN
                            && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ixxx).toCenterPos())) > this.range.getValue())
                            && this.canBreak(pos.offset(ixxx))
                            && this.canPlaceCrystal(pos.offset(ixxx), true)) {
                        list.add(pos.offset(ixxx));
                    }
                }

                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(mc.player.getEyePos()))).get());
                } else {
                    for (Direction ixxxx : Direction.values()) {
                        if (ixxxx != Direction.UP
                                && ixxxx != Direction.DOWN
                                && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ixxxx).toCenterPos())) > this.range.getValue())
                                && this.canBreak(pos.offset(ixxxx))
                                && this.canPlaceCrystal(pos.offset(ixxxx), false)) {
                            list.add(pos.offset(ixxxx));
                        }
                    }

                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(mc.player.getEyePos()))).get());
                    }
                }
            } else if (this.mineMode.getValue() == AutoMinePlus.First) {
                for (Direction ixxxxx : Direction.values()) {
                    if (ixxxxx != Direction.UP
                            && ixxxxx != Direction.DOWN
                            && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ixxxxx).toCenterPos())) > this.range.getValue())
                            && pos.offset(ixxxxx).equals(SpeedMine.getBreakPos())
                            && SpeedMine.secondPos == null) {
                        return;
                    }
                }

                ArrayList<BlockPos> list = new ArrayList();

                for (Direction ixxxxxx : Direction.values()) {
                    if (ixxxxxx != Direction.UP
                            && ixxxxxx != Direction.DOWN
                            && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ixxxxxx).toCenterPos())) > this.range.getValue())
                            && this.canBreak(pos.offset(ixxxxxx))
                            && this.canPlaceCrystal(pos.offset(ixxxxxx), true)) {
                        list.add(pos.offset(ixxxxxx));
                    }
                }

                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(mc.player.getEyePos()))).get());
                } else {
                    for (Direction ixxxxxxx : Direction.values()) {
                        if (ixxxxxxx != Direction.UP
                                && ixxxxxxx != Direction.DOWN
                                && !(Math.sqrt(mc.player.getEyePos().squaredDistanceTo(pos.offset(ixxxxxxx).toCenterPos())) > this.range.getValue())
                                && this.canBreak(pos.offset(ixxxxxxx))
                                && this.canPlaceCrystal(pos.offset(ixxxxxxx), false)) {
                            list.add(pos.offset(ixxxxxxx));
                        }
                    }

                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(mc.player.getEyePos()))).get());
                    }
                }
            }
        }
    }

    private void doMine(BlockPos pos) {
        if (this.canBreak(pos)) {
            this.mine(pos);
        } else if (this.canBreak(pos.up())) {
            this.mine(pos.up());
        }
    }

    private boolean canCrystal(BlockPos pos) {
        return !SpeedMine.godBlocks.contains(BlockUtil.getBlock(pos))
                && !(BlockUtil.getBlock(pos) instanceof BedBlock)
                && !(BlockUtil.getBlock(pos) instanceof CobwebBlock)
                && this.canPlaceCrystal(pos, true)
                && BlockUtil.getClickSideStrict(pos) != null
                ? !SpeedMine.godBlocks.contains(BlockUtil.getBlock(pos.up()))
                && !(BlockUtil.getBlock(pos.up()) instanceof BedBlock)
                && !(BlockUtil.getBlock(pos.up()) instanceof CobwebBlock)
                && BlockUtil.getClickSideStrict(pos.up()) != null
                : false;
    }

    private int getAir(BlockPos pos) {
        int value = 0;
        if (!this.canBreak(pos)) {
            value++;
        }

        if (!this.canBreak(pos.up())) {
            value++;
        }

        return value;
    }

    public boolean canPlaceCrystal(BlockPos pos, boolean block) {
        BlockPos obsPos = pos.method_10074();
        BlockPos boost = obsPos.up();
        return (BlockUtil.getBlock(obsPos) == Blocks.field_9987 || BlockUtil.getBlock(obsPos) == Blocks.field_10540 || !block)
                && !BlockUtil.hasEntityBlockCrystal(boost, true, true)
                && !BlockUtil.hasEntityBlockCrystal(boost.up(), true, true);
    }

    public boolean isObsidian(BlockPos pos) {
        return mc.player.getEyePos().method_1022(pos.toCenterPos()) <= this.range.getValue()
                && (
                BlockUtil.getBlock(pos) == Blocks.field_10540
                        || BlockUtil.getBlock(pos) == Blocks.field_10443
                        || BlockUtil.getBlock(pos) == Blocks.field_22108
                        || BlockUtil.getBlock(pos) == Blocks.field_23152
        )
                && BlockUtil.getClickSide(pos) != null;
    }

    void mine(BlockPos pos) {
        if (SpeedMine.INSTANCE.isOn()) {
            SpeedMine.INSTANCE.mine(pos);
        }
    }

    private boolean canBreak(BlockPos pos) {
        return this.isObsidian(pos)
                && (BlockUtil.getClickSide(pos) != null || SpeedMine.getBreakPos().equals(pos))
                && (!pos.equals(SpeedMine.secondPos) || !this.second.getValue());
    }
}
