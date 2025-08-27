package me.hextech.remapped;

import java.util.HashMap;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FastFall;
import me.hextech.remapped.Flight;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import me.hextech.remapped.TimerEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class FastFall_mtLznGzMDzxhgBaLMnXD
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final EnumSetting<FastFall> mode = this.add(new EnumSetting<FastFall>("Mode", FastFall.Fast));
    private final BooleanSetting noLag = this.add(new BooleanSetting("NoLag", true, v -> this.mode.getValue() == FastFall.Fast));
    private final SliderSetting height = this.add(new SliderSetting("Height", 10.0, 1.0, 20.0, 0.5));
    private final SliderSetting STimer = this.add(new SliderSetting("Timer", 2.0, 1.0, 20.0, 0.5));
    private final Timer lagTimer = new Timer();
    boolean onGround = false;
    private boolean useTimer;

    public FastFall_mtLznGzMDzxhgBaLMnXD() {
        super("FastFall", "Miyagi son simulator", Module_JlagirAibYQgkHtbRnhw.Movement);
    }

    @Override
    public void onDisable() {
        this.useTimer = false;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (this.height.getValue() > 0.0 && (double)this.traceDown() > this.height.getValue() || FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_5757() || FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_5869() || FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_5771() || FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_21754() || !this.lagTimer.passedMs(1000L) || FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_6128() || Flight.INSTANCE.isOn() || FastFall_mtLznGzMDzxhgBaLMnXD.nullCheck()) {
            return;
        }
        if (HoleKickTest.isInWeb((PlayerEntity)FastFall_mtLznGzMDzxhgBaLMnXD.mc.player)) {
            return;
        }
        if (FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_24828()) {
            if (this.mode.getValue() == FastFall.Fast) {
                MovementUtil.setMotionY(MovementUtil.getMotionY() - (double)(this.noLag.getValue() ? 0.62f : 1.0f));
            }
            if (this.traceDown() != 0 && (double)this.traceDown() <= this.height.getValue() && this.trace()) {
                MovementUtil.setMotionX(MovementUtil.getMotionX() * 0.05);
                MovementUtil.setMotionZ(MovementUtil.getMotionZ() * 0.05);
            }
        }
        if (this.mode.getValue() == FastFall.Strict) {
            if (!FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_24828()) {
                if (this.onGround) {
                    this.useTimer = true;
                }
                if (MovementUtil.getMotionY() >= 0.0) {
                    this.useTimer = false;
                }
                this.onGround = false;
            } else {
                this.useTimer = false;
                MovementUtil.setMotionY(-0.08);
                this.onGround = true;
            }
        } else {
            this.useTimer = false;
        }
    }

    @EventHandler
    public void onTimer(TimerEvent event) {
        if (FastFall_mtLznGzMDzxhgBaLMnXD.nullCheck()) {
            return;
        }
        if (!FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_24828() && this.useTimer) {
            event.set((float)this.STimer.getValue());
        }
    }

    @EventHandler
    public void onPacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (!FastFall_mtLznGzMDzxhgBaLMnXD.nullCheck() && event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.lagTimer.reset();
        }
    }

    private int traceDown() {
        int y;
        int retval = 0;
        for (int tracey = y = (int)Math.round(FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.getY()) - 1; tracey >= 0; --tracey) {
            BlockHitResult trace = FastFall_mtLznGzMDzxhgBaLMnXD.mc.world.raycast(new RaycastContext(FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_19538(), new Vec3d(FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.getX(), (double)tracey, FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.getZ()), RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)FastFall_mtLznGzMDzxhgBaLMnXD.mc.player));
            if (trace != null && trace.method_17783() == HitResult.Type.field_1332) {
                return retval;
            }
            ++retval;
        }
        return retval;
    }

    private boolean trace() {
        Box bbox = FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.method_5829();
        Vec3d basepos = bbox.method_1005();
        double minX = bbox.field_1323;
        double minZ = bbox.field_1321;
        double maxX = bbox.field_1320;
        double maxZ = bbox.field_1324;
        HashMap<Vec3d, Vec3d> positions = new HashMap<Vec3d, Vec3d>();
        positions.put(basepos, new Vec3d(basepos.field_1352, basepos.field_1351 - 1.0, basepos.field_1350));
        positions.put(new Vec3d(minX, basepos.field_1351, minZ), new Vec3d(minX, basepos.field_1351 - 1.0, minZ));
        positions.put(new Vec3d(maxX, basepos.field_1351, minZ), new Vec3d(maxX, basepos.field_1351 - 1.0, minZ));
        positions.put(new Vec3d(minX, basepos.field_1351, maxZ), new Vec3d(minX, basepos.field_1351 - 1.0, maxZ));
        positions.put(new Vec3d(maxX, basepos.field_1351, maxZ), new Vec3d(maxX, basepos.field_1351 - 1.0, maxZ));
        for (Vec3d key : positions.keySet()) {
            RaycastContext context = new RaycastContext(key, (Vec3d)positions.get(key), RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)FastFall_mtLznGzMDzxhgBaLMnXD.mc.player);
            BlockHitResult result = FastFall_mtLznGzMDzxhgBaLMnXD.mc.world.raycast(context);
            if (result == null || result.method_17783() != HitResult.Type.field_1332) continue;
            return false;
        }
        BlockState state = FastFall_mtLznGzMDzxhgBaLMnXD.mc.world.getBlockState((BlockPos)new BlockPosX(FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.getX(), FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.getY() - 1.0, FastFall_mtLznGzMDzxhgBaLMnXD.mc.player.getZ()));
        return state.method_26215();
    }
}
