package me.hextech.remapped;

import net.minecraft.block.Blocks;

public class AntiVoid extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final SliderSetting voidHeight = this.add(new SliderSetting("VoidHeight", -64.0, -64.0, 319.0, 1.0));
   private final SliderSetting height = this.add(new SliderSetting("Height", 100.0, -40.0, 256.0, 1.0));

   public AntiVoid() {
      super("AntiVoid", "Allows you to fly over void blocks", Module_JlagirAibYQgkHtbRnhw.Movement);
   }

   @Override
   public void onUpdate() {
      boolean isVoid = true;

      for (int i = (int)mc.field_1724.method_23318(); i > this.voidHeight.getValueInt() - 1; i--) {
         if (mc.field_1687.method_8320(new BlockPosX(mc.field_1724.method_23317(), (double)i, mc.field_1724.method_23321())).method_26204()
            != Blocks.field_10124) {
            isVoid = false;
            break;
         }
      }

      if (mc.field_1724.method_23318() < this.height.getValue() + this.voidHeight.getValue() && isVoid) {
         MovementUtil.setMotionY(0.0);
      }
   }
}
