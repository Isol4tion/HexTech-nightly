package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Beta
public class BedAura_BzCWaQEhnpenizjBqrRp extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static BedAura_BzCWaQEhnpenizjBqrRp INSTANCE;
   public static BlockPos placePos;
   public final EnumSetting<BedAura_mDouduXLLBVPsGyiReXU> page = this.add(new EnumSetting("Page", BedAura_mDouduXLLBVPsGyiReXU.General));
   public final EnumSetting<BedAura_uTGCNmVWbsqrhUODnXeN> mode = this.add(new EnumSetting("BedMod", BedAura_uTGCNmVWbsqrhUODnXeN.NullPoint));
   private final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General));
   private final BooleanSetting checkMine = this.add(
      new BooleanSetting("DetectMining", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final BooleanSetting noUsing = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General));
   private final EnumSetting<Enum_IKgLeKHCELPvcpdGlLhV> calcMode = this.add(
      new EnumSetting("CalcMode", Enum_IKgLeKHCELPvcpdGlLhV.OyVey, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final EnumSetting<SwingSide> swingMode = this.add(
      new EnumSetting("Swing", SwingSide.Server, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final SliderSetting antiSuicide = this.add(
      new SliderSetting("AntiSuicide", 3.0, 0.0, 10.0, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final SliderSetting targetRange = this.add(
      new SliderSetting("TargetRange", 12.0, 0.0, 20.0, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final SliderSetting updateDelay = this.add(
      new SliderSetting("UpdateDelay", 50, 0, 1000, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final SliderSetting calcDelay = this.add(
      new SliderSetting("CalcDelay", 200, 0, 1000, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final BooleanSetting inventorySwap = this.add(
      new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.General)
   );
   private final BooleanSetting rotate = this.add(
      new BooleanSetting("Rotation", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate).setParent()
   );
   private final BooleanSetting newRotate = this.add(
      new BooleanSetting("NewRotate", false, v -> this.rotate.isOpen() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate)
   );
   private final SliderSetting yawStep = this.add(
      new SliderSetting(
         "YawStep",
         0.3F,
         0.1F,
         1.0,
         0.01F,
         v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate
      )
   );
   private final BooleanSetting random = this.add(
      new BooleanSetting("Random", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate)
   );
   private final BooleanSetting sync = this.add(
      new BooleanSetting("Sync", false, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate)
   );
   private final BooleanSetting checkLook = this.add(
      new BooleanSetting(
         "CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate
      )
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov",
         5.0,
         0.0,
         30.0,
         v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Rotate
      )
   );
   private final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc));
   private final SliderSetting placeDelay = this.add(
      new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc && this.place.getValue())
   );
   private final BooleanSetting Break = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc));
   private final SliderSetting breakDelay = this.add(
      new SliderSetting("BreakDelay", 300, 0, 1000, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc && this.Break.getValue())
   );
   private final BooleanSetting breakOnlyHasCrystal = this.add(
      new BooleanSetting("OnlyHasBed", false, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc && this.Break.getValue())
   );
   private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc));
   private final SliderSetting placeMinDamage = this.add(
      new SliderSetting("MinDamage", 5.0, 0.0, 36.0, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc)
   );
   private final SliderSetting placeMaxSelf = this.add(
      new SliderSetting("MaxSelfDamage", 12.0, 0.0, 36.0, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc)
   );
   private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Calc));
   private final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render));
   private final BooleanSetting shrink = this.add(
      new BooleanSetting("Shrink", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final BooleanSetting outline = this.add(
      new BooleanSetting("Outline", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue()).setParent()
   );
   private final SliderSetting outlineAlpha = this.add(
      new SliderSetting(
         "OutlineAlpha", 150, 0, 255, v -> this.outline.isOpen() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue()
      )
   );
   private final BooleanSetting box = this.add(
      new BooleanSetting("Box", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue()).setParent()
   );
   private final SliderSetting boxAlpha = this.add(
      new SliderSetting("BoxAlpha", 70, 0, 255, v -> this.box.isOpen() && this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final BooleanSetting reset = this.add(
      new BooleanSetting("Reset", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final ColorSetting color = this.add(
      new ColorSetting("Color", new Color(255, 255, 255), v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final SliderSetting animationTime = this.add(
      new SliderSetting("AnimationTime", 2.0, 0.0, 8.0, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final SliderSetting startFadeTime = this.add(
      new SliderSetting("StartFadeTime", 0.3, 0.0, 2.0, 0.01, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final SliderSetting fadeTime = this.add(
      new SliderSetting("FadeTime", 0.3, 0.0, 2.0, 0.01, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Render && this.render.getValue())
   );
   private final SliderSetting predictTicks = this.add(
      new SliderSetting("PredictTicks", 4, 0, 10, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Predict)
   );
   private final BooleanSetting terrainIgnore = this.add(
      new BooleanSetting("TerrainIgnore", true, v -> this.page.getValue() == BedAura_mDouduXLLBVPsGyiReXU.Predict)
   );
   private final Timer delayTimer = new Timer();
   private final Timer calcTimer = new Timer();
   private final Timer breakTimer = new Timer();
   private final Timer placeTimer = new Timer();
   private final Timer noPosTimer = new Timer();
   private final FadeUtils_DPfHthPqEJdfXfNYhDbG fadeUtils = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
   private final FadeUtils_DPfHthPqEJdfXfNYhDbG animation = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
   public float lastDamage;
   public Vec3d directionVec = null;
   double lastSize = 0.0;
   private PlayerEntity displayTarget;
   private float lastYaw = 0.0F;
   private float lastPitch = 0.0F;
   private BlockPos renderPos = null;
   private Box lastBB = null;
   private Box nowBB = null;

   public BedAura_BzCWaQEhnpenizjBqrRp() {
      super("BedAura", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public String getInfo() {
      return this.displayTarget != null && placePos != null ? this.displayTarget.method_5477().getString() : super.getInfo();
   }

   @Override
   public void onEnable() {
      this.lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
      this.lastPitch = RotateManager.lastPitch;
   }

   @EventHandler
   public void onRotate(RotateEvent event) {
      if (placePos != null && this.newRotate.getValue() && this.directionVec != null) {
         float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
         this.lastYaw = newAngle[0];
         this.lastPitch = newAngle[1];
         if (this.random.getValue() && new Random().nextBoolean()) {
            this.lastPitch = Math.min(new Random().nextFloat() * 2.0F + this.lastPitch, 90.0F);
         }

         event.setYaw(this.lastYaw);
         event.setPitch(this.lastPitch);
      } else {
         this.lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
         this.lastPitch = RotateManager.lastPitch;
      }
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      this.update();
   }

   @Override
   public void onUpdate() {
      this.update();
   }

   private void update() {
      if (!nullCheck()) {
         this.animUpdate();
         if (this.delayTimer.passedMs((long)this.updateDelay.getValue())) {
            if (this.noUsing.getValue() && EntityUtil.isUsing()) {
               placePos = null;
            } else if (mc.field_1724.method_5715()) {
               placePos = null;
            } else if (mc.field_1687.method_27983().equals(World.field_25179)) {
               placePos = null;
            } else if (this.breakOnlyHasCrystal.getValue() && this.getBed() == -1) {
               placePos = null;
            } else {
               this.delayTimer.reset();
               if (this.calcTimer.passedMs((long)this.calcDelay.getValueInt())) {
                  this.calcTimer.reset();
                  placePos = null;
                  this.lastDamage = 0.0F;
                  ArrayList<PredictionSetting_XBpBEveLWEKUGQPHCCIS> list = new ArrayList();

                  for (PlayerEntity target : CombatUtil.getEnemies(this.targetRange.getRange())) {
                     list.add(new PredictionSetting_XBpBEveLWEKUGQPHCCIS(target));
                  }

                  PredictionSetting_XBpBEveLWEKUGQPHCCIS self = new PredictionSetting_XBpBEveLWEKUGQPHCCIS(mc.field_1724);

                  for (BlockPos pos : BlockUtil.getSphere((float)this.range.getValue())) {
                     if (this.canPlaceBed(pos) || BlockUtil.getBlock(pos) instanceof BedBlock) {
                        for (PredictionSetting_XBpBEveLWEKUGQPHCCIS pap : list) {
                           float damage = this.calculateDamage(pos, pap.player, pap.predict);
                           float selfDamage = this.calculateDamage(pos, self.player, self.predict);
                           if (!((double)selfDamage > this.placeMaxSelf.getValue())
                              && (
                                 !(this.antiSuicide.getValue() > 0.0)
                                    || !((double)selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) - this.antiSuicide.getValue())
                              )
                              && (
                                 !(damage < EntityUtil.getHealth(pap.player))
                                    || !(damage < this.placeMinDamage.getValueFloat()) && (!this.smart.getValue() || !(damage < selfDamage))
                              )
                              && (placePos == null || damage > this.lastDamage)) {
                              this.displayTarget = pap.player;
                              placePos = pos;
                              this.lastDamage = damage;
                           }
                        }
                     }
                  }
               }

               if (placePos != null) {
                  this.doBed(placePos);
               }
            }
         }
      }
   }

   public void doBed(BlockPos pos) {
      switch ((BedAura_uTGCNmVWbsqrhUODnXeN)this.mode.getValue()) {
         case NullPoint:
            if (this.canPlaceBed(pos) && !(BlockUtil.getBlock(pos) instanceof BedBlock)) {
               if (this.getBed() != -1) {
                  this.doPlace(pos);
               }
            } else {
               this.doBreak(pos);
            }
            break;
         case Scanner:
            this.doBreak(pos);
            this.doPlace(pos);
            this.doBreak(pos);
      }
   }

   private void doBreak(BlockPos pos) {
      if (this.Break.getValue()) {
         if (mc.field_1687.method_8320(pos).method_26204() instanceof BedBlock) {
            Direction side = BlockUtil.getClickSide(pos);
            Vec3d directionVec = new Vec3d(
               (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
               (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
               (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
            );
            if (this.rotate.getValue() && !this.faceVector(directionVec)) {
               return;
            }

            if (!this.breakTimer.passedMs((long)this.breakDelay.getValue())) {
               return;
            }

            this.breakTimer.reset();
            EntityUtil.swingHand(Hand.field_5808, (SwingSide)this.swingMode.getValue());
            BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
            mc.field_1724.field_3944.method_52787(new PlayerInteractBlockC2SPacket(Hand.field_5808, result, BlockUtil.getWorldActionId(mc.field_1687)));
         }
      }
   }

   private void doPlace(BlockPos pos) {
      if (this.place.getValue()) {
         int bedSlot;
         if ((bedSlot = this.getBed()) == -1) {
            placePos = null;
         } else {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            Direction facing = null;

            for (Direction i : Direction.values()) {
               if (i != Direction.field_11036
                  && i != Direction.field_11033
                  && BlockUtil.clientCanPlace(pos.method_10093(i), false)
                  && BlockUtil.canClick(pos.method_10093(i).method_10074())
                  && (!this.checkMine.getValue() || !BlockUtil.isMining(pos.method_10093(i)))) {
                  facing = i;
                  break;
               }
            }

            if (facing != null) {
               Vec3d directionVec = new Vec3d(
                  (double)pos.method_10263() + 0.5 + (double)Direction.field_11036.method_10163().method_10263() * 0.5,
                  (double)pos.method_10264() + 0.5 + (double)Direction.field_11036.method_10163().method_10264() * 0.5,
                  (double)pos.method_10260() + 0.5 + (double)Direction.field_11036.method_10163().method_10260() * 0.5
               );
               if (this.rotate.getValue() && !this.faceVector(directionVec)) {
                  return;
               }

               if (!this.placeTimer.passedMs((long)this.placeDelay.getValue())) {
                  return;
               }

               this.placeTimer.reset();
               this.doSwap(bedSlot);
               if (this.yawDeceive.getValue()) {
                  HoleKickTest.pistonFacing(facing.method_10153());
               }

               BlockUtil.clickBlock(pos.method_10093(facing).method_10074(), Direction.field_11036, false);
               if (this.rotate.getValue() && this.sync.getValue()) {
                  EntityUtil.faceVector(directionVec);
               }

               if (this.inventorySwap.getValue()) {
                  this.doSwap(bedSlot);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }
            }
         }
      }
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      this.update();
      double quad = this.noPosTimer.passedMs(this.startFadeTime.getValue() * 1000.0) ? this.fadeUtils.easeOutQuad() : 0.0;
      if (this.nowBB != null && this.render.getValue() && quad < 1.0) {
         Box bb = this.nowBB;
         if (this.shrink.getValue()) {
            bb = this.nowBB.method_1002(quad * 0.5, quad * 0.5, quad * 0.5);
            bb = bb.method_1002(-quad * 0.5, -quad * 0.5, -quad * 0.5);
         }

         if (this.box.getValue()) {
            Render3DUtil.drawFill(matrixStack, bb, ColorUtil.injectAlpha(this.color.getValue(), (int)(this.boxAlpha.getValue() * Math.abs(quad - 1.0))));
         }

         if (this.outline.getValue()) {
            Render3DUtil.drawBox(matrixStack, bb, ColorUtil.injectAlpha(this.color.getValue(), (int)(this.outlineAlpha.getValue() * Math.abs(quad - 1.0))));
         }
      } else if (this.reset.getValue()) {
         this.nowBB = null;
      }
   }

   private void animUpdate() {
      this.fadeUtils.setLength((long)(this.fadeTime.getValue() * 1000.0));
      if (placePos != null) {
         this.lastBB = new Box(placePos);
         this.noPosTimer.reset();
         if (this.nowBB == null) {
            this.nowBB = this.lastBB;
         }

         if (this.renderPos == null || !this.renderPos.equals(placePos)) {
            this.animation
               .setLength(
                  this.animationTime.getValue() * 1000.0 <= 0.0
                     ? 0L
                     : (long)(
                        Math.abs(this.nowBB.field_1323 - this.lastBB.field_1323)
                                 + Math.abs(this.nowBB.field_1322 - this.lastBB.field_1322)
                                 + Math.abs(this.nowBB.field_1321 - this.lastBB.field_1321)
                              <= 5.0
                           ? (double)(
                              (long)(
                                 (
                                       Math.abs(this.nowBB.field_1323 - this.lastBB.field_1323)
                                          + Math.abs(this.nowBB.field_1322 - this.lastBB.field_1322)
                                          + Math.abs(this.nowBB.field_1321 - this.lastBB.field_1321)
                                    )
                                    * this.animationTime.getValue()
                                    * 1000.0
                              )
                           )
                           : this.animationTime.getValue() * 5000.0
                     )
               );
            this.animation.reset();
            this.renderPos = placePos;
         }
      }

      if (!this.noPosTimer.passedMs((long)(this.startFadeTime.getValue() * 1000.0))) {
         this.fadeUtils.reset();
      }

      double size = this.animation.easeOutQuad();
      if (this.nowBB != null && this.lastBB != null) {
         if (Math.abs(this.nowBB.field_1323 - this.lastBB.field_1323)
               + Math.abs(this.nowBB.field_1322 - this.lastBB.field_1322)
               + Math.abs(this.nowBB.field_1321 - this.lastBB.field_1321)
            > 16.0) {
            this.nowBB = this.lastBB;
         }

         if (this.lastSize != size) {
            this.nowBB = new Box(
               this.nowBB.field_1323 + (this.lastBB.field_1323 - this.nowBB.field_1323) * size,
               this.nowBB.field_1322 + (this.lastBB.field_1322 - this.nowBB.field_1322) * size,
               this.nowBB.field_1321 + (this.lastBB.field_1321 - this.nowBB.field_1321) * size,
               this.nowBB.field_1320 + (this.lastBB.field_1320 - this.nowBB.field_1320) * size,
               this.nowBB.field_1325 + (this.lastBB.field_1325 - this.nowBB.field_1325) * size,
               this.nowBB.field_1324 + (this.lastBB.field_1324 - this.nowBB.field_1324) * size
            );
            this.lastSize = size;
         }
      }
   }

   public int getBed() {
      return this.inventorySwap.getValue() ? InventoryUtil.findClassInventorySlot(BedItem.class) : InventoryUtil.findClass(BedItem.class);
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   public float calculateDamage(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
      CombatUtil.modifyPos = pos;
      CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
      float damage = this.calculateDamage(pos.method_46558(), player, predict);
      CombatUtil.modifyPos = null;
      return damage;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
      if (this.terrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      float damage = 0.0F;
      switch (<unrepresentable>.$SwitchMap$me$hextech$mod$modules$impl$combat$autocrystal$mode$Enum$CalcMode[((Enum_IKgLeKHCELPvcpdGlLhV)this.calcMode
            .getValue())
         .ordinal()]) {
         case 1:
            damage = (float)MeteorExplosionUtil.crystalDamage(player, pos, predict);
            break;
         case 2:
            damage = ThunderExplosionUtil.calculateDamage(pos, player, predict, 6.0F);
            break;
         case 3:
            damage = OyveyExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
            break;
         case 4:
            damage = ExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
      }

      CombatUtil.terrainIgnore = false;
      return damage;
   }

   private boolean canPlaceBed(BlockPos pos) {
      if (BlockUtil.canReplace(pos) && (!this.checkMine.getValue() || !BlockUtil.isMining(pos))) {
         for (Direction i : Direction.values()) {
            if (i != Direction.field_11036
               && i != Direction.field_11033
               && BlockUtil.isStrictDirection(pos.method_10093(i).method_10074(), Direction.field_11036)
               && this.isTrueFacing(pos.method_10093(i), i.method_10153())
               && BlockUtil.clientCanPlace(pos.method_10093(i), false)
               && BlockUtil.canClick(pos.method_10093(i).method_10074())
               && (!this.checkMine.getValue() || !BlockUtil.isMining(pos.method_10093(i)))) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isTrueFacing(BlockPos pos, Direction facing) {
      if (this.yawDeceive.getValue()) {
         return true;
      } else {
         Vec3d hitVec = pos.method_46558().method_1019(new Vec3d(0.0, -0.5, 0.0));
         return Direction.method_10150((double)EntityUtil.getLegitRotations(hitVec)[0]) == facing;
      }
   }

   public boolean faceVector(Vec3d directionVec) {
      if (!this.newRotate.getValue()) {
         EntityUtil.faceVector(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         float[] angle = EntityUtil.getLegitRotations(directionVec);
         if (Math.abs(MathHelper.method_15393(angle[0] - this.lastYaw)) < this.fov.getValueFloat()
            && Math.abs(MathHelper.method_15393(angle[1] - this.lastPitch)) < this.fov.getValueFloat()) {
            if (this.sync.getValue()) {
               EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }

            return true;
         } else {
            return !this.checkLook.getValue();
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
         float diff = MathHelper.method_15393(angle[0] - this.lastYaw);
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
