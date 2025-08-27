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
public class DesyncESP_EZqjXHyHjNyfFrdyAmeM {
    private final PlayerEntityModel<PlayerEntity> modelPlayer;

    public DesyncESP_EZqjXHyHjNyfFrdyAmeM() {
        this.modelPlayer = new PlayerEntityModel(new EntityRendererFactory.Context(Wrapper.mc.getEntityRenderDispatcher(), Wrapper.mc.getItemRenderer(), Wrapper.mc.getBlockRenderManager(), Wrapper.mc.getEntityRenderDispatcher().getHeldItemRenderer(), Wrapper.mc.getResourceManager(), Wrapper.mc.getLoadedEntityModels(), Wrapper.mc.textRenderer).getPart(EntityModelLayers.PLAYER), false);
        this.modelPlayer.getHead().scale(new Vector3f(-0.3f, -0.3f, -0.3f));
    }
}
