package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MoveUp;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Step_EShajbhvQeYkCdreEeNY;
import me.hextech.remapped.TimerEvent;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class HoleSnap
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HoleSnap INSTANCE;
    public final BooleanSetting any = this.add(new BooleanSetting("AnyHole", true));
    public final SliderSetting timer = this.add(new SliderSetting("Timer", 1.0, 0.1, 8.0, 0.1));
    public final BooleanSetting step = this.add(new BooleanSetting("UseStep", true));
    public final BooleanSetting burrowStepUp = this.add(new BooleanSetting("BurrowStepUp", true));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    public final SliderSetting circleSize = this.add(new SliderSetting("CircleSize", 1.0, 0.1f, 2.5));
    public final BooleanSetting fade = this.add(new BooleanSetting("Fade", true));
    public final SliderSetting segments = this.add(new SliderSetting("Segments", 180, 0, 360));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5, 1, 50));
    private final SliderSetting timeoutTicks = this.add(new SliderSetting("TimeOut", 40, 0, 100));
    boolean resetMove = false;
    boolean applyTimer = false;
    Vec3d targetPos;
    boolean prev;
    float preYaw;
    private BlockPos holePos;
    private int stuckTicks;
    private int enabledTicks;

    public HoleSnap() {
        super("HoleSnap", "HoleSnap", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        Vec3d vec3d = posTo.method_1020(posFrom);
        return HoleSnap.getRotationFromVec(vec3d);
    }

    public static void doCircle(MatrixStack matrixStack, Color color, double circleSize, Vec3d pos, int segments) {
        Vec3d camPos = HoleSnap.mc.method_31975().field_4344.method_19326();
        GL11.glDisable((int)2929);
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.setShader(GameRenderer::method_34539);
        RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_27381, VertexFormats.field_1592);
        for (double i = 0.0; i < 360.0; i += 360.0 / (double)segments) {
            double x = Math.sin(Math.toRadians(i)) * circleSize;
            double z = Math.cos(Math.toRadians(i)) * circleSize;
            Vec3d tempPos = new Vec3d(pos.field_1352 + x, pos.field_1351, pos.field_1350 + z).method_1031(-camPos.field_1352, -camPos.field_1351, -camPos.field_1350);
            bufferBuilder.method_22918(matrix, (float)tempPos.field_1352, (float)tempPos.field_1351, (float)tempPos.field_1350).method_1344();
        }
        tessellator.method_1350();
        GL11.glEnable((int)2929);
    }

    private static Vec2f getRotationFromVec(Vec3d vec) {
        double d = vec.field_1352;
        double d2 = vec.field_1350;
        double xz = Math.hypot(d, d2);
        d2 = vec.field_1350;
        double d3 = vec.field_1352;
        double yaw = HoleSnap.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = HoleSnap.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
        return new Vec2f((float)yaw, (float)pitch);
    }

    private static double normalizeAngle(double angleIn) {
        double d;
        double angle = angleIn;
        angle %= 360.0;
        if (d >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    @EventHandler(priority=-99)
    public void onTimer(TimerEvent event) {
        if (this.applyTimer) {
            event.set(this.timer.getValueFloat());
        }
    }

    @Override
    public void onEnable() {
        this.applyTimer = false;
        if (HoleSnap.nullCheck()) {
            this.disable();
            return;
        }
        this.resetMove = false;
        this.holePos = CombatUtil.getHole((float)this.range.getValue(), true, this.any.getValue());
        if (this.step.getValue()) {
            Step_EShajbhvQeYkCdreEeNY.INSTANCE.enable();
        }
        if (this.burrowStepUp.getValue()) {
            MoveUp.INSTANCE.enable();
        }
    }

    @Override
    public void onDisable() {
        this.holePos = null;
        this.stuckTicks = 0;
        this.enabledTicks = 0;
        if (HoleSnap.nullCheck()) {
            return;
        }
        if (this.resetMove) {
            MovementUtil.setMotionX(0.0);
            MovementUtil.setMotionZ(0.0);
        }
        if (this.step.getValue()) {
            Step_EShajbhvQeYkCdreEeNY.INSTANCE.disable();
        }
    }

    @EventHandler
    public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        if (this.holePos == null) {
            return;
        }
        this.applyTimer = true;
        if (!BlockUtil.isHole(this.holePos) && !CombatUtil.isDoubleHole(this.holePos)) {
            this.holePos = CombatUtil.getHole((float)this.range.getValue(), true, this.any.getValue());
        }
    }

    @EventHandler
    public void onMove(MoveEvent event) {
        Direction facing;
        ++this.enabledTicks;
        if ((double)this.enabledTicks > this.timeoutTicks.getValue() - 1.0) {
            this.disable();
            return;
        }
        if (!HoleSnap.mc.field_1724.method_5805() || HoleSnap.mc.field_1724.method_6128()) {
            this.disable();
            return;
        }
        if (this.stuckTicks > 8) {
            this.disable();
            return;
        }
        if (this.holePos == null) {
            this.disable();
            return;
        }
        Vec3d playerPos = HoleSnap.mc.field_1724.method_19538();
        this.targetPos = new Vec3d((double)this.holePos.method_10263() + 0.5, HoleSnap.mc.field_1724.method_23318(), (double)this.holePos.method_10260() + 0.5);
        if (CombatUtil.isDoubleHole(this.holePos) && (facing = CombatUtil.is3Block(this.holePos)) != null) {
            this.targetPos = this.targetPos.method_1019(new Vec3d((double)facing.method_10163().method_10263() * 0.5, (double)facing.method_10163().method_10264() * 0.5, (double)facing.method_10163().method_10260() * 0.5));
        }
        this.applyTimer = true;
        this.resetMove = true;
        float rotation = HoleSnap.getRotationTo((Vec3d)playerPos, (Vec3d)this.targetPos).field_1343;
        float yawRad = rotation / 180.0f * (float)Math.PI;
        double dist = playerPos.method_1022(this.targetPos);
        double cappedSpeed = Math.min(0.2873, dist);
        double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
        double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
        event.setX(x);
        event.setZ(z);
        if (Math.abs(x) < 0.1 && Math.abs(z) < 0.1 && playerPos.field_1351 <= (double)this.holePos.method_10264() + 0.5) {
            this.disable();
        }
        this.stuckTicks = HoleSnap.mc.field_1724.field_5976 ? ++this.stuckTicks : 0;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.targetPos == null || this.holePos == null) {
            return;
        }
        GL11.glEnable((int)3042);
        Color color = this.color.getValue();
        Vec3d pos = new Vec3d(this.targetPos.field_1352, (double)this.holePos.method_10264(), this.targetPos.method_10215());
        if (this.fade.getValue()) {
            double temp = 0.01;
            for (double i = 0.0; i < this.circleSize.getValue(); i += temp) {
                HoleSnap.doCircle(matrixStack, ColorUtil.injectAlpha(color, (int)Math.min((double)(color.getAlpha() * 2) / (this.circleSize.getValue() / temp), 255.0)), i, pos, this.segments.getValueInt());
            }
        } else {
            HoleSnap.doCircle(matrixStack, color, this.circleSize.getValue(), pos, this.segments.getValueInt());
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
    }
}
