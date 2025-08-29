package me.hextech.api.utils.render;

import java.awt.*;

public class ColorUtil {
    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static Color injectAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static int injectAlpha(int color, int alpha) {
        return ColorUtil.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
    }

    public static Color pulseColor(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (float) index / (float) count * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * brightness;
        hsb[2] = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color pulseColor(Color startColor, Color endColor, int index, int count, double speed) {
        double brightness = Math.abs(((double) System.currentTimeMillis() * speed % 2000.0 / (double) Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (double) index / (double) count * (double) Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % (double) Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - (double) Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        double quad = brightness % (double) Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return ColorUtil.fadeColor(startColor, endColor, quad);
    }

    public static Color fadeColor(Color startColor, Color endColor, double quad) {
        quad = Math.min(Math.max(quad, 0.0), 1.0);
        int sR = startColor.getRed();
        int sG = startColor.getGreen();
        int sB = startColor.getBlue();
        int sA = startColor.getAlpha();
        int eR = endColor.getRed();
        int eG = endColor.getGreen();
        int eB = endColor.getBlue();
        int eA = endColor.getAlpha();
        return new Color(Math.min((int) ((double) sR + (double) (eR - sR) * quad), 255), Math.min((int) ((double) sG + (double) (eG - sG) * quad), 255), Math.min((int) ((double) sB + (double) (eB - sB) * quad), 255), Math.min((int) ((double) sA + (double) (eA - sA) * quad), 255));
    }
}
