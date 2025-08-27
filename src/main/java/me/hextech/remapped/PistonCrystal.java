package me.hextech.remapped;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PistonCrystal extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static PistonCrystal INSTANCE;
   public final EnumSetting<PistonCrystal_nsRHxiHWZMPWnytkAhif> page = this.add(new EnumSetting("Page", PistonCrystal_nsRHxiHWZMPWnytkAhif.General));
   public final BooleanSetting yawDeceive = this.add(
      new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate)
   );
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", false, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate));
   private final BooleanSetting newRotate = this.add(
      new BooleanSetting("NewRotate", false, v -> this.rotate.isOpen() && this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate)
   );
   private final SliderSetting yawStep = this.add(
      new SliderSetting(
         "YawStep",
         0.3F,
         0.1F,
         1.0,
         0.01F,
         v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate
      )
   );
   private final BooleanSetting packet = this.add(
      new BooleanSetting(
         "Packet", false, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate
      )
   );
   private final BooleanSetting checkLook = this.add(
      new BooleanSetting(
         "CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate
      )
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov",
         5.0,
         0.0,
         30.0,
         v -> this.rotate.isOpen()
               && this.newRotate.getValue()
               && this.checkLook.getValue()
               && this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate
      )
   );
   private final BooleanSetting autoYaw = this.add(new BooleanSetting("AutoYaw", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Rotate));
   private final BooleanSetting preferAnchor = this.add(
      new BooleanSetting("PreferAnchor", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final BooleanSetting preferCrystal = this.add(
      new BooleanSetting("PreferCrystal", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting placeRange = this.add(
      new SliderSetting("PlaceRange", 5.0, 1.0, 8.0, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting range = this.add(
      new SliderSetting("TargetRange", 4.0, 1.0, 8.0, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting updateDelay = this.add(
      new SliderSetting("PlaceDelay", 100, 0, 500, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting posUpdateDelay = this.add(
      new SliderSetting("UpdateDelay", 500, 0, 1000, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting stageSetting = this.add(
      new SliderSetting("Stage", 4, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting pistonStage = this.add(
      new SliderSetting("PistonStage", 1, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting pistonMaxStage = this.add(
      new SliderSetting("PistonMaxStage", 1, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting powerStage = this.add(
      new SliderSetting("PowerStage", 3, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting powerMaxStage = this.add(
      new SliderSetting("PowerMaxStage", 3, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting crystalStage = this.add(
      new SliderSetting("CrystalStage", 4, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting crystalMaxStage = this.add(
      new SliderSetting("CrystalMaxStage", 4, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting fireStage = this.add(
      new SliderSetting("FireStage", 2, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final SliderSetting fireMaxStage = this.add(
      new SliderSetting("FireMaxStage", 2, 1, 10, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General)
   );
   private final BooleanSetting inventory = this.add(
      new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc)
   );
   private final BooleanSetting endSwing = this.add(new BooleanSetting("EndSwing", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc));
   private final BooleanSetting debug = this.add(new BooleanSetting("Debug", false, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc));
   private final BooleanSetting fire = this.add(new BooleanSetting("Fire", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc));
   private final BooleanSetting switchPos = this.add(new BooleanSetting("Switch", false, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc));
   private final BooleanSetting onlyGround = this.add(
      new BooleanSetting("SelfGround", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc)
   );
   private final BooleanSetting onlyStatic = this.add(
      new BooleanSetting("MovingPause", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc)
   );
   private final BooleanSetting noEating = this.add(new BooleanSetting("NoEating", true, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc));
   private final BooleanSetting eatingBreak = this.add(
      new BooleanSetting("EatingBreak", false, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.Misc)
   );
   private final Timer timer = new Timer();
   private final Timer crystalTimer = new Timer();
   public SliderSetting speed = this.add(new SliderSetting("MaxSpeed", 8, 0, 20, v -> this.page.getValue() == PistonCrystal_nsRHxiHWZMPWnytkAhif.General));
   public Vec3d directionVec = null;
   public BlockPos bestPos = null;
   public BlockPos bestOPos = null;
   public Direction bestFacing = null;
   public double distance = 100.0;
   public boolean getPos = false;
   public int stage = 1;
   private PlayerEntity target = null;
   private float lastYaw = 0.0F;
   private float lastPitch = 0.0F;

   public PistonCrystal() {
      super("PistonCrystal", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static void pistonFacing(Direction i) {
      if (i == Direction.field_11034) {
         EntityUtil.sendYawAndPitch(-90.0F, 5.0F);
      } else if (i == Direction.field_11039) {
         EntityUtil.sendYawAndPitch(90.0F, 5.0F);
      } else if (i == Direction.field_11043) {
         EntityUtil.sendYawAndPitch(180.0F, 5.0F);
      } else if (i == Direction.field_11035) {
         EntityUtil.sendYawAndPitch(0.0F, 5.0F);
      }
   }

   private static boolean canFire(BlockPos pos) {
      if (BlockUtil.canReplace(pos.method_10074())) {
         return false;
      } else if (mc.field_1687 != null && !mc.field_1687.method_22347(pos)) {
         return false;
      } else {
         return !BlockUtil.canClick(pos.method_10093(Direction.field_11033)) ? false : BlockUtil.isStrictDirection(pos.method_10074(), Direction.field_11036);
      }
   }

   @EventHandler
   public void onRotate(RotateEvent event) {
      if (this.newRotate.getValue() && this.directionVec != null) {
         float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
         if (newAngle != null) {
            this.lastYaw = newAngle[0];
         }

         if (newAngle != null) {
            this.lastPitch = newAngle[1];
         }

         event.setYaw(this.lastYaw);
         event.setPitch(this.lastPitch);
      } else {
         this.lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
         this.lastPitch = RotateManager.lastPitch;
      }
   }

   public void onTick() {
      if (this.pistonStage.getValue() > this.stageSetting.getValue()) {
         this.pistonStage.setValue(this.stageSetting.getValue());
      }

      if (this.fireStage.getValue() > this.stageSetting.getValue()) {
         this.fireStage.setValue(this.stageSetting.getValue());
      }

      if (this.powerStage.getValue() > this.stageSetting.getValue()) {
         this.powerStage.setValue(this.stageSetting.getValue());
      }

      if (this.crystalStage.getValue() > this.stageSetting.getValue()) {
         this.crystalStage.setValue(this.stageSetting.getValue());
      }

      if (this.pistonMaxStage.getValue() > this.stageSetting.getValue()) {
         this.pistonMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.fireMaxStage.getValue() > this.stageSetting.getValue()) {
         this.fireMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.powerMaxStage.getValue() > this.stageSetting.getValue()) {
         this.powerMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.crystalMaxStage.getValue() > this.stageSetting.getValue()) {
         this.crystalMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.crystalMaxStage.getValue() < this.crystalStage.getValue()) {
         this.crystalStage.setValue(this.crystalMaxStage.getValue());
      }

      if (this.powerMaxStage.getValue() < this.powerStage.getValue()) {
         this.powerStage.setValue(this.powerMaxStage.getValue());
      }

      if (this.pistonMaxStage.getValue() < this.pistonStage.getValue()) {
         this.pistonStage.setValue(this.pistonMaxStage.getValue());
      }

      if (this.fireMaxStage.getValue() < this.fireStage.getValue()) {
         this.fireStage.setValue(this.fireMaxStage.getValue());
      }
   }

   @Override
   public void onUpdate() {
      this.onTick();
      this.directionVec = null;
      this.target = CombatUtil.getClosestEnemy(this.range.getValue());
      if (this.target != null) {
         if (!this.preferAnchor.getValue() || AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos == null) {
            if (!this.preferCrystal.getValue() || AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null) {
               if (!this.noEating.getValue() || !EntityUtil.isUsing()) {
                  if (mc.field_1724 == null || !this.check(this.onlyStatic.getValue(), !mc.field_1724.method_24828(), this.onlyGround.getValue())) {
                     BlockPos pos = EntityUtil.getEntityPos(this.target, true);
                     if (!EntityUtil.isUsing() || this.eatingBreak.getValue()) {
                        if (this.checkCrystal(pos.method_10086(0))) {
                           this.attackCrystal(pos.method_10086(0), this.rotate.getValue(), false);
                        }

                        if (this.checkCrystal(pos.method_10086(1))) {
                           this.attackCrystal(pos.method_10086(1), this.rotate.getValue(), false);
                        }

                        if (this.checkCrystal(pos.method_10086(2))) {
                           this.attackCrystal(pos.method_10086(2), this.rotate.getValue(), false);
                        }
                     }

                     if (mc.field_1687 != null && this.bestPos != null) {
                        mc.field_1687.method_8320(this.bestPos).method_26204();
                     }

                     if (this.crystalTimer.passedMs((long)this.posUpdateDelay.getValueInt())) {
                        this.stage = 0;
                        this.distance = 100.0;
                        this.getPos = false;
                        this.getBestPos(pos.method_10086(2));
                        this.getBestPos(pos.method_10084());
                     }

                     if (this.timer.passedMs((long)this.updateDelay.getValueInt())) {
                        if (this.getPos && this.bestPos != null) {
                           this.timer.reset();
                           if (this.debug.getValue()) {
                              CommandManager.sendChatMessage(
                                 "[Debug] PistonPos:"
                                    + this.bestPos
                                    + " Facing:"
                                    + this.bestFacing
                                    + " CrystalPos:"
                                    + this.bestOPos.method_10093(this.bestFacing)
                              );
                           }

                           if (this.check(this.onlyStatic.getValue(), !mc.field_1724.method_24828(), this.onlyGround.getValue())) {
                              return;
                           }

                           this.doPistonAura(this.bestPos, this.bestFacing, this.bestOPos);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void attackCrystal(BlockPos pos, boolean rotate, boolean eatingPause) {
      Iterator var4 = mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos)).iterator();
      if (var4.hasNext()) {
         EndCrystalEntity entity = (EndCrystalEntity)var4.next();
         this.attackCrystal(entity, rotate, eatingPause);
      }
   }

   public void attackCrystal(Entity crystal, boolean rotate, boolean usingPause) {
      if (CombatUtil.breakTimer.passedMs((long)(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackDelay.getValue() * 1000.0))) {
         if (!usingPause || !EntityUtil.isUsing()) {
            if (crystal != null) {
               CombatUtil.breakTimer.reset();
               if (rotate
                  && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue()
                  && !this.faceVector(new Vec3d(crystal.method_23317(), crystal.method_23318() + 0.25, crystal.method_23321()))) {
                  return;
               }

               mc.field_1724.field_3944.method_52787(PlayerInteractEntityC2SPacket.method_34206(crystal, mc.field_1724.method_5715()));
               mc.field_1724.method_7350();
               EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
            }
         }
      }
   }

   public boolean check(boolean onlyStatic, boolean onGround, boolean onlyGround) {
      if (MovementUtil.isMoving() && onlyStatic) {
         return true;
      } else if (onGround && onlyGround) {
         return true;
      } else if (this.findBlock(Blocks.field_10002) == -1) {
         return true;
      } else {
         return this.findClass(PistonBlock.class) == -1 ? true : this.findItem(Items.field_8301) == -1;
      }
   }

   private boolean checkCrystal(BlockPos pos) {
      if (mc.field_1687 != null) {
         for (Entity entity : mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos))) {
            float damage = ThunderExplosionUtil.calculateDamage(entity.method_19538(), this.target, this.target, 6.0F);
            if (damage > 6.0F) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean checkCrystal2(BlockPos pos) {
      if (mc.field_1687 != null) {
         for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
            if (entity instanceof EndCrystalEntity && EntityUtil.getEntityPos(entity).equals(pos)) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public String getInfo() {
      return this.target != null ? this.target.method_5477().getString() : null;
   }

   private void getBestPos(BlockPos pos) {
      for (Direction i : Direction.values()) {
         if (i != Direction.field_11033 && i != Direction.field_11036) {
            this.getPos(pos, i);
         }
      }
   }

   private void getPos(BlockPos pos, Direction i) {
      if (BlockUtil.canPlaceCrystal(pos.method_10093(i)) || this.checkCrystal2(pos.method_10093(i))) {
         this.getPos(pos.method_10079(i, 3), i, pos);
         this.getPos(pos.method_10079(i, 3).method_10084(), i, pos);
         int offsetX = pos.method_10093(i).method_10263() - pos.method_10263();
         int offsetZ = pos.method_10093(i).method_10260() - pos.method_10260();
         this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 0, offsetX), i, pos);
         this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 0, -offsetX), i, pos);
         this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 1, offsetX), i, pos);
         this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 1, -offsetX), i, pos);
         this.getPos(pos.method_10079(i, 2), i, pos);
         this.getPos(pos.method_10079(i, 2).method_10084(), i, pos);
         this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 0, offsetX), i, pos);
         this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 0, -offsetX), i, pos);
         this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 1, offsetX), i, pos);
         this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 1, -offsetX), i, pos);
      }
   }

   private void getPos(BlockPos pos, Direction facing, BlockPos oPos) {
      if (mc.field_1687 == null || !this.switchPos.getValue() || this.bestPos == null || !this.bestPos.equals(pos) || !mc.field_1687.method_22347(this.bestPos)
         )
       {
         if (BlockUtil.canPlace(pos, this.placeRange.getValue()) || this.getBlock(pos) instanceof PistonBlock) {
            if (this.findClass(PistonBlock.class) != -1) {
               if (!(this.getBlock(pos) instanceof PistonBlock)) {
                  if (mc.field_1724 != null
                     && (mc.field_1724.method_23318() - (double)pos.method_10264() <= -2.0 || mc.field_1724.method_23318() - (double)pos.method_10264() >= 3.0)
                     && BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6) {
                     return;
                  }

                  if (!this.isTrueFacing(pos, facing)) {
                     return;
                  }
               }

               if (mc.field_1687.method_22347(pos.method_10079(facing, -1))
                  && mc.field_1687.method_8320(pos.method_10079(facing, -1)).method_26204() != Blocks.field_10036
                  && (
                     this.getBlock(pos.method_10093(facing.method_10153())) != Blocks.field_10008
                        || this.checkCrystal2(pos.method_10093(facing.method_10153()))
                  )) {
                  if (BlockUtil.canPlace(pos, this.placeRange.getValue()) || this.isPiston(pos, facing)) {
                     if ((double)MathHelper.method_15355((float)EntityUtil.getEyesPos().method_1025(pos.method_46558())) < this.distance
                        || this.bestPos == null) {
                        this.bestPos = pos;
                        this.bestOPos = oPos;
                        this.bestFacing = facing;
                        this.distance = (double)MathHelper.method_15355((float)EntityUtil.getEyesPos().method_1025(pos.method_46558()));
                        this.getPos = true;
                        this.crystalTimer.reset();
                     }
                  }
               }
            }
         }
      }
   }

   private void doPistonAura(BlockPos pos, Direction facing, BlockPos oPos) {
      if ((double)this.stage >= this.stageSetting.getValue()) {
         this.stage = 0;
      }

      this.stage++;
      if (mc.field_1687 != null && mc.field_1687.method_22347(pos)) {
         if (!BlockUtil.canPlace(pos)) {
            return;
         }

         if ((double)this.stage >= this.pistonStage.getValue() && (double)this.stage <= this.pistonMaxStage.getValue()) {
            Direction side = BlockUtil.getPlaceSide(pos);
            if (side == null) {
               return;
            }

            int old = 0;
            if (mc.field_1724 != null) {
               old = mc.field_1724.method_31548().field_7545;
            }

            BlockPos neighbour = pos.method_10093(side);
            Direction opposite = side.method_10153();
            if (this.rotate.getValue()) {
               Vec3d hitVec = pos.method_46558()
                  .method_1019(
                     new Vec3d(
                        (double)side.method_10163().method_10263() * 0.5,
                        (double)side.method_10163().method_10264() * 0.5,
                        (double)side.method_10163().method_10260() * 0.5
                     )
                  );
               if (!this.faceVector(hitVec)) {
                  return;
               }
            }

            if (this.shouldYawCheck()) {
               pistonFacing(facing);
            }

            int piston = this.findClass(PistonBlock.class);
            this.doSwap(piston);
            this.placeBlock(pos, false, this.endSwing.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(piston);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

            if (this.rotate.getValue()) {
               EntityUtil.facePosSide(neighbour, opposite);
            }
         }
      }

      if ((double)this.stage >= this.powerStage.getValue() && (double)this.stage <= this.powerMaxStage.getValue()) {
         this.doRedStone(pos, facing, oPos.method_10093(facing));
      }

      if ((double)this.stage >= this.crystalStage.getValue() && (double)this.stage <= this.crystalMaxStage.getValue()) {
         this.placeCrystal(oPos, facing);
      }

      if ((double)this.stage >= this.fireStage.getValue() && (double)this.stage <= this.fireMaxStage.getValue()) {
         this.doFire(oPos, facing);
      }
   }

   private void placeCrystal(BlockPos pos, Direction facing) {
      if (BlockUtil.canPlaceCrystal(pos.method_10093(facing))) {
         int crystal = this.findItem(Items.field_8301);
         if (crystal != -1) {
            int old = 0;
            if (mc.field_1724 != null) {
               old = mc.field_1724.method_31548().field_7545;
            }

            this.doSwap(crystal);
            this.placeCrystal(pos.method_10093(facing), this.rotate.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(crystal);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }
         }
      }
   }

   public void placeCrystal(BlockPos pos, boolean rotate) {
      BlockPos obsPos = pos.method_10074();
      Direction facing = BlockUtil.getClickSide(obsPos);
      this.clickBlock(obsPos, facing, rotate);
   }

   private boolean isPiston(BlockPos pos, Direction facing) {
      if (mc.field_1687 != null && !(mc.field_1687.method_8320(pos).method_26204() instanceof PistonBlock)) {
         return false;
      } else {
         return ((Direction)mc.field_1687.method_8320(pos).method_11654(FacingBlock.field_10927)).method_10153() != facing
            ? false
            : mc.field_1687.method_22347(pos.method_10079(facing, -1))
               || this.getBlock(pos.method_10079(facing, -1)) == Blocks.field_10036
               || this.getBlock(pos.method_10093(facing.method_10153())) == Blocks.field_10008;
      }
   }

   private void doFire(BlockPos pos, Direction facing) {
      if (this.fire.getValue()) {
         int fire = this.findItem(Items.field_8884);
         if (fire != -1) {
            int old = 0;
            if (mc.field_1724 != null) {
               old = mc.field_1724.method_31548().field_7545;
            }

            int[] xOffset = new int[]{0, facing.method_10165(), -facing.method_10165()};
            int[] yOffset = new int[]{0, 1};
            int[] zOffset = new int[]{0, facing.method_10148(), -facing.method_10148()};

            for (int x : xOffset) {
               for (int y : yOffset) {
                  for (int z : zOffset) {
                     if (this.getBlock(pos.method_10069(x, y, z)) == Blocks.field_10036) {
                        return;
                     }
                  }
               }
            }

            for (int x : xOffset) {
               for (int y : yOffset) {
                  for (int zx : zOffset) {
                     if (canFire(pos.method_10069(x, y, zx))) {
                        this.doSwap(fire);
                        this.placeFire(pos.method_10069(x, y, zx));
                        if (this.inventory.getValue()) {
                           this.doSwap(fire);
                           EntityUtil.syncInventory();
                        } else {
                           this.doSwap(old);
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   public void placeFire(BlockPos pos) {
      BlockPos neighbour = pos.method_10093(Direction.field_11033);
      this.clickBlock(neighbour, Direction.field_11036, this.rotate.getValue());
   }

   private void doRedStone(BlockPos pos, Direction facing, BlockPos crystalPos) {
      if (mc.field_1687 == null
         || mc.field_1687.method_22347(pos.method_10079(facing, -1))
         || this.getBlock(pos.method_10079(facing, -1)) == Blocks.field_10036
         || this.getBlock(pos.method_10093(facing.method_10153())) == Blocks.field_10008) {
         for (Direction i : Direction.values()) {
            if (this.getBlock(pos.method_10093(i)) == Blocks.field_10002) {
               return;
            }
         }

         int power = this.findBlock(Blocks.field_10002);
         if (power != -1) {
            int old = 0;
            if (mc.field_1724 != null) {
               old = mc.field_1724.method_31548().field_7545;
            }

            Direction bestNeighboring = BlockUtil.getBestNeighboring(pos, facing);
            if (bestNeighboring != null
               && bestNeighboring != facing.method_10153()
               && BlockUtil.canPlace(pos.method_10093(bestNeighboring), this.placeRange.getValue())
               && !pos.method_10093(bestNeighboring).equals(crystalPos)) {
               this.doSwap(power);
               this.placeBlock(pos.method_10093(bestNeighboring), this.rotate.getValue(), this.endSwing.getValue());
               if (this.inventory.getValue()) {
                  this.doSwap(power);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(old);
               }
            } else {
               for (Direction ix : Direction.values()) {
                  if (BlockUtil.canPlace(pos.method_10093(ix), this.placeRange.getValue())
                     && !pos.method_10093(ix).equals(crystalPos)
                     && ix != facing.method_10153()) {
                     this.doSwap(power);
                     this.placeBlock(pos.method_10093(ix), this.rotate.getValue(), this.endSwing.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(power);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }

                     return;
                  }
               }
            }
         }
      }
   }

   private boolean shouldYawCheck() {
      return this.yawDeceive.getValue() || this.autoYaw.getValue() && !EntityUtil.isInsideBlock();
   }

   private boolean isTrueFacing(BlockPos pos, Direction facing) {
      if (this.shouldYawCheck()) {
         return true;
      } else {
         Direction side = BlockUtil.getPlaceSide(pos);
         if (side == null) {
            side = Direction.field_11036;
         }

         side = side.method_10153();
         Vec3d hitVec = pos.method_10093(side.method_10153())
            .method_46558()
            .method_1019(
               new Vec3d(
                  (double)side.method_10163().method_10263() * 0.5,
                  (double)side.method_10163().method_10264() * 0.5,
                  (double)side.method_10163().method_10260() * 0.5
               )
            );
         return Direction.method_10150((double)EntityUtil.getLegitRotations(hitVec)[0]) == facing;
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         if (mc.field_1724 != null) {
            InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
         }
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   public int findItem(Item itemIn) {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(itemIn) : InventoryUtil.findItem(itemIn);
   }

   public int findBlock(Block blockIn) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
   }

   public int findClass(Class clazz) {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(clazz) : InventoryUtil.findClass(clazz);
   }

   private Block getBlock(BlockPos pos) {
      return mc.field_1687 != null ? mc.field_1687.method_8320(pos).method_26204() : null;
   }

   public void placeBlock(BlockPos pos, boolean rotate, boolean bypass) {
      if (BlockUtil.airPlace()) {
         for (Direction i : Direction.values()) {
            if (mc.field_1687 != null && mc.field_1687.method_22347(pos.method_10093(i))) {
               this.clickBlock(pos, i, rotate);
               return;
            }
         }
      }

      Direction side = BlockUtil.getPlaceSide(pos);
      if (side != null) {
         Vec3d directionVec = new Vec3d(
            (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
            (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
            (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
         );
         EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
         BlockUtil.placedPos.add(pos);
         boolean sprint = false;
         if (mc.field_1724 != null) {
            sprint = mc.field_1724.method_5624();
         }

         boolean sneak = false;
         if (mc.field_1687 != null) {
            sneak = BlockUtil.needSneak(mc.field_1687.method_8320(result.method_17777()).method_26204()) && !mc.field_1724.method_5715();
         }

         if (sprint) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12985));
         }

         if (sneak) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12979));
         }

         this.clickBlock(pos.method_10093(side), side.method_10153(), rotate);
         if (sneak) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12984));
         }

         if (sprint) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12981));
         }

         if (bypass) {
            EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         }
      }
   }

   private float[] injectStep(float[] angle, float steps) {
      if (steps < 0.01F) {
         steps = 0.01F;
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

      return angle != null ? new float[]{angle[0], angle[1]} : null;
   }

   public boolean clickBlock(BlockPos pos, Direction side, boolean rotate) {
      Vec3d directionVec = new Vec3d(
         (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
         (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
         (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
      );
      if (rotate && !this.faceVector(directionVec)) {
         return false;
      } else {
         EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
         if (mc.field_1724 != null) {
            mc.field_1724.field_3944.method_52787(new PlayerInteractBlockC2SPacket(Hand.field_5808, result, BlockUtil.getWorldActionId(mc.field_1687)));
         }

         return true;
      }
   }

   private boolean faceVector(Vec3d directionVec) {
      if (!this.newRotate.getValue()) {
         EntityUtil.faceVector(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         float[] angle = EntityUtil.getLegitRotations(directionVec);
         if (Math.abs(MathHelper.method_15393(angle[0] - this.lastYaw)) < this.fov.getValueFloat()
            && Math.abs(MathHelper.method_15393(angle[1] - this.lastPitch)) < this.fov.getValueFloat()) {
            if (this.packet.getValue()) {
               EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }

            return true;
         } else {
            return !this.checkLook.getValue();
         }
      }
   }
}
