package me.hextech.remapped;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ListenerHelper implements Wrapper {
   public static Block getBlock(BlockPos pos) {
      return BlockUtil.getState(pos).method_26204();
   }

   private static int getBlock() {
      return AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory
         ? InventoryUtil.findBlockInventorySlot(Blocks.field_10540)
         : InventoryUtil.findBlock(Blocks.field_10540);
   }

   public static void tryBase() {
      for (BlockPos pos : BlockUtil.getSphere((float)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue() + 1.0F)) {
         if (!ListenerHelperUtil.behindWall(pos)
            && ListenerHelperUtil.canBasePlaceCrystal(pos.method_10084(), false, false)
            && ListenerHelperUtil.canTouch(pos)
            && !(
               (double)ListenerHelperUtil.calculateObsidian(
                     pos,
                     pos.method_10084().method_46558(),
                     AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget,
                     AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget
                  )
                  < AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.minDamage.getValue()
            )) {
            doBase(pos);
         }
      }
   }

   public static void doBase(BlockPos pos) {
      if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.obsidian.getValue()) {
         if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.placeBaseTimer.passedMs((long)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.placeObsDelay.getValue())) {
            if (!(
               (double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.tempDamage
                  < ListenerDamage.getDamage(BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.displayTarget)
            )) {
               int block = getBlock();
               if (block != -1) {
                  Direction side = BlockUtil.getPlaceSide(pos);
                  if (side != null) {
                     Vec3d directionVec = new Vec3d(
                        (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
                        (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
                        (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
                     );
                     if (BlockUtil.canPlace(pos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())) {
                        if (!AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.rotate.getValue() || faceVector(directionVec)) {
                           int old = 0;
                           if (mc.field_1724 != null) {
                              old = mc.field_1724.method_31548().field_7545;
                           }

                           doSwap(block);
                           if (BlockUtil.airPlace()) {
                              BlockUtil.placedPos.add(pos);
                              BlockUtil.clickBlock(pos, Direction.field_11033, false, Hand.field_5808);
                           } else {
                              BlockUtil.placedPos.add(pos);
                              BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, Hand.field_5808);
                           }

                           if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
                              doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              doSwap(old);
                           }

                           BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.placeTimer.reset();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void doSwap(int slot) {
      if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
         if (mc.field_1724 != null) {
            InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
         }
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private static boolean faceVector(Vec3d directionVec) {
      if (!AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.faceVector.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         return me.hextech.HexTech.ROTATE.inFov(directionVec, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.fov.getValueFloat())
            ? true
            : !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.checkLook.getValue();
      }
   }
}
