package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class AntiCev extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AntiCev INSTANCE;
   final Timer timer = new Timer();
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 50, 0, 500));
   private final SliderSetting multiPlace = this.add(new SliderSetting("MultiPlace", 1, 1, 8));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
   private final BooleanSetting breakCrystal = this.add(new BooleanSetting("BreakCrystal", true));
   private final BooleanSetting checkMine = this.add(new BooleanSetting("CheckMine", true));
   private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final List<BlockPos> crystalPos = new ArrayList();
   int progress = 0;
   private BlockPos pos;

   public AntiCev() {
      super("AntiCev", "Anti cev", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (this.timer.passedMs(this.delay.getValue())) {
         if (!this.eatingPause.getValue() || !EntityUtil.isUsing()) {
            this.progress = 0;
            if (this.pos != null && !this.pos.equals(EntityUtil.getPlayerPos(true))) {
               this.crystalPos.clear();
            }

            this.pos = EntityUtil.getPlayerPos(true);

            for (Direction i : Direction.values()) {
               if (i != Direction.field_11033 && !this.isGod(this.pos.method_10093(i).method_10084())) {
                  BlockPos offsetPos = this.pos.method_10093(i).method_10086(2);
                  if (this.crystalHere(offsetPos) && !this.crystalPos.contains(offsetPos)) {
                     this.crystalPos.add(offsetPos);
                  }
               }
            }

            if (this.getBlock() != -1) {
               if (!this.onlyGround.getValue() || ((ClientPlayerEntity)Objects.requireNonNull(mc.field_1724)).method_24828()) {
                  this.crystalPos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));

                  for (BlockPos defensePos : this.crystalPos) {
                     if (this.crystalHere(defensePos) && this.breakCrystal.getValue()) {
                        CombatUtil.attackCrystal(defensePos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
                     }

                     if (BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) {
                        this.placeBlock(defensePos);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean crystalHere(BlockPos pos) {
      for (Entity entity : ((ClientWorld)Objects.requireNonNull(mc.field_1687)).method_18467(EndCrystalEntity.class, new Box(pos))) {
         if (EntityUtil.getEntityPos(entity).equals(pos)) {
            return true;
         }
      }

      return false;
   }

   private boolean isGod(BlockPos pos) {
      return ((ClientWorld)Objects.requireNonNull(mc.field_1687)).method_8320(pos).method_26204() == Blocks.field_9987;
   }

   private void placeBlock(BlockPos pos) {
      if ((double)this.progress < this.multiPlace.getValue()) {
         if (!this.checkMine.getValue() || !BlockUtil.isMining(pos)) {
            int block;
            if ((block = this.getBlock()) != -1) {
               this.doSwap(block);
               BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue());
               if (this.inventory.getValue()) {
                  this.doSwap(block);
                  EntityUtil.syncInventory();
               } else {
                  int old = 0;
                  this.doSwap(old);
               }

               this.progress++;
               this.timer.reset();
            }
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10540) : InventoryUtil.findBlock(Blocks.field_10540);
   }
}
