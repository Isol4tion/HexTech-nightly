package me.hextech.mod.modules.impl.setting;

import com.mojang.authlib.GameProfile;
import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn;
import me.hextech.api.utils.render.JelloUtil;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PredictionSetting
        extends Module_eSdgMXWuzcxgQVaJFmKZ
        implements Wrapper {
    public static PredictionSetting INSTANCE;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.Prediction));
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.HexTech, v -> this.page.getValue() == Page.Prediction));
    public final SliderSetting placeExtrap = this.add(new SliderSetting("PlaceExtrap", 3, 0, 20, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.Aurora).setSuffix("tick"));
    public final SliderSetting breakExtrap = this.add(new SliderSetting("BreakExtrap", 2, 0, 20, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.Aurora).setSuffix("tick"));
    public final SliderSetting extrapTicks = this.add(new SliderSetting("ExtrapTicks", 3, 1, 20, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.Aurora).setSuffix("hist"));
    public final SliderSetting selfExtrap = this.add(new SliderSetting("SelfExtrap", 2, 0, 20, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.Aurora).setSuffix("tick"));
    public final SliderSetting smoothTicks = this.add(new SliderSetting("SmoothTicks", 3, 0, 20, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.Aurora).setSuffix("tick"));
    public final BooleanSetting step = this.add(new BooleanSetting("Step", false, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.Aurora));
    public final SliderSetting breakextrap = this.add(new SliderSetting("BreakExtrap", 0, 0, 10, v -> this.page.getValue() == Page.Prediction && this.mode.getValue() == Mode.HexTech).setSuffix("tick"));
    public final BooleanSetting prediction = this.add(new BooleanSetting("Prediction", true, v -> this.page.getValue() == Page.Prediction));
    public final BooleanSetting idPredict = this.add(new BooleanSetting("ID-Predict\u00a74[OnlyAC]", false, v -> this.page.getValue() == Page.IDPrediction));
    public final SliderSetting idStartOffset = this.add(new SliderSetting("ID-StartOffset", 1, 0, 10, v -> this.page.getValue() == Page.IDPrediction && this.idPredict.getValue()));
    public final SliderSetting idPacketOffset = this.add(new SliderSetting("ID-PacketOffset", 1, 1, 10, v -> this.page.getValue() == Page.IDPrediction && this.idPredict.getValue()));
    public final SliderSetting idPackets = this.add(new SliderSetting("ID-Packets", 3, 1, 10, v -> this.page.getValue() == Page.IDPrediction && this.idPredict.getValue()));
    public final SliderSetting idStartDelay = this.add(new SliderSetting("ID-StartDelay", 50, 0, 1000, v -> this.page.getValue() == Page.IDPrediction && this.idPredict.getValue()).setSuffix("ms"));
    public final SliderSetting idPacketDelay = this.add(new SliderSetting("ID-PacketDelay", 50, 0, 1000, v -> this.page.getValue() == Page.IDPrediction && this.idPredict.getValue()).setSuffix("ms"));
    public final BooleanSetting drawnSelf = this.add(new BooleanSetting("DrawnSelf", true, v -> this.page.getValue() == Page.Render));
    public final BooleanSetting drawnTarget = this.add(new BooleanSetting("DrawnTarget", true, v -> this.page.getValue() == Page.Render));
    public final EnumSetting<RenderMode> renderMode = this.add(new EnumSetting<RenderMode>("RenderMode", RenderMode.Line, v -> this.page.getValue() == Page.Render));
    public final SliderSetting subSteps = this.add(new SliderSetting("subSteps", 15, 5, 50, v -> this.page.getValue() == Page.Render && this.renderMode.getValue() == RenderMode.Line));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 120), v -> this.page.getValue() == Page.Render));
    public final SliderSetting lineWidth = this.add(new SliderSetting("LineWidth", 1.5, 0.1, 5.0, 0.1, v -> this.page.getValue() == Page.Render));
    private final Map<PlayerEntity, List<Vec3d>> pathCache = new HashMap<PlayerEntity, List<Vec3d>>();

    public PredictionSetting() {
        super("PredictionSetting", "PredictionSetting", Category.Setting);
        INSTANCE = this;
    }

    @Override
    public void onRender3D(MatrixStack matrices, float pt) {
        if (PredictionSetting.mc.world == null || PredictionSetting.mc.player == null) {
            return;
        }
        this.pathCache.clear();
        if (this.drawnSelf.getValue()) {
            this.pathCache.put(PredictionSetting.mc.player, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(PredictionSetting.mc.player, this.selfExtrap.getValueInt()));
        }
        if (this.drawnTarget.getValue()) {
            for (PlayerEntity p : PredictionSetting.mc.world.getPlayers()) {
                if (p == PredictionSetting.mc.player || p.squaredDistanceTo(PredictionSetting.mc.player) > 4096.0)
                    continue;
                int ticks = Math.max(this.placeExtrap.getValueInt(), this.breakExtrap.getValueInt());
                this.pathCache.put(p, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(p, ticks));
            }
        }
        this.pathCache.forEach((real, path) -> {
            switch (this.renderMode.getValue()) {
                case Box: {
                    this.drawBox(matrices, pt, real, path);
                    break;
                }
                case Jello: {
                    this.drawJello(matrices, pt, real, path);
                    break;
                }
                case Line: {
                    this.drawLinePath(matrices, path);
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
        fake.setPosition(last);
        JelloUtil.drawJello(matrices, fake, this.color.getValue());
    }

    private void drawLinePath(MatrixStack matrices, List<Vec3d> path) {
        if (path == null || path.size() < 2) {
            return;
        }
        Vec3d prev = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            Vec3d cur = path.get(i);
            for (int j = 1; j <= this.subSteps.getValueInt(); ++j) {
                double t = (double) j / (double) this.subSteps.getValueInt();
                Vec3d interp = new Vec3d(prev.x + (cur.x - prev.x) * t, prev.y + (cur.y - prev.y) * t, prev.z + (cur.z - prev.z) * t);
                Vec3d before = new Vec3d(prev.x + (cur.x - prev.x) * (t - 1.0 / (double) this.subSteps.getValueInt()), prev.y + (cur.y - prev.y) * (t - 1.0 / (double) this.subSteps.getValueInt()), prev.z + (cur.z - prev.z) * (t - 1.0 / (double) this.subSteps.getValueInt()));
                Render3DUtil.drawLine(before.x, before.y, before.z, interp.x, interp.y, interp.z, this.color.getValue(), this.lineWidth.getValueFloat());
            }
            prev = cur;
        }
    }

    public enum Page {
        Prediction,
        IDPrediction,
        Render

    }

    public enum Mode {
        Aurora,
        HexTech

    }

    public enum RenderMode {
        Box,
        Jello,
        Line

    }

    public static class _XBpBEveLWEKUGQPHCCIS {
        public final PlayerEntity player;
        public final PlayerEntity predict;

        public _XBpBEveLWEKUGQPHCCIS(PlayerEntity player) {
            this.player = player;
            if (PredictionSetting.INSTANCE.prediction.getValue()) {
                this.predict = new PlayerEntity(Wrapper.mc.world, player.getBlockPos(), player.getYaw(), new GameProfile(UUID.fromString("66123666-1234-5432-6666-667563866600"), "PredictEntity339")) {
                    @Override
                    public boolean isSpectator() {
                        return false;
                    }

                    @Override
                    public boolean isCreative() {
                        return false;
                    }
                };
                if (PredictionSetting.INSTANCE.mode.getValue() == Mode.Aurora) {
                    int ticks = PredictionSetting.INSTANCE.breakExtrap.getValueInt();
                    Box future = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.extrapolate(player, ticks, (int) PredictionSetting.INSTANCE.extrapTicks.getValue());
                    Vec3d center = new Vec3d((future.minX + future.maxX) / 2.0, future.minY, (future.minZ + future.maxZ) / 2.0);
                    this.predict.setPosition(center);
                    this.predict.setHealth(player.getHealth());
                    this.predict.prevX = player.prevX;
                    this.predict.prevY = player.prevY;
                    this.predict.prevZ = player.prevZ;
                    this.predict.setOnGround(player.isOnGround());
                    this.predict.getInventory().clone(player.getInventory());
                    this.predict.setPose(player.getPose());
                    player.getStatusEffects().forEach(arg_0 -> this.predict.addStatusEffect(arg_0));
                } else if (PredictionSetting.INSTANCE.mode.getValue() == Mode.HexTech) {
                    this.predict.setPosition(player.getPos().add(CombatUtil.getMotionVec(player, PredictionSetting.INSTANCE.breakextrap.getValueInt(), true)));
                    this.predict.setHealth(player.getHealth());
                    this.predict.prevX = player.prevX;
                    this.predict.prevZ = player.prevZ;
                    this.predict.prevY = player.prevY;
                    this.predict.setOnGround(player.isOnGround());
                    this.predict.getInventory().clone(player.getInventory());
                    this.predict.setPose(player.getPose());
                    for (StatusEffectInstance se : new ArrayList<>(player.getStatusEffects())) {
                        this.predict.addStatusEffect(se);
                    }
                }
            } else {
                this.predict = player;
            }
        }
    }
}
