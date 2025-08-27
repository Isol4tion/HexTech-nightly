package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class Blocker_mEBqWazfEhCLEwVSYEFP extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Blocker_mEBqWazfEhCLEwVSYEFP INSTANCE;
   final Timer timer = new Timer();
   private final EnumSetting<Blocker_BybKYKuAntfATLqEYmcO> page = this.add(new EnumSetting("Page", Blocker_BybKYKuAntfATLqEYmcO.General));
   public final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Render));
   public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 5000, v -> this.render.getValue()));
   final ColorSetting box = this.add(
      new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Render).injectBoolean(true)
   );
   final ColorSetting fill = this.add(
      new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Render).injectBoolean(true)
   );
   private final SliderSetting delay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
   private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
   private final BooleanSetting inventorySwap = this.add(
      new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General)
   );
   private final BooleanSetting bevelCev = this.add(new BooleanSetting("BevelCev", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Target));
   private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Target).setParent());
   private final BooleanSetting onlySurround = this.add(
      new BooleanSetting("OnlySurround", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Target && this.feet.isOpen())
   );
   private final BooleanSetting inAirPause = this.add(new BooleanSetting("InAirPause", false, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Check));
   private final BooleanSetting detectMining = this.add(
      new BooleanSetting("DetectMining", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Check)
   );
   private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Check));
   private final List<BlockPos> placePos = new ArrayList();
   private int placeProgress = 0;
   private BlockPos playerBP;

   public Blocker_mEBqWazfEhCLEwVSYEFP() {
      super("Blocker", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
      me.hextech.HexTech.EVENT_BUS.subscribe(new Blocker(this));
   }

   @Override
   public void onUpdate() {
      if (this.timer.passedMs(this.delay.getValue())) {
         if (!this.eatingPause.getValue() || !EntityUtil.isUsing()) {
            this.placeProgress = 0;
            if (this.playerBP != null && !this.playerBP.equals(EntityUtil.getPlayerPos(true))) {
               this.placePos.clear();
            }

            this.playerBP = EntityUtil.getPlayerPos(true);
            if (this.bevelCev.getValue()) {
               for (Direction i : Direction.values()) {
                  if (i != Direction.field_11033 && !this.isBedrock(this.playerBP.method_10093(i).method_10084())) {
                     BlockPos blockerPos = this.playerBP.method_10093(i).method_10086(2);
                     if (this.crystalHere(blockerPos) && !this.placePos.contains(blockerPos)) {
                        this.placePos.add(blockerPos);
                     }
                  }
               }
            }

            if (this.getObsidian() != -1) {
               if (!this.inAirPause.getValue() || mc.field_1724.method_24828()) {
                  this.placePos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));
                  if (this.feet.getValue() && (!this.onlySurround.getValue() || Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn())) {
                     for (Direction ix : Direction.values()) {
                        if (ix != Direction.field_11033 && ix != Direction.field_11036) {
                           BlockPos surroundPos = this.playerBP.method_10093(ix);
                           if (!this.isBedrock(surroundPos) && BlockUtil.isMining(surroundPos)) {
                              for (Direction direction : Direction.values()) {
                                 if (direction != Direction.field_11033 && direction != Direction.field_11036) {
                                    BlockPos defensePos = this.playerBP.method_10093(ix).method_10093(direction);
                                    if (this.breakCrystal.getValue()) {
                                       CombatUtil.attackCrystal(defensePos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
                                    }

                                    if (BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) {
                                       this.tryPlaceObsidian(defensePos);
                                    }
                                 }
                              }

                              BlockPos defensePosx = this.playerBP.method_10093(ix).method_10084();
                              if (this.breakCrystal.getValue()) {
                                 CombatUtil.attackCrystal(defensePosx, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
                              }

                              if (BlockUtil.canPlace(defensePosx, 6.0, this.breakCrystal.getValue())) {
                                 this.tryPlaceObsidian(defensePosx);
                              }
                           }
                        }
                     }
                  }

                  for (BlockPos defensePosxx : this.placePos) {
                     if (this.breakCrystal.getValue() && this.crystalHere(defensePosxx)) {
                        CombatUtil.attackCrystal(defensePosxx, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
                     }

                     if (BlockUtil.canPlace(defensePosxx, 6.0, this.breakCrystal.getValue())) {
                        this.tryPlaceObsidian(defensePosxx);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean crystalHere(BlockPos pos) {
      return mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos)).stream().anyMatch(entity -> entity.method_24515().equals(pos));
   }

   private boolean isBedrock(BlockPos pos) {
      return mc.field_1687.method_8320(pos).method_26204() == Blocks.field_9987;
   }

   private void tryPlaceObsidian(BlockPos pos) {
      if ((double)this.placeProgress < this.blocksPer.getValue()) {
         if (!this.detectMining.getValue() || !BlockUtil.isMining(pos)) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            int block;
            if ((block = this.getObsidian()) != -1) {
               this.doSwap(block);
               BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue());
               if (this.inventorySwap.getValue()) {
                  this.doSwap(block);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }

               this.placeProgress++;
               Blocker.addBlock(pos);
               this.timer.reset();
            }
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private int getObsidian() {
      return this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10540) : InventoryUtil.findBlock(Blocks.field_10540);
   }
}
