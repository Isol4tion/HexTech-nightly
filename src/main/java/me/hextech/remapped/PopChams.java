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
private static class PopChams {
    private final PlayerEntity player;
    private final PlayerEntityModel<PlayerEntity> modelPlayer;
    private int alpha;
    final /* synthetic */ PopChams_WNWBvFQQYNjRmTHDKpkM this$0;

    public PopChams(PopChams_WNWBvFQQYNjRmTHDKpkM popChams_WNWBvFQQYNjRmTHDKpkM, PlayerEntity player) {
        this.this$0 = popChams_WNWBvFQQYNjRmTHDKpkM;
        this.player = player;
        this.modelPlayer = new PlayerEntityModel(new EntityRendererFactory.Context(Wrapper.mc.method_1561(), Wrapper.mc.method_1480(), Wrapper.mc.method_1541(), Wrapper.mc.method_1561().method_43336(), Wrapper.mc.method_1478(), Wrapper.mc.method_31974(), Wrapper.mc.field_1772).method_32167(EntityModelLayers.field_27577), false);
        this.modelPlayer.method_2838().method_41924(new Vector3f(-0.3f, -0.3f, -0.3f));
        this.alpha = popChams_WNWBvFQQYNjRmTHDKpkM.color.getValue().getAlpha();
    }

    public void update(CopyOnWriteArrayList<PopChams> arrayList) {
        if (this.alpha <= 0) {
            arrayList.remove(this);
            this.player.method_5768();
            this.player.method_5650(Entity.RemovalReason.field_26998);
            this.player.method_36209();
            return;
        }
        this.alpha = (int)(AnimateUtil.animate(this.alpha, 0.0, this.this$0.alphaSpeed.getValue()) - 0.2);
    }

    public int getAlpha() {
        return (int)MathUtil.clamp(this.alpha, 0.0f, 255.0f);
    }
}
