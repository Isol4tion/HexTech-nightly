package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PopChams;
import me.hextech.remapped.PopChams_YempjTMivfvNUIHSOfBB;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.TotemEvent;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
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
        matrixStack.method_22905(-1.0f, -1.0f, 1.0f);
        matrixStack.method_22905(1.6f, 1.8f, 1.6f);
        matrixStack.method_46416(0.0f, -1.501f, 0.0f);
    }

    @Override
    public void onUpdate() {
        this.popList.forEach(person -> person.update(this.popList));
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        this.popList.forEach(person -> {
            person.modelPlayer.field_3482.field_3665 = false;
            person.modelPlayer.field_3479.field_3665 = false;
            person.modelPlayer.field_3484.field_3665 = false;
            person.modelPlayer.field_3486.field_3665 = false;
            person.modelPlayer.field_3483.field_3665 = false;
            person.modelPlayer.field_3394.field_3665 = false;
            this.renderEntity(matrixStack, (LivingEntity)person.player, (BipedEntityModel<PlayerEntity>)person.modelPlayer, person.getAlpha());
        });
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
    }

    @EventHandler
    private void onTotemPop(TotemEvent e) {
        if (e.getPlayer().equals((Object)PopChams_WNWBvFQQYNjRmTHDKpkM.mc.field_1724) || PopChams_WNWBvFQQYNjRmTHDKpkM.mc.field_1687 == null) {
            return;
        }
        PopChams_YempjTMivfvNUIHSOfBB entity = new PopChams_YempjTMivfvNUIHSOfBB(this, (World)PopChams_WNWBvFQQYNjRmTHDKpkM.mc.field_1687, BlockPos.field_10980, e.getPlayer().field_6283, new GameProfile(e.getPlayer().method_5667(), e.getPlayer().method_5477().getString()));
        entity.method_5719((Entity)e.getPlayer());
        entity.field_6283 = e.getPlayer().field_6283;
        entity.field_6241 = e.getPlayer().field_6241;
        entity.field_6251 = e.getPlayer().field_6251;
        entity.field_6279 = e.getPlayer().field_6279;
        entity.method_5660(e.getPlayer().method_5715());
        entity.field_42108.method_48567(e.getPlayer().field_42108.method_48566());
        entity.field_42108.field_42111 = e.getPlayer().field_42108.method_48569();
        this.popList.add(new PopChams(this, entity));
    }

    private void renderEntity(MatrixStack matrices, LivingEntity entity, BipedEntityModel<PlayerEntity> modelBase, int alpha) {
        double x = entity.method_23317() - PopChams_WNWBvFQQYNjRmTHDKpkM.mc.method_1561().field_4686.method_19326().method_10216();
        double y = entity.method_23318() - PopChams_WNWBvFQQYNjRmTHDKpkM.mc.method_1561().field_4686.method_19326().method_10214();
        double z = entity.method_23321() - PopChams_WNWBvFQQYNjRmTHDKpkM.mc.method_1561().field_4686.method_19326().method_10215();
        matrices.method_22903();
        matrices.method_46416((float)x, (float)y, (float)z);
        matrices.method_22907(RotationAxis.field_40716.rotation(MathUtil.rad(180.0f - entity.field_6283)));
        PopChams_WNWBvFQQYNjRmTHDKpkM.prepareScale(matrices);
        modelBase.method_17086((LivingEntity)((PlayerEntity)entity), entity.field_42108.method_48569(), entity.field_42108.method_48566(), mc.method_1488());
        modelBase.method_17087((LivingEntity)((PlayerEntity)entity), entity.field_42108.method_48569(), entity.field_42108.method_48566(), (float)entity.field_6012, entity.field_6241 - entity.field_6283, entity.method_36455());
        RenderSystem.enableBlend();
        GL11.glDisable((int)2929);
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ONE, (GlStateManager.DstFactor)GlStateManager.DstFactor.ZERO);
        RenderSystem.setShader(GameRenderer::method_34540);
        buffer.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1576);
        modelBase.method_2828(matrices, (VertexConsumer)buffer, 10, 0, (float)this.color.getValue().getRed() / 255.0f, (float)this.color.getValue().getGreen() / 255.0f, (float)this.color.getValue().getBlue() / 255.0f, (float)alpha / 255.0f);
        tessellator.method_1350();
        RenderSystem.disableBlend();
        GL11.glEnable((int)2929);
        matrices.method_22909();
    }
}
