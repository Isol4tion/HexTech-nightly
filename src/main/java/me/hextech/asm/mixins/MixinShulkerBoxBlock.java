package me.hextech.asm.mixins;

import java.util.List;
import me.hextech.remapped.ShulkerViewer;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ShulkerBoxBlock.class})
public class MixinShulkerBoxBlock {
    @Inject(method={"appendTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAppendTooltip(ItemStack stack, BlockView view, List<Text> tooltip, TooltipType options, CallbackInfo info) {
        if (ShulkerViewer.INSTANCE.isOn()) {
            info.cancel();
        }
    }
}
