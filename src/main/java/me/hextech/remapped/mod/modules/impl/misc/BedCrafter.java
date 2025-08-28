package me.hextech.remapped.mod.modules.impl.misc;

import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
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

public class BedCrafter
extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = BedCrafter.mc.player.getInventory().getStack(i);
            if (itemStack != null && !(itemStack.getItem() instanceof AirBlockItem)) continue;
            ++emptySlots;
        }
        return emptySlots;
    }

    @Override
    public void onDisable() {
        this.open = false;
    }

    @Override
    public void onUpdate() {
        if (BedCrafter.getEmptySlots() == 0) {
            if (BedCrafter.mc.player.currentScreenHandler instanceof CraftingScreenHandler) {
                BedCrafter.mc.player.closeHandledScreen();
            }
            if (this.disable.getValue()) {
                this.disable();
            }
            return;
        }
        if (BedCrafter.mc.player.currentScreenHandler instanceof CraftingScreenHandler) {
            this.open = true;
            boolean craft = false;
            block0: for (RecipeResultCollection recipeResult : BedCrafter.mc.player.getRecipeBook().getOrderedResults()) {
                for (RecipeEntry recipe : recipeResult.getRecipes(true)) {
                    if (!(recipe.value().getResult(BedCrafter.mc.world.getRegistryManager()).getItem() instanceof BedItem)) continue;
                    int bed = 0;
                    for (int i = 0; i < BedCrafter.getEmptySlots(); ++i) {
                        craft = true;
                        if (bed >= this.beds.getValueInt()) continue block0;
                        ++bed;
                        BedCrafter.mc.interactionManager.clickRecipe(BedCrafter.mc.player.currentScreenHandler.syncId, recipe, false);
                        BedCrafter.mc.interactionManager.clickSlot(BedCrafter.mc.player.currentScreenHandler.syncId, 0, 1, SlotActionType.QUICK_MOVE, BedCrafter.mc.player);
                    }
                    continue block0;
                }
            }
            if (!craft) {
                if (BedCrafter.mc.player.currentScreenHandler instanceof CraftingScreenHandler) {
                    BedCrafter.mc.player.closeHandledScreen();
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

    private void doPlace() {
        BlockPos bestPos = null;
        double distance = 100.0;
        boolean place = true;
        for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
            if (BedCrafter.mc.world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && BlockUtil.getClickSideStrict(pos) != null) {
                place = false;
                bestPos = pos;
                break;
            }
            if (!BlockUtil.canPlace(pos) || bestPos != null && !((double)MathHelper.sqrt((float)BedCrafter.mc.player.squaredDistanceTo(pos.toCenterPos())) < distance)) continue;
            bestPos = pos;
            distance = MathHelper.sqrt((float)BedCrafter.mc.player.squaredDistanceTo(pos.toCenterPos()));
        }
        if (bestPos != null) {
            if (!place) {
                BlockUtil.clickBlock(bestPos, BlockUtil.getClickSide(bestPos), this.rotate.getValue());
            } else {
                if (InventoryUtil.findItem(Item.fromBlock(Blocks.CRAFTING_TABLE)) == -1) {
                    return;
                }
                int old = BedCrafter.mc.player.getInventory().selectedSlot;
                InventoryUtil.switchToSlot(InventoryUtil.findItem(Item.fromBlock(Blocks.CRAFTING_TABLE)));
                BlockUtil.placeBlock(bestPos, this.rotate.getValue());
                InventoryUtil.switchToSlot(old);
            }
        }
    }
}
