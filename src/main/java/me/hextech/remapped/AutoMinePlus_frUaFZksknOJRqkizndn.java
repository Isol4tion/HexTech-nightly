package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoMinePlus_frUaFZksknOJRqkizndn extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoMinePlus_frUaFZksknOJRqkizndn INSTANCE;
   public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
   public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
   private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
   private final BooleanSetting face = this.add(new BooleanSetting("Face", true));
   private final BooleanSetting down = this.add(new BooleanSetting("Down", false).setParent());
   public final BooleanSetting smart = this.add(new BooleanSetting("Smart", false, v -> this.down.isOpen()));
   public final SliderSetting yandy = this.add(new SliderSetting("Y", 2, 1, 3, v -> this.down.isOpen() && this.smart.getValue()).setSuffix("m"));
   private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true).setParent());
   private final EnumSetting<AutoMinePlus> mineMode = this.add(new EnumSetting("MineMode", AutoMinePlus.Normal, v -> this.surround.isOpen()));
   private final BooleanSetting second = this.add(new BooleanSetting("DoubleMine", false).setParent());
   private final EnumSetting<AutoMinePlus_iBVeFXhOwamRbRZuxxcN> coverMode = this.add(
      new EnumSetting("CoverMode", AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Normal, v -> this.second.isOpen())
   );
   private final BooleanSetting checkBedrock = this.add(new BooleanSetting("CheckBedrock", true));
   boolean mineBur = false;
   boolean mineBlocker = false;

   public AutoMinePlus_frUaFZksknOJRqkizndn() {
      super("AutoMine+", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      PlayerEntity player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
      if (player != null) {
         if (this.coverMode.getValue() != AutoMinePlus_iBVeFXhOwamRbRZuxxcN.Sync
            || SpeedMine.secondPos == null
            || SpeedMine.secondPos.equals(SpeedMine.breakPos)) {
            if (!Blink.INSTANCE.isOn()) {
               BlockPos pos = EntityUtil.getEntityPos(player);
               if (mc.field_1687.method_8320(pos).method_26204() != Blocks.field_9987 || !this.checkBedrock.getValue()) {
                  this.doBreak(player);
               }
            }
         }
      }
   }

   private void doBreak(PlayerEntity player) {
      this.mineBur = false;
      this.mineBlocker = false;
      BlockPos pos = EntityUtil.getEntityPos(player, true);
      double[] yOffset = new double[]{-0.8, 0.5, 1.1};
      double[] xzOffset = new double[]{0.3, -0.3};

      for (PlayerEntity entity : CombatUtil.getEnemies(this.targetRange.getValue())) {
         for (double y : yOffset) {
            for (double x : xzOffset) {
               for (double z : xzOffset) {
                  BlockPos offsetPos = new BlockPosX(entity.method_23317() + x, entity.method_23318() + y, entity.method_23321() + z);
                  if (this.canBreak(offsetPos) && offsetPos.equals(SpeedMine.getBreakPos())) {
                     return;
                  }
               }
            }
         }
      }

      List<Float> yList = new ArrayList();
      if (this.down.getValue()) {
         if (this.smart.getValue() && (double)(player.method_24515().method_10264() - mc.field_1724.method_24515().method_10264()) > this.yandy.getValue()) {
            yList.add(-0.8F);
         } else if (!this.smart.getValue()) {
            yList.add(-0.8F);
         }
      }

      if (this.burrow.getValue()) {
         yList.add(0.15F);
      }

      if (this.face.getValue()) {
         yList.add(1.1F);
      }

      Iterator var55 = yList.iterator();

      while (var55.hasNext()) {
         double y = (double)((Float)var55.next()).floatValue();

         for (double offset : xzOffset) {
            BlockPos offsetPos = new BlockPosX(player.method_23317() + offset, player.method_23318() + y, player.method_23321() + offset);
            if (this.canBreak(offsetPos)) {
               SpeedMine.INSTANCE.mine(offsetPos);
               this.mineBur = true;
               return;
            }
         }
      }

      var55 = yList.iterator();

      while (var55.hasNext()) {
         double y = (double)((Float)var55.next()).floatValue();

         for (double offsetx : xzOffset) {
            for (double offset2 : xzOffset) {
               BlockPos offsetPos = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y, player.method_23321() + offsetx);
               if (this.canBreak(offsetPos)) {
                  SpeedMine.INSTANCE.mine(offsetPos);
                  this.mineBur = true;
                  return;
               }
            }
         }
      }

      if (this.surround.getValue()) {
         if (this.mineMode.getValue() == AutoMinePlus.Normal) {
            for (Direction i : Direction.values()) {
               if (i != Direction.field_11036
                  && i != Direction.field_11033
                  && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558())) > this.range.getValue())
                  && (mc.field_1687.method_22347(pos.method_10093(i)) || pos.method_10093(i).equals(SpeedMine.getBreakPos()))
                  && this.canPlaceCrystal(pos.method_10093(i), false)) {
                  return;
               }
            }

            ArrayList<BlockPos> list = new ArrayList();

            for (Direction ix : Direction.values()) {
               if (ix != Direction.field_11036
                  && ix != Direction.field_11033
                  && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ix).method_46558())) > this.range.getValue())
                  && this.canBreak(pos.method_10093(ix))
                  && this.canPlaceCrystal(pos.method_10093(ix), true)) {
                  list.add(pos.method_10093(ix));
               }
            }

            if (!list.isEmpty()) {
               SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
            } else {
               for (Direction ixx : Direction.values()) {
                  if (ixx != Direction.field_11036
                     && ixx != Direction.field_11033
                     && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixx).method_46558())) > this.range.getValue())
                     && this.canBreak(pos.method_10093(ixx))
                     && this.canPlaceCrystal(pos.method_10093(ixx), false)) {
                     list.add(pos.method_10093(ixx));
                  }
               }

               if (!list.isEmpty()) {
                  SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
               }
            }
         } else if (this.mineMode.getValue() == AutoMinePlus.Always) {
            ArrayList<BlockPos> list = new ArrayList();

            for (Direction ixxx : Direction.values()) {
               if (ixxx != Direction.field_11036
                  && ixxx != Direction.field_11033
                  && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixxx).method_46558())) > this.range.getValue())
                  && this.canBreak(pos.method_10093(ixxx))
                  && this.canPlaceCrystal(pos.method_10093(ixxx), true)) {
                  list.add(pos.method_10093(ixxx));
               }
            }

            if (!list.isEmpty()) {
               SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
            } else {
               for (Direction ixxxx : Direction.values()) {
                  if (ixxxx != Direction.field_11036
                     && ixxxx != Direction.field_11033
                     && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixxxx).method_46558())) > this.range.getValue())
                     && this.canBreak(pos.method_10093(ixxxx))
                     && this.canPlaceCrystal(pos.method_10093(ixxxx), false)) {
                     list.add(pos.method_10093(ixxxx));
                  }
               }

               if (!list.isEmpty()) {
                  SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
               }
            }
         } else if (this.mineMode.getValue() == AutoMinePlus.First) {
            for (Direction ixxxxx : Direction.values()) {
               if (ixxxxx != Direction.field_11036
                  && ixxxxx != Direction.field_11033
                  && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixxxxx).method_46558())) > this.range.getValue())
                  && pos.method_10093(ixxxxx).equals(SpeedMine.getBreakPos())
                  && SpeedMine.secondPos == null) {
                  return;
               }
            }

            ArrayList<BlockPos> list = new ArrayList();

            for (Direction ixxxxxx : Direction.values()) {
               if (ixxxxxx != Direction.field_11036
                  && ixxxxxx != Direction.field_11033
                  && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixxxxxx).method_46558())) > this.range.getValue())
                  && this.canBreak(pos.method_10093(ixxxxxx))
                  && this.canPlaceCrystal(pos.method_10093(ixxxxxx), true)) {
                  list.add(pos.method_10093(ixxxxxx));
               }
            }

            if (!list.isEmpty()) {
               SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
            } else {
               for (Direction ixxxxxxx : Direction.values()) {
                  if (ixxxxxxx != Direction.field_11036
                     && ixxxxxxx != Direction.field_11033
                     && !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixxxxxxx).method_46558())) > this.range.getValue())
                     && this.canBreak(pos.method_10093(ixxxxxxx))
                     && this.canPlaceCrystal(pos.method_10093(ixxxxxxx), false)) {
                     list.add(pos.method_10093(ixxxxxxx));
                  }
               }

               if (!list.isEmpty()) {
                  SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
               }
            }
         }
      }
   }

   private void doMine(BlockPos pos) {
      if (this.canBreak(pos)) {
         this.mine(pos);
      } else if (this.canBreak(pos.method_10084())) {
         this.mine(pos.method_10084());
      }
   }

   private boolean canCrystal(BlockPos pos) {
      return !SpeedMine.godBlocks.contains(BlockUtil.getBlock(pos))
            && !(BlockUtil.getBlock(pos) instanceof BedBlock)
            && !(BlockUtil.getBlock(pos) instanceof CobwebBlock)
            && this.canPlaceCrystal(pos, true)
            && BlockUtil.getClickSideStrict(pos) != null
         ? !SpeedMine.godBlocks.contains(BlockUtil.getBlock(pos.method_10084()))
            && !(BlockUtil.getBlock(pos.method_10084()) instanceof BedBlock)
            && !(BlockUtil.getBlock(pos.method_10084()) instanceof CobwebBlock)
            && BlockUtil.getClickSideStrict(pos.method_10084()) != null
         : false;
   }

   private int getAir(BlockPos pos) {
      int value = 0;
      if (!this.canBreak(pos)) {
         value++;
      }

      if (!this.canBreak(pos.method_10084())) {
         value++;
      }

      return value;
   }

   public boolean canPlaceCrystal(BlockPos pos, boolean block) {
      BlockPos obsPos = pos.method_10074();
      BlockPos boost = obsPos.method_10084();
      return (BlockUtil.getBlock(obsPos) == Blocks.field_9987 || BlockUtil.getBlock(obsPos) == Blocks.field_10540 || !block)
         && !BlockUtil.hasEntityBlockCrystal(boost, true, true)
         && !BlockUtil.hasEntityBlockCrystal(boost.method_10084(), true, true);
   }

   public boolean isObsidian(BlockPos pos) {
      return mc.field_1724.method_33571().method_1022(pos.method_46558()) <= this.range.getValue()
         && (
            BlockUtil.getBlock(pos) == Blocks.field_10540
               || BlockUtil.getBlock(pos) == Blocks.field_10443
               || BlockUtil.getBlock(pos) == Blocks.field_22108
               || BlockUtil.getBlock(pos) == Blocks.field_23152
         )
         && BlockUtil.getClickSide(pos) != null;
   }

   void mine(BlockPos pos) {
      if (SpeedMine.INSTANCE.isOn()) {
         SpeedMine.INSTANCE.mine(pos);
      }
   }

   private boolean canBreak(BlockPos pos) {
      return this.isObsidian(pos)
         && (BlockUtil.getClickSide(pos) != null || SpeedMine.getBreakPos().equals(pos))
         && (!pos.equals(SpeedMine.secondPos) || !this.second.getValue());
   }
}
