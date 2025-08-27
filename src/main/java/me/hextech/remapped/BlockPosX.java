package me.hextech.remapped;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockPosX extends BlockPos {
   public BlockPosX(double x, double y, double z) {
      super(MathHelper.method_15357(x), MathHelper.method_15357(y), MathHelper.method_15357(z));
   }

   public BlockPosX(double x, double y, double z, boolean fix) {
      this(x, y + (double)(fix ? BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.blockposx.getValueFloat() : 0.0F), z);
   }

   public BlockPosX(Vec3d vec3d) {
      this(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350);
   }

   public BlockPosX(Vec3d vec3d, boolean fix) {
      this(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, fix);
   }
}
