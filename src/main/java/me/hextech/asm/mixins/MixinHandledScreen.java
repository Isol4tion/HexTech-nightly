package me.hextech.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.ShulkerViewer;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HandledScreen.class})
public abstract class MixinHandledScreen<T extends ScreenHandler>
extends Screen
implements ScreenHandlerProvider<T> {
    @Shadow
    @Nullable
    protected Slot field_2787;
    @Shadow
    protected int field_2776;
    @Shadow
    protected int field_2800;

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.field_2787 != null && !this.field_2787.method_7677().method_7960() && this.field_22787.player.field_7498.method_34255().method_7960() && this.hasItems(this.field_2787.method_7677()) && ShulkerViewer.INSTANCE.isOn()) {
            this.renderShulkerToolTip(context, mouseX, mouseY, this.field_2787.method_7677());
        }
    }

    public void renderShulkerToolTip(DrawContext context, int mouseX, int mouseY, ItemStack stack) {
        try {
            NbtCompound compoundTag = stack.method_7941("BlockEntityTag");
            DefaultedList itemStacks = DefaultedList.method_10213((int)27, (Object)ItemStack.field_8037);
            Inventories.method_5429((NbtCompound)compoundTag, (DefaultedList)itemStacks);
            this.draw(context, (DefaultedList<ItemStack>)itemStacks, mouseX, mouseY);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void draw(DrawContext context, DefaultedList<ItemStack> itemStacks, int mouseX, int mouseY) {
        RenderSystem.disableDepthTest();
        GL11.glClear((int)256);
        this.drawBackground(context, mouseX += 8, mouseY -= 82);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        DiffuseLighting.method_24211();
        int row = 0;
        int i = 0;
        for (ItemStack itemStack : itemStacks) {
            context.method_51427(itemStack, mouseX + 8 + i * 18, mouseY + 7 + row * 18);
            context.method_51431(Wrapper.mc.field_1772, itemStack, mouseX + 8 + i * 18, mouseY + 7 + row * 18);
            if (++i < 9) continue;
            i = 0;
            ++row;
        }
        DiffuseLighting.method_24210();
        RenderSystem.enableDepthTest();
    }

    private void drawBackground(DrawContext context, int x, int y) {
        Render2DUtil.drawRect(context.method_51448(), (float)x, (float)y, 176.0f, 67.0f, new Color(0, 0, 0, 120));
    }

    private boolean hasItems(ItemStack itemStack) {
        NbtCompound compoundTag = itemStack.method_7941("BlockEntityTag");
        return compoundTag != null && compoundTag.method_10573("Items", 9);
    }
}
