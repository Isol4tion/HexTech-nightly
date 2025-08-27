package me.hextech.remapped;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

public class InventoryMove extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final BooleanSetting sneak = this.add(new BooleanSetting("Sneak", false));

   public InventoryMove() {
      super("InventoryMove", Module_JlagirAibYQgkHtbRnhw.Movement);
      this.setDescription("Walk in inventory.");
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      this.update();
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      this.update();
   }

   @Override
   public void onUpdate() {
      this.update();
   }

   private void update() {
      if (!(mc.field_1755 instanceof ChatScreen)) {
         for (KeyBinding k : new KeyBinding[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903}) {
            k.method_23481(InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(k.method_1428()).method_1444()));
         }

         mc.field_1690
            .field_1894
            .method_23481(
               AutoWalk.INSTANCE.isOn()
                  || InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1894.method_1428()).method_1444())
            );
         mc.field_1690
            .field_1867
            .method_23481(
               Sprint.INSTANCE.isOn()
                  || InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1867.method_1428()).method_1444())
            );
         if (this.sneak.getValue()) {
            mc.field_1690
               .field_1832
               .method_23481(
                  InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1832.method_1428()).method_1444())
               );
         }
      }
   }
}
