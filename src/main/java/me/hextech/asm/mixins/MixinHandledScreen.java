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

@Mixin({HandledScreen.class})
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
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

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (this.field_2787 != null
         && !this.field_2787.method_7677().method_7960()
         && this.field_22787.field_1724.field_7498.method_34255().method_7960()
         && this.hasItems(this.field_2787.method_7677())
         && ShulkerViewer.INSTANCE.isOn()) {
         this.renderShulkerToolTip(context, mouseX, mouseY, this.field_2787.method_7677());
      }
   }

   public void renderShulkerToolTip(DrawContext context, int mouseX, int mouseY, ItemStack stack) {
      try {
         NbtCompound compoundTag = stack.method_7941("BlockEntityTag");
         DefaultedList<ItemStack> itemStacks = DefaultedList.method_10213(27, ItemStack.field_8037);
         Inventories.method_5429(compoundTag, itemStacks);
         this.draw(context, itemStacks, mouseX, mouseY);
      } catch (Exception var7) {
      }
   }

   private void draw(DrawContext context, DefaultedList<ItemStack> itemStacks, int mouseX, int mouseY) {
      RenderSystem.disableDepthTest();
      GL11.glClear(256);
      mouseX += 8;
      mouseY -= 82;
      this.drawBackground(context, mouseX, mouseY);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      DiffuseLighting.method_24211();
      int row = 0;
      int i = 0;

      for (ItemStack itemStack : itemStacks) {
         context.method_51427(itemStack, mouseX + 8 + i * 18, mouseY + 7 + row * 18);
         context.method_51431(Wrapper.mc.field_1772, itemStack, mouseX + 8 + i * 18, mouseY + 7 + row * 18);
         if (++i >= 9) {
            i = 0;
            row++;
         }
      }

      DiffuseLighting.method_24210();
      RenderSystem.enableDepthTest();
   }

   private void drawBackground(DrawContext context, int x, int y) {
      Render2DUtil.drawRect(context.method_51448(), (float)x, (float)y, 176.0F, 67.0F, new Color(0, 0, 0, 120));
   }

   private boolean hasItems(ItemStack itemStack) {
      NbtCompound compoundTag = itemStack.method_7941("BlockEntityTag");
      return compoundTag != null && compoundTag.method_10573("Items", 9);
   }
}
