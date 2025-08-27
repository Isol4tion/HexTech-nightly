package me.hextech.remapped;

public class AnimateUtil implements Wrapper {
   public static double animate(double current, double endPoint, double speed) {
      if (speed >= 1.0) {
         return endPoint;
      } else {
         return speed == 0.0 ? current : thunder(current, endPoint, speed);
      }
   }

   public static double animate(double current, double endPoint, double speed, AnimateUtil_AcLZzRdHWZkNeKEYTOwI mode) {
      switch (mode) {
         case Thunder:
            return thunder(current, endPoint, speed);
         case Mio:
            return mio(current, endPoint, speed);
         case My:
            return my(current, endPoint, speed);
         case Old:
            return old(current, endPoint, speed);
         case Normal:
            return normal(current, endPoint, speed);
         default:
            return endPoint;
      }
   }

   public static double mio(double current, double endPoint, double speed) {
      if (Math.max(endPoint, current) - Math.min(endPoint, current) < 0.001) {
         return endPoint;
      } else {
         int negative = speed < 0.0 ? -1 : 1;
         if (negative == -1) {
            speed *= -1.0;
         }

         double diff = endPoint - current;
         double factor = diff * (double)mc.method_1488() / (1.0 / speed * (Math.min(240.0, (double)me.hextech.HexTech.FPS.getFps()) / 240.0));
         if (diff < 0.0 && factor < diff) {
            factor = diff;
         } else if (diff > 0.0 && factor >= diff) {
            factor = diff;
         }

         return current + factor * (double)negative;
      }
   }

   public static double old(double current, double endPoint, double speed) {
      if (Math.max(endPoint, current) - Math.min(endPoint, current) < 0.001) {
         return endPoint;
      } else {
         int negative = speed < 0.0 ? -1 : 1;
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
   }

   public static double my(double current, double endPoint, double speed) {
      if (Math.max(endPoint, current) - Math.min(endPoint, current) < 0.001) {
         return endPoint;
      } else {
         int negative = speed < 0.0 ? -1 : 1;
         if (negative == -1) {
            speed *= -1.0;
         }

         double diff = endPoint - current;
         double factor = diff * (double)mc.method_1488() * speed;
         if (diff < 0.0 && factor < diff) {
            factor = diff;
         } else if (diff > 0.0 && factor >= diff) {
            factor = diff;
         }

         return current + factor * (double)negative;
      }
   }

   public static double thunder(double current, double endPoint, double speed) {
      boolean shouldContinueAnimation = endPoint > current;
      double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
      if (Math.abs(dif) <= 0.001) {
         return endPoint;
      } else {
         double factor = dif * speed;
         return current + (shouldContinueAnimation ? factor : -factor);
      }
   }

   public static double normal(double current, double endPoint, double speed) {
      boolean shouldContinueAnimation = endPoint > current;
      speed *= 10.0;
      return Math.abs(Math.max(endPoint, current) - Math.min(endPoint, current)) <= speed ? endPoint : current + (shouldContinueAnimation ? speed : -speed);
   }
}
