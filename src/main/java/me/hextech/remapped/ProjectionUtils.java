package me.hextech.remapped;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ProjectionUtils {
   public static Vec3d worldToScreen(Vec3d destination) {
      MinecraftClient client = MinecraftClient.method_1551();
      GameRenderer renderer = client.field_1773;
      Camera camera = renderer.method_19418();
      Vec3d position = camera.method_19326();
      Quaternionf rotation = camera.method_23767();
      Vector3f calculation = rotation.conjugate().transform(position.method_1020(destination).method_46409());
      Integer fov = (Integer)client.field_1690.method_41808().method_41753();
      int half = client.method_22683().method_4502() / 2;
      double scale = (double)half / ((double)calculation.z() * Math.tan(Math.toRadians((double)(fov / 2))));
      return new Vec3d((double)calculation.x() * scale, (double)calculation.y() * scale, (double)calculation.z());
   }
}
