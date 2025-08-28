package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
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
        Vec3d vec3d = posTo.subtract(posFrom);
        return HoleSnap.getRotationFromVec(vec3d);
    }

    public static void doCircle(MatrixStack matrixStack, Color color, double circleSize, Vec3d pos, int segments) {
        Vec3d camPos = HoleSnap.mc.getBlockEntityRenderDispatcher().camera.getPos();
        GL11.glDisable(2929);
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.setShaderColor((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        for (double i = 0.0; i < 360.0; i += 360.0 / (double)segments) {
            double x = Math.sin(Math.toRadians(i)) * circleSize;
            double z = Math.cos(Math.toRadians(i)) * circleSize;
            Vec3d tempPos = new Vec3d(pos.x + x, pos.y, pos.z + z).add(-camPos.x, -camPos.y, -camPos.z);
            bufferBuilder.vertex(matrix, (float)tempPos.x, (float)tempPos.y, (float)tempPos.z).next();
        }
        tessellator.draw();
        GL11.glEnable(2929);
    }

    private static Vec2f getRotationFromVec(Vec3d vec) {
        double d = vec.x;
        double d2 = vec.z;
        double xz = Math.hypot(d, d2);
        d2 = vec.z;
        double d3 = vec.x;
        double yaw = HoleSnap.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = HoleSnap.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f((float)yaw, (float)pitch);
    }

    private static double normalizeAngle(final double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
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
        if (!HoleSnap.mc.player.isAlive() || HoleSnap.mc.player.isFallFlying()) {
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
        Vec3d playerPos = HoleSnap.mc.player.getPos();
        this.targetPos = new Vec3d((double)this.holePos.getX() + 0.5, HoleSnap.mc.player.getY(), (double)this.holePos.getZ() + 0.5);
        if (CombatUtil.isDoubleHole(this.holePos) && (facing = CombatUtil.is3Block(this.holePos)) != null) {
            this.targetPos = this.targetPos.add(new Vec3d((double)facing.getVector().getX() * 0.5, (double)facing.getVector().getY() * 0.5, (double)facing.getVector().getZ() * 0.5));
        }
        this.applyTimer = true;
        this.resetMove = true;
        float rotation = HoleSnap.getRotationTo(playerPos, this.targetPos).x;
        float yawRad = rotation / 180.0f * (float)Math.PI;
        double dist = playerPos.distanceTo(this.targetPos);
        double cappedSpeed = Math.min(0.2873, dist);
        double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
        double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
        event.setX(x);
        event.setZ(z);
        if (Math.abs(x) < 0.1 && Math.abs(z) < 0.1 && playerPos.y <= (double)this.holePos.getY() + 0.5) {
            this.disable();
        }
        this.stuckTicks = HoleSnap.mc.player.horizontalCollision ? ++this.stuckTicks : 0;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.targetPos == null || this.holePos == null) {
            return;
        }
        GL11.glEnable(3042);
        Color color = this.color.getValue();
        Vec3d pos = new Vec3d(this.targetPos.x, this.holePos.getY(), this.targetPos.getZ());
        if (this.fade.getValue()) {
            double temp = 0.01;
            for (double i = 0.0; i < this.circleSize.getValue(); i += temp) {
                HoleSnap.doCircle(matrixStack, ColorUtil.injectAlpha(color, (int)Math.min((double)(color.getAlpha() * 2) / (this.circleSize.getValue() / temp), 255.0)), i, pos, this.segments.getValueInt());
            }
        } else {
            HoleSnap.doCircle(matrixStack, color, this.circleSize.getValue(), pos, this.segments.getValueInt());
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3042);
    }
}
