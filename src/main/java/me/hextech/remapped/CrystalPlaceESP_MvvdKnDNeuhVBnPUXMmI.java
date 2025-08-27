package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final ConcurrentHashMap<EndCrystalEntity, CrystalPlaceESP> cryList = new ConcurrentHashMap();
   private final Timer timer = new Timer();
   BooleanSetting range = this.add(new BooleanSetting("Check Range", true)).setParent();
   SliderSetting rangeValue = this.add(new SliderSetting("Range", 12, 0, 256, v -> this.range.getValue()));
   ColorSetting color = this.add(new ColorSetting("Color ", new Color(255, 255, 255, 150)));
   SliderSetting animationTime = this.add(new SliderSetting("AnimationTime ", 500, 0, 1500));
   SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 500.0, 0.0, 1500.0, 0.1));
   SliderSetting upSpeed = this.add(new SliderSetting("UpSpeed", 1500.0, 0.0, 3000.0, 0.1));
   EnumSetting mode = this.add(new EnumSetting("Mode", CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.Normal));
   SliderSetting pointsNew = this.add(new SliderSetting("Points", 3, 1, 10, v -> this.mode.getValue() == CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.Normal));
   SliderSetting interval = this.add(new SliderSetting("Interval ", 2, 1, 100, v -> this.mode.getValue() == CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.New));

   public CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI() {
      super("CrystalPlaceESP", Module_JlagirAibYQgkHtbRnhw.Render);
   }

   public static void drawCircle3D(MatrixStack stack, Entity ent, float radius, float height, float up, Color color) {
      Render3DUtil.setupRender();
      GL11.glDisable(2929);
      Tessellator tessellator = Tessellator.method_1348();
      BufferBuilder bufferBuilder = tessellator.method_1349();
      RenderSystem.setShader(GameRenderer::method_34540);
      bufferBuilder.method_1328(DrawMode.field_29345, VertexFormats.field_1576);
      GL11.glLineWidth(2.0F);
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_1488() - mc.method_1561().field_4686.method_19326().method_10216();
      double y = ent.field_6036
         + (double)height
         + (ent.method_23318() - ent.field_6036) * (double)mc.method_1488()
         - mc.method_1561().field_4686.method_19326().method_10214();
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_1488() - mc.method_1561().field_4686.method_19326().method_10215();
      stack.method_22903();
      stack.method_22904(x, y, z);
      Matrix4f matrix = stack.method_23760().method_23761();

      for (int i = 0; i <= 180; i++) {
         bufferBuilder.method_22918(
               matrix, (float)((double)radius * Math.cos((double)i * 6.28 / 45.0)), up, (float)((double)radius * Math.sin((double)i * 6.28 / 45.0))
            )
            .method_39415(color.getRGB())
            .method_1344();
      }

      tessellator.method_1350();
      Render3DUtil.endRender();
      stack.method_22904(-x, -y + (double)height, -z);
      GL11.glEnable(2929);
      stack.method_22909();
   }

   @EventHandler
   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      for (Entity e : new Iterable<Entity>() {
         public Iterator<Entity> iterator() {
            return Wrapper.mc.field_1687.method_18112().iterator();
         }
      }) {
         if (e instanceof EndCrystalEntity
            && (!this.range.getValue() || !((double)mc.field_1724.method_5739(e) > this.rangeValue.getValue()))
            && !this.cryList.containsKey(e)) {
            this.cryList.put((EndCrystalEntity)e, new CrystalPlaceESP((EndCrystalEntity)e, System.currentTimeMillis()));
         }
      }

      if (this.mode.getValue().equals(CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.Normal)) {
         this.cryList.forEach((ex, renderInfo) -> this.draw(matrixStack, renderInfo.entity, renderInfo.time, renderInfo.time));
      } else if (this.mode.getValue().equals(CrystalPlaceESP_KzUakBpdzbLUIKutBXtY.New)) {
         int time = 0;

         for (int i = 0; (double)i < this.pointsNew.getValue(); i++) {
            if (this.timer.passedMs(500L)) {
               int finalTime = time;
               this.cryList
                  .forEach((ex, renderInfo) -> this.draw(matrixStack, renderInfo.entity, renderInfo.time - (long)finalTime, renderInfo.time - (long)finalTime));
            }

            time = (int)((double)time + this.interval.getValue());
         }
      }

      this.cryList
         .forEach(
            (ex, renderInfo) -> {
               if ((double)(System.currentTimeMillis() - renderInfo.time) > this.animationTime.getValue() && !ex.method_5805()) {
                  this.cryList.remove(ex);
               }

               if ((double)(System.currentTimeMillis() - renderInfo.time) > this.animationTime.getValue()
                  && (double)mc.field_1724.method_5739(ex) > this.rangeValue.getValue()) {
                  this.cryList.remove(ex);
               }
            }
         );
   }

   private void draw(MatrixStack matrixStack, EndCrystalEntity entity, long radTime, long heightTime) {
      long rad = System.currentTimeMillis() - radTime;
      long height = System.currentTimeMillis() - heightTime;
      if ((double)rad <= this.animationTime.getValue()) {
         drawCircle3D(
            matrixStack,
            entity,
            (float)rad / this.fadeSpeed.getValueFloat(),
            (float)height / 1000.0F,
            (float)rad / this.upSpeed.getValueFloat(),
            this.color.getValue()
         );
      }
   }

   @Override
   public void onDisable() {
      this.cryList.clear();
   }
}
