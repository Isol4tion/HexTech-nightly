package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.AutoPearl;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PearlClip
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PearlClip INSTANCE;
    public static float lastYaw;
    public static float lastPitch;
    public final BooleanSetting autoYaw = this.add(new BooleanSetting("AutoYaw", true));
    public final BooleanSetting bypass = this.add(new BooleanSetting("Bypass", true));
    private final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", false));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("checkFov", false));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 10, 0, 50));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 100, 0, 100));
    private final BooleanSetting sync = this.add(new BooleanSetting("Sync", true));
    public Vec3d directionVec = null;
    Vec3d targetPos;

    public PearlClip() {
        super("PearlClip", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.directionVec != null) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    private boolean faceVector(Vec3d directionVec) {
        this.directionVec = directionVec;
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (Math.abs(MathHelper.method_15393((float)(angle[0] - lastYaw))) < this.fov.getValueFloat() && Math.abs(MathHelper.method_15393((float)(angle[1] - lastPitch))) < this.fov.getValueFloat()) {
            if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.rotatepacket.getValue()) {
                EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }
            return true;
        }
        return !this.checkFov.getValue();
    }

    private void updatePos() {
        this.targetPos = new Vec3d(PearlClip.mc.field_1724.method_23317() + MathHelper.method_15350((double)(this.roundToClosest(PearlClip.mc.field_1724.method_23317(), Math.floor(PearlClip.mc.field_1724.method_23317()) + 0.241, Math.floor(PearlClip.mc.field_1724.method_23317()) + 0.759) - PearlClip.mc.field_1724.method_23317()), (double)-0.2, (double)0.2), PearlClip.mc.field_1724.method_23318() - 0.5, PearlClip.mc.field_1724.method_23321() + MathHelper.method_15350((double)(this.roundToClosest(PearlClip.mc.field_1724.method_23321(), Math.floor(PearlClip.mc.field_1724.method_23321()) + 0.241, Math.floor(PearlClip.mc.field_1724.method_23321()) + 0.759) - PearlClip.mc.field_1724.method_23321()), (double)-0.2, (double)0.2));
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        this.updatePos();
        if (!this.faceVector(this.targetPos)) {
            return;
        }
        if (this.sync.getValue()) {
            AutoPearl.INSTANCE.throwPearl(this.autoYaw.getValue() ? HexTech.ROTATE.getRotation(this.targetPos)[0] : PearlClip.mc.field_1724.method_36454(), this.bypass.getValue() ? 89.0f : 80.0f);
        } else {
            this.throwPearl();
        }
        this.disable();
    }

    @Override
    public void onUpdate() {
        this.updatePos();
        if (!this.faceVector(this.targetPos)) {
            return;
        }
        this.throwPearl();
        this.disable();
    }

    public void throwPearl() {
        int pearl;
        AutoPearl.throwing = true;
        if (PearlClip.mc.field_1724.method_6047().method_7909() == Items.field_8634) {
            PearlClip.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
        } else if (AutoPearl.INSTANCE.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlot(Items.field_8634)) != -1) {
            InventoryUtil.inventorySwap(pearl, PearlClip.mc.field_1724.method_31548().field_7545);
            PearlClip.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
            InventoryUtil.inventorySwap(pearl, PearlClip.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
        } else {
            pearl = InventoryUtil.findItem(Items.field_8634);
            if (pearl != -1) {
                int old = PearlClip.mc.field_1724.method_31548().field_7545;
                InventoryUtil.switchToSlot(pearl);
                PearlClip.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
                InventoryUtil.switchToSlot(old);
            }
        }
        AutoPearl.throwing = false;
    }

    @Override
    public void onEnable() {
        this.directionVec = null;
        if (PearlClip.nullCheck()) {
            this.disable();
            return;
        }
        this.updatePos();
        if (this.rotation.getValue()) {
            return;
        }
        if (this.sync.getValue()) {
            AutoPearl.INSTANCE.throwPearl(this.autoYaw.getValue() ? HexTech.ROTATE.getRotation(this.targetPos)[0] : PearlClip.mc.field_1724.method_36454(), this.bypass.getValue() ? 89.0f : 80.0f);
        } else {
            this.throwPearl();
        }
        this.disable();
    }

    private double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        if (d2 > d1) {
            return low;
        }
        return high;
    }
}
