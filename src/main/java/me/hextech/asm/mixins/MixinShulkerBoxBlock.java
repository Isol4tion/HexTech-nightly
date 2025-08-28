package me.hextech.asm.mixins;

import me.hextech.mod.modules.impl.misc.ShulkerViewer;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value={ShulkerBoxBlock.class})
public class MixinShulkerBoxBlock {
    @Inject(method={"appendTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAppendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options, CallbackInfo ci) {
        if (ShulkerViewer.INSTANCE.isOn()) {
            ci.cancel();
        }
    }
}
