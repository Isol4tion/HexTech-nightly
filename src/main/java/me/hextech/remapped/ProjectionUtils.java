package me.hextech.remapped;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ProjectionUtils {
    public static Vec3d worldToScreen(Vec3d destination) {
        MinecraftClient client = MinecraftClient.getInstance();
        GameRenderer renderer = client.gameRenderer;
        Camera camera = renderer.getCamera();
        Vec3d position = camera.getPos();
        Quaternionf rotation = camera.getRotation();
        Vector3f calculation = rotation.conjugate().transform(position.subtract(destination).toVector3f());
        Integer fov = client.options.getFov().getValue();
        int half = client.getWindow().getScaledHeight() / 2;
        double scale = (double)half / ((double)calculation.z() * Math.tan(Math.toRadians(fov / 2)));
        return new Vec3d((double)calculation.x() * scale, (double)calculation.y() * scale, calculation.z());
    }
}
