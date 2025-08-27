package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CrystalPlaceESP;
import me.hextech.remapped.CrystalPlaceESP_KzUakBpdzbLUIKutBXtY;
import me.hextech.remapped.CrystalPlaceESP_cIUDDoAQRmgkqQqHhomK;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final ConcurrentHashMap<EndCrystalEntity, CrystalPlaceESP> cryList = new ConcurrentHashMap();
    private final Timer timer = new Timer();
    BooleanSetting range = this.add(new BooleanSetting("Check Range", true)).setParent();
    SliderSetting rangeValue = this.add(new SliderSetting("Range", 12, 0, 256, v -> this.range.getValue()));
    ColorSetting color = this.add(new ColorSetting("Color ", new Color(255, 255, 255, 150)));
    SliderSetting animationTime = this.add(new SliderSetting("AnimationTime ", 500, 0, 1500));
    SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 500.0, 0.0, 1500.0, 0.1));
    SliderSetting upSpeed = this.add(new SliderSetting("UpSpeed", 1500.0, 0.0, 3000.0, 0.1));
    EnumSetting mode = this.add(new EnumSetting<CrystalPlaceESP_KzUakBpdzbLUIKutBXtY>("Mode", CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.Normal));
    SliderSetting pointsNew = this.add(new SliderSetting("Points", 3, 1, 10, v -> this.mode.getValue() == CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.Normal));
    SliderSetting interval = this.add(new SliderSetting("Interval ", 2, 1, 100, v -> this.mode.getValue() == CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.New));

    public CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI() {
        super("CrystalPlaceESP", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    public static void drawCircle3D(MatrixStack stack, Entity ent, float radius, float height, float up, Color color) {
        Render3DUtil.setupRender();
        GL11.glDisable((int)2929);
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.setShader(GameRenderer::method_34540);
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_29345, VertexFormats.field_1576);
        GL11.glLineWidth((float)2.0f);
        double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_1488() - CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI.mc.method_1561().field_4686.method_19326().method_10216();
        double y = ent.field_6036 + (double)height + (ent.method_23318() - ent.field_6036) * (double)mc.method_1488() - CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI.mc.method_1561().field_4686.method_19326().method_10214();
        double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_1488() - CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI.mc.method_1561().field_4686.method_19326().method_10215();
        stack.method_22903();
        stack.method_22904(x, y, z);
        Matrix4f matrix = stack.method_23760().method_23761();
        for (int i = 0; i <= 180; ++i) {
            bufferBuilder.method_22918(matrix, (float)((double)radius * Math.cos((double)i * 6.28 / 45.0)), up, (float)((double)radius * Math.sin((double)i * 6.28 / 45.0))).method_39415(color.getRGB()).method_1344();
        }
        tessellator.method_1350();
        Render3DUtil.endRender();
        stack.method_22904(-x, -y + (double)height, -z);
        GL11.glEnable((int)2929);
        stack.method_22909();
    }

    @Override
    @EventHandler
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        for (Entity e2 : new CrystalPlaceESP_cIUDDoAQRmgkqQqHhomK(this)) {
            if (!(e2 instanceof EndCrystalEntity) || this.range.getValue() && (double)CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI.mc.field_1724.method_5739(e2) > this.rangeValue.getValue() || this.cryList.containsKey(e2)) continue;
            this.cryList.put((EndCrystalEntity)e2, new CrystalPlaceESP((EndCrystalEntity)e2, System.currentTimeMillis()));
        }
        if (((Enum)this.mode.getValue()).equals((Object)CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.Normal)) {
            this.cryList.forEach((e, renderInfo) -> this.draw(matrixStack, renderInfo.entity, renderInfo.time, renderInfo.time));
        } else if (((Enum)this.mode.getValue()).equals((Object)CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.New)) {
            int time = 0;
            int i = 0;
            while ((double)i < this.pointsNew.getValue()) {
                if (this.timer.passedMs(500L)) {
                    int finalTime = time;
                    this.cryList.forEach((e, renderInfo) -> this.draw(matrixStack, renderInfo.entity, renderInfo.time - (long)finalTime, renderInfo.time - (long)finalTime));
                }
                time = (int)((double)time + this.interval.getValue());
                ++i;
            }
        }
        this.cryList.forEach((e, renderInfo) -> {
            if ((double)(System.currentTimeMillis() - renderInfo.time) > this.animationTime.getValue() && !e.method_5805()) {
                this.cryList.remove(e);
            }
            if ((double)(System.currentTimeMillis() - renderInfo.time) > this.animationTime.getValue() && (double)CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI.mc.field_1724.method_5739((Entity)e) > this.rangeValue.getValue()) {
                this.cryList.remove(e);
            }
        });
    }

    private void draw(MatrixStack matrixStack, EndCrystalEntity entity, long radTime, long heightTime) {
        long rad = System.currentTimeMillis() - radTime;
        long height = System.currentTimeMillis() - heightTime;
        if ((double)rad <= this.animationTime.getValue()) {
            CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI.drawCircle3D(matrixStack, (Entity)entity, (float)rad / this.fadeSpeed.getValueFloat(), (float)height / 1000.0f, (float)rad / this.upSpeed.getValueFloat(), this.color.getValue());
        }
    }

    @Override
    public void onDisable() {
        this.cryList.clear();
    }
}
