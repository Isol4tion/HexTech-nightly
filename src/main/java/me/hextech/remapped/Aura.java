package me.hextech.remapped;

import java.awt.Color;
import me.hextech.HexTech;
import me.hextech.asm.accessors.IEntity;
import me.hextech.asm.accessors.ILivingEntity;
import me.hextech.remapped.Aura_nurTqHTNjexQmuWdDgIn;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.Criticals;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.JelloUtil;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SwingSide;
import me.hextech.remapped.Timer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Aura
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Aura INSTANCE;
    public static Entity target;
    public final EnumSetting<_PJzsesCBLMlruCifoKuT> page = this.add(new EnumSetting<_PJzsesCBLMlruCifoKuT>("Page", _PJzsesCBLMlruCifoKuT.General));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, (double)0.1f, 7.0, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    public final BooleanSetting Players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Target));
    public final BooleanSetting Mobs = this.add(new BooleanSetting("Mobs", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Target));
    public final BooleanSetting Animals = this.add(new BooleanSetting("Animals", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Target));
    public final BooleanSetting Villagers = this.add(new BooleanSetting("Villagers", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Target));
    public final BooleanSetting Slimes = this.add(new BooleanSetting("Slimes", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Target));
    private final BooleanSetting ghost = this.add(new BooleanSetting("SweepingBypass", false, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    private final EnumSetting<_VwTXxsLfDdMNyKpyAwyl> cd = this.add(new EnumSetting<_VwTXxsLfDdMNyKpyAwyl>("CooldownMode", _VwTXxsLfDdMNyKpyAwyl.Delay));
    private final SliderSetting cooldown = this.add(new SliderSetting("Cooldown", 1.1f, 0.0, 1.2f, 0.01, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    private final SliderSetting wallRange = this.add(new SliderSetting("WallRange", 6.0, (double)0.1f, 7.0, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    private final BooleanSetting whileEating = this.add(new BooleanSetting("WhileUsing", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    private final BooleanSetting weaponOnly = this.add(new BooleanSetting("WeaponOnly", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.Server, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Rotate).setParent());
    private final BooleanSetting newRotate = this.add(new BooleanSetting("NewRotate", true, v -> this.rotate.isOpen() && this.page.getValue() == _PJzsesCBLMlruCifoKuT.Rotate));
    private final SliderSetting yawStep = this.add(new SliderSetting("YawStep", (double)0.3f, (double)0.1f, 1.0, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _PJzsesCBLMlruCifoKuT.Rotate));
    private final BooleanSetting checkLook = this.add(new BooleanSetting("CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _PJzsesCBLMlruCifoKuT.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == _PJzsesCBLMlruCifoKuT.Rotate));
    private final EnumSetting<_EhoXCPdRwyZMhABJnmhT> targetMode = this.add(new EnumSetting<_EhoXCPdRwyZMhABJnmhT>("Filter", _EhoXCPdRwyZMhABJnmhT.DISTANCE, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Target));
    private final EnumSetting<Aura_nurTqHTNjexQmuWdDgIn> mode = this.add(new EnumSetting<Aura_nurTqHTNjexQmuWdDgIn>("TargetESP", Aura_nurTqHTNjexQmuWdDgIn.Jello, v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Render));
    private final ColorSetting color = this.add(new ColorSetting("TargetColor", new Color(255, 255, 255, 250), v -> this.page.getValue() == _PJzsesCBLMlruCifoKuT.Render));
    private final Timer ghostTimer = new Timer();
    private final Timer tick = new Timer();
    public Vec3d directionVec = null;
    public boolean sweeping = false;
    int attackTicks;
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;

    public Aura() {
        super("Aura", "Attacks players in radius", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static void doRender(MatrixStack matrixStack, float partialTicks, Entity entity, Color color, Aura_nurTqHTNjexQmuWdDgIn mode) {
        switch (mode.ordinal()) {
            case 0: {
                Render3DUtil.draw3DBox(matrixStack, ((IEntity)entity).getDimensions().method_30757(new Vec3d(MathUtil.interpolate(entity.field_6038, entity.method_23317(), partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), partialTicks))).method_1009(0.0, 0.1, 0.0), color, false, true);
                break;
            }
            case 1: {
                JelloUtil.drawJello(matrixStack, entity, color);
                break;
            }
            case 2: {
                JelloUtil.drawJello(matrixStack, entity, color);
            }
        }
    }

    public static float getAttackCooldownProgressPerTick() {
        return (float)(1.0 / Aura.mc.field_1724.method_26825(EntityAttributes.field_23723) * 20.0);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.tick.passed(50L)) {
            ++this.attackTicks;
            this.tick.reset();
        }
        if (target != null) {
            Aura.doRender(matrixStack, partialTicks, target, this.color.getValue(), this.mode.getValue());
        }
    }

    @Override
    public String getInfo() {
        return target == null ? null : target.method_5477().getString();
    }

    @Override
    public void onUpdate() {
        if (this.tick.passed(50L)) {
            ++this.attackTicks;
            this.tick.reset();
        }
        if (this.weaponOnly.getValue() && !EntityUtil.isHoldingWeapon((PlayerEntity)Aura.mc.field_1724)) {
            target = null;
            return;
        }
        target = this.getTarget();
        if (target == null) {
            return;
        }
        if (this.check()) {
            this.doAura();
        }
    }

    @EventHandler(priority=98)
    public void onRotate(RotateEvent event) {
        if (target != null && this.newRotate.getValue() && this.directionVec != null) {
            float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
            this.lastYaw = newAngle[0];
            this.lastPitch = newAngle[1];
            event.setYaw(this.lastYaw);
            event.setPitch(this.lastPitch);
        } else {
            this.lastYaw = HexTech.ROTATE.lastYaw;
            this.lastPitch = RotateManager.lastPitch;
        }
    }

    private boolean check() {
        int at = this.attackTicks;
        if (this.cd.getValue() == _VwTXxsLfDdMNyKpyAwyl.Vanilla) {
            at = ((ILivingEntity)Aura.mc.field_1724).getLastAttackedTicks();
        }
        if (!((double)Math.max((float)at / Aura.getAttackCooldownProgressPerTick(), 0.0f) >= this.cooldown.getValue())) {
            return false;
        }
        if (this.ghost.getValue()) {
            if (!this.ghostTimer.passedMs(600L)) {
                return false;
            }
            if (InventoryUtil.findClassInventorySlot(SwordItem.class) == -1) {
                return false;
            }
        }
        return this.whileEating.getValue() || !Aura.mc.field_1724.method_6115();
    }

    private void doAura() {
        if (!this.check()) {
            return;
        }
        if (this.rotate.getValue() && !this.faceVector(target.method_19538().method_1031(0.0, 1.5, 0.0))) {
            return;
        }
        int slot = InventoryUtil.findItemInventorySlot(Items.field_22022);
        if (this.ghost.getValue()) {
            this.sweeping = true;
            InventoryUtil.inventorySwap(slot, Aura.mc.field_1724.method_31548().field_7545);
        }
        this.ghostTimer.reset();
        if (!this.ghost.getValue() && Criticals.INSTANCE.isOn()) {
            Criticals.INSTANCE.doCrit();
        }
        Aura.mc.field_1761.method_2918((PlayerEntity)Aura.mc.field_1724, target);
        EntityUtil.swingHand(Hand.field_5808, this.swingMode.getValue());
        if (this.ghost.getValue()) {
            InventoryUtil.inventorySwap(slot, Aura.mc.field_1724.method_31548().field_7545);
            this.sweeping = false;
        }
        this.attackTicks = 0;
    }

    public boolean faceVector(Vec3d directionVec) {
        if (!this.newRotate.getValue()) {
            EntityUtil.faceVectorNoStay(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (Math.abs(MathHelper.method_15393((float)(angle[0] - this.lastYaw))) < this.fov.getValueFloat() && Math.abs(MathHelper.method_15393((float)(angle[1] - this.lastPitch))) < this.fov.getValueFloat()) {
            EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            return true;
        }
        return !this.checkLook.getValue();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue();
        double maxHealth = 36.0;
        for (Entity entity : Aura.mc.field_1687.method_18112()) {
            if (!this.isEnemy(entity) || !Aura.mc.field_1724.method_6057(entity) && (double)Aura.mc.field_1724.method_5739(entity) > this.wallRange.getValue() || !CombatUtil.isValid(entity, this.range.getValue())) continue;
            if (target == null) {
                target = entity;
                distance = Aura.mc.field_1724.method_5739(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof PlayerEntity && EntityUtil.isArmorLow((PlayerEntity)entity, 10)) {
                target = entity;
                break;
            }
            if (this.targetMode.getValue() == _EhoXCPdRwyZMhABJnmhT.HEALTH && (double)EntityUtil.getHealth(entity) < maxHealth) {
                target = entity;
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (this.targetMode.getValue() != _EhoXCPdRwyZMhABJnmhT.DISTANCE || !((double)Aura.mc.field_1724.method_5739(entity) < distance)) continue;
            target = entity;
            distance = Aura.mc.field_1724.method_5739(entity);
        }
        return target;
    }

    private boolean isEnemy(Entity entity) {
        if (entity instanceof SlimeEntity && this.Slimes.getValue()) {
            return true;
        }
        if (entity instanceof PlayerEntity && this.Players.getValue()) {
            return true;
        }
        if (entity instanceof VillagerEntity && this.Villagers.getValue()) {
            return true;
        }
        if (!(entity instanceof VillagerEntity) && entity instanceof MobEntity && this.Mobs.getValue()) {
            return true;
        }
        return entity instanceof AnimalEntity && this.Animals.getValue();
    }

    private float[] injectStep(float[] angle, float steps) {
        if (steps < 0.1f) {
            steps = 0.1f;
        }
        if (steps > 1.0f) {
            steps = 1.0f;
        }
        if (steps < 1.0f && angle != null) {
            float packetPitch;
            float packetYaw = this.lastYaw;
            float diff = MathHelper.method_15393((float)(angle[0] - packetYaw));
            if (Math.abs(diff) > 90.0f * steps) {
                angle[0] = packetYaw + diff * (90.0f * steps / Math.abs(diff));
            }
            if (Math.abs(diff = angle[1] - (packetPitch = this.lastPitch)) > 90.0f * steps) {
                angle[1] = packetPitch + diff * (90.0f * steps / Math.abs(diff));
            }
        }
        return new float[]{angle[0], angle[1]};
    }

    public static final class _PJzsesCBLMlruCifoKuT
    extends Enum<_PJzsesCBLMlruCifoKuT> {
        public static final /* enum */ _PJzsesCBLMlruCifoKuT General;
        public static final /* enum */ _PJzsesCBLMlruCifoKuT Rotate;
        public static final /* enum */ _PJzsesCBLMlruCifoKuT Target;
        public static final /* enum */ _PJzsesCBLMlruCifoKuT Render;

        public static _PJzsesCBLMlruCifoKuT[] values() {
            return null;
        }

        public static _PJzsesCBLMlruCifoKuT valueOf(String string) {
            return null;
        }
    }

    public static final class _VwTXxsLfDdMNyKpyAwyl
    extends Enum<_VwTXxsLfDdMNyKpyAwyl> {
        public static final /* enum */ _VwTXxsLfDdMNyKpyAwyl Vanilla;
        public static final /* enum */ _VwTXxsLfDdMNyKpyAwyl Delay;

        public static _VwTXxsLfDdMNyKpyAwyl[] values() {
            return null;
        }

        public static _VwTXxsLfDdMNyKpyAwyl valueOf(String string) {
            return null;
        }
    }

    private static final class _EhoXCPdRwyZMhABJnmhT
    extends Enum<_EhoXCPdRwyZMhABJnmhT> {
        public static final /* enum */ _EhoXCPdRwyZMhABJnmhT DISTANCE;
        public static final /* enum */ _EhoXCPdRwyZMhABJnmhT HEALTH;

        public static _EhoXCPdRwyZMhABJnmhT[] values() {
            return null;
        }

        public static _EhoXCPdRwyZMhABJnmhT valueOf(String string) {
            return null;
        }
    }
}
