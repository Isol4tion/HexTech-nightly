package me.hextech.remapped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map$Entry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class MathUtil {
   public static Direction getFacingOrder(float yaw, float pitch) {
      float f = pitch * (float) (Math.PI / 180.0);
      float g = -yaw * (float) (Math.PI / 180.0);
      float h = MathHelper.method_15374(f);
      float i = MathHelper.method_15362(f);
      float j = MathHelper.method_15374(g);
      float k = MathHelper.method_15362(g);
      boolean bl = j > 0.0F;
      boolean bl2 = h < 0.0F;
      boolean bl3 = k > 0.0F;
      float l = bl ? j : -j;
      float m = bl2 ? -h : h;
      float n = bl3 ? k : -k;
      float o = l * i;
      float p = n * i;
      Direction direction = bl ? Direction.field_11034 : Direction.field_11039;
      Direction direction2 = bl2 ? Direction.field_11036 : Direction.field_11033;
      Direction direction3 = bl3 ? Direction.field_11035 : Direction.field_11043;
      if (l > n) {
         return m > o ? direction2 : direction;
      } else {
         return m > p ? direction2 : direction3;
      }
   }

   public static float clamp(float num, float min, float max) {
      return num < min ? min : Math.min(num, max);
   }

   public static double clamp(double value, double min, double max) {
      return value < min ? min : Math.min(value, max);
   }

   public static double square(double input) {
      return input * input;
   }

   public static float random(float min, float max) {
      return (float)(Math.random() * (double)(max - min) + (double)min);
   }

   public static double random(double min, double max) {
      return (double)((float)(Math.random() * (max - min) + min));
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
      } else {
         BigDecimal bd = BigDecimal.valueOf((double)value);
         bd = bd.setScale(places, RoundingMode.FLOOR);
         return bd.floatValue();
      }
   }

   public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
      LinkedList<Map$Entry<K, V>> list = new LinkedList(map.entrySet());
      if (descending) {
         list.sort(Map$Entry.comparingByValue(Comparator.reverseOrder()));
      } else {
         list.sort(Map$Entry.comparingByValue());
      }

      LinkedHashMap result = new LinkedHashMap();

      for (Map$Entry entry : list) {
         result.put(entry.getKey(), entry.getValue());
      }

      return result;
   }

   public static Direction getDirectionFromEntityLiving(BlockPos pos, LivingEntity entity) {
      if (Math.abs(entity.method_23317() - ((double)pos.method_10263() + 0.5)) < 2.0
         && Math.abs(entity.method_23321() - ((double)pos.method_10260() + 0.5)) < 2.0) {
         double d0 = entity.method_23318() + (double)entity.method_18381(entity.method_18376());
         if (d0 - (double)pos.method_10264() > 2.0) {
            return Direction.field_11036;
         }

         if ((double)pos.method_10264() - d0 > 0.0) {
            return Direction.field_11033;
         }
      }

      return entity.method_5735().method_10153();
   }
}
