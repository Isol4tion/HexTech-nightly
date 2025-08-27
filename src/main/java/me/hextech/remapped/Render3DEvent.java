package me.hextech.remapped;

import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private final float partialTicks;
   private final MatrixStack matrixStack;

   public Render3DEvent(MatrixStack matrixStack, float partialTicks) {
      super(Event.Pre);
      this.partialTicks = partialTicks;
      this.matrixStack = matrixStack;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }
}
