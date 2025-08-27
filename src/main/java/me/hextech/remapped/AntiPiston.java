package me.hextech.remapped;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class AntiPiston extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AntiPiston INSTANCE;
   public final BooleanSetting moveUp = this.add(new BooleanSetting("MoveUp", true));
   public final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
   public final BooleanSetting packet = this.add(new BooleanSetting("Packet", true));
   public final BooleanSetting helper = this.add(new BooleanSetting("Helper", true));
   public final BooleanSetting trap = this.add(new BooleanSetting("Trap", true).setParent());
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final BooleanSetting onlyBurrow = this.add(new BooleanSetting("OnlyBurrow", true, v -> this.trap.isOpen()).setParent());
   private final BooleanSetting whenDouble = this.add(new BooleanSetting("WhenDouble", true, v -> this.onlyBurrow.isOpen()));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));

   public AntiPiston() {
      super("AntiPiston", "Trap self when piston kick", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static boolean canPlace(BlockPos pos) {
      if (!BlockUtil.canBlockFacing(pos)) {
         return false;
      } else {
         return !BlockUtil.canReplace(pos) ? false : !BlockUtil.hasEntity(pos, false);
      }
   }

   @Override
   public void onUpdate() {
      if (mc.field_1724.method_24828()) {
         if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
            this.block();
         }
      }
   }

   private void block() {
      BlockPos pos = EntityUtil.getPlayerPos();
      if (this.moveUp.getValue()) {
         boolean canMove = false;

         for (Direction i : Direction.values()) {
            if (i != Direction.field_11033
               && i != Direction.field_11036
               && this.getBlock(pos.method_10093(i).method_10084()) instanceof PistonBlock
               && ((Direction)mc.field_1687.method_8320(pos.method_10093(i).method_10084()).method_11654(FacingBlock.field_10927)).method_10153() == i) {
               if (canMove) {
                  mc.method_1562()
                     .method_52787(
                        new PositionAndOnGround(
                           mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.4199999868869781, mc.field_1724.method_23321(), false
                        )
                     );
                  mc.method_1562()
                     .method_52787(
                        new PositionAndOnGround(
                           mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.7531999805212017, mc.field_1724.method_23321(), false
                        )
                     );
                  mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0, mc.field_1724.method_23321());
                  mc.method_1562()
                     .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
                  canMove = false;
               }
            } else if (!this.webUpdate(mc.field_1724)) {
               canMove = true;
            }
         }
      }

      if (this.getBlock(pos.method_10086(2)) != Blocks.field_10540 && this.getBlock(pos.method_10086(2)) != Blocks.field_9987) {
         int progress = 0;
         if (this.whenDouble.getValue()) {
            for (Direction ix : Direction.values()) {
               if (ix != Direction.field_11033
                  && ix != Direction.field_11036
                  && this.getBlock(pos.method_10093(ix).method_10084()) instanceof PistonBlock
                  && ((Direction)mc.field_1687.method_8320(pos.method_10093(ix).method_10084()).method_11654(FacingBlock.field_10927)).method_10153() == ix) {
                  progress++;
               }
            }
         }

         if (!this.webUpdate(mc.field_1724)) {
            mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 3.0, mc.field_1724.method_23321());
            mc.method_1562()
               .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
         }

         for (Direction ixx : Direction.values()) {
            if (ixx != Direction.field_11033
               && ixx != Direction.field_11036
               && this.getBlock(pos.method_10093(ixx).method_10084()) instanceof PistonBlock
               && ((Direction)mc.field_1687.method_8320(pos.method_10093(ixx).method_10084()).method_11654(FacingBlock.field_10927)).method_10153() == ixx) {
               this.placeBlock(pos.method_10084().method_10079(ixx, -1));
               if (this.trap.getValue() && (this.getBlock(pos) != Blocks.field_10124 || !this.onlyBurrow.getValue() || progress >= 2)) {
                  this.placeBlock(pos.method_10086(2));
                  if (!BlockUtil.canPlace(pos.method_10086(2))) {
                     for (Direction i2 : Direction.values()) {
                        if (canPlace(pos.method_10093(i2).method_10086(2))) {
                           this.placeBlock(pos.method_10093(i2).method_10086(2));
                           break;
                        }
                     }
                  }
               }

               if (!BlockUtil.canPlace(pos.method_10084().method_10079(ixx, -1)) && this.helper.getValue()) {
                  if (BlockUtil.canPlace(pos.method_10079(ixx, -1))) {
                     this.placeBlock(pos.method_10079(ixx, -1));
                  } else {
                     this.placeBlock(pos.method_10079(ixx, -1).method_10074());
                  }
               }
            }
         }
      }
   }

   private Block getBlock(BlockPos block) {
      return mc.field_1687.method_8320(block).method_26204();
   }

   private void placeBlock(BlockPos pos) {
      if (canPlace(pos)) {
         int old = mc.field_1724.method_31548().field_7545;
         int block = this.findBlock(Blocks.field_10540);
         if (block != -1) {
            this.doSwap(block);
            BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), this.packet.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }
         }
      }
   }

   public int findBlock(Block blockIn) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private boolean webUpdate(PlayerEntity player) {
      for (float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for (float z : new float[]{0.0F, 0.3F, -0.3F}) {
            for (int y : new int[]{-1, 0, 1, 2}) {
               BlockPos pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318(), player.method_23321() + (double)z).method_10086(y);
               if (new Box(pos).method_994(player.method_5829()) && mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10343) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
