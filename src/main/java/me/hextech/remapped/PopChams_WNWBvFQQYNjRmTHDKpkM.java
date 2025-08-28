package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;

import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.utils.render.AnimateUtil;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public final class PopChams_WNWBvFQQYNjRmTHDKpkM
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PopChams_WNWBvFQQYNjRmTHDKpkM INSTANCE;
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
    private final SliderSetting alphaSpeed = this.add(new SliderSetting("AlphaSpeed", 0.2, 0.0, 1.0, 0.01));
    private final CopyOnWriteArrayList<PopChams> popList = new CopyOnWriteArrayList();

    public PopChams_WNWBvFQQYNjRmTHDKpkM() {
        super("PopChams", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    private static void prepareScale(MatrixStack matrixStack) {
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.scale(1.6f, 1.8f, 1.6f);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
    }

    @Override
    public void onUpdate() {
        this.popList.forEach(person -> person.update(this.popList));
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 0, 1);
        this.popList.forEach(person -> {
            person.modelPlayer.leftPants.visible = false;
            person.modelPlayer.rightPants.visible = false;
            person.modelPlayer.leftSleeve.visible = false;
            person.modelPlayer.rightSleeve.visible = false;
            person.modelPlayer.jacket.visible = false;
            person.modelPlayer.hat.visible = false;
            this.renderEntity(matrixStack, person.player, person.modelPlayer, person.getAlpha());
        });
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    @EventHandler
    private void onTotemPop(TotemEvent e) {
        if (e.getPlayer().equals(PopChams_WNWBvFQQYNjRmTHDKpkM.mc.player) || PopChams_WNWBvFQQYNjRmTHDKpkM.mc.world == null) {
            return;
        }
        PlayerEntity entity = new PlayerEntity(PopChams_WNWBvFQQYNjRmTHDKpkM.mc.world, BlockPos.ORIGIN, e.getPlayer().bodyYaw, new GameProfile(e.getPlayer().getUuid(), e.getPlayer().getName().getString())) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
        entity.copyPositionAndRotation(e.getPlayer());
        entity.bodyYaw = e.getPlayer().bodyYaw;
        entity.headYaw = e.getPlayer().headYaw;
        entity.handSwingProgress = e.getPlayer().handSwingProgress;
        entity.handSwingTicks = e.getPlayer().handSwingTicks;
        entity.setSneaking(e.getPlayer().isSneaking());
        entity.limbAnimator.setSpeed(e.getPlayer().limbAnimator.getSpeed());
        entity.limbAnimator.pos = e.getPlayer().limbAnimator.getPos();
        this.popList.add(new PopChams(entity));
    }

    private void renderEntity(MatrixStack matrices, LivingEntity entity, BipedEntityModel<PlayerEntity> modelBase, int alpha) {
        double x = entity.getX() - PopChams_WNWBvFQQYNjRmTHDKpkM.mc.getEntityRenderDispatcher().camera.getPos().getX();
        double y = entity.getY() - PopChams_WNWBvFQQYNjRmTHDKpkM.mc.getEntityRenderDispatcher().camera.getPos().getY();
        double z = entity.getZ() - PopChams_WNWBvFQQYNjRmTHDKpkM.mc.getEntityRenderDispatcher().camera.getPos().getZ();
        matrices.push();
        matrices.translate((float)x, (float)y, (float)z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(MathUtil.rad(180.0f - entity.bodyYaw)));
        PopChams_WNWBvFQQYNjRmTHDKpkM.prepareScale(matrices);
        modelBase.animateModel((PlayerEntity) entity, entity.limbAnimator.getPos(), entity.limbAnimator.getSpeed(), mc.getTickDelta());
        modelBase.setAngles((PlayerEntity) entity, entity.limbAnimator.getPos(), entity.limbAnimator.getSpeed(), (float)entity.age, entity.headYaw - entity.bodyYaw, entity.getPitch());
        RenderSystem.enableBlend();
        GL11.glDisable(2929);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        modelBase.render(matrices, buffer, 10, 0, (float)this.color.getValue().getRed() / 255.0f, (float)this.color.getValue().getGreen() / 255.0f, (float)this.color.getValue().getBlue() / 255.0f, (float)alpha / 255.0f);
        tessellator.draw();
        RenderSystem.disableBlend();
        GL11.glEnable(2929);
        matrices.pop();
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public class PopChams {
        private final PlayerEntity player;
        private final PlayerEntityModel<PlayerEntity> modelPlayer;
        private int alpha;

        public PopChams( PlayerEntity player) {
            this.player = player;
            this.modelPlayer = new PlayerEntityModel(new EntityRendererFactory.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderManager(), mc.getEntityRenderDispatcher().getHeldItemRenderer(), mc.getResourceManager(), mc.getEntityModelLoader(), mc.textRenderer).getPart(EntityModelLayers.PLAYER), false);
            this.modelPlayer.getHead().scale(new Vector3f(-0.3f, -0.3f, -0.3f));
            this.alpha = color.getValue().getAlpha();
        }

        public void update(CopyOnWriteArrayList<PopChams> arrayList) {
            if (this.alpha <= 0) {
                arrayList.remove(this);
                this.player.kill();
                this.player.remove(Entity.RemovalReason.KILLED);
                this.player.onRemoved();
                return;
            }
            this.alpha = (int)(AnimateUtil.animate(this.alpha, 0.0, alphaSpeed.getValue()) - 0.2);
        }

        public int getAlpha() {
            return (int)MathUtil.clamp(this.alpha, 0.0f, 255.0f);
        }
    }
}
