package me.hextech.remapped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class MathUtil {
    public static Direction getFacingOrder(float yaw, float pitch) {
        Direction direction3;
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = MathHelper.sin((float)f);
        float i = MathHelper.cos((float)f);
        float j = MathHelper.sin((float)g);
        float k = MathHelper.cos((float)g);
        boolean bl = j > 0.0f;
        boolean bl2 = h < 0.0f;
        boolean bl3 = k > 0.0f;
        float l = bl ? j : -j;
        float m = bl2 ? -h : h;
        float n = bl3 ? k : -k;
        float o = l * i;
        float p = n * i;
        Direction direction = bl ? Direction.EAST : Direction.WEST;
        Direction direction2 = bl2 ? Direction.UP : Direction.DOWN;
        Direction direction4 = direction3 = bl3 ? Direction.SOUTH : Direction.NORTH;
        if (l > n) {
            if (m > o) {
                return direction2;
            }
            return direction;
        }
        if (m > p) {
            return direction2;
        }
        return direction3;
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static double square(double input) {
        return input * input;
    }

    public static float random(float min, float max) {
        return (float)(Math.random() * (double)(max - min) + (double)min);
    }

    public static double random(double min, double max) {
        return (float)(Math.random() * (max - min) + min);
    }

    public static float rad(float angle) {
        return (float)((double)angle * Math.PI / 180.0);
    }

    public static double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    public static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.floatValue();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        if (descending) {
            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        } else {
            list.sort(Map.Entry.comparingByValue());
        }
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Direction getDirectionFromEntityLiving(BlockPos pos, LivingEntity entity) {
        if (Math.abs(entity.getX() - ((double)pos.getX() + 0.5)) < 2.0 && Math.abs(entity.getZ() - ((double)pos.getZ() + 0.5)) < 2.0) {
            double d0 = entity.getY() + (double)entity.method_18381(entity.method_18376());
            if (d0 - (double)pos.getY() > 2.0) {
                return Direction.UP;
            }
            if ((double)pos.getY() - d0 > 0.0) {
                return Direction.DOWN;
            }
        }
        return entity.method_5735().getOpposite();
    }
}
