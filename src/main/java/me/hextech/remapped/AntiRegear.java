package me.hextech.remapped;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.MathHelper;

public class AntiRegear extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final SliderSetting safeRange = this.add(new SliderSetting("SafeRange", 2, 0, 8));
   private final SliderSetting range = this.add(new SliderSetting("Range", 5, 0, 8));

   public AntiRegear() {
      super("AntiRegear", "Shulker nuker", Module_JlagirAibYQgkHtbRnhw.Combat);
   }

   @Override
   public void onUpdate() {
      if (SpeedMine.breakPos == null || !(mc.field_1687.method_8320(SpeedMine.breakPos).method_26204() instanceof ShulkerBoxBlock)) {
         if (this.getBlock() != null) {
            SpeedMine.INSTANCE.mine(this.getBlock().method_11016());
         }
      }
   }

   private ShulkerBoxBlockEntity getBlock() {
      for (BlockEntity entity : BlockUtil.getTileEntities()) {
         if (entity instanceof ShulkerBoxBlockEntity shulker
            && !((double)MathHelper.method_15355((float)mc.field_1724.method_5707(shulker.method_11016().method_46558())) <= this.safeRange.getValue())
            && (double)MathHelper.method_15355((float)mc.field_1724.method_5707(shulker.method_11016().method_46558())) <= this.range.getValue()) {
            return shulker;
         }
      }

      return null;
   }
}
