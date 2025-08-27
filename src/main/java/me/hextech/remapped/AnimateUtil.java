package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.AnimateUtil_AcLZzRdHWZkNeKEYTOwI;
import me.hextech.remapped.Wrapper;

public class AnimateUtil
implements Wrapper {
    public static double animate(double current, double endPoint, double speed) {
        if (speed >= 1.0) {
            return endPoint;
        }
        if (speed == 0.0) {
            return current;
        }
        return AnimateUtil.thunder(current, endPoint, speed);
    }

    public static double animate(double current, double endPoint, double speed, AnimateUtil_AcLZzRdHWZkNeKEYTOwI mode) {
        switch (mode.ordinal()) {
            case 1: {
                return AnimateUtil.mio(current, endPoint, speed);
            }
            case 0: {
                return AnimateUtil.thunder(current, endPoint, speed);
            }
            case 2: {
                return AnimateUtil.my(current, endPoint, speed);
            }
            case 3: {
                return AnimateUtil.old(current, endPoint, speed);
            }
            case 4: {
                return AnimateUtil.normal(current, endPoint, speed);
            }
        }
        return endPoint;
    }

    public static double mio(double current, double endPoint, double speed) {
        int negative;
        if (Math.max(endPoint, current) - Math.min(endPoint, current) < 0.001) {
            return endPoint;
        }
        int n = negative = speed < 0.0 ? -1 : 1;
        if (negative == -1) {
            speed *= -1.0;
        }
        double diff = endPoint - current;
        double factor = diff * (double)mc.getTickDelta() / (1.0 / speed * (Math.min(240.0, (double)HexTech.FPS.getFps()) / 240.0));
        if (diff < 0.0 && factor < diff) {
            factor = diff;
        } else if (diff > 0.0 && factor >= diff) {
            factor = diff;
        }
        return current + factor * (double)negative;
    }

    public static double old(double current, double endPoint, double speed) {
        int negative;
        if (Math.max(endPoint, current) - Math.min(endPoint, current) < 0.001) {
            return endPoint;
        }
        int n = negative = speed < 0.0 ? -1 : 1;
        if (negative == -1) {
            speed *= -1.0;
        }
        double diff = endPoint - current;
        double factor = diff * speed;
        if (diff < 0.0 && factor < diff) {
            factor = diff;
        } else if (diff > 0.0 && factor >= diff) {
            factor = diff;
        }
        return current + factor * (double)negative;
    }

    public static double my(double current, double endPoint, double speed) {
        int negative;
        if (Math.max(endPoint, current) - Math.min(endPoint, current) < 0.001) {
            return endPoint;
        }
        int n = negative = speed < 0.0 ? -1 : 1;
        if (negative == -1) {
            speed *= -1.0;
        }
        double diff = endPoint - current;
        double factor = diff * (double)mc.getTickDelta() * speed;
        if (diff < 0.0 && factor < diff) {
            factor = diff;
        } else if (diff > 0.0 && factor >= diff) {
            factor = diff;
        }
        return current + factor * (double)negative;
    }

    public static double thunder(double current, double endPoint, double speed) {
        boolean shouldContinueAnimation = endPoint > current;
        double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        if (Math.abs(dif) <= 0.001) {
            return endPoint;
        }
        double factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : -factor);
    }

    public static double normal(double current, double endPoint, double speed) {
        boolean shouldContinueAnimation = endPoint > current;
        speed *= 10.0;
        if (Math.abs(Math.max(endPoint, current) - Math.min(endPoint, current)) <= speed) {
            return endPoint;
        }
        return current + (shouldContinueAnimation ? speed : -speed);
    }
}
