package me.hextech.remapped;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.hextech.asm.accessors.IEntity;
import me.hextech.asm.accessors.ILivingEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class TPAura_LycLkxHLQeGfgqfryvmV
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static TPAura_LycLkxHLQeGfgqfryvmV INSTANCE;
    public static LivingEntity target;
    public static boolean attacking;
    public final EnumSetting<TPAura> page = this.add(new EnumSetting<TPAura>("Page", TPAura.General));
    public final SliderSetting range = this.add(new SliderSetting("Range", 60.0, (double)0.1f, 250.0, v -> this.page.getValue() == TPAura.General));
    public final BooleanSetting Players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == TPAura.Target));
    public final BooleanSetting Mobs = this.add(new BooleanSetting("Mobs", true, v -> this.page.getValue() == TPAura.Target));
    public final BooleanSetting Animals = this.add(new BooleanSetting("Animals", true, v -> this.page.getValue() == TPAura.Target));
    public final BooleanSetting Villagers = this.add(new BooleanSetting("Villagers", true, v -> this.page.getValue() == TPAura.Target));
    public final BooleanSetting Slimes = this.add(new BooleanSetting("Slimes", true, v -> this.page.getValue() == TPAura.Target));
    private final SliderSetting cooldown = this.add(new SliderSetting("Cooldown", 1.1f, 0.0, 1.2f, 0.01, v -> this.page.getValue() == TPAura.General));
    private final EnumSetting<Aura._VwTXxsLfDdMNyKpyAwyl> cd = this.add(new EnumSetting<Aura._VwTXxsLfDdMNyKpyAwyl>("CooldownMode", Aura._VwTXxsLfDdMNyKpyAwyl.Delay));
    private final BooleanSetting whileEating = this.add(new BooleanSetting("WhileUsing", true, v -> this.page.getValue() == TPAura.General));
    private final BooleanSetting cc = this.add(new BooleanSetting("TPOff", true, v -> this.page.getValue() == TPAura.General));
    private final BooleanSetting test = this.add(new BooleanSetting("TPOffTest", true, v -> this.page.getValue() == TPAura.General));
    private final BooleanSetting weaponOnly = this.add(new BooleanSetting("WeaponOnly", true, v -> this.page.getValue() == TPAura.General));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.Server, v -> this.page.getValue() == TPAura.General));
    private final EnumSetting<TPAura_XzEqwIDEtUaoPqXpLXTU> targetMode = this.add(new EnumSetting<TPAura_XzEqwIDEtUaoPqXpLXTU>("Filter", TPAura_XzEqwIDEtUaoPqXpLXTU.DISTANCE, v -> this.page.getValue() == TPAura.Target));
    private final Timer tick = new Timer();
    int attackTicks;
    private ArrayList<Vec3> lastPath;

    public TPAura_LycLkxHLQeGfgqfryvmV() {
        super("TPAura", "Attacks players in radius", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static float getAttackCooldownProgressPerTick() {
        return (float)(1.0 / TPAura_LycLkxHLQeGfgqfryvmV.mc.player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) * 20.0);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.tick.passed(50L)) {
            ++this.attackTicks;
            this.tick.reset();
        }
        if (this.lastPath != null) {
            for (Vec3 vec3 : this.lastPath) {
                Render3DUtil.draw3DBox(matrixStack, ((IEntity)TPAura_LycLkxHLQeGfgqfryvmV.mc.player).getDimensions().getBoxAt(vec3.mc()), new Color(255, 255, 255, 150), true, true);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.tick.passed(50L)) {
            ++this.attackTicks;
            this.tick.reset();
        }
        if (this.weaponOnly.getValue() && !EntityUtil.isHoldingWeapon((PlayerEntity)TPAura_LycLkxHLQeGfgqfryvmV.mc.player)) {
            target = null;
            return;
        }
        target = this.getTarget();
        if (target == null) {
            return;
        }
        if (this.auraReady()) {
            this.doTPHit(target);
        } else {
            target = null;
        }
    }

    private boolean auraReady() {
        int at = this.attackTicks;
        if (this.cd.getValue() == Aura._VwTXxsLfDdMNyKpyAwyl.Vanilla) {
            at = ((ILivingEntity)TPAura_LycLkxHLQeGfgqfryvmV.mc.player).getLastAttackedTicks();
        }
        if (!((double)Math.max((float)at / TPAura_LycLkxHLQeGfgqfryvmV.getAttackCooldownProgressPerTick(), 0.0f) >= this.cooldown.getValue())) {
            return false;
        }
        return this.whileEating.getValue() || !TPAura_LycLkxHLQeGfgqfryvmV.mc.player.isUsingItem();
    }

    private LivingEntity getTarget() {
        LivingEntity target = null;
        double distance = this.range.getValue();
        double maxHealth = 36.0;
        for (Entity e : TPAura_LycLkxHLQeGfgqfryvmV.mc.world.getEntities()) {
            LivingEntity entity;
            if (!(e instanceof LivingEntity) || !this.isEnemy((Entity)(entity = (LivingEntity)e)) || !CombatUtil.isValid((Entity)entity, this.range.getValue())) continue;
            if (target == null) {
                target = entity;
                distance = TPAura_LycLkxHLQeGfgqfryvmV.mc.player.distanceTo((Entity)entity);
                maxHealth = EntityUtil.getHealth((Entity)entity);
                continue;
            }
            if (entity instanceof PlayerEntity && EntityUtil.isArmorLow((PlayerEntity)entity, 10)) {
                target = entity;
                break;
            }
            if (this.targetMode.getValue() == TPAura_XzEqwIDEtUaoPqXpLXTU.HEALTH && (double)EntityUtil.getHealth((Entity)entity) < maxHealth) {
                target = entity;
                maxHealth = EntityUtil.getHealth((Entity)entity);
                continue;
            }
            if (this.targetMode.getValue() != TPAura_XzEqwIDEtUaoPqXpLXTU.DISTANCE || !((double)TPAura_LycLkxHLQeGfgqfryvmV.mc.player.distanceTo((Entity)entity) < distance)) continue;
            target = entity;
            distance = TPAura_LycLkxHLQeGfgqfryvmV.mc.player.distanceTo((Entity)entity);
        }
        return target;
    }

    private void doTPHit(LivingEntity entity) {
        attacking = true;
        List<Vec3> tpPath = PathUtils.computePath((LivingEntity)TPAura_LycLkxHLQeGfgqfryvmV.mc.player, entity);
        this.lastPath = new ArrayList<Vec3>(tpPath);
        tpPath.forEach(vec3 -> TPAura_LycLkxHLQeGfgqfryvmV.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(vec3.getX(), vec3.getY(), vec3.getZ(), false)));
        TPAura_LycLkxHLQeGfgqfryvmV.mc.interactionManager.attackEntity((PlayerEntity)TPAura_LycLkxHLQeGfgqfryvmV.mc.player, (Entity)target);
        EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
        tpPath = Lists.reverse(tpPath);
        tpPath.forEach(vec3 -> TPAura_LycLkxHLQeGfgqfryvmV.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(vec3.getX(), vec3.getY(), vec3.getZ(), false)));
        if (this.test.getValue()) {
            TPAura_LycLkxHLQeGfgqfryvmV.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(0.0, -0.354844, 0.0, false));
            TPAura_LycLkxHLQeGfgqfryvmV.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(0.0, 0.325488, 0.0, false));
            TPAura_LycLkxHLQeGfgqfryvmV.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(0.0, -0.15441, 0.0, false));
            TPAura_LycLkxHLQeGfgqfryvmV.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(0.0, -0.15444, 0.0, false));
        }
        attacking = false;
        this.attackTicks = 0;
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

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum TPAura_XzEqwIDEtUaoPqXpLXTU {
        DISTANCE,
        HEALTH;

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum TPAura {
        General,
        Target;

    }
}
