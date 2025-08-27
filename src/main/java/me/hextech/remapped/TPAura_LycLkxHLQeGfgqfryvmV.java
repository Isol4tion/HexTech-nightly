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
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.Hand;

public class TPAura_LycLkxHLQeGfgqfryvmV extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static TPAura_LycLkxHLQeGfgqfryvmV INSTANCE;
   public static LivingEntity target;
   public static boolean attacking;
   public final EnumSetting<TPAura> page = this.add(new EnumSetting("Page", TPAura.General));
   public final SliderSetting range = this.add(new SliderSetting("Range", 60.0, 0.1F, 250.0, v -> this.page.getValue() == TPAura.General));
   public final BooleanSetting Players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == TPAura.Target));
   public final BooleanSetting Mobs = this.add(new BooleanSetting("Mobs", true, v -> this.page.getValue() == TPAura.Target));
   public final BooleanSetting Animals = this.add(new BooleanSetting("Animals", true, v -> this.page.getValue() == TPAura.Target));
   public final BooleanSetting Villagers = this.add(new BooleanSetting("Villagers", true, v -> this.page.getValue() == TPAura.Target));
   public final BooleanSetting Slimes = this.add(new BooleanSetting("Slimes", true, v -> this.page.getValue() == TPAura.Target));
   private final SliderSetting cooldown = this.add(new SliderSetting("Cooldown", 1.1F, 0.0, 1.2F, 0.01, v -> this.page.getValue() == TPAura.General));
   private final EnumSetting<Aura_VwTXxsLfDdMNyKpyAwyl> cd = this.add(new EnumSetting("CooldownMode", Aura_VwTXxsLfDdMNyKpyAwyl.Delay));
   private final BooleanSetting whileEating = this.add(new BooleanSetting("WhileUsing", true, v -> this.page.getValue() == TPAura.General));
   private final BooleanSetting cc = this.add(new BooleanSetting("TPOff", true, v -> this.page.getValue() == TPAura.General));
   private final BooleanSetting test = this.add(new BooleanSetting("TPOffTest", true, v -> this.page.getValue() == TPAura.General));
   private final BooleanSetting weaponOnly = this.add(new BooleanSetting("WeaponOnly", true, v -> this.page.getValue() == TPAura.General));
   private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting("Swing", SwingSide.Server, v -> this.page.getValue() == TPAura.General));
   private final EnumSetting<TPAura_XzEqwIDEtUaoPqXpLXTU> targetMode = this.add(
      new EnumSetting("Filter", TPAura_XzEqwIDEtUaoPqXpLXTU.DISTANCE, v -> this.page.getValue() == TPAura.Target)
   );
   private final Timer tick = new Timer();
   int attackTicks;
   private ArrayList<Vec3> lastPath;

   public TPAura_LycLkxHLQeGfgqfryvmV() {
      super("TPAura", "Attacks players in radius", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static float getAttackCooldownProgressPerTick() {
      return (float)(1.0 / mc.field_1724.method_26825(EntityAttributes.field_23723) * 20.0);
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (this.tick.passed(50L)) {
         this.attackTicks++;
         this.tick.reset();
      }

      if (this.lastPath != null) {
         for (Vec3 vec3 : this.lastPath) {
            Render3DUtil.draw3DBox(matrixStack, ((IEntity)mc.field_1724).getDimensions().method_30757(vec3.mc()), new Color(255, 255, 255, 150), true, true);
         }
      }
   }

   @Override
   public void onUpdate() {
      if (this.tick.passed(50L)) {
         this.attackTicks++;
         this.tick.reset();
      }

      if (this.weaponOnly.getValue() && !EntityUtil.isHoldingWeapon(mc.field_1724)) {
         target = null;
      } else {
         target = this.getTarget();
         if (target != null) {
            if (this.auraReady()) {
               this.doTPHit(target);
            } else {
               target = null;
            }
         }
      }
   }

   private boolean auraReady() {
      int at = this.attackTicks;
      if (this.cd.getValue() == Aura_VwTXxsLfDdMNyKpyAwyl.Vanilla) {
         at = ((ILivingEntity)mc.field_1724).getLastAttackedTicks();
      }

      return !((double)Math.max((float)at / getAttackCooldownProgressPerTick(), 0.0F) >= this.cooldown.getValue())
         ? false
         : this.whileEating.getValue() || !mc.field_1724.method_6115();
   }

   private LivingEntity getTarget() {
      LivingEntity target = null;
      double distance = this.range.getValue();
      double maxHealth = 36.0;

      for (Entity e : mc.field_1687.method_18112()) {
         if (e instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity)e;
            if (this.isEnemy(entity) && CombatUtil.isValid(entity, this.range.getValue())) {
               if (target == null) {
                  target = entity;
                  distance = (double)mc.field_1724.method_5739(entity);
                  maxHealth = (double)EntityUtil.getHealth(entity);
               } else {
                  if (entity instanceof PlayerEntity && EntityUtil.isArmorLow((PlayerEntity)entity, 10)) {
                     target = entity;
                     break;
                  }

                  if (this.targetMode.getValue() == TPAura_XzEqwIDEtUaoPqXpLXTU.HEALTH && (double)EntityUtil.getHealth(entity) < maxHealth) {
                     target = entity;
                     maxHealth = (double)EntityUtil.getHealth(entity);
                  } else if (this.targetMode.getValue() == TPAura_XzEqwIDEtUaoPqXpLXTU.DISTANCE && (double)mc.field_1724.method_5739(entity) < distance) {
                     target = entity;
                     distance = (double)mc.field_1724.method_5739(entity);
                  }
               }
            }
         }
      }

      return target;
   }

   private void doTPHit(LivingEntity entity) {
      attacking = true;
      List<Vec3> tpPath = PathUtils.computePath(mc.field_1724, entity);
      this.lastPath = new ArrayList(tpPath);
      tpPath.forEach(vec3 -> mc.field_1724.field_3944.method_52787(new PositionAndOnGround(vec3.getX(), vec3.getY(), vec3.getZ(), false)));
      mc.field_1761.method_2918(mc.field_1724, target);
      EntityUtil.swingHand(Hand.field_5808, (SwingSide)this.swingMode.getValue());
      tpPath = Lists.reverse(tpPath);
      tpPath.forEach(vec3 -> mc.field_1724.field_3944.method_52787(new PositionAndOnGround(vec3.getX(), vec3.getY(), vec3.getZ(), false)));
      if (this.test.getValue()) {
         mc.field_1724.field_3944.method_52787(new PositionAndOnGround(0.0, -0.354844, 0.0, false));
         mc.field_1724.field_3944.method_52787(new PositionAndOnGround(0.0, 0.325488, 0.0, false));
         mc.field_1724.field_3944.method_52787(new PositionAndOnGround(0.0, -0.15441, 0.0, false));
         mc.field_1724.field_3944.method_52787(new PositionAndOnGround(0.0, -0.15444, 0.0, false));
      }

      attacking = false;
      this.attackTicks = 0;
   }

   private boolean isEnemy(Entity entity) {
      if (entity instanceof SlimeEntity && this.Slimes.getValue()) {
         return true;
      } else if (entity instanceof PlayerEntity && this.Players.getValue()) {
         return true;
      } else if (entity instanceof VillagerEntity && this.Villagers.getValue()) {
         return true;
      } else {
         return !(entity instanceof VillagerEntity) && entity instanceof MobEntity && this.Mobs.getValue()
            ? true
            : entity instanceof AnimalEntity && this.Animals.getValue();
      }
   }
}
