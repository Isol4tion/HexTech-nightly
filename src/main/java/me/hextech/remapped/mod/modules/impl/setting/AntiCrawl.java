package me.hextech.remapped.mod.modules.impl.setting;

import me.hextech.HexTech;
import me.hextech.remapped.api.utils.world.BlockPosX;
import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
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
        if (AntiCrawl.mc.player.isCrawling() || this.pre.getValue() && HexTech.BREAK.isMining(AntiCrawl.mc.player.getBlockPos())) {
            for (double offset : this.xzOffset) {
                for (double offset2 : this.xzOffset) {
                    BlockPosX pos = new BlockPosX(AntiCrawl.mc.player.getX() + offset, AntiCrawl.mc.player.getY() + 1.2, AntiCrawl.mc.player.getZ() + offset2);
                    if (!this.mine.getValue() || !this.canBreak(pos)) continue;
                    SpeedMine.INSTANCE.mine(pos);
                    this.work = true;
                    return;
                }
            }
        }
    }

    private boolean canBreak(BlockPos pos) {
        return (BlockUtil.getClickSideStrict(pos) != null || SpeedMine.breakPos.equals(pos)) && !SpeedMine.godBlocks.contains(AntiCrawl.mc.world.getBlockState(pos).getBlock()) && !AntiCrawl.mc.world.isAir(pos);
    }
}
