package me.hextech.remapped;

import me.hextech.remapped.Beta;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.entity.player.PlayerEntity;
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
            ItemStack itemStack = BedCrafter.mc.player.method_31548().method_5438(i);
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
            if (BedCrafter.mc.player.field_7512 instanceof CraftingScreenHandler) {
                BedCrafter.mc.player.method_7346();
            }
            if (this.disable.getValue()) {
                this.disable();
            }
            return;
        }
        if (BedCrafter.mc.player.field_7512 instanceof CraftingScreenHandler) {
            this.open = true;
            boolean craft = false;
            block0: for (RecipeResultCollection recipeResult : BedCrafter.mc.player.getRecipeBook().getOrderedResults()) {
                for (RecipeEntry recipe : recipeResult.method_2648(true)) {
                    if (!(recipe.value().method_8110(BedCrafter.mc.world.method_30349()).getItem() instanceof BedItem)) continue;
                    int bed = 0;
                    for (int i = 0; i < BedCrafter.getEmptySlots(); ++i) {
                        craft = true;
                        if (bed >= this.beds.getValueInt()) continue block0;
                        ++bed;
                        BedCrafter.mc.interactionManager.method_2912(BedCrafter.mc.player.field_7512.syncId, recipe, false);
                        BedCrafter.mc.interactionManager.clickSlot(BedCrafter.mc.player.field_7512.syncId, 0, 1, SlotActionType.QUICK_MOVE, (PlayerEntity)BedCrafter.mc.player);
                    }
                    continue block0;
                }
            }
            if (!craft) {
                if (BedCrafter.mc.player.field_7512 instanceof CraftingScreenHandler) {
                    BedCrafter.mc.player.method_7346();
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
            if (BedCrafter.mc.world.method_8320(pos).method_26204() == Blocks.CRAFTING_TABLE && BlockUtil.getClickSideStrict(pos) != null) {
                place = false;
                bestPos = pos;
                break;
            }
            if (!BlockUtil.canPlace(pos) || bestPos != null && !((double)MathHelper.sqrt((float)((float)BedCrafter.mc.player.method_5707(pos.toCenterPos()))) < distance)) continue;
            bestPos = pos;
            distance = MathHelper.sqrt((float)((float)BedCrafter.mc.player.method_5707(pos.toCenterPos())));
        }
        if (bestPos != null) {
            if (!place) {
                BlockUtil.clickBlock(bestPos, BlockUtil.getClickSide(bestPos), this.rotate.getValue());
            } else {
                if (InventoryUtil.findItem(Item.fromBlock((Block)Blocks.CRAFTING_TABLE)) == -1) {
                    return;
                }
                int old = BedCrafter.mc.player.method_31548().selectedSlot;
                InventoryUtil.switchToSlot(InventoryUtil.findItem(Item.fromBlock((Block)Blocks.CRAFTING_TABLE)));
                BlockUtil.placeBlock(bestPos, this.rotate.getValue());
                InventoryUtil.switchToSlot(old);
            }
        }
    }
}
