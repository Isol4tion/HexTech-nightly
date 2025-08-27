package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Debug extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Debug INSTANCE;
   private final BooleanSetting prerender = this.add(new BooleanSetting("PredictRender", true));
   private final BooleanSetting chat = this.add(new BooleanSetting("Chat", true));
   public ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 200)));
   public SliderSetting ticks = this.add(new SliderSetting("Ticks", 5, 0, 40));

   public Debug() {
      super("Debug", Module_JlagirAibYQgkHtbRnhw.Setting);
      INSTANCE = this;
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (!nullCheck()) {
         if (mc.field_1687 != null) {
            for (AbstractClientPlayerEntity player : mc.field_1687.method_18456()) {
               Vec3d vec3d = CombatUtil.getEntityPosVec(player, this.ticks.getValueInt());
               if (this.prerender.getValue()) {
                  Render3DUtil.draw3DBox(matrixStack, new Box(BlockPos.method_49638(vec3d)), this.color.getValue());
               }

               if (this.chat.getValue()) {
                  CommandManager.sendChatMessage(vec3d.field_1352 + " " + vec3d.field_1351 + " " + vec3d.field_1350);
               }
            }
         }
      }
   }
}
