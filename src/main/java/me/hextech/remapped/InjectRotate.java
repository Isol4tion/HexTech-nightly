package me.hextech.remapped;

import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import net.minecraft.util.math.MathHelper;

public class InjectRotate {
    public static float[] injectStep(float[] angle, float steps) {
        if (steps < 0.01f) {
            steps = 0.01f;
        }
        if (steps > 1.0f) {
            steps = 1.0f;
        }
        if (steps < 1.0f && angle != null) {
            float packetPitch;
            float packetYaw = AutoCrystal_QcRVYRsOqpKivetoXSJa.lastYaw;
            float diff = MathHelper.angleBetween(angle[0], packetYaw);
            if (Math.abs(diff) > 180.0f * steps) {
                angle[0] = packetYaw + diff * (180.0f * steps / Math.abs(diff));
            }
            if (Math.abs(diff = angle[1] - (packetPitch = AutoCrystal_QcRVYRsOqpKivetoXSJa.lastPitch)) > 90.0f * steps) {
                angle[1] = packetPitch + diff * (90.0f * steps / Math.abs(diff));
            }
        }
        return new float[]{angle[0], angle[1]};
    }
}
