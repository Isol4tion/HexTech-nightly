package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class BlockerESP extends Module_eSdgMXWuzcxgQVaJFmKZ {
   final List<BlockPos> renderList = new ArrayList();
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
   private final BooleanSetting box = this.add(new BooleanSetting("Box", true));
   private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true));
   private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
   private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true));

   public BlockerESP() {
      super("BlockerESP", Module_JlagirAibYQgkHtbRnhw.Render);
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      this.renderList.clear();
      float pOffset = (float)CombatSetting_kxXrLvbWbduSuFoeBUsC.getOffset();

      for (Entity player : CombatUtil.getEnemies(10.0)) {
         if (this.burrow.getValue()) {
            float[] offset = new float[]{-pOffset, 0.0F, pOffset};

            for (float x : offset) {
               for (float z : offset) {
                  BlockPos tempPos;
                  if (this.isObsidian(tempPos = new BlockPosX(player.method_19538().method_1031((double)x, 0.0, (double)z)))) {
                     this.renderList.add(tempPos);
                  }

                  BlockPosX var23;
                  if (this.isObsidian(var23 = new BlockPosX(player.method_19538().method_1031((double)x, 0.5, (double)z)))) {
                     this.renderList.add(var23);
                  }
               }
            }
         }

         if (this.surround.getValue()) {
            BlockPos pos = EntityUtil.getEntityPos(player, true);
            if (BlockUtil.isHole(pos)) {
               for (Direction i : Direction.values()) {
                  if (i != Direction.field_11036 && i != Direction.field_11033 && this.isObsidian(pos.method_10093(i))) {
                     this.renderList.add(pos.method_10093(i));
                  }
               }
            }
         }
      }

      for (BlockPos pos : this.renderList) {
         Render3DUtil.draw3DBox(matrixStack, new Box(pos), this.color.getValue(), this.outline.getValue(), this.box.getValue());
      }
   }

   private boolean isObsidian(BlockPos pos) {
      return (BlockUtil.getBlock(pos) == Blocks.field_10540 || BlockUtil.getBlock(pos) == Blocks.field_10443) && !this.renderList.contains(pos);
   }
}
