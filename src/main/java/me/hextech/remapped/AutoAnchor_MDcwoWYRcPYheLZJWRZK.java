package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class AutoAnchor_MDcwoWYRcPYheLZJWRZK extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoAnchor_MDcwoWYRcPYheLZJWRZK INSTANCE;
   public static BlockPos currentPos;
   static Vec3d placeVec3d;
   static Vec3d curVec3d;
   public final EnumSetting<AutoAnchor> page = this.add(new EnumSetting("Page", AutoAnchor.General));
   public final SliderSetting range = this.add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == AutoAnchor.General));
   public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 8.0, 0.1, 12.0, 0.1, v -> this.page.getValue() == AutoAnchor.General));
   public final SliderSetting minDamage = this.add(new SliderSetting("Min", 4.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == AutoAnchor.Calc).setSuffix("dmg"));
   public final SliderSetting breakMin = this.add(
      new SliderSetting("ExplosionMin", 4.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == AutoAnchor.Calc).setSuffix("dmg")
   );
   public final SliderSetting headDamage = this.add(
      new SliderSetting("ForceHead", 7.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == AutoAnchor.Calc).setSuffix("dmg")
   );
   public final SliderSetting predictTicks = this.add(
      new SliderSetting("Predict", 2.0, 0.0, 50.0, 1.0, v -> this.page.getValue() == AutoAnchor.Calc).setSuffix("ticks")
   );
   final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == AutoAnchor.Render));
   final BooleanSetting shrink = this.add(new BooleanSetting("Shrink", true, v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue()));
   final ColorSetting box = this.add(
      new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue()).injectBoolean(true)
   );
   final SliderSetting lineWidth = this.add(
      new SliderSetting("LineWidth", 1.5, 0.01, 3.0, 0.01, v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue())
   );
   final ColorSetting fill = this.add(
      new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue()).injectBoolean(true)
   );
   final SliderSetting sliderSpeed = this.add(
      new SliderSetting("SliderSpeed", 0.2, 0.0, 1.0, 0.01, v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue())
   );
   final SliderSetting startFadeTime = this.add(
      new SliderSetting("StartFade", 0.3, 0.0, 2.0, 0.01, v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue()).setSuffix("s")
   );
   final SliderSetting fadeSpeed = this.add(
      new SliderSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == AutoAnchor.Render && this.render.getValue())
   );
   final Timer noPosTimer = new Timer();
   private final BooleanSetting assist = this.add(new BooleanSetting("Assist", true, v -> this.page.getValue() == AutoAnchor.Assist));
   private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", false, v -> this.page.getValue() == AutoAnchor.Assist));
   private final SliderSetting assistRange = this.add(new SliderSetting("AssistRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == AutoAnchor.Assist));
   private final SliderSetting assistDamage = this.add(new SliderSetting("AssistDamage", 6.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == AutoAnchor.Assist));
   private final SliderSetting delay = this.add(new SliderSetting("AssistDelay", 0.1, 0.0, 1.0, 0.01, v -> this.page.getValue() == AutoAnchor.Assist));
   private final BooleanSetting thread = this.add(new BooleanSetting("Thread", false, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting light = this.add(new BooleanSetting("Light", true, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting breakCrystal = this.add(new BooleanSetting("BreakCrystal", true, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting spam = this.add(new BooleanSetting("Spam", true, v -> this.page.getValue() == AutoAnchor.General).setParent());
   private final BooleanSetting mineSpam = this.add(
      new BooleanSetting("OnlyMining", true, v -> this.page.getValue() == AutoAnchor.General && this.spam.isOpen())
   );
   private final BooleanSetting spamPlace = this.add(new BooleanSetting("Fast", true, v -> this.page.getValue() == AutoAnchor.General).setParent());
   private final BooleanSetting inSpam = this.add(
      new BooleanSetting("WhenSpamming", true, v -> this.page.getValue() == AutoAnchor.General && this.spamPlace.isOpen())
   );
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting cancelblink = this.add(new BooleanSetting("CancelBlink", true, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting syncCyrstal = this.add(new BooleanSetting("SyncCrystal", false, v -> this.page.getValue() == AutoAnchor.General));
   private final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", false, v -> this.page.getValue() == AutoAnchor.General));
   private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting("Swing", SwingSide.All, v -> this.page.getValue() == AutoAnchor.General));
   private final SliderSetting placeDelay = this.add(
      new SliderSetting("Delay", 100.0, 0.0, 500.0, 1.0, v -> this.page.getValue() == AutoAnchor.General).setSuffix("ms")
   );
   private final SliderSetting spamDelay = this.add(
      new SliderSetting("SpamDelay", 200.0, 0.0, 1000.0, 1.0, v -> this.page.getValue() == AutoAnchor.General).setSuffix("ms")
   );
   private final SliderSetting updateDelay = this.add(
      new SliderSetting("UpdateDelay", 200.0, 0.0, 1000.0, 1.0, v -> this.page.getValue() == AutoAnchor.General).setSuffix("ms")
   );
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == AutoAnchor.Rotate).setParent());
   private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", true, v -> this.rotate.isOpen() && this.page.getValue() == AutoAnchor.Rotate));
   private final SliderSetting steps = this.add(
      new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == AutoAnchor.Rotate)
   );
   private final BooleanSetting checkFov = this.add(
      new BooleanSetting("OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == AutoAnchor.Rotate)
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov", 30, 0, 50, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == AutoAnchor.Rotate
      )
   );
   private final SliderSetting priority = this.add(
      new SliderSetting("Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == AutoAnchor.Rotate)
   );
   private final BooleanSetting noSuicide = this.add(new BooleanSetting("NoSuicide", true, v -> this.page.getValue() == AutoAnchor.Calc));
   private final BooleanSetting terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, v -> this.page.getValue() == AutoAnchor.Calc));
   private final SliderSetting minPrefer = this.add(
      new SliderSetting("Prefer", 7.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == AutoAnchor.Calc).setSuffix("dmg")
   );
   private final SliderSetting maxSelfDamage = this.add(
      new SliderSetting("MaxSelf", 8.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == AutoAnchor.Calc).setSuffix("dmg")
   );
   private final EnumSetting<Aura_nurTqHTNjexQmuWdDgIn> mode = this.add(
      new EnumSetting("TargetESP", Aura_nurTqHTNjexQmuWdDgIn.Jello, v -> this.page.getValue() == AutoAnchor.Render)
   );
   private final ColorSetting color = this.add(new ColorSetting("TargetColor", new Color(255, 255, 255, 250), v -> this.page.getValue() == AutoAnchor.Render));
   private final BooleanSetting bold = this.add(new BooleanSetting("Bold", false, v -> this.page.getValue() == AutoAnchor.Render));
   private final Timer delayTimer = new Timer();
   private final Timer calcTimer = new Timer();
   private final ArrayList<BlockPos> chargeList = new ArrayList();
   private final Timer assistTimer = new Timer();
   public Vec3d directionVec = null;
   public PlayerEntity displayTarget;
   public BlockPos tempPos;
   public double lastDamage;
   double fade = 0.0;
   BlockPos assistPos;

   public AutoAnchor_MDcwoWYRcPYheLZJWRZK() {
      super("AutoAnchor", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
      me.hextech.HexTech.EVENT_BUS.subscribe(new AutoAnchor_fWvHjchZKtWCdDnpHPYc(this));
   }

   public static boolean canSee(Vec3d from, Vec3d to) {
      HitResult result = null;
      if (mc.field_1687 != null) {
         result = mc.field_1687.method_17742(new RaycastContext(from, to, ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724));
      }

      return result == null || result.method_17783() == Type.field_1333;
   }

   @Override
   public String getInfo() {
      return this.displayTarget != null && currentPos != null ? this.displayTarget.method_5477().getString() : null;
   }

   @Override
   public void onRender3D(MatrixStack matrixStack) {
      if (this.displayTarget != null && currentPos != null) {
         Aura.doRender(matrixStack, mc.method_1488(), this.displayTarget, this.color.getValue(), (Aura_nurTqHTNjexQmuWdDgIn)this.mode.getValue());
      }
   }

   @EventHandler
   public void onRotate(OffTrackEvent event) {
      if (currentPos != null && this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }
   }

   @Override
   public void onDisable() {
      currentPos = null;
   }

   @Override
   public void onThread() {
      if (this.thread.getValue()) {
         this.calc();
      }
   }

   @Override
   public void onUpdate() {
      if (this.assist.getValue()) {
         this.onAssist();
      }

      int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_23152) : InventoryUtil.findBlock(Blocks.field_23152);
      int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10171) : InventoryUtil.findBlock(Blocks.field_10171);
      int unBlock = this.inventorySwap.getValue() ? anchor : InventoryUtil.findUnBlock();
      int old = 0;
      if (mc.field_1724 != null) {
         old = mc.field_1724.method_31548().field_7545;
      }

      if (!this.thread.getValue()) {
         this.calc();
      }

      if (anchor == -1) {
         currentPos = null;
         this.tempPos = null;
      } else if (glowstone == -1) {
         currentPos = null;
         this.tempPos = null;
      } else if (unBlock == -1) {
         currentPos = null;
         this.tempPos = null;
      } else if (mc.field_1724.method_5715()) {
         currentPos = null;
         this.tempPos = null;
      } else if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
         currentPos = null;
         this.tempPos = null;
      } else if (!this.syncCyrstal.getValue() || AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null) {
         if (!this.cancelBurrow.getValue() || !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            if (!Blink.INSTANCE.isOn() || !this.cancelblink.getValue()) {
               if (currentPos != null) {
                  if (this.breakCrystal.getValue()) {
                     CombatUtil.attackCrystal(new BlockPos(currentPos), this.rotate.getValue(), false);
                  }

                  boolean shouldSpam = this.spam.getValue() && (!this.mineSpam.getValue() || me.hextech.HexTech.BREAK.isMining(currentPos));
                  if (shouldSpam) {
                     if (!this.delayTimer.passed((long)this.spamDelay.getValueFloat())) {
                        return;
                     }

                     this.delayTimer.reset();
                     if (BlockUtil.canPlace(currentPos, this.range.getValue(), this.breakCrystal.getValue())) {
                        this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                     }

                     if (!this.chargeList.contains(currentPos)) {
                        this.delayTimer.reset();
                        this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), glowstone);
                        this.chargeList.add(currentPos);
                     }

                     this.chargeList.remove(currentPos);
                     this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), unBlock);
                     if (this.spamPlace.getValue() && this.inSpam.getValue()) {
                        if (this.yawStep.getValue() && this.checkFov.getValue()) {
                           Direction side = BlockUtil.getClickSide(currentPos);
                           Vec3d directionVec = new Vec3d(
                              (double)currentPos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
                              (double)currentPos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
                              (double)currentPos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
                           );
                           if (me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
                              CombatUtil.modifyPos = currentPos;
                              CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
                              this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                              CombatUtil.modifyPos = null;
                           }
                        } else {
                           CombatUtil.modifyPos = currentPos;
                           CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
                           this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                           CombatUtil.modifyPos = null;
                        }
                     }
                  } else if (BlockUtil.canPlace(currentPos, this.range.getValue(), this.breakCrystal.getValue())) {
                     if (!this.delayTimer.passed((long)this.placeDelay.getValueFloat())) {
                        return;
                     }

                     this.delayTimer.reset();
                     this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                  } else if (BlockUtil.getBlock(currentPos) == Blocks.field_23152) {
                     if (!this.chargeList.contains(currentPos)) {
                        if (!this.delayTimer.passed((long)this.placeDelay.getValueFloat())) {
                           return;
                        }

                        this.delayTimer.reset();
                        this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), glowstone);
                        this.chargeList.add(currentPos);
                     } else {
                        if (!this.delayTimer.passed((long)this.placeDelay.getValueFloat())) {
                           return;
                        }

                        this.delayTimer.reset();
                        this.chargeList.remove(currentPos);
                        this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), unBlock);
                        if (this.spamPlace.getValue()) {
                           if (this.yawStep.getValue() && this.checkFov.getValue()) {
                              Direction side = BlockUtil.getClickSide(currentPos);
                              Vec3d directionVec = new Vec3d(
                                 (double)currentPos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
                                 (double)currentPos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
                                 (double)currentPos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
                              );
                              if (me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
                                 CombatUtil.modifyPos = currentPos;
                                 CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
                                 this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                                 CombatUtil.modifyPos = null;
                              }
                           } else {
                              CombatUtil.modifyPos = currentPos;
                              CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
                              this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                              CombatUtil.modifyPos = null;
                           }
                        }
                     }
                  }

                  if (!this.inventorySwap.getValue()) {
                     this.doSwap(old);
                  }
               }
            }
         }
      }
   }

   private void calc() {
      if (!nullCheck()) {
         if (this.calcTimer.passed((long)this.updateDelay.getValueFloat())) {
            PredictionSetting_XBpBEveLWEKUGQPHCCIS selfPredict = new PredictionSetting_XBpBEveLWEKUGQPHCCIS(mc.field_1724);
            this.calcTimer.reset();
            this.tempPos = null;
            double placeDamage = this.minDamage.getValue();
            double breakDamage = this.breakMin.getValue();
            boolean anchorFound = false;
            List<PlayerEntity> enemies = CombatUtil.getEnemies(this.targetRange.getValue());
            ArrayList<PredictionSetting_XBpBEveLWEKUGQPHCCIS> list = new ArrayList();

            for (PlayerEntity player : enemies) {
               list.add(new PredictionSetting_XBpBEveLWEKUGQPHCCIS(player));
            }

            for (PredictionSetting_XBpBEveLWEKUGQPHCCIS pap : list) {
               BlockPos pos = EntityUtil.getEntityPos(pap.player, true).method_10086(2);
               double selfDamage;
               double damage;
               if ((
                     BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())
                        || BlockUtil.getBlock(pos) == Blocks.field_23152 && BlockUtil.getClickSideStrict(pos) != null
                  )
                  && (
                     mc.field_1724 == null
                        || !((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue())
                           && (!this.noSuicide.getValue() || !(selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())))
                  )
                  && (damage = this.getAnchorDamage(pos, pap.player, pap.predict)) > (double)this.headDamage.getValueFloat()) {
                  this.lastDamage = damage;
                  this.displayTarget = pap.player;
                  this.tempPos = pos;
                  break;
               }
            }

            if (this.tempPos == null) {
               for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
                  for (PredictionSetting_XBpBEveLWEKUGQPHCCIS papx : list) {
                     if (this.light.getValue()) {
                        CombatUtil.modifyPos = pos;
                        CombatUtil.modifyBlockState = Blocks.field_10124.method_9564();
                        boolean skip = !canSee(pos.method_46558(), papx.predict.method_19538());
                        CombatUtil.modifyPos = null;
                        if (skip) {
                           continue;
                        }
                     }

                     if (BlockUtil.getBlock(pos) != Blocks.field_23152) {
                        if (!anchorFound && BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) {
                           CombatUtil.modifyPos = pos;
                           CombatUtil.modifyBlockState = Blocks.field_10540.method_9564();
                           boolean skip = BlockUtil.getClickSideStrict(pos) == null;
                           CombatUtil.modifyPos = null;
                           if (!skip) {
                              double damage = this.getAnchorDamage(pos, papx.player, papx.predict);
                              double selfDamage;
                              if (damage >= placeDamage
                                 && (
                                    AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null
                                       || AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOff()
                                       || (double)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastDamage < damage
                                 )
                                 && (
                                    mc.field_1724 == null
                                       || !((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue())
                                          && (!this.noSuicide.getValue() || !(selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())))
                                 )) {
                                 this.lastDamage = damage;
                                 this.displayTarget = papx.player;
                                 placeDamage = damage;
                                 this.tempPos = pos;
                              }
                           }
                        }
                     } else {
                        double damage = this.getAnchorDamage(pos, papx.player, papx.predict);
                        if (BlockUtil.getClickSideStrict(pos) != null && damage >= breakDamage) {
                           if (damage >= this.minPrefer.getValue()) {
                              anchorFound = true;
                           }

                           double selfDamage;
                           if ((anchorFound || !(damage < placeDamage))
                              && (
                                 AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null
                                    || AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOff()
                                    || (double)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastDamage < damage
                              )
                              && (
                                 mc.field_1724 == null
                                    || !((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue())
                                       && (!this.noSuicide.getValue() || !(selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())))
                              )) {
                              this.lastDamage = damage;
                              this.displayTarget = papx.player;
                              breakDamage = damage;
                              this.tempPos = pos;
                           }
                        }
                     }
                  }
               }
            }
         }

         currentPos = this.tempPos;
      }
   }

   public double getAnchorDamage(BlockPos anchorPos, PlayerEntity target, PlayerEntity predict) {
      if (this.terrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      double damage = (double)ExplosionUtil.anchorDamage(anchorPos, target, predict);
      CombatUtil.terrainIgnore = false;
      return damage;
   }

   public void placeBlock(BlockPos pos, boolean rotate, int slot) {
      if (BlockUtil.airPlace()) {
         this.clickBlock(pos, Direction.field_11033, rotate, slot);
      } else {
         Direction side = BlockUtil.getPlaceSide(pos);
         if (side != null) {
            this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, slot);
         }
      }
   }

   public void clickBlock(BlockPos pos, Direction side, boolean rotate, int slot) {
      if (pos != null) {
         Vec3d directionVec = new Vec3d(
            (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
            (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
            (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
         );
         if (!rotate || this.faceVector(directionVec)) {
            this.doSwap(slot);
            EntityUtil.swingHand(Hand.field_5808, (SwingSide)this.swingMode.getValue());
            BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
            Module_eSdgMXWuzcxgQVaJFmKZ.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(Hand.field_5808, result, id));
            if (this.inventorySwap.getValue()) {
               this.doSwap(slot);
            }
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         if (mc.field_1724 != null) {
            InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
         }
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   public boolean faceVector(Vec3d directionVec) {
      if (!this.yawStep.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         return me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
      }
   }

   public void onAssist() {
      this.assistPos = null;
      int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_23152) : InventoryUtil.findBlock(Blocks.field_23152);
      int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10171) : InventoryUtil.findBlock(Blocks.field_10171);
      int old = 0;
      if (mc.field_1724 != null) {
         old = mc.field_1724.method_31548().field_7545;
      }

      if (anchor != -1) {
         if (glowstone != -1) {
            if (!mc.field_1724.method_5715()) {
               if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
                  if (!Blink.INSTANCE.isOn() || !this.cancelblink.getValue()) {
                     if (this.assistTimer.passed((long)(this.delay.getValueFloat() * 1000.0F))) {
                        this.assistTimer.reset();
                        ArrayList<PredictionSetting_XBpBEveLWEKUGQPHCCIS> list = new ArrayList();

                        for (PlayerEntity player : CombatUtil.getEnemies(this.assistRange.getValue())) {
                           list.add(new PredictionSetting_XBpBEveLWEKUGQPHCCIS(player));
                        }

                        double bestDamage = this.assistDamage.getValue();

                        for (PredictionSetting_XBpBEveLWEKUGQPHCCIS pap : list) {
                           BlockPos pos = EntityUtil.getEntityPos(pap.player, true).method_10086(2);
                           if (mc.field_1687 != null && mc.field_1687.method_8320(pos).method_26204() == Blocks.field_23152) {
                              return;
                           }

                           if (BlockUtil.clientCanPlace(pos, false)) {
                              double damage = this.getAnchorDamage(pos, pap.player, pap.predict);
                              if (damage >= bestDamage) {
                                 bestDamage = damage;
                                 this.assistPos = pos;
                              }
                           }

                           for (Direction i : Direction.values()) {
                              if (i != Direction.field_11036 && i != Direction.field_11033 && BlockUtil.clientCanPlace(pos.method_10093(i), false)) {
                                 double damage = this.getAnchorDamage(pos.method_10093(i), pap.player, pap.predict);
                                 if (damage >= bestDamage) {
                                    bestDamage = damage;
                                    this.assistPos = pos.method_10093(i);
                                 }
                              }
                           }
                        }

                        BlockPos placePos;
                        if (this.assistPos != null
                           && BlockUtil.getPlaceSide(this.assistPos, this.range.getValue()) == null
                           && (placePos = this.getHelper(this.assistPos)) != null) {
                           this.doSwap(anchor);
                           BlockUtil.placeBlock(placePos, this.rotate.getValue());
                           if (this.inventorySwap.getValue()) {
                              this.doSwap(anchor);
                           } else {
                              this.doSwap(old);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public BlockPos getHelper(BlockPos pos) {
      for (Direction i : Direction.values()) {
         if ((!this.checkMine.getValue() || !me.hextech.HexTech.BREAK.isMining(pos.method_10093(i)))
            && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153())
            && BlockUtil.canPlace(pos.method_10093(i))) {
            return pos.method_10093(i);
         }
      }

      return null;
   }
}
