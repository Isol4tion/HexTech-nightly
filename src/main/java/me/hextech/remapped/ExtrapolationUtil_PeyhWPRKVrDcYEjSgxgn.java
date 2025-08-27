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
            list.add(0, p.method_19538().subtract(p.field_6014, p.field_6036, p.field_5969));
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
        Vec3d center = new Vec3d((future.minX + future.maxX) / 2.0, future.minY, (future.minZ + future.maxZ) / 2.0);
        ExtrapolationUtil_GIipvtNGRWEFrnWjqFrx fake = null;
        if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
            fake = new ExtrapolationUtil_GIipvtNGRWEFrnWjqFrx((World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, p.method_24515(), p.method_36454(), new GameProfile(UUID.randomUUID(), "Predict"));
        }
        fake.method_33574(center);
        fake.method_6033(p.method_6032());
        fake.method_24830(p.method_24828());
        fake.getInventory().clone(p.getInventory());
        p.method_6026().forEach(arg_0 -> ((PlayerEntity)fake).addStatusEffect(arg_0));
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
            sum = sum.add(v.x, 0.0, v.z);
        }
        return new Vec3d(sum.x / (double)s, list.get((int)0).y, sum.z / (double)s);
    }

    private static Box simulate(PlayerEntity p, Vec3d motion, int ticks) {
        double x = motion.x;
        double y = motion.y;
        double z = motion.z;
        Box box = p.method_5829();
        double stepHeight = 0.6;
        boolean onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, box.offset(0.0, -0.04, 0.0));
        for (int i = 0; i < ticks; ++i) {
            List collisions = null;
            if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
                collisions = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_20743(null, box.stretch(x, y, z));
            }
            Vec3d adjusted = null;
            if (collisions != null) {
                adjusted = Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(x, y, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
            }
            boolean canStep = false;
            if (adjusted != null) {
                boolean bl = canStep = PredictionSetting.INSTANCE.step.getValue() && (onGround || y < 0.0 && adjusted.y != y) && (adjusted.x != x || adjusted.z != z);
            }
            if (canStep) {
                Vec3d comb;
                Vec3d step = Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(x, stepHeight, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                Vec3d stepY = Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(0.0, stepHeight, 0.0), (Box)box.stretch(x, 0.0, z), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                if (stepY.y < stepHeight && (comb = stepY.add(Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(x, 0.0, z), (Box)box.offset(stepY), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions))).horizontalLengthSquared() > step.horizontalLengthSquared()) {
                    step = comb;
                }
                if (step.horizontalLengthSquared() > adjusted.horizontalLengthSquared()) {
                    adjusted = step.add(Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(0.0, -step.y + y, 0.0), (Box)box.offset(step), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions));
                }
            }
            if (onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, (box = box.offset(adjusted)).offset(0.0, -0.04, 0.0))) {
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
        boolean onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, box.offset(0.0, -0.04, 0.0));
        path.add(new Vec3d((box.minX + box.maxX) / 2.0, box.minY, (box.minZ + box.maxZ) / 2.0));
        double x = motion.x;
        double y = motion.y;
        double z = motion.z;
        for (int i = 0; i < ticks; ++i) {
            List collisions = null;
            if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
                collisions = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_20743(null, box.stretch(x, y, z));
            }
            Vec3d adjusted = null;
            if (collisions != null) {
                adjusted = Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(x, y, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
            }
            boolean canStep = false;
            if (adjusted != null) {
                boolean bl = canStep = PredictionSetting.INSTANCE.step.getValue() && (onGround || y < 0.0 && adjusted.y != y) && (adjusted.x != x || adjusted.z != z);
            }
            if (canStep) {
                Vec3d comb;
                Vec3d step = Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(x, stepHeight, z), (Box)box, (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                Vec3d stepY = Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(0.0, stepHeight, 0.0), (Box)box.stretch(x, 0.0, z), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions);
                if (stepY.y < stepHeight && (comb = stepY.add(Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(x, 0.0, z), (Box)box.offset(stepY), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions))).horizontalLengthSquared() > step.horizontalLengthSquared()) {
                    step = comb;
                }
                if (step.horizontalLengthSquared() > adjusted.horizontalLengthSquared()) {
                    adjusted = step.add(Entity.adjustMovementForCollisions(null, (Vec3d)new Vec3d(0.0, -step.y + y, 0.0), (Box)box.offset(step), (World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, (List)collisions));
                }
            }
            if (onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, (box = box.offset(adjusted)).offset(0.0, -0.04, 0.0))) {
                y = 0.0;
            }
            y = (y - 0.08) * 0.98;
            path.add(new Vec3d((box.minX + box.maxX) / 2.0, box.minY, (box.minZ + box.maxZ) / 2.0));
        }
        return path;
    }

    public static PlayerEntity createSelfPredict(PlayerEntity p, int ticks) {
        Box future = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.extrapolate(p, ticks, PredictionSetting.INSTANCE.smoothTicks.getValueInt());
        Vec3d center = new Vec3d((future.minX + future.maxX) / 2.0, future.minY, (future.minZ + future.maxZ) / 2.0);
        ExtrapolationUtil fake = new ExtrapolationUtil((World)ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, p.method_24515(), p.method_36454(), new GameProfile(UUID.randomUUID(), "SelfPredict"));
        fake.method_33574(center);
        fake.method_6033(p.method_6032());
        fake.method_24830(p.method_24828());
        fake.getInventory().clone(p.getInventory());
        p.method_6026().forEach(arg_0 -> ((PlayerEntity)fake).addStatusEffect(arg_0));
        return fake;
    }

    static boolean inside(PlayerEntity p, Box box) {
        return !ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.method_8587((Entity)p, box);
    }
}
