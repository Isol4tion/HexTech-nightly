package me.hextech.remapped;

import me.hextech.remapped.Wrapper;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector3f;

/*
 * Exception performing whole class analysis ignored.
 */
private static class DesyncESP_EZqjXHyHjNyfFrdyAmeM {
    private final PlayerEntityModel<PlayerEntity> modelPlayer;

    public DesyncESP_EZqjXHyHjNyfFrdyAmeM() {
        this.modelPlayer = new PlayerEntityModel(new EntityRendererFactory.Context(Wrapper.mc.method_1561(), Wrapper.mc.method_1480(), Wrapper.mc.method_1541(), Wrapper.mc.method_1561().method_43336(), Wrapper.mc.method_1478(), Wrapper.mc.method_31974(), Wrapper.mc.field_1772).method_32167(EntityModelLayers.field_27577), false);
        this.modelPlayer.method_2838().method_41924(new Vector3f(-0.3f, -0.3f, -0.3f));
    }
}
