package me.hextech.remapped;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer;
import net.fabricmc.fabric.impl.client.rendering.DimensionRenderingRegistryImpl;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

@Beta
public class Skybox extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static final CustomSkyRenderer_zKYRFtdqYKdqrQYfieOq skyRenderer;
   public static Skybox INSTANCE;
   public final ColorSetting color = this.add(new ColorSetting("Color", new Color(0.77F, 0.31F, 0.73F)));
   public final ColorSetting color2 = this.add(new ColorSetting("Color2", new Color(0.77F, 0.31F, 0.73F)));
   public final ColorSetting color3 = this.add(new ColorSetting("Color3", new Color(0.77F, 0.31F, 0.73F)));
   public final ColorSetting color4 = this.add(new ColorSetting("Color4", new Color(0.77F, 0.31F, 0.73F)));
   public final ColorSetting color5 = this.add(new ColorSetting("Color5", new Color(255, 255, 255)));
   final BooleanSetting stars = this.add(new BooleanSetting("Stars", true));

   public Skybox() {
      super("Skybox", "Custom skybox", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   @Override
   public void onEnable() {
      try {
         Field field = DimensionRenderingRegistryImpl.class.getDeclaredField("SKY_RENDERERS");
         field.setAccessible(true);
         Map<RegistryKey<World>, SkyRenderer> SKY_RENDERERS = (Map<RegistryKey<World>, SkyRenderer>)field.get(null);
         SKY_RENDERERS.putIfAbsent(World.field_25179, skyRenderer);
         SKY_RENDERERS.putIfAbsent(World.field_25180, skyRenderer);
         SKY_RENDERERS.putIfAbsent(World.field_25181, skyRenderer);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void onDisable() {
      try {
         Field field = DimensionRenderingRegistryImpl.class.getDeclaredField("SKY_RENDERERS");
         field.setAccessible(true);
         Map<RegistryKey<World>, SkyRenderer> SKY_RENDERERS = (Map<RegistryKey<World>, SkyRenderer>)field.get(null);
         SKY_RENDERERS.remove(World.field_25179, skyRenderer);
         SKY_RENDERERS.remove(World.field_25180, skyRenderer);
         SKY_RENDERERS.remove(World.field_25181, skyRenderer);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
