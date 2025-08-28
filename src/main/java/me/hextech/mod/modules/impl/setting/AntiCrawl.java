package me.hextech.mod.modules.impl.setting;

import me.hextech.HexTech;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.SpeedMine;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
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
        super("AntiCrawl", Category.Setting);
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
