package me.hextech.asm.accessors;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({HeldItemRenderer.class})
public interface IHeldItemRenderer {
   @Accessor("equipProgressMainHand")
   float getEquippedProgressMainHand();

   @Accessor("equipProgressMainHand")
   void setEquippedProgressMainHand(float var1);

   @Accessor("equipProgressOffHand")
   float getEquippedProgressOffHand();

   @Accessor("equipProgressOffHand")
   void setEquippedProgressOffHand(float var1);

   @Accessor("mainHand")
   void setItemStackMainHand(ItemStack var1);

   @Accessor("offHand")
   void setItemStackOffHand(ItemStack var1);
}
