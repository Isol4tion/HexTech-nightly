package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@Beta
public class BedCrafter extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static BedCrafter INSTANCE;
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", false));
   private final SliderSetting range = this.add(new SliderSetting("Range", 5, 0, 8));
   private final SliderSetting beds = this.add(new SliderSetting("Beds", 5, 1, 30));
   private final BooleanSetting disable = this.add(new BooleanSetting("Disable", true));
   boolean open = false;

   public BedCrafter() {
      super("BedCrafter", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   public static int getEmptySlots() {
      int emptySlots = 0;

      for (int i = 0; i < 36; i++) {
         ItemStack itemStack = mc.field_1724.method_31548().method_5438(i);
         if (itemStack == null || itemStack.method_7909() instanceof AirBlockItem) {
            emptySlots++;
         }
      }

      return emptySlots;
   }

   @Override
   public void onDisable() {
      this.open = false;
   }

   @Override
   public void onUpdate() {
      if (getEmptySlots() == 0) {
         if (mc.field_1724.field_7512 instanceof CraftingScreenHandler) {
            mc.field_1724.method_7346();
         }

         if (this.disable.getValue()) {
            this.disable();
         }
      } else {
         if (mc.field_1724.field_7512 instanceof CraftingScreenHandler) {
            this.open = true;
            boolean craft = false;

            for (RecipeResultCollection recipeResult : mc.field_1724.method_3130().method_1393()) {
               for (RecipeEntry<?> recipe : recipeResult.method_2648(true)) {
                  if (recipe.comp_1933().method_8110(mc.field_1687.method_30349()).method_7909() instanceof BedItem) {
                     int bed = 0;

                     for (int i = 0; i < getEmptySlots(); i++) {
                        craft = true;
                        if (bed >= this.beds.getValueInt()) {
                           break;
                        }

                        bed++;
                        mc.field_1761.method_2912(mc.field_1724.field_7512.field_7763, recipe, false);
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 0, 1, SlotActionType.field_7794, mc.field_1724);
                     }
                     break;
                  }
               }
            }

            if (!craft) {
               if (mc.field_1724.field_7512 instanceof CraftingScreenHandler) {
                  mc.field_1724.method_7346();
               }

               if (this.disable.getValue()) {
                  this.disable();
               }
            }
         } else {
            if (this.disable.getValue() && this.open) {
               this.disable();
               return;
            }

            this.doPlace();
         }
      }
   }

   private void doPlace() {
      BlockPos bestPos = null;
      double distance = 100.0;
      boolean place = true;

      for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
         if (mc.field_1687.method_8320(pos).method_26204() == Blocks.field_9980 && BlockUtil.getClickSideStrict(pos) != null) {
            place = false;
            bestPos = pos;
            break;
         }

         if (BlockUtil.canPlace(pos) && (bestPos == null || (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < distance)) {
            bestPos = pos;
            distance = (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_46558()));
         }
      }

      if (bestPos != null) {
         if (!place) {
            BlockUtil.clickBlock(bestPos, BlockUtil.getClickSide(bestPos), this.rotate.getValue());
         } else {
            if (InventoryUtil.findItem(Item.method_7867(Blocks.field_9980)) == -1) {
               return;
            }

            int old = mc.field_1724.method_31548().field_7545;
            InventoryUtil.switchToSlot(InventoryUtil.findItem(Item.method_7867(Blocks.field_9980)));
            BlockUtil.placeBlock(bestPos, this.rotate.getValue());
            InventoryUtil.switchToSlot(old);
         }
      }
   }
}
