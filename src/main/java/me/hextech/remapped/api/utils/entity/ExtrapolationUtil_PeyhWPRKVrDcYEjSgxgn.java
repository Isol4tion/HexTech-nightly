package me.hextech.remapped.api.utils.entity;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.hextech.remapped.PredictionSetting;
import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn
implements Wrapper {
    private static final Map<PlayerEntity, List<Vec3d>> motionHistory = new HashMap<PlayerEntity, List<Vec3d>>();

    public static void updateHistory() {
        if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world == null) {
            return;
        }
        HashMap<AbstractClientPlayerEntity, List> newMap = new HashMap<AbstractClientPlayerEntity, List>();
        for (AbstractClientPlayerEntity p : ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.getPlayers()) {
            List list = motionHistory.computeIfAbsent(p, k -> new ArrayList());
            list.add(0, p.getPos().subtract(p.prevX, p.prevY, p.prevZ));
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
            return p.getBoundingBox();
        }
        Vec3d motion = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.getAverageMotion(hist, smoothTicks);
        return ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(p, motion, extrapTicks);
    }

    public static PlayerEntity createPredict(PlayerEntity p, int ticks, int smooth) {
        Box future = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.extrapolate(p, ticks, smooth);
        Vec3d center = new Vec3d((future.minX + future.maxX) / 2.0, future.minY, (future.minZ + future.maxZ) / 2.0);
        PlayerEntity fake = null;
        if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
            fake = new PlayerEntity(ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, p.getBlockPos(), p.getYaw(), new GameProfile(UUID.randomUUID(), "Predict")) {
                @Override
                public boolean isSpectator() {
                    return false;
                }

                @Override
                public boolean isCreative() {
                    return false;
                }
            };
        }
        fake.setPosition(center);
        fake.setHealth(p.getHealth());
        fake.setOnGround(p.isOnGround());
        fake.getInventory().clone(p.getInventory());
        p.getStatusEffects().forEach(fake::addStatusEffect);
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
        return new Vec3d(sum.x / (double)s, list.get(0).y, sum.z / (double)s);
    }

    private static Box simulate(PlayerEntity p, Vec3d motion, int ticks) {
        double x = motion.x;
        double y = motion.y;
        double z = motion.z;
        Box box = p.getBoundingBox();
        double stepHeight = 0.6;
        boolean onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, box.offset(0.0, -0.04, 0.0));
        for (int i = 0; i < ticks; ++i) {
            List collisions = null;
            if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
                collisions = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.getEntityCollisions(null, box.stretch(x, y, z));
            }
            Vec3d adjusted = null;
            if (collisions != null) {
                adjusted = Entity.adjustMovementForCollisions(null, new Vec3d(x, y, z), box, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions);
            }
            boolean canStep = false;
            if (adjusted != null) {
                boolean bl = canStep = PredictionSetting.INSTANCE.step.getValue() && (onGround || y < 0.0 && adjusted.y != y) && (adjusted.x != x || adjusted.z != z);
            }
            if (canStep) {
                Vec3d comb;
                Vec3d step = Entity.adjustMovementForCollisions(null, new Vec3d(x, stepHeight, z), box, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions);
                Vec3d stepY = Entity.adjustMovementForCollisions(null, new Vec3d(0.0, stepHeight, 0.0), box.stretch(x, 0.0, z), ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions);
                if (stepY.y < stepHeight && (comb = stepY.add(Entity.adjustMovementForCollisions(null, new Vec3d(x, 0.0, z), box.offset(stepY), ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions))).horizontalLengthSquared() > step.horizontalLengthSquared()) {
                    step = comb;
                }
                if (step.horizontalLengthSquared() > adjusted.horizontalLengthSquared()) {
                    adjusted = step.add(Entity.adjustMovementForCollisions(null, new Vec3d(0.0, -step.y + y, 0.0), box.offset(step), ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions));
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
        Box box = p.getBoundingBox();
        double stepHeight = 0.6;
        boolean onGround = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.inside(p, box.offset(0.0, -0.04, 0.0));
        path.add(new Vec3d((box.minX + box.maxX) / 2.0, box.minY, (box.minZ + box.maxZ) / 2.0));
        double x = motion.x;
        double y = motion.y;
        double z = motion.z;
        for (int i = 0; i < ticks; ++i) {
            List collisions = null;
            if (ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world != null) {
                collisions = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.getEntityCollisions(null, box.stretch(x, y, z));
            }
            Vec3d adjusted = null;
            if (collisions != null) {
                adjusted = Entity.adjustMovementForCollisions(null, new Vec3d(x, y, z), box, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions);
            }
            boolean canStep = false;
            if (adjusted != null) {
                boolean bl = canStep = PredictionSetting.INSTANCE.step.getValue() && (onGround || y < 0.0 && adjusted.y != y) && (adjusted.x != x || adjusted.z != z);
            }
            if (canStep) {
                Vec3d comb;
                Vec3d step = Entity.adjustMovementForCollisions(null, new Vec3d(x, stepHeight, z), box, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions);
                Vec3d stepY = Entity.adjustMovementForCollisions(null, new Vec3d(0.0, stepHeight, 0.0), box.stretch(x, 0.0, z), ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions);
                if (stepY.y < stepHeight && (comb = stepY.add(Entity.adjustMovementForCollisions(null, new Vec3d(x, 0.0, z), box.offset(stepY), ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions))).horizontalLengthSquared() > step.horizontalLengthSquared()) {
                    step = comb;
                }
                if (step.horizontalLengthSquared() > adjusted.horizontalLengthSquared()) {
                    adjusted = step.add(Entity.adjustMovementForCollisions(null, new Vec3d(0.0, -step.y + y, 0.0), box.offset(step), ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, collisions));
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
        PlayerEntity fake = new PlayerEntity(ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world, p.getBlockPos(), p.getYaw(), new GameProfile(UUID.randomUUID(), "SelfPredict")) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
        fake.setPosition(center);
        fake.setHealth(p.getHealth());
        fake.setOnGround(p.isOnGround());
        fake.getInventory().clone(p.getInventory());
        p.getStatusEffects().forEach(fake::addStatusEffect);
        return fake;
    }

    static boolean inside(PlayerEntity p, Box box) {
        return !ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.mc.world.isSpaceEmpty(p, box);
    }
}
