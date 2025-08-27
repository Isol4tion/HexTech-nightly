package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.remapped.AutoMine;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.MineManager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SilentDouble;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;

public class AutoMine_TjXbWuTzfnbezzlShKiP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoMine_TjXbWuTzfnbezzlShKiP INSTANCE;
    public final SliderSetting burrowlistY = this.add(new SliderSetting("Ylist", 0.5, 0.0, 3.0, 0.1f));
    public final EnumSetting<AutoMine> mineMode = this.add(new EnumSetting<AutoMine>("BurrowMode", AutoMine.FastSlot));
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
        if (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player != null && this.eatpause.getValue() && AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_6115()) {
            return;
        }
        if (this.noblink.getValue() && Blink.INSTANCE.isOn()) {
            return;
        }
        PlayerEntity player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
        if (player == null) {
            return;
        }
        if (SpeedMine.secondPos != null && !SpeedMine.secondPos.equals((Object)SpeedMine.breakPos)) {
            return;
        }
        this.doBreak(player);
    }

    /*
     * WARNING - void declaration
     */
    private void doBreak(PlayerEntity player) {
        double y;
        BlockPosX offsetPos;
        BlockPos pos = EntityUtil.getEntityPos((Entity)player, true);
        double[] dArray = new double[]{-0.8, 0.5, 1.1};
        double[] xzOffset6222 = new double[]{0.3, -0.3};
        for (PlayerEntity playerEntity : CombatUtil.getEnemies(this.targetRange.getValue())) {
            for (double y2 : dArray) {
                for (double x : xzOffset6222) {
                    for (double z : xzOffset6222) {
                        offsetPos = new BlockPosX(playerEntity.method_23317() + x, playerEntity.method_23318() + y2, playerEntity.method_23321() + z);
                        if (!this.canBreak(offsetPos) || !offsetPos.equals(SpeedMine.getBreakPos())) continue;
                        return;
                    }
                }
            }
        }
        ArrayList<Float> arrayList = new ArrayList<Float>();
        if (this.down.getValue()) {
            arrayList.add(Float.valueOf(-0.8f));
        }
        if (this.burrowylist.getValue()) {
            arrayList.add(Float.valueOf(this.burrowlistY.getValueFloat()));
        }
        if (this.face.getValue()) {
            arrayList.add(Float.valueOf(1.1f));
        }
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            y = ((Float)iterator.next()).floatValue();
            for (double offset : xzOffset6222) {
                BlockPosX offsetPos2 = new BlockPosX(player.method_23317() + offset, player.method_23318() + y, player.method_23321() + offset);
                if (!this.canBreak(offsetPos2)) continue;
                SpeedMine.INSTANCE.mine(offsetPos2);
                return;
            }
        }
        Iterator iterator2 = arrayList.iterator();
        while (iterator2.hasNext()) {
            y = ((Float)iterator2.next()).floatValue();
            for (double offset : xzOffset6222) {
                for (double offset2 : xzOffset6222) {
                    BlockPosX offsetPos3 = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y, player.method_23321() + offset);
                    if (!this.canBreak(offsetPos3)) continue;
                    SpeedMine.INSTANCE.mine(offsetPos3);
                    return;
                }
            }
        }
        if (this.surround.getValue()) {
            if (!this.lowVersion.getValue()) {
                void var6_44;
                void var5_24;
                Direction[] directionArray = Direction.values();
                int xzOffset6222 = directionArray.length;
                boolean bl = false;
                while (var5_24 < xzOffset6222) {
                    Direction direction = directionArray[var5_24];
                    if (direction != Direction.UP && direction != Direction.DOWN && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || !(Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571().squaredDistanceTo(pos.offset(direction).toCenterPos())) > this.range.getValue())) && AutoMine_TjXbWuTzfnbezzlShKiP.mc.world != null && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.method_22347(pos.offset(direction)) || pos.offset(direction).equals((Object)SpeedMine.getBreakPos())) && this.canPlaceCrystal(pos.offset(direction), false)) {
                        return;
                    }
                    ++var5_24;
                }
                ArrayList<BlockPos> arrayList2 = new ArrayList<BlockPos>();
                Direction[] xzOffset6222 = Direction.values();
                int n = xzOffset6222.length;
                boolean bl2 = false;
                while (var6_44 < n) {
                    i = xzOffset6222[var6_44];
                    if (i != Direction.UP && i != Direction.DOWN && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || !(Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue())) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), true)) {
                        arrayList2.add(pos.offset(i));
                    }
                    ++var6_44;
                }
                if (!arrayList2.isEmpty()) {
                    SpeedMine.INSTANCE.mine(arrayList2.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571()))).get());
                } else {
                    void var6_46;
                    xzOffset6222 = Direction.values();
                    int n2 = xzOffset6222.length;
                    boolean bl3 = false;
                    while (var6_46 < n2) {
                        i = xzOffset6222[var6_46];
                        if (i != Direction.UP && i != Direction.DOWN && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || !(Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue())) && this.canBreak(pos.offset(i)) && this.canPlaceCrystal(pos.offset(i), false)) {
                            arrayList2.add(pos.offset(i));
                        }
                        ++var6_46;
                    }
                    if (!arrayList2.isEmpty()) {
                        SpeedMine.INSTANCE.mine(arrayList2.stream().min(Comparator.comparingDouble(E -> E.method_19770((Position)AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571()))).get());
                    }
                }
            } else {
                void var5_31;
                void var6_50;
                void var5_28;
                Direction[] directionArray = Direction.values();
                int xzOffset6222 = directionArray.length;
                boolean bl = false;
                while (var5_28 < xzOffset6222) {
                    Direction direction = directionArray[var5_28];
                    if (direction != Direction.UP && direction != Direction.DOWN && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || !(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571().distanceTo(pos.offset(direction).toCenterPos()) > this.range.getValue())) && AutoMine_TjXbWuTzfnbezzlShKiP.mc.world != null && AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.method_22347(pos.offset(direction)) && AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.method_22347(pos.offset(direction).up()) && this.canPlaceCrystal(pos.offset(direction), false)) {
                        return;
                    }
                    ++var5_28;
                }
                ArrayList<BlockPos> arrayList3 = new ArrayList<BlockPos>();
                Direction[] xzOffset6222 = Direction.values();
                int n = xzOffset6222.length;
                boolean bl4 = false;
                while (var6_50 < n) {
                    i = xzOffset6222[var6_50];
                    if (i != Direction.UP && i != Direction.DOWN && (AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || !(Math.sqrt(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571().squaredDistanceTo(pos.offset(i).toCenterPos())) > this.range.getValue())) && this.canCrystal(pos.offset(i))) {
                        arrayList3.add(pos.offset(i));
                    }
                    ++var6_50;
                }
                int max = 0;
                Object var5_30 = null;
                for (BlockPos cPos : arrayList3) {
                    if (this.getAir(cPos) < max) continue;
                    max = this.getAir(cPos);
                    BlockPos blockPos = cPos;
                }
                if (var5_31 != null) {
                    this.doMine((BlockPos)var5_31);
                }
            }
        }
        if (this.burrow.getValue()) {
            if (this.mineMode.is(AutoMine.FastSlot)) {
                double[] dArray2;
                double[] dArray3 = new double[]{-0.8, 0.5, 1.1};
                double[] xzOffset3 = new double[]{0.25, -0.25, 0.0};
                for (PlayerEntity playerEntity : CombatUtil.getEnemies(this.targetRange.getValue())) {
                    for (double y2 : dArray3) {
                        for (double x : xzOffset3) {
                            for (double z : xzOffset3) {
                                offsetPos = new BlockPosX(playerEntity.method_23317() + x, playerEntity.method_23318() + y2, playerEntity.method_23321() + z);
                                if (!this.isObsidian(offsetPos) || !offsetPos.equals(SpeedMine.breakPos)) continue;
                                return;
                            }
                        }
                    }
                }
                double[] dArray4 = dArray2 = new double[]{0.5, 1.1};
                int n = dArray4.length;
                for (int i = 0; i < n; ++i) {
                    double y3 = dArray4[i];
                    for (double offset : xzOffset3) {
                        BlockPosX offsetPos5 = new BlockPosX(player.method_23317() + offset, player.method_23318() + y3, player.method_23321() + offset);
                        if (!this.isObsidian(offsetPos5)) continue;
                        for (MineManager breakData : new HashMap<Integer, MineManager>(HexTech.BREAK.breakMap).values()) {
                            if (breakData == null || breakData.getEntity() == null || !breakData.pos.equals((Object)offsetPos5) || breakData.getEntity() == AutoMine_TjXbWuTzfnbezzlShKiP.mc.player) continue;
                            return;
                        }
                        SpeedMine.INSTANCE.mine(offsetPos5);
                        return;
                    }
                }
                for (double y3 : dArray2) {
                    for (double offset : xzOffset3) {
                        for (double offset2 : xzOffset3) {
                            BlockPosX offsetPos2 = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y3, player.method_23321() + offset);
                            if (!this.isObsidian(offsetPos2)) continue;
                            for (MineManager breakData : new HashMap<Integer, MineManager>(HexTech.BREAK.breakMap).values()) {
                                if (breakData == null || breakData.getEntity() == null || !breakData.pos.equals((Object)offsetPos2) || breakData.getEntity() == AutoMine_TjXbWuTzfnbezzlShKiP.mc.player) continue;
                                return;
                            }
                            SpeedMine.INSTANCE.mine(offsetPos2);
                            return;
                        }
                    }
                }
            }
            if (this.mineMode.is(AutoMine.SyncSlot)) {
                double[] dArray5;
                double[] xzOffset4 = new double[]{0.0, 0.3, -0.3};
                double[] dArray6 = dArray5 = new double[]{0.5, 1.1};
                int n = dArray6.length;
                for (int i = 0; i < n; ++i) {
                    double y3 = dArray6[i];
                    for (double offset : xzOffset4) {
                        BlockPosX offsetPos6 = new BlockPosX(player.method_23317() + offset, player.method_23318() + y3, player.method_23321() + offset);
                        if (!this.isObsidian(offsetPos6)) continue;
                        SpeedMine.INSTANCE.mine(offsetPos6);
                        return;
                    }
                }
                for (double y3 : dArray5) {
                    for (double offset : xzOffset4) {
                        for (double offset2 : xzOffset4) {
                            BlockPosX offsetPos4 = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y3, player.method_23321() + offset);
                            if (!this.isObsidian(offsetPos4)) continue;
                            SpeedMine.INSTANCE.mine(offsetPos4);
                            return;
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
        return !(BlockUtil.getBlock(obsPos) != Blocks.BEDROCK && BlockUtil.getBlock(obsPos) != Blocks.OBSIDIAN && block || BlockUtil.hasEntityBlockCrystal(boost, true, true) || BlockUtil.hasEntityBlockCrystal(boost.up(), true, true) || this.lowVersion.getValue() && !AutoMine_TjXbWuTzfnbezzlShKiP.mc.world.method_22347(boost.up()));
    }

    private boolean isObsidian(BlockPos pos) {
        if (pos == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.world == null) {
            return false;
        }
        return AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_33571().distanceTo(pos.toCenterPos()) <= this.range.getValue() && (BlockUtil.getBlock(pos) == Blocks.OBSIDIAN || BlockUtil.getBlock(pos) == Blocks.ENDER_CHEST || BlockUtil.getBlock(pos) == Blocks.NETHERITE_BLOCK || BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) && BlockUtil.getClickSideStrict(pos) != null;
    }

    private boolean canBreak(BlockPos pos) {
        if (pos == null || AutoMine_TjXbWuTzfnbezzlShKiP.mc.player == null) {
            return false;
        }
        return this.isObsidian(pos) && (BlockUtil.getClickSideStrict(pos) != null || Objects.equals(SpeedMine.getBreakPos(), pos)) && (!pos.equals((Object)SpeedMine.secondPos) || !(AutoMine_TjXbWuTzfnbezzlShKiP.mc.player.method_6047().getItem() instanceof PickaxeItem) && !SilentDouble.INSTANCE.isOn());
    }
}
