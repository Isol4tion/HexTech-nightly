package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoMine_TjXbWuTzfnbezzlShKiP extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoMine_TjXbWuTzfnbezzlShKiP INSTANCE;
   public final SliderSetting burrowlistY = this.add(new SliderSetting("Ylist", 0.5, 0.0, 3.0, 0.1F));
   public final EnumSetting<AutoMine> mineMode = this.add(new EnumSetting("BurrowMode", AutoMine.FastSlot));
   public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 6.0, 0.0, 8.0, 0.1));
   public final SliderSetting range = this.add(new SliderSetting("MineRange", 6.0, 0.0, 8.0, 0.1));
   private final BooleanSetting burrowylist = this.add(new BooleanSetting("BurrowYList", true));
   private final BooleanSetting burrow = this.add(new BooleanSetting("BurrowMine", true));
   private final BooleanSetting face = this.add(new BooleanSetting("FaceMine", true));
   private final BooleanSetting down = this.add(new BooleanSetting("DownMine", false));
   private final BooleanSetting surround = this.add(new BooleanSetting("SurroundMine", true));
   private final BooleanSetting eatpause = this.add(new BooleanSetting("UsingPause", false));
   private final BooleanSetting noblink = this.add(new BooleanSetting("CancelBlink", false));
   private final BooleanSetting lowVersion = this.add(new BooleanSetting("1.12", false));

   public AutoMine_TjXbWuTzfnbezzlShKiP() {
      super("AutoMine", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (mc.field_1724 == null || !this.eatpause.getValue() || !mc.field_1724.method_6115()) {
         if (!this.noblink.getValue() || !Blink.INSTANCE.isOn()) {
            PlayerEntity player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
            if (player != null) {
               if (SpeedMine.secondPos == null || SpeedMine.secondPos.equals(SpeedMine.breakPos)) {
                  this.doBreak(player);
               }
            }
         }
      }
   }

   private void doBreak(PlayerEntity player) {
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
         yList.add(-0.8F);
      }

      if (this.burrowylist.getValue()) {
         yList.add(this.burrowlistY.getValueFloat());
      }

      if (this.face.getValue()) {
         yList.add(1.1F);
      }

      Iterator var68 = yList.iterator();

      while (var68.hasNext()) {
         double y = (double)((Float)var68.next()).floatValue();

         for (double offset : xzOffset) {
            BlockPos offsetPos = new BlockPosX(player.method_23317() + offset, player.method_23318() + y, player.method_23321() + offset);
            if (this.canBreak(offsetPos)) {
               SpeedMine.INSTANCE.mine(offsetPos);
               return;
            }
         }
      }

      var68 = yList.iterator();

      while (var68.hasNext()) {
         double y = (double)((Float)var68.next()).floatValue();

         for (double offsetx : xzOffset) {
            for (double offset2 : xzOffset) {
               BlockPos offsetPos = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y, player.method_23321() + offsetx);
               if (this.canBreak(offsetPos)) {
                  SpeedMine.INSTANCE.mine(offsetPos);
                  return;
               }
            }
         }
      }

      if (this.surround.getValue()) {
         if (!this.lowVersion.getValue()) {
            for (Direction i : Direction.values()) {
               if (i != Direction.field_11036
                  && i != Direction.field_11033
                  && (
                     mc.field_1724 == null
                        || !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558())) > this.range.getValue())
                  )
                  && mc.field_1687 != null
                  && (mc.field_1687.method_22347(pos.method_10093(i)) || pos.method_10093(i).equals(SpeedMine.getBreakPos()))
                  && this.canPlaceCrystal(pos.method_10093(i), false)) {
                  return;
               }
            }

            ArrayList<BlockPos> list = new ArrayList();

            for (Direction ix : Direction.values()) {
               if (ix != Direction.field_11036
                  && ix != Direction.field_11033
                  && (
                     mc.field_1724 == null
                        || !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ix).method_46558())) > this.range.getValue())
                  )
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
                     && (
                        mc.field_1724 == null
                           || !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixx).method_46558())) > this.range.getValue())
                     )
                     && this.canBreak(pos.method_10093(ixx))
                     && this.canPlaceCrystal(pos.method_10093(ixx), false)) {
                     list.add(pos.method_10093(ixx));
                  }
               }

               if (!list.isEmpty()) {
                  SpeedMine.INSTANCE.mine((BlockPos)list.stream().min(Comparator.comparingDouble(E -> E.method_19770(mc.field_1724.method_33571()))).get());
               }
            }
         } else {
            for (Direction ixxx : Direction.values()) {
               if (ixxx != Direction.field_11036
                  && ixxx != Direction.field_11033
                  && (mc.field_1724 == null || !(mc.field_1724.method_33571().method_1022(pos.method_10093(ixxx).method_46558()) > this.range.getValue()))
                  && mc.field_1687 != null
                  && mc.field_1687.method_22347(pos.method_10093(ixxx))
                  && mc.field_1687.method_22347(pos.method_10093(ixxx).method_10084())
                  && this.canPlaceCrystal(pos.method_10093(ixxx), false)) {
                  return;
               }
            }

            ArrayList<BlockPos> list = new ArrayList();

            for (Direction ixxxx : Direction.values()) {
               if (ixxxx != Direction.field_11036
                  && ixxxx != Direction.field_11033
                  && (
                     mc.field_1724 == null
                        || !(Math.sqrt(mc.field_1724.method_33571().method_1025(pos.method_10093(ixxxx).method_46558())) > this.range.getValue())
                  )
                  && this.canCrystal(pos.method_10093(ixxxx))) {
                  list.add(pos.method_10093(ixxxx));
               }
            }

            int max = 0;
            BlockPos minePos = null;

            for (BlockPos cPos : list) {
               if (this.getAir(cPos) >= max) {
                  max = this.getAir(cPos);
                  minePos = cPos;
               }
            }

            if (minePos != null) {
               this.doMine(minePos);
            }
         }
      }

      if (this.burrow.getValue()) {
         if (this.mineMode.is(AutoMine.FastSlot)) {
            yOffset = new double[]{-0.8, 0.5, 1.1};
            xzOffset = new double[]{0.25, -0.25, 0.0};

            for (PlayerEntity entity : CombatUtil.getEnemies(this.targetRange.getValue())) {
               for (double y : yOffset) {
                  for (double x : xzOffset) {
                     for (double zx : xzOffset) {
                        BlockPos offsetPos = new BlockPosX(entity.method_23317() + x, entity.method_23318() + y, entity.method_23321() + zx);
                        if (this.isObsidian(offsetPos) && offsetPos.equals(SpeedMine.breakPos)) {
                           return;
                        }
                     }
                  }
               }
            }

            yOffset = new double[]{0.5, 1.1};

            for (double y : yOffset) {
               for (double offsetx : xzOffset) {
                  BlockPos offsetPos = new BlockPosX(player.method_23317() + offsetx, player.method_23318() + y, player.method_23321() + offsetx);
                  if (this.isObsidian(offsetPos)) {
                     for (MineManager breakData : new HashMap(me.hextech.HexTech.BREAK.breakMap).values()) {
                        if (breakData != null && breakData.getEntity() != null && breakData.pos.equals(offsetPos) && breakData.getEntity() != mc.field_1724) {
                           return;
                        }
                     }

                     SpeedMine.INSTANCE.mine(offsetPos);
                     return;
                  }
               }
            }

            for (double y : yOffset) {
               for (double offsetxx : xzOffset) {
                  for (double offset2x : xzOffset) {
                     BlockPos offsetPos = new BlockPosX(player.method_23317() + offset2x, player.method_23318() + y, player.method_23321() + offsetxx);
                     if (this.isObsidian(offsetPos)) {
                        for (MineManager breakDatax : new HashMap(me.hextech.HexTech.BREAK.breakMap).values()) {
                           if (breakDatax != null
                              && breakDatax.getEntity() != null
                              && breakDatax.pos.equals(offsetPos)
                              && breakDatax.getEntity() != mc.field_1724) {
                              return;
                           }
                        }

                        SpeedMine.INSTANCE.mine(offsetPos);
                        return;
                     }
                  }
               }
            }
         }

         if (this.mineMode.is(AutoMine.SyncSlot)) {
            xzOffset = new double[]{0.0, 0.3, -0.3};
            yOffset = new double[]{0.5, 1.1};

            for (double y : yOffset) {
               for (double offsetxx : xzOffset) {
                  BlockPos offsetPos = new BlockPosX(player.method_23317() + offsetxx, player.method_23318() + y, player.method_23321() + offsetxx);
                  if (this.isObsidian(offsetPos)) {
                     SpeedMine.INSTANCE.mine(offsetPos);
                     return;
                  }
               }
            }

            for (double y : yOffset) {
               for (double offsetxxx : xzOffset) {
                  for (double offset2xx : xzOffset) {
                     BlockPos offsetPos = new BlockPosX(player.method_23317() + offset2xx, player.method_23318() + y, player.method_23321() + offsetxxx);
                     if (this.isObsidian(offsetPos)) {
                        SpeedMine.INSTANCE.mine(offsetPos);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void doMine(BlockPos pos) {
      if (this.canBreak(pos)) {
         SpeedMine.INSTANCE.mine(pos);
      } else if (this.canBreak(pos.method_10084())) {
         SpeedMine.INSTANCE.mine(pos.method_10084());
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
      if (pos != null && mc.field_1687 != null) {
         BlockPos obsPos = pos.method_10074();
         BlockPos boost = obsPos.method_10084();
         return (BlockUtil.getBlock(obsPos) == Blocks.field_9987 || BlockUtil.getBlock(obsPos) == Blocks.field_10540 || !block)
            && !BlockUtil.hasEntityBlockCrystal(boost, true, true)
            && !BlockUtil.hasEntityBlockCrystal(boost.method_10084(), true, true)
            && (!this.lowVersion.getValue() || mc.field_1687.method_22347(boost.method_10084()));
      } else {
         return false;
      }
   }

   private boolean isObsidian(BlockPos pos) {
      return pos != null && mc.field_1724 != null && mc.field_1687 != null
         ? mc.field_1724.method_33571().method_1022(pos.method_46558()) <= this.range.getValue()
            && (
               BlockUtil.getBlock(pos) == Blocks.field_10540
                  || BlockUtil.getBlock(pos) == Blocks.field_10443
                  || BlockUtil.getBlock(pos) == Blocks.field_22108
                  || BlockUtil.getBlock(pos) == Blocks.field_23152
            )
            && BlockUtil.getClickSideStrict(pos) != null
         : false;
   }

   private boolean canBreak(BlockPos pos) {
      return pos != null && mc.field_1724 != null
         ? this.isObsidian(pos)
            && (BlockUtil.getClickSideStrict(pos) != null || Objects.equals(SpeedMine.getBreakPos(), pos))
            && (!pos.equals(SpeedMine.secondPos) || !(mc.field_1724.method_6047().method_7909() instanceof PickaxeItem) && !SilentDouble.INSTANCE.isOn())
         : false;
   }
}
