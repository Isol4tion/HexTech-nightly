package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn;
import me.hextech.remapped.JelloUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PredictionSetting_JKvMiRXQVAwkYSSmKMez;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PredictionSetting
extends Module_eSdgMXWuzcxgQVaJFmKZ
implements Wrapper {
    public static PredictionSetting INSTANCE;
    public final EnumSetting<_JgmYdSqRlhzWcxRMZVQF> page = this.add(new EnumSetting<_JgmYdSqRlhzWcxRMZVQF>("Page", _JgmYdSqRlhzWcxRMZVQF.Prediction));
    public final EnumSetting<_mJSQReswTiaqOSqkjOmh> mode = this.add(new EnumSetting<_mJSQReswTiaqOSqkjOmh>("Mode", _mJSQReswTiaqOSqkjOmh.HexTech, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction));
    public final SliderSetting placeExtrap = this.add(new SliderSetting("PlaceExtrap", 3, 0, 20, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora).setSuffix("tick"));
    public final SliderSetting breakExtrap = this.add(new SliderSetting("BreakExtrap", 2, 0, 20, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora).setSuffix("tick"));
    public final SliderSetting extrapTicks = this.add(new SliderSetting("ExtrapTicks", 3, 1, 20, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora).setSuffix("hist"));
    public final SliderSetting selfExtrap = this.add(new SliderSetting("SelfExtrap", 2, 0, 20, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora).setSuffix("tick"));
    public final SliderSetting smoothTicks = this.add(new SliderSetting("SmoothTicks", 3, 0, 20, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora).setSuffix("tick"));
    public final BooleanSetting step = this.add(new BooleanSetting("Step", false, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora));
    public final SliderSetting breakextrap = this.add(new SliderSetting("BreakExtrap", 0, 0, 10, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction && this.mode.getValue() == _mJSQReswTiaqOSqkjOmh.HexTech).setSuffix("tick"));
    public final BooleanSetting prediction = this.add(new BooleanSetting("Prediction", true, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Prediction));
    public final BooleanSetting idPredict = this.add(new BooleanSetting("ID-Predict\u00a74[OnlyAC]", false, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.IDPrediction));
    public final SliderSetting idStartOffset = this.add(new SliderSetting("ID-StartOffset", 1, 0, 10, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()));
    public final SliderSetting idPacketOffset = this.add(new SliderSetting("ID-PacketOffset", 1, 1, 10, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()));
    public final SliderSetting idPackets = this.add(new SliderSetting("ID-Packets", 3, 1, 10, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()));
    public final SliderSetting idStartDelay = this.add(new SliderSetting("ID-StartDelay", 50, 0, 1000, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()).setSuffix("ms"));
    public final SliderSetting idPacketDelay = this.add(new SliderSetting("ID-PacketDelay", 50, 0, 1000, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()).setSuffix("ms"));
    public final BooleanSetting drawnSelf = this.add(new BooleanSetting("DrawnSelf", true, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Render));
    public final BooleanSetting drawnTarget = this.add(new BooleanSetting("DrawnTarget", true, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Render));
    public final EnumSetting<_JkDAbATfmVbQbOuEIDLo> renderMode = this.add(new EnumSetting<_JkDAbATfmVbQbOuEIDLo>("RenderMode", _JkDAbATfmVbQbOuEIDLo.Line, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Render));
    public final SliderSetting subSteps = this.add(new SliderSetting("subSteps", 15, 5, 50, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Render && this.renderMode.getValue() == _JkDAbATfmVbQbOuEIDLo.Line));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 120), v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Render));
    public final SliderSetting lineWidth = this.add(new SliderSetting("LineWidth", 1.5, 0.1, 5.0, 0.1, v -> this.page.getValue() == _JgmYdSqRlhzWcxRMZVQF.Render));
    private final Map<PlayerEntity, List<Vec3d>> pathCache = new HashMap<PlayerEntity, List<Vec3d>>();

    public PredictionSetting() {
        super("PredictionSetting", "PredictionSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    @Override
    public void onRender3D(MatrixStack matrices, float pt) {
        if (PredictionSetting.mc.world == null || PredictionSetting.mc.player == null) {
            return;
        }
        this.pathCache.clear();
        if (this.drawnSelf.getValue()) {
            this.pathCache.put((PlayerEntity)PredictionSetting.mc.player, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate((PlayerEntity)PredictionSetting.mc.player, this.selfExtrap.getValueInt()));
        }
        if (this.drawnTarget.getValue()) {
            for (PlayerEntity p : PredictionSetting.mc.world.method_18456()) {
                if (p == PredictionSetting.mc.player || p.method_5858((Entity)PredictionSetting.mc.player) > 4096.0) continue;
                int ticks = Math.max(this.placeExtrap.getValueInt(), this.breakExtrap.getValueInt());
                this.pathCache.put(p, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(p, ticks));
            }
        }
        this.pathCache.forEach((real, path) -> {
            switch (this.renderMode.getValue().ordinal()) {
                case 0: {
                    this.drawBox(matrices, pt, (PlayerEntity)real, (List<Vec3d>)path);
                    break;
                }
                case 1: {
                    this.drawJello(matrices, pt, (PlayerEntity)real, (List<Vec3d>)path);
                    break;
                }
                case 2: {
                    this.drawLinePath(matrices, (List<Vec3d>)path);
                }
            }
        });
    }

    private void drawBox(MatrixStack matrices, float pt, PlayerEntity real, List<Vec3d> path) {
        if (path.isEmpty()) {
            return;
        }
        Vec3d last = path.get(path.size() - 1);
        Box box = real.getBoundingBox().offset(last.subtract(real.getPos()));
        Render3DUtil.draw3DBox(matrices, box, this.color.getValue(), true, true);
    }

    private void drawJello(MatrixStack matrices, float pt, PlayerEntity real, List<Vec3d> path) {
        if (path.isEmpty()) {
            return;
        }
        Vec3d last = path.get(path.size() - 1);
        PlayerEntity fake = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createPredict(real, Math.max(this.placeExtrap.getValueInt(), this.breakExtrap.getValueInt()), this.smoothTicks.getValueInt());
        fake.method_33574(last);
        JelloUtil.drawJello(matrices, (Entity)fake, this.color.getValue());
    }

    private void drawLinePath(MatrixStack matrices, List<Vec3d> path) {
        if (path == null || path.size() < 2) {
            return;
        }
        Vec3d prev = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            Vec3d cur = path.get(i);
            for (int j = 1; j <= this.subSteps.getValueInt(); ++j) {
                double t = (double)j / (double)this.subSteps.getValueInt();
                Vec3d interp = new Vec3d(prev.x + (cur.x - prev.x) * t, prev.y + (cur.y - prev.y) * t, prev.z + (cur.z - prev.z) * t);
                Vec3d before = new Vec3d(prev.x + (cur.x - prev.x) * (t - 1.0 / (double)this.subSteps.getValueInt()), prev.y + (cur.y - prev.y) * (t - 1.0 / (double)this.subSteps.getValueInt()), prev.z + (cur.z - prev.z) * (t - 1.0 / (double)this.subSteps.getValueInt()));
                Render3DUtil.drawLine(before.x, before.y, before.z, interp.x, interp.y, interp.z, this.color.getValue(), this.lineWidth.getValueFloat());
            }
            prev = cur;
        }
    }

    public static enum _JgmYdSqRlhzWcxRMZVQF {
        Prediction,
        IDPrediction,
        Render;

    }

    public static enum _mJSQReswTiaqOSqkjOmh {
        Aurora,
        HexTech;

    }

    public static enum _JkDAbATfmVbQbOuEIDLo {
        Box,
        Jello,
        Line;

    }

    public static class _XBpBEveLWEKUGQPHCCIS {
        public final PlayerEntity player;
        public final PlayerEntity predict;

        public _XBpBEveLWEKUGQPHCCIS(PlayerEntity player) {
            this.player = player;
            if (PredictionSetting.INSTANCE.prediction.getValue()) {
                this.predict = new PredictionSetting_JKvMiRXQVAwkYSSmKMez(this, (World)Wrapper.mc.world, player.getBlockPos(), player.method_36454(), new GameProfile(UUID.fromString("66123666-1234-5432-6666-667563866600"), "PredictEntity339"));
                if (PredictionSetting.INSTANCE.mode.getValue() == _mJSQReswTiaqOSqkjOmh.Aurora) {
                    int ticks = PredictionSetting.INSTANCE.breakExtrap.getValueInt();
                    Box future = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.extrapolate(player, ticks, (int)PredictionSetting.INSTANCE.extrapTicks.getValue());
                    Vec3d center = new Vec3d((future.minX + future.maxX) / 2.0, future.minY, (future.minZ + future.maxZ) / 2.0);
                    this.predict.method_33574(center);
                    this.predict.method_6033(player.getHealth());
                    this.predict.field_6014 = player.field_6014;
                    this.predict.field_6036 = player.field_6036;
                    this.predict.field_5969 = player.field_5969;
                    this.predict.method_24830(player.isOnGround());
                    this.predict.getInventory().clone(player.getInventory());
                    this.predict.method_18380(player.method_18376());
                    player.method_6026().forEach(arg_0 -> ((PlayerEntity)this.predict).addStatusEffect(arg_0));
                } else if (PredictionSetting.INSTANCE.mode.getValue() == _mJSQReswTiaqOSqkjOmh.HexTech) {
                    this.predict.method_33574(player.getPos().add(CombatUtil.getMotionVec((Entity)player, PredictionSetting.INSTANCE.breakextrap.getValueInt(), true)));
                    this.predict.method_6033(player.getHealth());
                    this.predict.field_6014 = player.field_6014;
                    this.predict.field_5969 = player.field_5969;
                    this.predict.field_6036 = player.field_6036;
                    this.predict.method_24830(player.isOnGround());
                    this.predict.getInventory().clone(player.getInventory());
                    this.predict.method_18380(player.method_18376());
                    for (StatusEffectInstance se : new ArrayList(player.method_6026())) {
                        this.predict.addStatusEffect(se);
                    }
                }
            } else {
                this.predict = player;
            }
        }
    }
}
