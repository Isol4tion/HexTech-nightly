package me.hextech.remapped;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector3f;

class DesyncESP_EZqjXHyHjNyfFrdyAmeM {
   private final PlayerEntityModel<PlayerEntity> modelPlayer = new PlayerEntityModel(
      new Context(
            Wrapper.mc.method_1561(),
            Wrapper.mc.method_1480(),
            Wrapper.mc.method_1541(),
            Wrapper.mc.method_1561().method_43336(),
            Wrapper.mc.method_1478(),
            Wrapper.mc.method_31974(),
            Wrapper.mc.field_1772
         )
         .method_32167(EntityModelLayers.field_27577),
      false
   );

   public DesyncESP_EZqjXHyHjNyfFrdyAmeM() {
      this.modelPlayer.method_2838().method_41924(new Vector3f(-0.3F, -0.3F, -0.3F));
   }
}
