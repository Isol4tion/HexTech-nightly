package me.hextech.mod.modules.impl.render.sky;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import net.fabricmc.fabric.impl.client.rendering.DimensionRenderingRegistryImpl;
import net.minecraft.world.World;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;

public class Skybox
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final CustomSkyRenderer_zKYRFtdqYKdqrQYfieOq skyRenderer = new CustomSkyRenderer_zKYRFtdqYKdqrQYfieOq();
    public static Skybox INSTANCE;
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(0.77f, 0.31f, 0.73f)));
    public final ColorSetting color2 = this.add(new ColorSetting("Color2", new Color(0.77f, 0.31f, 0.73f)));
    public final ColorSetting color3 = this.add(new ColorSetting("Color3", new Color(0.77f, 0.31f, 0.73f)));
    public final ColorSetting color4 = this.add(new ColorSetting("Color4", new Color(0.77f, 0.31f, 0.73f)));
    public final ColorSetting color5 = this.add(new ColorSetting("Color5", new Color(255, 255, 255)));
    final BooleanSetting stars = this.add(new BooleanSetting("Stars", true));

    public Skybox() {
        super("Skybox", "Custom skybox", Category.Render);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        try {
            Field field = DimensionRenderingRegistryImpl.class.getDeclaredField("SKY_RENDERERS");
            field.setAccessible(true);
            Map SKY_RENDERERS = (Map)field.get(null);
            SKY_RENDERERS.putIfAbsent(World.OVERWORLD, skyRenderer);
            SKY_RENDERERS.putIfAbsent(World.NETHER, skyRenderer);
            SKY_RENDERERS.putIfAbsent(World.END, skyRenderer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            Field field = DimensionRenderingRegistryImpl.class.getDeclaredField("SKY_RENDERERS");
            field.setAccessible(true);
            Map SKY_RENDERERS = (Map)field.get(null);
            SKY_RENDERERS.remove(World.OVERWORLD, skyRenderer);
            SKY_RENDERERS.remove(World.NETHER, skyRenderer);
            SKY_RENDERERS.remove(World.END, skyRenderer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
