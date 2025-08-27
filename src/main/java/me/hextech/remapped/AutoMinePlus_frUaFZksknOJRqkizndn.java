package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import me.hextech.remapped.AutoMinePlus;
import me.hextech.remapped.AutoMinePlus_iBVeFXhOwamRbRZuxxcN;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;

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
    private final EnumSetting<AutoMinePlus> mineMode = this.add(new EnumSetting<AutoMinePlus>("MineMode", AutoMinePlus.Normal, v -> this.surround.isOpen()));
    private final BooleanSetting second = this.add(new BooleanSetting("DoubleMine", false).setParent());
    private final EnumSetting<AutoMinePlus_iBVeFXhOwamRbRZuxxcN> coverMode = this.add(new EnumSetting<AutoMinePlus_iBVeFXhOwamRbRZuxxcN>("CoverMode", AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Normal, v -> this.second.isOpen()));
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
        if (player == null) {
            return;
        }
        if (this.coverMode.getValue() == AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Sync && SpeedMine.secondPos != null && !SpeedMine.secondPos.equals((Object)SpeedMine.breakPos)) {
            return;
        }
        if (Blink.INSTANCE.isOn()) {
            return;
        }
        BlockPos pos = EntityUtil.getEntityPos((Entity)player);
        if (AutoMinePlus_frUaFZksknOJRqkizndn.mc.world.method_8320(pos).method_26204() == Blocks.BEDROCK && this.checkBedrock.getValue()) {
            return;
        }
        this.doBreak(player);
    }

    /*
     * WARNING - void declaration
     */
    private void doBreak(PlayerEntity player) {
        double y;
        this.mineBur = false;
        this.mineBlocker = false;
        BlockPos pos = EntityUtil.getEntityPos((Entity)player, true);
        double[] dArray = new double[]{-0.8, 0.5, 1.1};
        double[] xzOffset = new double[]{0.3, -0.3};
        for (PlayerEntity playerEntity : CombatUtil.getEnemies(this.targetRange.getValue())) {
            for (double y2 : dArray) {
                for (double x : xzOffset) {
                    for (double z : xzOffset) {
                        BlockPosX offsetPos = new BlockPosX(playerEntity.method_23317() + x, playerEntity.method_23318() + y2, playerEntity.method_23321() + z);
                        if (!this.canBreak(offsetPos) || !offsetPos.equals(SpeedMine.getBreakPos())) continue;
                        return;
                    }
                }
            }
        }
        ArrayList<Float> yList = new ArrayList<Float>();
        if (this.down.getValue()) {
            if (this.smart.getValue() && (double)(player.method_24515().method_10264() - AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_24515().method_10264()) > this.yandy.getValue()) {
                yList.add(Float.valueOf(-0.8f));
            } else if (!this.smart.getValue()) {
                yList.add(Float.valueOf(-0.8f));
            }
        }
        if (this.burrow.getValue()) {
            yList.add(Float.valueOf(0.15f));
        }
        if (this.face.getValue()) {
            yList.add(Float.valueOf(1.1f));
        }
        Iterator iterator = yList.iterator();
        while (iterator.hasNext()) {
            y = ((Float)iterator.next()).floatValue();
            for (double offset : xzOffset) {
                BlockPosX offsetPos = new BlockPosX(player.method_23317() + offset, player.method_23318() + y, player.method_23321() + offset);
                if (!this.canBreak(offsetPos)) continue;
                SpeedMine.INSTANCE.mine(offsetPos);
                this.mineBur = true;
                return;
            }
        }
        Iterator iterator2 = yList.iterator();
        while (iterator2.hasNext()) {
            y = ((Float)iterator2.next()).floatValue();
            for (double offset : xzOffset) {
                for (double offset2 : xzOffset) {
                    BlockPosX offsetPos = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y, player.method_23321() + offset);
                    if (!this.canBreak(offsetPos)) continue;
                    SpeedMine.INSTANCE.mine(offsetPos);
                    this.mineBur = true;
                    return;
                }
            }
        }
        if (this.surround.getValue()) {
            if (this.mineMode.getValue() == AutoMinePlus.Normal) {
                void var6_24;
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.UP || direction == Direction.DOWN || Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(direction).toCenterPos())) > this.range.getValue() || !AutoMinePlus_frUaFZksknOJRqkizndn.mc.world.method_22347(pos.offset(direction)) && !pos.offset(direction).equals((Object)SpeedMine.getBreakPos()) || !this.canPlaceCrystal(pos.offset(direction), false)) continue;
                    return;
                }
                ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
                Direction[] directionArray = Direction.values();
                int n = directionArray.length;
                boolean bl = false;
                while (var6_24 < n) {
                    i = directionArray[var6_24];
                    if (i != Direction.UP && i != Direction.DOWN && !(Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue()) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), true)) {
                        arrayList.add(pos.offset(i));
                    }
                    ++var6_24;
                }
                if (!arrayList.isEmpty()) {
                    SpeedMine.INSTANCE.mine(arrayList.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571()))).get());
                } else {
                    void var6_26;
                    directionArray = Direction.values();
                    n = directionArray.length;
                    boolean bl2 = false;
                    while (var6_26 < n) {
                        i = directionArray[var6_26];
                        if (i != Direction.UP && i != Direction.DOWN && !(Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue()) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), false)) {
                            arrayList.add(pos.offset(i));
                        }
                        ++var6_26;
                    }
                    if (!arrayList.isEmpty()) {
                        SpeedMine.INSTANCE.mine(arrayList.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571()))).get());
                    }
                }
            } else if (this.mineMode.getValue() == AutoMinePlus.Always) {
                void var6_28;
                ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
                Direction[] directionArray = Direction.values();
                int n = directionArray.length;
                boolean bl = false;
                while (var6_28 < n) {
                    i = directionArray[var6_28];
                    if (i != Direction.UP && i != Direction.DOWN && !(Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue()) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), true)) {
                        arrayList.add(pos.offset(i));
                    }
                    ++var6_28;
                }
                if (!arrayList.isEmpty()) {
                    SpeedMine.INSTANCE.mine(arrayList.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571()))).get());
                } else {
                    void var6_30;
                    directionArray = Direction.values();
                    n = directionArray.length;
                    boolean bl3 = false;
                    while (var6_30 < n) {
                        i = directionArray[var6_30];
                        if (i != Direction.UP && i != Direction.DOWN && !(Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue()) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), false)) {
                            arrayList.add(pos.offset(i));
                        }
                        ++var6_30;
                    }
                    if (!arrayList.isEmpty()) {
                        SpeedMine.INSTANCE.mine(arrayList.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571()))).get());
                    }
                }
            } else if (this.mineMode.getValue() == AutoMinePlus.First) {
                void var6_34;
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.UP || direction == Direction.DOWN || Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(direction).toCenterPos())) > this.range.getValue() || !pos.offset(direction).equals((Object)SpeedMine.getBreakPos()) || SpeedMine.secondPos != null) continue;
                    return;
                }
                ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
                Direction[] directionArray = Direction.values();
                int n = directionArray.length;
                boolean bl = false;
                while (var6_34 < n) {
                    i = directionArray[var6_34];
                    if (i != Direction.UP && i != Direction.DOWN && !(Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue()) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), true)) {
                        arrayList.add(pos.offset(i));
                    }
                    ++var6_34;
                }
                if (!arrayList.isEmpty()) {
                    SpeedMine.INSTANCE.mine(arrayList.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571()))).get());
                } else {
                    void var6_36;
                    directionArray = Direction.values();
                    n = directionArray.length;
                    boolean bl4 = false;
                    while (var6_36 < n) {
                        i = directionArray[var6_36];
                        if (i != Direction.UP && i != Direction.DOWN && !(Math.sqrt(AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue()) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), false)) {
                            arrayList.add(pos.offset(i));
                        }
                        ++var6_36;
                    }
                    if (!arrayList.isEmpty()) {
                        SpeedMine.INSTANCE.mine(arrayList.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571()))).get());
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
        return AutoMinePlus_frUaFZksknOJRqkizndn.mc.player.method_33571().distanceTo(pos.toCenterPos()) <= this.range.getValue() && (BlockUtil.getBlock(pos) == Blocks.OBSIDIAN || BlockUtil.getBlock(pos) == Blocks.ENDER_CHEST || BlockUtil.getBlock(pos) == Blocks.NETHERITE_BLOCK || BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) && BlockUtil.getClickSide(pos) != null;
    }

    void mine(BlockPos pos) {
        if (SpeedMine.INSTANCE.isOn()) {
            SpeedMine.INSTANCE.mine(pos);
        }
    }

    private boolean canBreak(BlockPos pos) {
        return !(!this.isObsidian(pos) || BlockUtil.getClickSide(pos) == null && !SpeedMine.getBreakPos().equals((Object)pos) || pos.equals((Object)SpeedMine.secondPos) && this.second.getValue());
    }
}
