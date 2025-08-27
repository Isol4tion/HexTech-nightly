package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.render.LightmapTextureManager;

public class WeatherRenderEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final WeatherRenderEvent INSTANCE;
    public LightmapTextureManager lightmapTextureManager;
    public float tickDelta;
    public double cameraX;
    public double cameraY;
    public double cameraZ;

    public WeatherRenderEvent(Event stage) {
        super(stage);
        INSTANCE = new WeatherRenderEvent(stage);
    }

    public static WeatherRenderEvent get(LightmapTextureManager lightmapTextureManager, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        INSTANCE.setCancelled(false);
        WeatherRenderEvent.INSTANCE.lightmapTextureManager = lightmapTextureManager;
        WeatherRenderEvent.INSTANCE.tickDelta = tickDelta;
        WeatherRenderEvent.INSTANCE.cameraX = cameraX;
        WeatherRenderEvent.INSTANCE.cameraY = cameraY;
        WeatherRenderEvent.INSTANCE.cameraZ = cameraZ;
        return INSTANCE;
    }
}
