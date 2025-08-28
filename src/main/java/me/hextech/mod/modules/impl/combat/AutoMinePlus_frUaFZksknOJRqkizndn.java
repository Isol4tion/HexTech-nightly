package me.hextech.mod.modules.impl.combat;

import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.player.Blink;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoMinePlus_frUaFZksknOJRqkizndn
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoMinePlus_frUaFZksknOJRqkizndn INSTANCE;
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
    private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
    private final BooleanSetting face = this.add(new BooleanSetting("Face", true));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false).setParent());
    public final BooleanSetting smart = this.add(new BooleanSetting("Smart", false, v -> this.down.isOpen()));
    public final SliderSetting yandy = this.add(new SliderSetting("Y", 2, 1, 3, v -> this.down.isOpen() && this.smart.getValue()).setSuffix("m"));
    private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true).setParent());
    private final EnumSetting<AutoMinePlus> mineMode = this.add(new EnumSetting<AutoMinePlus>("AutoMinePlus", AutoMinePlus.Normal, v -> this.surround.isOpen()));
    private final BooleanSetting second = this.add(new BooleanSetting("DoubleMine", false).setParent());
    private final EnumSetting<AutoMinePlus_iBVeFXhOwamRbRZuxxcN> coverMode = this.add(new EnumSetting<AutoMinePlus_iBVeFXhOwamRbRZuxxcN>("CoverMode", AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Normal, v -> this.second.isOpen()));
    private final BooleanSetting checkBedrock = this.add(new BooleanSetting("CheckBedrock", true));
    boolean mineBur = false;
    boolean mineBlocker = false;

    public AutoMinePlus_frUaFZksknOJRqkizndn() {
        super("AutoMine+", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        PlayerEntity player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
        if (player == null) {
            return;
        }
        if (this.coverMode.getValue() == AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Sync && SpeedMine.secondPos != null && !SpeedMine.secondPos.equals(SpeedMine.breakPos)) {
            return;
        }
        if (Blink.INSTANCE.isOn()) {
            return;
        }
        BlockPos pos = EntityUtil.getEntityPos(player);
        if (AutoMinePlus_frUaFZksknOJRqkizndn.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK && this.checkBedrock.getValue()) {
            return;
        }
        this.doBreak(player);
    }

    /*
     * WARNING - void declaration
     */
    private void doBreak(final PlayerEntity player) {
        this.mineBur = false;
        this.mineBlocker = false;
        final BlockPos pos = EntityUtil.getEntityPos(player, true);
        final double[] yOffset = { -0.8, 0.5, 1.1 };
        final double[] xzOffset = { 0.3, -0.3 };
        for (final PlayerEntity entity : CombatUtil.getEnemies(this.targetRange.getValue())) {
            for (final double y : yOffset) {
                for (final double x : xzOffset) {
                    for (final double z : xzOffset) {
                        final BlockPos offsetPos = new BlockPosX(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                        if (this.canBreak(offsetPos) && offsetPos.equals(SpeedMine.getBreakPos())) {
                            return;
                        }
                    }
                }
            }
        }
        final List<Float> yList = getFloats(player);
        for (final double y2 : yList) {
            for (final double offset : xzOffset) {
                final BlockPos offsetPos2 = new BlockPosX(player.getX() + offset, player.getY() + y2, player.getZ() + offset);
                if (this.canBreak(offsetPos2)) {
                    SpeedMine.INSTANCE.mine(offsetPos2);
                    this.mineBur = true;
                    return;
                }
            }
        }
        for (final double y2 : yList) {
            for (final double offset : xzOffset) {
                for (final double offset2 : xzOffset) {
                    final BlockPos offsetPos3 = new BlockPosX(player.getX() + offset2, player.getY() + y2, player.getZ() + offset);
                    if (this.canBreak(offsetPos3)) {
                        SpeedMine.INSTANCE.mine(offsetPos3);
                        this.mineBur = true;
                        return;
                    }
                }
            }
        }
        if (this.surround.getValue()) {
            if (this.mineMode.getValue() == AutoMinePlus.Normal) {
                for (final Direction i : Direction.values()) {
                    if (i != Direction.UP) {
                        if (i != Direction.DOWN) {
                            if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(i).toCenterPos())) <= this.range.getValue()) {
                                if ((AutoMinePlus_frUaFZksknOJRqkizndn.mc.world.isAir(pos.offset(i)) || pos.offset(i).equals(SpeedMine.getBreakPos())) && this.canPlaceCrystal(pos.offset(i), false)) {
                                    return;
                                }
                            }
                        }
                    }
                }
                final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
                for (final Direction j : Direction.values()) {
                    if (j != Direction.UP) {
                        if (j != Direction.DOWN) {
                            if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), true)) {
                                    list.add(pos.offset(j));
                                }
                            }
                        }
                    }
                }
                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos()))).get());
                }
                else {
                    for (final Direction j : Direction.values()) {
                        if (j != Direction.UP) {
                            if (j != Direction.DOWN) {
                                if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                    if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), false)) {
                                        list.add(pos.offset(j));
                                    }
                                }
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos()))).get());
                    }
                }
            }
            else if (this.mineMode.getValue() == AutoMinePlus.Always) {
                final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
                for (final Direction j : Direction.values()) {
                    if (j != Direction.UP) {
                        if (j != Direction.DOWN) {
                            if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), true)) {
                                    list.add(pos.offset(j));
                                }
                            }
                        }
                    }
                }
                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos()))).get());
                }
                else {
                    for (final Direction j : Direction.values()) {
                        if (j != Direction.UP) {
                            if (j != Direction.DOWN) {
                                if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                    if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), false)) {
                                        list.add(pos.offset(j));
                                    }
                                }
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos()))).get());
                    }
                }
            }
            else if (this.mineMode.getValue() == AutoMinePlus.First) {
                for (final Direction i : Direction.values()) {
                    if (i != Direction.UP) {
                        if (i != Direction.DOWN) {
                            if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(i).toCenterPos())) <= this.range.getValue()) {
                                if (pos.offset(i).equals(SpeedMine.getBreakPos()) && SpeedMine.secondPos == null) {
                                    return;
                                }
                            }
                        }
                    }
                }
                final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
                for (final Direction j : Direction.values()) {
                    if (j != Direction.UP) {
                        if (j != Direction.DOWN) {
                            if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), true)) {
                                    list.add(pos.offset(j));
                                }
                            }
                        }
                    }
                }
                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos()))).get());
                }
                else {
                    for (final Direction j : Direction.values()) {
                        if (j != Direction.UP) {
                            if (j != Direction.DOWN) {
                                if (Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                    if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), false)) {
                                        list.add(pos.offset(j));
                                    }
                                }
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos()))).get());
                    }
                }
            }
        }
    }

    private @NotNull List<Float> getFloats(PlayerEntity player) {
        final List<Float> yList = new ArrayList<>();
        if (this.down.getValue()) {
            if (this.smart.getValue() && player.getBlockPos().getY() - AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getBlockPos().getY() > this.yandy.getValue()) {
                yList.add(-0.8f);
            }
            else if (!this.smart.getValue()) {
                yList.add(-0.8f);
            }
        }
        if (this.burrow.getValue()) {
            yList.add(0.15f);
        }
        if (this.face.getValue()) {
            yList.add(1.1f);
        }
        return yList;
    }

    private void doMine(BlockPos pos) {
        if (this.canBreak(pos)) {
            this.mine(pos);
        } else if (this.canBreak(pos.up())) {
            this.mine(pos.up());
        }
    }

    private boolean canCrystal(BlockPos pos) {
        if (SpeedMine.godBlocks.contains(BlockUtil.getBlock(pos)) || BlockUtil.getBlock(pos) instanceof BedBlock || BlockUtil.getBlock(pos) instanceof CobwebBlock || !this.canPlaceCrystal(pos, true) || BlockUtil.getClickSideStrict(pos) == null) {
            return false;
        }
        return !SpeedMine.godBlocks.contains(BlockUtil.getBlock(pos.up())) && !(BlockUtil.getBlock(pos.up()) instanceof BedBlock) && !(BlockUtil.getBlock(pos.up()) instanceof CobwebBlock) && BlockUtil.getClickSideStrict(pos.up()) != null;
    }

    private int getAir(BlockPos pos) {
        int value = 0;
        if (!this.canBreak(pos)) {
            ++value;
        }
        if (!this.canBreak(pos.up())) {
            ++value;
        }
        return value;
    }

    public boolean canPlaceCrystal(BlockPos pos, boolean block) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        return (BlockUtil.getBlock(obsPos) == Blocks.BEDROCK || BlockUtil.getBlock(obsPos) == Blocks.OBSIDIAN || !block) && !BlockUtil.hasEntityBlockCrystal(boost, true, true) && !BlockUtil.hasEntityBlockCrystal(boost.up(), true, true);
    }

    public boolean isObsidian(BlockPos pos) {
        return AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.getEyePos().distanceTo(pos.toCenterPos()) <= this.range.getValue() && (BlockUtil.getBlock(pos) == Blocks.OBSIDIAN || BlockUtil.getBlock(pos) == Blocks.ENDER_CHEST || BlockUtil.getBlock(pos) == Blocks.NETHERITE_BLOCK || BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) && BlockUtil.getClickSide(pos) != null;
    }

    void mine(BlockPos pos) {
        if (SpeedMine.INSTANCE.isOn()) {
            SpeedMine.INSTANCE.mine(pos);
        }
    }

    private boolean canBreak(BlockPos pos) {
        return !(!this.isObsidian(pos) || BlockUtil.getClickSide(pos) == null && !SpeedMine.getBreakPos().equals(pos) || pos.equals(SpeedMine.secondPos) && this.second.getValue());
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum AutoMinePlus_iBVeFXhOwamRbRZuxxcN {
        Sync,
        Normal

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum AutoMinePlus {
        Normal,
        Always,
        First,
        Blocker

    }
}
