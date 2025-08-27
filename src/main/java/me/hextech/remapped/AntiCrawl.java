package me.hextech.remapped;

import net.minecraft.util.math.BlockPos;

public class AntiCrawl extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
      if (mc.field_1724.method_20448() || this.pre.getValue() && me.hextech.HexTech.BREAK.isMining(mc.field_1724.method_24515())) {
         for (double offset : this.xzOffset) {
            for (double offset2 : this.xzOffset) {
               BlockPos pos = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + 1.2, mc.field_1724.method_23321() + offset2);
               if (this.mine.getValue() && this.canBreak(pos)) {
                  SpeedMine.INSTANCE.mine(pos);
                  this.work = true;
                  return;
               }
            }
         }
      }
   }

   private boolean canBreak(BlockPos pos) {
      return (BlockUtil.getClickSideStrict(pos) != null || SpeedMine.breakPos.equals(pos))
         && !SpeedMine.godBlocks.contains(mc.field_1687.method_8320(pos).method_26204())
         && !mc.field_1687.method_22347(pos);
   }
}
