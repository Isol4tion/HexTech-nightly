package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public final class ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn implements Wrapper {
   private static final Map<PlayerEntity, List<Vec3d>> motionHistory = new HashMap();

   public static void updateHistory() {
      if (mc.field_1687 != null) {
         Map<AbstractClientPlayerEntity, List<Vec3d>> newMap = new HashMap();

         for (AbstractClientPlayerEntity p : mc.field_1687.method_18456()) {
            List<Vec3d> list = (List<Vec3d>)motionHistory.computeIfAbsent(p, k -> new ArrayList());
            list.add(0, p.method_19538().method_1023(p.field_6014, p.field_6036, p.field_5969));
            if (list.size() > 20) {
               list.remove(20);
            }

            newMap.put(p, list);
         }

         motionHistory.keySet().retainAll(newMap.keySet());
      }
   }

   public static Box extrapolate(PlayerEntity p, int extrapTicks, int smoothTicks) {
      List<Vec3d> hist = (List<Vec3d>)motionHistory.get(p);
      if (hist != null && !hist.isEmpty()) {
         Vec3d motion = getAverageMotion(hist, smoothTicks);
         return simulate(p, motion, extrapTicks);
      } else {
         return p.method_5829();
      }
   }

   public static PlayerEntity createPredict(PlayerEntity p, int ticks, int smooth) {
      Box future = extrapolate(p, ticks, smooth);
      Vec3d center = new Vec3d((future.field_1323 + future.field_1320) / 2.0, future.field_1322, (future.field_1321 + future.field_1324) / 2.0);
      PlayerEntity fake = null;
      if (mc.field_1687 != null) {
         fake = new PlayerEntity(mc.field_1687, p.method_24515(), p.method_36454(), new GameProfile(UUID.randomUUID(), "Predict")) {
            public boolean method_7325() {
               return false;
            }

            public boolean method_7337() {
               return false;
            }
         };
      }

      fake.method_33574(center);
      fake.method_6033(p.method_6032());
      fake.method_24830(p.method_24828());
      fake.method_31548().method_7377(p.method_31548());
      p.method_6026().forEach(fake::method_6092);
      return fake;
   }

   private static Vec3d getAverageMotion(List<Vec3d> list, int max) {
      if (list.isEmpty()) {
         return new Vec3d(0.0, 0.0, 0.0);
      } else {
         int s = Math.min(list.size(), Math.max(1, max));
         Vec3d sum = new Vec3d(0.0, 0.0, 0.0);

         for (int i = 0; i < s; i++) {
            Vec3d v = (Vec3d)list.get(i);
            sum = sum.method_1031(v.field_1352, 0.0, v.field_1350);
         }

         return new Vec3d(sum.field_1352 / (double)s, ((Vec3d)list.get(0)).field_1351, sum.field_1350 / (double)s);
      }
   }

   private static Box simulate(PlayerEntity p, Vec3d motion, int ticks) {
      double x = motion.field_1352;
      double y = motion.field_1351;
      double z = motion.field_1350;
      Box box = p.method_5829();
      double stepHeight = 0.6;
      boolean onGround = inside(p, box.method_989(0.0, -0.04, 0.0));

      for (int i = 0; i < ticks; i++) {
         List<VoxelShape> collisions = null;
         if (mc.field_1687 != null) {
            collisions = mc.field_1687.method_20743(null, box.method_1012(x, y, z));
         }

         Vec3d adjusted = null;
         if (collisions != null) {
            adjusted = Entity.method_20736(null, new Vec3d(x, y, z), box, mc.field_1687, collisions);
         }

         boolean canStep = false;
         if (adjusted != null) {
            canStep = PredictionSetting.INSTANCE.step.getValue()
               && (onGround || y < 0.0 && adjusted.field_1351 != y)
               && (adjusted.field_1352 != x || adjusted.field_1350 != z);
         }

         if (canStep) {
            Vec3d step = Entity.method_20736(null, new Vec3d(x, stepHeight, z), box, mc.field_1687, collisions);
            Vec3d stepY = Entity.method_20736(null, new Vec3d(0.0, stepHeight, 0.0), box.method_1012(x, 0.0, z), mc.field_1687, collisions);
            if (stepY.field_1351 < stepHeight) {
               Vec3d comb = stepY.method_1019(Entity.method_20736(null, new Vec3d(x, 0.0, z), box.method_997(stepY), mc.field_1687, collisions));
               if (comb.method_37268() > step.method_37268()) {
                  step = comb;
               }
            }

            if (step.method_37268() > adjusted.method_37268()) {
               adjusted = step.method_1019(
                  Entity.method_20736(null, new Vec3d(0.0, -step.field_1351 + y, 0.0), box.method_997(step), mc.field_1687, collisions)
               );
            }
         }

         box = box.method_997(adjusted);
         onGround = inside(p, box.method_989(0.0, -0.04, 0.0));
         if (onGround) {
            y = 0.0;
         }

         y = (y - 0.08) * 0.98;
      }

      return box;
   }

   public static List<Vec3d> simulate(PlayerEntity p, int ticks) {
      List<Vec3d> path = new ArrayList(ticks + 1);
      Vec3d motion = getAverageMotion((List<Vec3d>)motionHistory.getOrDefault(p, Collections.emptyList()), PredictionSetting.INSTANCE.smoothTicks.getValueInt());
      Box box = p.method_5829();
      double stepHeight = 0.6;
      boolean onGround = inside(p, box.method_989(0.0, -0.04, 0.0));
      path.add(new Vec3d((box.field_1323 + box.field_1320) / 2.0, box.field_1322, (box.field_1321 + box.field_1324) / 2.0));
      double x = motion.field_1352;
      double y = motion.field_1351;
      double z = motion.field_1350;

      for (int i = 0; i < ticks; i++) {
         List<VoxelShape> collisions = null;
         if (mc.field_1687 != null) {
            collisions = mc.field_1687.method_20743(null, box.method_1012(x, y, z));
         }

         Vec3d adjusted = null;
         if (collisions != null) {
            adjusted = Entity.method_20736(null, new Vec3d(x, y, z), box, mc.field_1687, collisions);
         }

         boolean canStep = false;
         if (adjusted != null) {
            canStep = PredictionSetting.INSTANCE.step.getValue()
               && (onGround || y < 0.0 && adjusted.field_1351 != y)
               && (adjusted.field_1352 != x || adjusted.field_1350 != z);
         }

         if (canStep) {
            Vec3d step = Entity.method_20736(null, new Vec3d(x, stepHeight, z), box, mc.field_1687, collisions);
            Vec3d stepY = Entity.method_20736(null, new Vec3d(0.0, stepHeight, 0.0), box.method_1012(x, 0.0, z), mc.field_1687, collisions);
            if (stepY.field_1351 < stepHeight) {
               Vec3d comb = stepY.method_1019(Entity.method_20736(null, new Vec3d(x, 0.0, z), box.method_997(stepY), mc.field_1687, collisions));
               if (comb.method_37268() > step.method_37268()) {
                  step = comb;
               }
            }

            if (step.method_37268() > adjusted.method_37268()) {
               adjusted = step.method_1019(
                  Entity.method_20736(null, new Vec3d(0.0, -step.field_1351 + y, 0.0), box.method_997(step), mc.field_1687, collisions)
               );
            }
         }

         box = box.method_997(adjusted);
         onGround = inside(p, box.method_989(0.0, -0.04, 0.0));
         if (onGround) {
            y = 0.0;
         }

         y = (y - 0.08) * 0.98;
         path.add(new Vec3d((box.field_1323 + box.field_1320) / 2.0, box.field_1322, (box.field_1321 + box.field_1324) / 2.0));
      }

      return path;
   }

   public static PlayerEntity createSelfPredict(PlayerEntity p, int ticks) {
      Box future = extrapolate(p, ticks, PredictionSetting.INSTANCE.smoothTicks.getValueInt());
      Vec3d center = new Vec3d((future.field_1323 + future.field_1320) / 2.0, future.field_1322, (future.field_1321 + future.field_1324) / 2.0);
      PlayerEntity fake = new PlayerEntity(mc.field_1687, p.method_24515(), p.method_36454(), new GameProfile(UUID.randomUUID(), "SelfPredict")) {
         public boolean method_7325() {
            return false;
         }

         public boolean method_7337() {
            return false;
         }
      };
      fake.method_33574(center);
      fake.method_6033(p.method_6032());
      fake.method_24830(p.method_24828());
      fake.method_31548().method_7377(p.method_31548());
      p.method_6026().forEach(fake::method_6092);
      return fake;
   }

   static boolean inside(PlayerEntity p, Box box) {
      return !mc.field_1687.method_8587(p, box);
   }
}
