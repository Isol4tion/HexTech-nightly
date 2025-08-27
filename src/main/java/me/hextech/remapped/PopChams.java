package me.hextech.remapped;

import java.util.concurrent.CopyOnWriteArrayList;
import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.PopChams_WNWBvFQQYNjRmTHDKpkM;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector3f;

/*
 * Exception performing whole class analysis ignored.
 */
public class PopChams {
    private final PlayerEntity player;
    private final PlayerEntityModel<PlayerEntity> modelPlayer;
    private int alpha;
    final /* synthetic */ PopChams_WNWBvFQQYNjRmTHDKpkM this$0;

    public PopChams(PopChams_WNWBvFQQYNjRmTHDKpkM popChams_WNWBvFQQYNjRmTHDKpkM, PlayerEntity player) {
        this.this$0 = popChams_WNWBvFQQYNjRmTHDKpkM;
        this.player = player;
        this.modelPlayer = new PlayerEntityModel(new EntityRendererFactory.Context(Wrapper.mc.getEntityRenderDispatcher(), Wrapper.mc.getItemRenderer(), Wrapper.mc.getBlockRenderManager(), Wrapper.mc.getEntityRenderDispatcher().getHeldItemRenderer(), Wrapper.mc.getResourceManager(), Wrapper.mc.getLoadedEntityModels(), Wrapper.mc.textRenderer).getPart(EntityModelLayers.PLAYER), false);
        this.modelPlayer.method_2838().scale(new Vector3f(-0.3f, -0.3f, -0.3f));
        this.alpha = popChams_WNWBvFQQYNjRmTHDKpkM.color.getValue().getAlpha();
    }

    public void update(CopyOnWriteArrayList<PopChams> arrayList) {
        if (this.alpha <= 0) {
            arrayList.remove(this);
            this.player.method_5768();
            this.player.method_5650(Entity.RemovalReason.KILLED);
            this.player.method_36209();
            return;
        }
        this.alpha = (int)(AnimateUtil.animate(this.alpha, 0.0, this.this$0.alphaSpeed.getValue()) - 0.2);
    }

    public int getAlpha() {
        return (int)MathUtil.clamp(this.alpha, 0.0f, 255.0f);
    }
}
