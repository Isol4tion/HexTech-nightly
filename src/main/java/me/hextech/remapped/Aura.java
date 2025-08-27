package me.hextech.remapped;

import java.awt.Color;
import me.hextech.asm.accessors.IEntity;
import me.hextech.asm.accessors.ILivingEntity;
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

public class Aura extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Aura INSTANCE;
   public static Entity target;
   public final EnumSetting<Aura_PJzsesCBLMlruCifoKuT> page = this.add(new EnumSetting("Page", Aura_PJzsesCBLMlruCifoKuT.General));
   public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 0.1F, 7.0, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General));
   public final BooleanSetting Players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Target));
   public final BooleanSetting Mobs = this.add(new BooleanSetting("Mobs", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Target));
   public final BooleanSetting Animals = this.add(new BooleanSetting("Animals", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Target));
   public final BooleanSetting Villagers = this.add(new BooleanSetting("Villagers", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Target));
   public final BooleanSetting Slimes = this.add(new BooleanSetting("Slimes", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Target));
   private final BooleanSetting ghost = this.add(new BooleanSetting("SweepingBypass", false, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General));
   private final EnumSetting<Aura_VwTXxsLfDdMNyKpyAwyl> cd = this.add(new EnumSetting("CooldownMode", Aura_VwTXxsLfDdMNyKpyAwyl.Delay));
   private final SliderSetting cooldown = this.add(
      new SliderSetting("Cooldown", 1.1F, 0.0, 1.2F, 0.01, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General)
   );
   private final SliderSetting wallRange = this.add(
      new SliderSetting("WallRange", 6.0, 0.1F, 7.0, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General)
   );
   private final BooleanSetting whileEating = this.add(new BooleanSetting("WhileUsing", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General));
   private final BooleanSetting weaponOnly = this.add(new BooleanSetting("WeaponOnly", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General));
   private final EnumSetting<SwingSide> swingMode = this.add(
      new EnumSetting("Swing", SwingSide.Server, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.General)
   );
   private final BooleanSetting rotate = this.add(
      new BooleanSetting("Rotation", true, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Rotate).setParent()
   );
   private final BooleanSetting newRotate = this.add(
      new BooleanSetting("NewRotate", true, v -> this.rotate.isOpen() && this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Rotate)
   );
   private final SliderSetting yawStep = this.add(
      new SliderSetting(
         "YawStep", 0.3F, 0.1F, 1.0, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Rotate
      )
   );
   private final BooleanSetting checkLook = this.add(
      new BooleanSetting("CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Rotate)
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov",
         5.0,
         0.0,
         30.0,
         v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Rotate
      )
   );
   private final EnumSetting<Aura_EhoXCPdRwyZMhABJnmhT> targetMode = this.add(
      new EnumSetting("Filter", Aura_EhoXCPdRwyZMhABJnmhT.DISTANCE, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Target)
   );
   private final EnumSetting<Aura_nurTqHTNjexQmuWdDgIn> mode = this.add(
      new EnumSetting("TargetESP", Aura_nurTqHTNjexQmuWdDgIn.Jello, v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Render)
   );
   private final ColorSetting color = this.add(
      new ColorSetting("TargetColor", new Color(255, 255, 255, 250), v -> this.page.getValue() == Aura_PJzsesCBLMlruCifoKuT.Render)
   );
   private final Timer ghostTimer = new Timer();
   private final Timer tick = new Timer();
   public Vec3d directionVec = null;
   public boolean sweeping = false;
   int attackTicks;
   private float lastYaw = 0.0F;
   private float lastPitch = 0.0F;

   public Aura() {
      super("Aura", "Attacks players in radius", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static void doRender(MatrixStack matrixStack, float partialTicks, Entity entity, Color color, Aura_nurTqHTNjexQmuWdDgIn mode) {
      switch (mode) {
         case Box:
            Render3DUtil.draw3DBox(
               matrixStack,
               ((IEntity)entity)
                  .getDimensions()
                  .method_30757(
                     new Vec3d(
                        MathUtil.interpolate(entity.field_6038, entity.method_23317(), partialTicks),
                        MathUtil.interpolate(entity.field_5971, entity.method_23318(), partialTicks),
                        MathUtil.interpolate(entity.field_5989, entity.method_23321(), partialTicks)
                     )
                  )
                  .method_1009(0.0, 0.1, 0.0),
               color,
               false,
               true
            );
            break;
         case Jello:
            JelloUtil.drawJello(matrixStack, entity, color);
            break;
         case NurikZapen:
            JelloUtil.drawJello(matrixStack, entity, color);
      }
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

      if (target != null) {
         doRender(matrixStack, partialTicks, target, this.color.getValue(), (Aura_nurTqHTNjexQmuWdDgIn)this.mode.getValue());
      }
   }

   @Override
   public String getInfo() {
      return target == null ? null : target.method_5477().getString();
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
            if (this.check()) {
               this.doAura();
            }
         }
      }
   }

   @EventHandler(
      priority = 98
   )
   public void onRotate(RotateEvent event) {
      if (target != null && this.newRotate.getValue() && this.directionVec != null) {
         float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
         this.lastYaw = newAngle[0];
         this.lastPitch = newAngle[1];
         event.setYaw(this.lastYaw);
         event.setPitch(this.lastPitch);
      } else {
         this.lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
         this.lastPitch = RotateManager.lastPitch;
      }
   }

   private boolean check() {
      int at = this.attackTicks;
      if (this.cd.getValue() == Aura_VwTXxsLfDdMNyKpyAwyl.Vanilla) {
         at = ((ILivingEntity)mc.field_1724).getLastAttackedTicks();
      }

      if (!((double)Math.max((float)at / getAttackCooldownProgressPerTick(), 0.0F) >= this.cooldown.getValue())) {
         return false;
      } else {
         if (this.ghost.getValue()) {
            if (!this.ghostTimer.passedMs(600L)) {
               return false;
            }

            if (InventoryUtil.findClassInventorySlot(SwordItem.class) == -1) {
               return false;
            }
         }

         return this.whileEating.getValue() || !mc.field_1724.method_6115();
      }
   }

   private void doAura() {
      if (this.check()) {
         if (!this.rotate.getValue() || this.faceVector(target.method_19538().method_1031(0.0, 1.5, 0.0))) {
            int slot = InventoryUtil.findItemInventorySlot(Items.field_22022);
            if (this.ghost.getValue()) {
               this.sweeping = true;
               InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
            }

            this.ghostTimer.reset();
            if (!this.ghost.getValue() && Criticals.INSTANCE.isOn()) {
               Criticals.INSTANCE.doCrit();
            }

            mc.field_1761.method_2918(mc.field_1724, target);
            EntityUtil.swingHand(Hand.field_5808, (SwingSide)this.swingMode.getValue());
            if (this.ghost.getValue()) {
               InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
               this.sweeping = false;
            }

            this.attackTicks = 0;
         }
      }
   }

   public boolean faceVector(Vec3d directionVec) {
      if (!this.newRotate.getValue()) {
         EntityUtil.faceVectorNoStay(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         float[] angle = EntityUtil.getLegitRotations(directionVec);
         if (Math.abs(MathHelper.method_15393(angle[0] - this.lastYaw)) < this.fov.getValueFloat()
            && Math.abs(MathHelper.method_15393(angle[1] - this.lastPitch)) < this.fov.getValueFloat()) {
            EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            return true;
         } else {
            return !this.checkLook.getValue();
         }
      }
   }

   private Entity getTarget() {
      Entity target = null;
      double distance = this.range.getValue();
      double maxHealth = 36.0;

      for (Entity entity : mc.field_1687.method_18112()) {
         if (this.isEnemy(entity)
            && (mc.field_1724.method_6057(entity) || !((double)mc.field_1724.method_5739(entity) > this.wallRange.getValue()))
            && CombatUtil.isValid(entity, this.range.getValue())) {
            if (target == null) {
               target = entity;
               distance = (double)mc.field_1724.method_5739(entity);
               maxHealth = (double)EntityUtil.getHealth(entity);
            } else {
               if (entity instanceof PlayerEntity && EntityUtil.isArmorLow((PlayerEntity)entity, 10)) {
                  target = entity;
                  break;
               }

               if (this.targetMode.getValue() == Aura_EhoXCPdRwyZMhABJnmhT.HEALTH && (double)EntityUtil.getHealth(entity) < maxHealth) {
                  target = entity;
                  maxHealth = (double)EntityUtil.getHealth(entity);
               } else if (this.targetMode.getValue() == Aura_EhoXCPdRwyZMhABJnmhT.DISTANCE && (double)mc.field_1724.method_5739(entity) < distance) {
                  target = entity;
                  distance = (double)mc.field_1724.method_5739(entity);
               }
            }
         }
      }

      return target;
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

   private float[] injectStep(float[] angle, float steps) {
      if (steps < 0.1F) {
         steps = 0.1F;
      }

      if (steps > 1.0F) {
         steps = 1.0F;
      }

      if (steps < 1.0F && angle != null) {
         float packetYaw = this.lastYaw;
         float diff = MathHelper.method_15393(angle[0] - packetYaw);
         if (Math.abs(diff) > 90.0F * steps) {
            angle[0] = packetYaw + diff * (90.0F * steps / Math.abs(diff));
         }

         float packetPitch = this.lastPitch;
         diff = angle[1] - packetPitch;
         if (Math.abs(diff) > 90.0F * steps) {
            angle[1] = packetPitch + diff * (90.0F * steps / Math.abs(diff));
         }
      }

      return new float[]{angle[0], angle[1]};
   }
}
