package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SelfFlatten extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static SelfFlatten INSTANCE;
   private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final SliderSetting blocksPer = this.add(new SliderSetting("MaxBlock", 1, 1, 4));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 1000));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false));
   private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
   private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true));
   private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0));
   private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100));
   private final Timer timer = new Timer();
   public Vec3d directionVec = null;
   int progress = 0;

   public SelfFlatten() {
      super("SelfFlatten", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @EventHandler
   public void onRotate(OffTrackEvent event) {
      if (this.directionVec != null && this.rotate.getValue() && this.yawStep.getValue()) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }
   }

   @Override
   public void onUpdate() {
      this.progress = 0;
      if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
         if (!this.selfGround.getValue() || mc.field_1724.method_24828()) {
            if (mc.field_1724.method_24828()) {
               if (this.timer.passedMs((long)this.delay.getValueInt())) {
                  int oldSlot = mc.field_1724.method_31548().field_7545;
                  int block;
                  if ((block = this.getBlock()) != -1) {
                     if (EntityUtil.isInsideBlock()) {
                        BlockPos pos1 = new BlockPosX(
                              mc.field_1724.method_23317() + 0.6, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + 0.6
                           )
                           .method_10074();
                        BlockPos pos2 = new BlockPosX(
                              mc.field_1724.method_23317() - 0.6, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + 0.6
                           )
                           .method_10074();
                        BlockPos pos3 = new BlockPosX(
                              mc.field_1724.method_23317() + 0.6, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - 0.6
                           )
                           .method_10074();
                        BlockPos pos4 = new BlockPosX(
                              mc.field_1724.method_23317() - 0.6, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - 0.6
                           )
                           .method_10074();
                        if (this.canPlace(pos1) || this.canPlace(pos2) || this.canPlace(pos3) || this.canPlace(pos4)) {
                           this.doSwap(block);
                           this.tryPlaceObsidian(pos1, this.rotate.getValue());
                           this.tryPlaceObsidian(pos2, this.rotate.getValue());
                           this.tryPlaceObsidian(pos3, this.rotate.getValue());
                           this.tryPlaceObsidian(pos4, this.rotate.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldSlot);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean tryPlaceObsidian(BlockPos pos, boolean rotate) {
      if (this.canPlace(pos)) {
         if (this.checkMine.getValue() && BlockUtil.isMining(pos)) {
            return false;
         } else if (!((double)this.progress < this.blocksPer.getValue())) {
            return false;
         } else {
            Direction side;
            if ((side = BlockUtil.getPlaceSide(pos)) == null) {
               if (BlockUtil.airPlace()) {
                  BlockUtil.placedPos.add(pos);
                  BlockUtil.clickBlock(pos, Direction.field_11036, rotate);
                  this.timer.reset();
                  this.progress++;
                  return true;
               } else {
                  return false;
               }
            } else {
               this.progress++;
               BlockUtil.placedPos.add(pos);
               BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), rotate);
               this.timer.reset();
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private boolean faceVector(Vec3d directionVec) {
      if (!this.yawStep.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         return me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private boolean canPlace(BlockPos pos) {
      if (BlockUtil.getPlaceSide(pos) == null) {
         return false;
      } else {
         return !BlockUtil.canReplace(pos) ? false : !this.hasEntity(pos);
      }
   }

   private boolean hasEntity(BlockPos pos) {
      for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
         if (entity != mc.field_1724
            && entity.method_5805()
            && !(entity instanceof ItemEntity)
            && !(entity instanceof ExperienceOrbEntity)
            && !(entity instanceof ExperienceBottleEntity)
            && !(entity instanceof ArrowEntity)
            && !(entity instanceof EndCrystalEntity)
            && (!(entity instanceof ArmorStandEntity) || !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue())) {
            return true;
         }
      }

      return false;
   }

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10540) : InventoryUtil.findBlock(Blocks.field_10540);
   }
}
