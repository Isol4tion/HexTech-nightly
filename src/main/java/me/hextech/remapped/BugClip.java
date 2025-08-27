package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class BugClip
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static BugClip INSTANCE;
    final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500));
    final Timer timer = new Timer();
    private final BooleanSetting clipIn = this.add(new BooleanSetting("ClipIn", true));
    boolean cancelPacket = true;

    public BugClip() {
        super("BugClip", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.cancelPacket = false;
        if (this.clipIn.getValue()) {
            Direction f = BugClip.mc.field_1724.method_5735();
            BugClip.mc.field_1724.method_5814(BugClip.mc.field_1724.method_23317() + (double)f.method_10148() * 0.5, BugClip.mc.field_1724.method_23318(), BugClip.mc.field_1724.method_23321() + (double)f.method_10165() * 0.5);
            BugClip.mc.field_1724.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(BugClip.mc.field_1724.method_23317(), BugClip.mc.field_1724.method_23318(), BugClip.mc.field_1724.method_23321(), true));
        } else {
            BugClip.mc.field_1724.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(BugClip.mc.field_1724.method_23317(), BugClip.mc.field_1724.method_23318(), BugClip.mc.field_1724.method_23321(), true));
            BugClip.mc.field_1724.method_5814(this.roundToClosest(BugClip.mc.field_1724.method_23317(), Math.floor(BugClip.mc.field_1724.method_23317()) + 0.23, Math.floor(BugClip.mc.field_1724.method_23317()) + 0.77), BugClip.mc.field_1724.method_23318(), this.roundToClosest(BugClip.mc.field_1724.method_23321(), Math.floor(BugClip.mc.field_1724.method_23321()) + 0.23, Math.floor(BugClip.mc.field_1724.method_23321()) + 0.77));
            BugClip.mc.field_1724.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(this.roundToClosest(BugClip.mc.field_1724.method_23317(), Math.floor(BugClip.mc.field_1724.method_23317()) + 0.23, Math.floor(BugClip.mc.field_1724.method_23317()) + 0.77), BugClip.mc.field_1724.method_23318(), this.roundToClosest(BugClip.mc.field_1724.method_23321(), Math.floor(BugClip.mc.field_1724.method_23321()) + 0.23, Math.floor(BugClip.mc.field_1724.method_23321()) + 0.77), true));
        }
        this.cancelPacket = true;
    }

    private double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        if (d2 > d1) {
            return low;
        }
        return high;
    }

    @Override
    public void onUpdate() {
        if (!this.insideBurrow()) {
            this.disable();
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        Object t;
        if (BugClip.nullCheck()) {
            return;
        }
        if (this.cancelPacket && (t = event.getPacket()) instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket)t;
            if (!this.insideBurrow()) {
                this.disable();
                return;
            }
            if (packet.method_36172()) {
                float packetYaw = packet.method_12271(0.0f);
                float packetPitch = packet.method_12270(0.0f);
                if (this.timer.passedMs(this.delay.getValue())) {
                    this.cancelPacket = false;
                    BugClip.mc.field_1724.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.Full(BugClip.mc.field_1724.method_23317(), BugClip.mc.field_1724.method_23318() + 1337.0, BugClip.mc.field_1724.method_23321(), packetYaw, packetPitch, false));
                    this.cancelPacket = true;
                    this.timer.reset();
                }
            }
            event.cancel();
        }
    }

    public boolean insideBurrow() {
        BlockPos playerBlockPos = EntityUtil.getPlayerPos(true);
        for (int xOffset = -1; xOffset <= 1; ++xOffset) {
            for (int yOffset = -1; yOffset <= 1; ++yOffset) {
                for (int zOffset = -1; zOffset <= 1; ++zOffset) {
                    BlockPos offsetPos = playerBlockPos.method_10069(xOffset, yOffset, zOffset);
                    if (BugClip.mc.field_1687.method_8320(offsetPos).method_26204() != Blocks.field_9987 || !BugClip.mc.field_1724.method_5829().method_994(new Box(offsetPos))) continue;
                    return true;
                }
            }
        }
        return false;
    }
}
