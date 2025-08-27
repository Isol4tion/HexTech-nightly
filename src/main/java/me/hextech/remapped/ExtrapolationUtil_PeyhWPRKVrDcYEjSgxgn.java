package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.hextech.remapped.ExtrapolationUtil;
import me.hextech.remapped.ExtrapolationUtil_GIipvtNGRWEFrnWjqFrx;
import me.hextech.remapped.PredictionSetting;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn
implements Wrapper {
    private static final Map<PlayerEntity, List<Vec3d>> motionHistory = new HashMap<PlayerEntity, List<Vec3d>>();

    public static void updateHistory() {
        if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world == null) {
            return;
        }
        HashMap<AbstractClientPlayerEntity, List> newMap = new HashMap<AbstractClientPlayerEntity, List>();
        for (AbstractClientPlayerEntity p : ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_18456()) {
            List list = motionHistory.computeIfAbsent((PlayerEntity)p, k -> new ArrayList());
            list.add(0, p.method_19538().method_1023(p.field_6014, p.field_6036, p.field_5969));
            if (list.size() > 20) {
                list.remove(20);
            }
            newMap.put(p, list);
        }
        motionHistory.keySet().retainAll(newMap.keySet());
    }

    public static Box extrapolate(PlayerEntity p, int extrapTicks, int smoothTicks) {
        List<Vec3d> hist = motionHistory.get(p);
        if (hist == null || hist.isEmpty()) {
            return p.method_5829();
        }
        Vec3d motion = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.getAverageMotion(hist, smoothTicks);
        return ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(p, motion, extrapTicks);
    }

    public static PlayerEntity createPredict(PlayerEntity p, int ticks, int smooth) {
        Box future = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.extrapolate(p, ticks, smooth);
        Vec3d center = new Vec3d((future.field_1323 + future.field_1320) / 2.0, future.field_1322, (future.field_1321 + future.field_1324) / 2.0);
        ExtrapolationUtil_GIipvtNGRWEFrnWjqFrx fake = null;
        if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
            fake = new ExtrapolationUtil_GIipvtNGRWEFrnWjqFrx((World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, p.method_24515(), p.method_36454(), new GameProfile(UUID.randomUUID(), "Predict"));
        }
        fake.method_33574(center);
        fake.method_6033(p.method_6032());
        fake.method_24830(p.method_24828());
        fake.method_31548().method_7377(p.method_31548());
        p.method_6026().forEach(arg_0 -> ((PlayerEntity)fake).method_6092(arg_0));
        return fake;
    }

    private static Vec3d getAverageMotion(List<Vec3d> list, int max) {
        if (list.isEmpty()) {
            return new Vec3d(0.0, 0.0, 0.0);
        }
        int s = Math.min(list.size(), Math.max(1, max));
        Vec3d sum = new Vec3d(0.0, 0.0, 0.0);
        for (int i = 0; i < s; ++i) {
            Vec3d v = list.get(i);
            sum = sum.method_1031(v.field_1352, 0.0, v.field_1350);
        }
        return new Vec3d(sum.field_1352 / (double)s, list.get((int)0).field_1351, sum.field_1350 / (double)s);
    }

    private static Box simulate(PlayerEntity p, Vec3d motion, int ticks) {
        double x = motion.field_1352;
        double y = motion.field_1351;
        double z = motion.field_1350;
        Box box = p.method_5829();
        double stepHeight = 0.6;
        boolean onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, box.method_989(0.0, -0.04, 0.0));
        for (int i = 0; i < ticks; ++i) {
            List collisions = null;
            if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
                collisions = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_20743(null, box.method_1012(x, y, z));
            }
            Vec3d adjusted = null;
            if (collisions != null) {
                adjusted = Entity.method_20736(null, (Vec3d)new Vec3d(x, y, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
            }
            boolean canStep = false;
            if (adjusted != null) {
                boolean bl = canStep = PredictionSetting.INSTANCE.step.getValue() && (onGround || y < 0.0 && adjusted.field_1351 != y) && (adjusted.field_1352 != x || adjusted.field_1350 != z);
            }
            if (canStep) {
                Vec3d comb;
                Vec3d step = Entity.method_20736(null, (Vec3d)new Vec3d(x, stepHeight, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                Vec3d stepY = Entity.method_20736(null, (Vec3d)new Vec3d(0.0, stepHeight, 0.0), (Box)box.method_1012(x, 0.0, z), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                if (stepY.field_1351 < stepHeight && (comb = stepY.method_1019(Entity.method_20736(null, (Vec3d)new Vec3d(x, 0.0, z), (Box)box.method_997(stepY), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions))).method_37268() > step.method_37268()) {
                    step = comb;
                }
                if (step.method_37268() > adjusted.method_37268()) {
                    adjusted = step.method_1019(Entity.method_20736(null, (Vec3d)new Vec3d(0.0, -step.field_1351 + y, 0.0), (Box)box.method_997(step), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions));
                }
            }
            if (onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, (box = box.method_997(adjusted)).method_989(0.0, -0.04, 0.0))) {
                y = 0.0;
            }
            y = (y - 0.08) * 0.98;
        }
        return box;
    }

    public static List<Vec3d> simulate(PlayerEntity p, int ticks) {
        ArrayList<Vec3d> path = new ArrayList<Vec3d>(ticks + 1);
        Vec3d motion = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.getAverageMotion(motionHistory.getOrDefault(p, Collections.emptyList()), PredictionSetting.INSTANCE.smoothTicks.getValueInt());
        Box box = p.method_5829();
        double stepHeight = 0.6;
        boolean onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, box.method_989(0.0, -0.04, 0.0));
        path.add(new Vec3d((box.field_1323 + box.field_1320) / 2.0, box.field_1322, (box.field_1321 + box.field_1324) / 2.0));
        double x = motion.field_1352;
        double y = motion.field_1351;
        double z = motion.field_1350;
        for (int i = 0; i < ticks; ++i) {
            List collisions = null;
            if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
                collisions = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_20743(null, box.method_1012(x, y, z));
            }
            Vec3d adjusted = null;
            if (collisions != null) {
                adjusted = Entity.method_20736(null, (Vec3d)new Vec3d(x, y, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
            }
            boolean canStep = false;
            if (adjusted != null) {
                boolean bl = canStep = PredictionSetting.INSTANCE.step.getValue() && (onGround || y < 0.0 && adjusted.field_1351 != y) && (adjusted.field_1352 != x || adjusted.field_1350 != z);
            }
            if (canStep) {
                Vec3d comb;
                Vec3d step = Entity.method_20736(null, (Vec3d)new Vec3d(x, stepHeight, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                Vec3d stepY = Entity.method_20736(null, (Vec3d)new Vec3d(0.0, stepHeight, 0.0), (Box)box.method_1012(x, 0.0, z), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                if (stepY.field_1351 < stepHeight && (comb = stepY.method_1019(Entity.method_20736(null, (Vec3d)new Vec3d(x, 0.0, z), (Box)box.method_997(stepY), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions))).method_37268() > step.method_37268()) {
                    step = comb;
                }
                if (step.method_37268() > adjusted.method_37268()) {
                    adjusted = step.method_1019(Entity.method_20736(null, (Vec3d)new Vec3d(0.0, -step.field_1351 + y, 0.0), (Box)box.method_997(step), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions));
                }
            }
            if (onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, (box = box.method_997(adjusted)).method_989(0.0, -0.04, 0.0))) {
                y = 0.0;
            }
            y = (y - 0.08) * 0.98;
            path.add(new Vec3d((box.field_1323 + box.field_1320) / 2.0, box.field_1322, (box.field_1321 + box.field_1324) / 2.0));
        }
        return path;
    }

    public static PlayerEntity createSelfPredict(PlayerEntity p, int ticks) {
        Box future = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.extrapolate(p, ticks, PredictionSetting.INSTANCE.smoothTicks.getValueInt());
        Vec3d center = new Vec3d((future.field_1323 + future.field_1320) / 2.0, future.field_1322, (future.field_1321 + future.field_1324) / 2.0);
        ExtrapolationUtil fake = new ExtrapolationUtil((World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, p.method_24515(), p.method_36454(), new GameProfile(UUID.randomUUID(), "SelfPredict"));
        fake.method_33574(center);
        fake.method_6033(p.method_6032());
        fake.method_24830(p.method_24828());
        fake.method_31548().method_7377(p.method_31548());
        p.method_6026().forEach(arg_0 -> ((PlayerEntity)fake).method_6092(arg_0));
        return fake;
    }

    static boolean inside(PlayerEntity p, Box box) {
        return !ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_8587((Entity)p, box);
    }
}
