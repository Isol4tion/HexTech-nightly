package me.hextech.remapped.mod.modules.impl.combat;

import java.util.*;

import me.hextech.HexTech;
import me.hextech.remapped.*;
import me.hextech.remapped.api.utils.world.BlockPosX;
import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.impl.player.Blink;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoMine_TjXbWuTzfnbezzlShKiP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoMine_TjXbWuTzfnbezzlShKiP INSTANCE;
    public final SliderSetting burrowlistY = this.add(new SliderSetting("Ylist", 0.5, 0.0, 3.0, 0.1f));
    public final EnumSetting<BurrowMode> mineMode = this.add(new EnumSetting<BurrowMode>("BurrowMode", BurrowMode.FastSlot));
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 6.0, 0.0, 8.0, 0.1));
    public final SliderSetting range = this.add(new SliderSetting("MineRange", 6.0, 0.0, 8.0, 0.1));
    private final BooleanSetting burrowylist = this.add(new BooleanSetting("BurrowYList", true));
    private final BooleanSetting burrow = this.add(new BooleanSetting("BurrowMine", true));
    private final BooleanSetting face = this.add(new BooleanSetting("FaceMine", true));
    private final BooleanSetting down = this.add(new BooleanSetting("DownMine", false));
    private final BooleanSetting surround = this.add(new BooleanSetting("SurroundMine", true));
    private final BooleanSetting eatpause = this.add(new BooleanSetting("UsingPause", false));
    private final BooleanSetting noblink = this.add(new BooleanSetting("CancelBlink", false));
    private final BooleanSetting lowVersion = this.add(new BooleanSetting("1.12", false));

    public AutoMine_TjXbWuTzfnbezzlShKiP() {
        super("AutoMine", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player != null && this.eatpause.getValue() && AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.isUsingItem()) {
            return;
        }
        if (this.noblink.getValue() && Blink.INSTANCE.isOn()) {
            return;
        }
        PlayerEntity player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
        if (player == null) {
            return;
        }
        if (SpeedMine.secondPos != null && !SpeedMine.secondPos.equals(SpeedMine.breakPos)) {
            return;
        }
        this.doBreak(player);
    }

    /*
     * WARNING - void declaration
     */
    private void doBreak(final PlayerEntity player) {
        final BlockPos pos = EntityUtil.getEntityPos(player, true);
        double[] yOffset = { -0.8, 0.5, 1.1 };
        double[] xzOffset = { 0.3, -0.3 };
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
        final List<Float> yList = new ArrayList<Float>();
        if (this.down.getValue()) {
            yList.add(-0.8f);
        }
        if (this.burrowylist.getValue()) {
            yList.add(this.burrowlistY.getValueFloat());
        }
        if (this.face.getValue()) {
            yList.add(1.1f);
        }
        for (final double y2 : yList) {
            for (final double offset : xzOffset) {
                final BlockPos offsetPos2 = new BlockPosX(player.getX() + offset, player.getY() + y2, player.getZ() + offset);
                if (this.canBreak(offsetPos2)) {
                    SpeedMine.INSTANCE.mine(offsetPos2);
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
                        return;
                    }
                }
            }
        }
        if (this.surround.getValue()) {
            if (!this.lowVersion.getValue()) {
                for (final Direction i : Direction.values()) {
                    if (i != Direction.UP) {
                        if (i != Direction.DOWN) {
                            if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos().squaredDistanceTo(pos.offset(i).toCenterPos())) <= this.range.getValue()) {
                                if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.world != null && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.isAir(pos.offset(i)) || pos.offset(i).equals(SpeedMine.getBreakPos())) && this.canPlaceCrystal(pos.offset(i), false)) {
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
                            if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), true)) {
                                    list.add(pos.offset(j));
                                }
                            }
                        }
                    }
                }
                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos()))).get());
                }
                else {
                    for (final Direction j : Direction.values()) {
                        if (j != Direction.UP) {
                            if (j != Direction.DOWN) {
                                if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                    if (this.canBreak(pos.offset(j)) && this.canPlaceCrystal(pos.offset(j), false)) {
                                        list.add(pos.offset(j));
                                    }
                                }
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.getSquaredDistance(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos()))).get());
                    }
                }
            }
            else {
                for (final Direction i : Direction.values()) {
                    if (i != Direction.UP) {
                        if (i != Direction.DOWN) {
                            if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos().distanceTo(pos.offset(i).toCenterPos()) <= this.range.getValue()) {
                                if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.world != null && AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.isAir(pos.offset(i)) && AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.isAir(pos.offset(i).up()) && this.canPlaceCrystal(pos.offset(i), false)) {
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
                            if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos().squaredDistanceTo(pos.offset(j).toCenterPos())) <= this.range.getValue()) {
                                if (this.canCrystal(pos.offset(j))) {
                                    list.add(pos.offset(j));
                                }
                            }
                        }
                    }
                }
                int max = 0;
                BlockPos minePos = null;
                for (final BlockPos cPos : list) {
                    if (this.getAir(cPos) >= max) {
                        max = this.getAir(cPos);
                        minePos = cPos;
                    }
                }
                if (minePos != null) {
                    this.doMine(minePos);
                }
            }
        }
        if (this.burrow.getValue()) {
            if (this.mineMode.is(BurrowMode.FastSlot)) {
                yOffset = new double[] { -0.8, 0.5, 1.1 };
                xzOffset = new double[] { 0.25, -0.25, 0.0 };
                for (final PlayerEntity entity : CombatUtil.getEnemies(this.targetRange.getValue())) {
                    for (final double y : yOffset) {
                        for (final double x : xzOffset) {
                            for (final double z : xzOffset) {
                                final BlockPos offsetPos = new BlockPosX(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                                if (this.isObsidian(offsetPos) && offsetPos.equals(SpeedMine.breakPos)) {
                                    return;
                                }
                            }
                        }
                    }
                }
                final double[] array10;
                yOffset = (array10 = new double[] { 0.5, 1.1 });
                for (final double y3 : array10) {
                    for (final double offset3 : xzOffset) {
                        final BlockPos offsetPos4 = new BlockPosX(player.getX() + offset3, player.getY() + y3, player.getZ() + offset3);
                        if (this.isObsidian(offsetPos4)) {
                            for (final MineManager breakData : new HashMap<>(HexTech.BREAK.breakMap).values()) {
                                if (breakData != null) {
                                    if (breakData.getEntity() == null) {
                                        continue;
                                    }
                                    if (breakData.pos.equals(offsetPos4) && breakData.getEntity() != AutoMine_TjXbWuTzfnbezzlShKiP.mc.player) {
                                        return;
                                    }
                                    continue;
                                }
                            }
                            SpeedMine.INSTANCE.mine(offsetPos4);
                            return;
                        }
                    }
                }
                for (final double y3 : yOffset) {
                    for (final double offset3 : xzOffset) {
                        for (final double offset4 : xzOffset) {
                            final BlockPos offsetPos5 = new BlockPosX(player.getX() + offset4, player.getY() + y3, player.getZ() + offset3);
                            if (this.isObsidian(offsetPos5)) {
                                for (final MineManager breakData2 : new HashMap<>(HexTech.BREAK.breakMap).values()) {
                                    if (breakData2 != null) {
                                        if (breakData2.getEntity() == null) {
                                            continue;
                                        }
                                        if (breakData2.pos.equals(offsetPos5) && breakData2.getEntity() != AutoMine_TjXbWuTzfnbezzlShKiP.mc.player) {
                                            return;
                                        }
                                        continue;
                                    }
                                }
                                SpeedMine.INSTANCE.mine(offsetPos5);
                                return;
                            }
                        }
                    }
                }
            }
            if (this.mineMode.is(BurrowMode.SyncSlot)) {
                xzOffset = new double[] { 0.0, 0.3, -0.3 };
                final double[] array15;
                yOffset = (array15 = new double[] { 0.5, 1.1 });
                for (final double y3 : array15) {
                    for (final double offset3 : xzOffset) {
                        final BlockPos offsetPos4 = new BlockPosX(player.getX() + offset3, player.getY() + y3, player.getZ() + offset3);
                        if (this.isObsidian(offsetPos4)) {
                            SpeedMine.INSTANCE.mine(offsetPos4);
                            return;
                        }
                    }
                }
                for (final double y3 : yOffset) {
                    for (final double offset3 : xzOffset) {
                        for (final double offset4 : xzOffset) {
                            final BlockPos offsetPos5 = new BlockPosX(player.getX() + offset4, player.getY() + y3, player.getZ() + offset3);
                            if (this.isObsidian(offsetPos5)) {
                                SpeedMine.INSTANCE.mine(offsetPos5);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void doMine(BlockPos pos) {
        if (this.canBreak(pos)) {
            SpeedMine.INSTANCE.mine(pos);
        } else if (this.canBreak(pos.up())) {
            SpeedMine.INSTANCE.mine(pos.up());
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
        if (pos == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.world == null) {
            return false;
        }
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        return !(BlockUtil.getBlock(obsPos) != Blocks.BEDROCK && BlockUtil.getBlock(obsPos) != Blocks.OBSIDIAN && block || BlockUtil.hasEntityBlockCrystal(boost, true, true) || BlockUtil.hasEntityBlockCrystal(boost.up(), true, true) || this.lowVersion.getValue() && !AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.isAir(boost.up()));
    }

    private boolean isObsidian(BlockPos pos) {
        if (pos == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.world == null) {
            return false;
        }
        return AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getEyePos().distanceTo(pos.toCenterPos()) <= this.range.getValue() && (BlockUtil.getBlock(pos) == Blocks.OBSIDIAN || BlockUtil.getBlock(pos) == Blocks.ENDER_CHEST || BlockUtil.getBlock(pos) == Blocks.NETHERITE_BLOCK || BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) && BlockUtil.getClickSideStrict(pos) != null;
    }

    private boolean canBreak(BlockPos pos) {
        if (pos == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null) {
            return false;
        }
        return this.isObsidian(pos) && (BlockUtil.getClickSideStrict(pos) != null || Objects.equals(SpeedMine.getBreakPos(), pos)) && (!pos.equals(SpeedMine.secondPos) || !(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.getMainHandStack().getItem() instanceof PickaxeItem) && !SilentDouble.INSTANCE.isOn());
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum BurrowMode {
        SyncSlot,
        FastSlot

    }
}
