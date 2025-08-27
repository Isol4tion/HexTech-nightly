package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SpeedMine;
import net.minecraft.util.math.BlockPos;

public class AntiCrawl
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AntiCrawl INSTANCE;
    public final BooleanSetting crawl = this.add(new BooleanSetting("Crawl", true));
    private final BooleanSetting pre = this.add(new BooleanSetting("Pre", true));
    private final BooleanSetting mine = this.add(new BooleanSetting("MineHead", true));
    public boolean work = false;
    double[] xzOffset = new double[]{0.0, 0.3, -0.3};

    public AntiCrawl() {
        super("AntiCrawl", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        this.work = false;
        if (AntiCrawl.mc.field_1724.method_20448() || this.pre.getValue() && HexTech.BREAK.isMining(AntiCrawl.mc.field_1724.method_24515())) {
            for (double offset : this.xzOffset) {
                for (double offset2 : this.xzOffset) {
                    BlockPosX pos = new BlockPosX(AntiCrawl.mc.field_1724.method_23317() + offset, AntiCrawl.mc.field_1724.method_23318() + 1.2, AntiCrawl.mc.field_1724.method_23321() + offset2);
                    if (!this.mine.getValue() || !this.canBreak(pos)) continue;
                    SpeedMine.INSTANCE.mine(pos);
                    this.work = true;
                    return;
                }
            }
        }
    }

    private boolean canBreak(BlockPos pos) {
        return (BlockUtil.getClickSideStrict(pos) != null || SpeedMine.breakPos.equals((Object)pos)) && !SpeedMine.godBlocks.contains(AntiCrawl.mc.field_1687.method_8320(pos).method_26204()) && !AntiCrawl.mc.field_1687.method_22347(pos);
    }
}
