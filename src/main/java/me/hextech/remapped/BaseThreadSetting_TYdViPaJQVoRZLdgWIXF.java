package me.hextech.remapped;

import java.util.ArrayList;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BaseThreadSetting_TYdViPaJQVoRZLdgWIXF extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static final Timer placeTimer = new Timer();
   public static final Timer placeBaseTimer = new Timer();
   public static BaseThreadSetting_TYdViPaJQVoRZLdgWIXF INSTANCE;
   public static BlockPos crystalPos;
   public final Timer delayTimer = new Timer();
   public final EnumSetting<BaseThreadSetting> page = this.add(new EnumSetting("Page", BaseThreadSetting.Thread));
   public final BooleanSetting multiThread = this.add(new BooleanSetting("MultiThread", true, v -> this.page.is(BaseThreadSetting.Thread)));
   public final BooleanSetting crystalThread = this.add(new BooleanSetting("CrystalThread", true, v -> this.page.is(BaseThreadSetting.Thread)));
   public final BooleanSetting rotatepacket = this.add(new BooleanSetting("PacketRotate", false, v -> this.page.is(BaseThreadSetting.Thread)));
   public final BooleanSetting jumpCooldown = this.add(new BooleanSetting("JumpCooldown", true, v -> this.page.is(BaseThreadSetting.Change)));
   public final BooleanSetting antiopen = this.add(new BooleanSetting("AntiOpen", true, v -> this.page.is(BaseThreadSetting.Change)));
   public final BooleanSetting packethook = this.add(new BooleanSetting("MixinPacketHook", true, v -> this.page.is(BaseThreadSetting.Mixin)));
   public final BooleanSetting movehook = this.add(new BooleanSetting("MixinMoveHook", true, v -> this.page.is(BaseThreadSetting.Mixin)));
   public final SliderSetting minrad = this.add(new SliderSetting("OffTrackRadian", 0, 0, 30, v -> this.page.is(BaseThreadSetting.Dev)).setSuffix("/-Age"));
   public final BooleanSetting attack = this.add(new BooleanSetting("Attack§4(DeSync)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Aweb = this.add(new BooleanSetting("Attack§4(AutoWeb)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Async = this.add(new BooleanSetting("Attack§4(AutoCrystal.INSTANCE)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Arenderslinder = this.add(new BooleanSetting("Attack§4(AutoCrystal)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Arotate = this.add(new BooleanSetting("Attack§4(Rotate)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting defend = this.add(new BooleanSetting("Defend§b(AutoCrystal.INSTANCE)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Dweb = this.add(new BooleanSetting("Defend§b(AutoWeb)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Dsync = this.add(new BooleanSetting("Defend§b(AutoCrystal.INSTANCE)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Drenderslinder = this.add(new BooleanSetting("Defend§b(AutoCrystal)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting Drotate = this.add(new BooleanSetting("Defend§b(Rotate)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting smart = this.add(new BooleanSetting("Smart-Out1", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting smart2 = this.add(new BooleanSetting("Smart-Out2", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting smart3 = this.add(new BooleanSetting("Smart-Out3", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting inmoveSync = this.add(new BooleanSetting("MovingSync", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting staticSync = this.add(new BooleanSetting("StaticSync", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final BooleanSetting burrowcleaner = this.add(new BooleanSetting("StartBurrowCleaner", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
   public final SliderSetting blockposx = this.add(new SliderSetting("BlockPosX", 0.3, 0.0, 1.0, v -> this.page.is(BaseThreadSetting.Fix)));
   public final BooleanSetting nullfix = this.add(new BooleanSetting("AntiWorldNull", true, v -> this.page.is(BaseThreadSetting.Fix)));
   public final SliderSetting fadeInQuad = this.add(new SliderSetting("FadeInQuad", 1, -1, 1, v -> this.page.is(BaseThreadSetting.FadeUtils)));
   public final SliderSetting fadeInEnd = this.add(new SliderSetting("FadeEnd", 1, -1, 1, v -> this.page.is(BaseThreadSetting.FadeUtils)));
   public final SliderSetting fadeInlength = this.add(new SliderSetting("FadeInLength", 3, -3, 3, v -> this.page.is(BaseThreadSetting.FadeUtils)));
   public PlayerEntity displayTarget;
   public float breakDamage;
   public float tempDamage;
   public float lastDamage;
   public BlockPos tempPos;
   public Vec3d directionVec = null;

   public BaseThreadSetting_TYdViPaJQVoRZLdgWIXF() {
      super("BaseThreadSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
      INSTANCE = this;
   }

   public static boolean faceVector(Vec3d directionVec) {
      if (!AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.ObbyVector.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         return me.hextech.HexTech.ROTATE.inFov(directionVec, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.fov.getValueFloat())
            ? true
            : !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.checkLook.getValue();
      }
   }

   @Override
   public void onDisable() {
      crystalPos = null;
      this.tempPos = null;
   }

   @Override
   public void onEnable() {
      INSTANCE = this;
   }

   @Override
   public void onThread() {
      if (!this.multiThread.getValue()) {
         this.updateCrystalPos();
      }
   }

   @Override
   public void onUpdate() {
      if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.cblink.getValue() && Blink.INSTANCE.isOn()) {
         this.tempPos = null;
         crystalPos = null;
      } else {
         if (this.burrowcleaner.getValue()) {
            if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
               Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.enable();
            } else {
               Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.disable();
            }
         }

         if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.SmartActive.getValue() && EntityUtil.isInsideBlock()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
         }

         if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.onlyStatic.getValue() && MovementUtil.isJumping()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.disable();
         }

         if (this.inmoveSync.getValue() && !MovementUtil.isMoving()) {
            CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateSync.setValue(true);
         }

         if (this.staticSync.getValue() && MovementUtil.isMoving()) {
            CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateSync.setValue(false);
         }

         if (this.smart.getValue() && !MovementUtil.isMoving()) {
            ComboBreaks.INSTANCE.disable();
         }

         if (this.smart3.getValue() && MovementUtil.isMoving()) {
            ComboBreaks.INSTANCE.enable();
            this.smart2.setValue(false);
         }

         if (this.smart2.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            ComboBreaks.INSTANCE.enable();
            this.smart3.setValue(false);
         }

         if (ComboBreaks.INSTANCE.isOn()) {
            if (this.Arenderslinder.getValue()) {
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.sliderSpeed.setValue((double)ComboBreaks.INSTANCE.Arender.getValueFloat());
            }

            if (this.Aweb.getValue()) {
               WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.placeDelay.setValue((double)ComboBreaks.INSTANCE.Aweb.getValueFloat());
            }

            if (this.Async.getValue()) {
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceWeb.setValue(this.isOff());
            }

            if (this.attack.getValue()) {
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.HurtTime.setValue((double)ComboBreaks.INSTANCE.attackSync.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTime.setValue((double)ComboBreaks.INSTANCE.AlastSync.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.OnlySyncTime.setValue((double)ComboBreaks.INSTANCE.AonlySync.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SpamSyncTime.setValue((double)ComboBreaks.INSTANCE.AspamTime.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.placeDelay.setValue((double)ComboBreaks.INSTANCE.Acyrstal.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.breakDelay.setValue((double)ComboBreaks.INSTANCE.Abreak.getValueFloat());
            }

            if (this.Arotate.getValue()) {
               NoRotateSet.INSTANCE.lagTime.setValue((double)ComboBreaks.INSTANCE.Arotate.getValueFloat());
            }
         } else {
            if (this.Drenderslinder.getValue()) {
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.sliderSpeed.setValue((double)ComboBreaks.INSTANCE.Drender.getValueFloat());
            }

            if (this.Dweb.getValue()) {
               WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.placeDelay.setValue((double)ComboBreaks.INSTANCE.Dweb.getValueFloat());
            }

            if (this.Dsync.getValue()) {
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceWeb.setValue(this.isOn());
            }

            if (this.defend.getValue()) {
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.HurtTime.setValue((double)ComboBreaks.INSTANCE.DattackSync.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTime.setValue((double)ComboBreaks.INSTANCE.DlastSync.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.OnlySyncTime.setValue((double)ComboBreaks.INSTANCE.DonlySync.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SpamSyncTime.setValue((double)ComboBreaks.INSTANCE.DspamTime.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.placeDelay.setValue((double)ComboBreaks.INSTANCE.Dcrystal.getValueFloat());
               AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.breakDelay.setValue((double)ComboBreaks.INSTANCE.Dbreak.getValueFloat());
            }

            if (this.Drotate.getValue()) {
               NoRotateSet.INSTANCE.lagTime.setValue((double)ComboBreaks.INSTANCE.Drotate.getValueFloat());
            }
         }

         if (this.jumpCooldown.getValue()) {
            mc.field_1724.field_6228 = 0;
         }

         if (this.multiThread.getValue()) {
            this.updateCrystalPos();
         }

         this.doUpdate();
      }
   }

   @EventHandler
   public void onPacket(PacketEvent event) {
      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.raytracebypass.getValue()
         && event.getPacket() instanceof PlayerMoveC2SPacket packet
         && RotateManager.lastPitch != -91.0F) {
         ((IPlayerMoveC2SPacket)packet).setPitch(-91.0F);
      }

      if (this.antiopen.getValue()) {
         if (nullCheck() || !(event.getPacket() instanceof PlayerInteractBlockC2SPacket packet)) {
            return;
         }

         Block var5 = mc.field_1687.method_8320(packet.method_12543().method_17777()).method_26204();
         if (!mc.field_1724.method_5715() && (var5 instanceof ChestBlock || var5 instanceof EnderChestBlock)) {
            event.cancel();
         }
      }
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      if (!this.multiThread.getValue()) {
         this.updateCrystalPos();
      }

      this.doUpdate();
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (!this.multiThread.getValue()) {
         this.updateCrystalPos();
      }

      this.doUpdate();
   }

   private void doUpdate() {
      if (crystalPos != null) {
         ListenerHelper.doBase(crystalPos);
      }
   }

   private void updateCrystalPos() {
      this.update();
      this.lastDamage = this.tempDamage;
      crystalPos = this.tempPos;
   }

   private void update() {
      if (!nullCheck()) {
         if (PredictionSetting.INSTANCE.prediction.getValue()) {
            ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.updateHistory();
         }

         if (this.delayTimer.passedMs((long)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.calcdelay.getValue())) {
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.eatingPause.getValue() && mc.field_1724.method_6115()) {
               this.tempPos = null;
            } else if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.breakOnlyHasCrystal.getValue()
               && !mc.field_1724.method_6047().method_7909().equals(Items.field_8281)
               && !mc.field_1724.method_6079().method_7909().equals(Items.field_8301)
               && !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.findCrystal()) {
               this.tempPos = null;
            } else {
               this.delayTimer.reset();
               this.breakDamage = 0.0F;
               this.tempPos = null;
               this.tempDamage = 0.0F;
               ArrayList<PredictionSetting_XBpBEveLWEKUGQPHCCIS> list = new ArrayList();

               for (PlayerEntity target : CombatUtil.getEnemies((double)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.targetRange.getValueFloat())) {
                  if (target.field_6235 <= AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.HurtTime.getValueInt()) {
                     list.add(new PredictionSetting_XBpBEveLWEKUGQPHCCIS(target));
                  }
               }

               PredictionSetting_XBpBEveLWEKUGQPHCCIS self = new PredictionSetting_XBpBEveLWEKUGQPHCCIS(mc.field_1724);
               if (!list.isEmpty()) {
                  for (BlockPos pos : BlockUtil.getSphere((float)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue() + 1.0F)) {
                     CombatUtil.modifyPos = null;
                     if (!(
                           mc.field_1724.method_33571().method_1022(pos.method_46558().method_1031(0.0, -0.5, 0.0))
                              > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue()
                        )
                        && ListenerHelperUtil.canPlaceCrystal(pos, true, false)) {
                        CombatUtil.modifyPos = pos.method_10074();
                        CombatUtil.modifyBlockState = Blocks.field_10540.method_9564();
                        if (!ListenerHelperUtil.behindWall(pos) && ListenerHelperUtil.canTouch(pos.method_10074())) {
                           for (PredictionSetting_XBpBEveLWEKUGQPHCCIS pap : list) {
                              if (pos.method_10074().method_10264() <= pap.player.method_31478()
                                 && (
                                    !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lite.getValue()
                                       || !ListenerHelperUtil.liteCheck(pos.method_46558().method_1031(0.0, -0.5, 0.0), pap.predict.method_19538())
                                 )) {
                                 float damage = ListenerHelperUtil.calculateBase(pos, pap.player, pap.predict);
                                 if (this.tempPos == null || damage > this.tempDamage) {
                                    float selfDamage = ListenerHelperUtil.calculateBase(pos, self.player, self.predict);
                                    if (!((double)selfDamage > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.maxSelf.getValue())
                                       && (
                                          !(AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.noSuicide.getValue() > 0.0)
                                             || !(
                                                (double)selfDamage
                                                   > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())
                                                      - AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.noSuicide.getValue()
                                             )
                                       )
                                       && (
                                          !(damage < EntityUtil.getHealth(pap.player))
                                             || !((double)damage < ListenerDamage.getDamage(pap.player))
                                                && (
                                                   !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.smart.getValue()
                                                      || (
                                                         ListenerDamage.getDamage(pap.player) == AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceMin.getValue()
                                                            ? !((double)damage < (double)selfDamage - 2.5)
                                                            : !(damage < selfDamage)
                                                      )
                                                )
                                       )) {
                                       this.displayTarget = pap.player;
                                       this.tempPos = pos.method_10074();
                                       this.tempDamage = damage;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  CombatUtil.modifyPos = null;
                  if (this.tempPos != null && !BlockUtil.canPlace(this.tempPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue())) {
                     this.tempPos = null;
                     this.tempDamage = 0.0F;
                  }
               }
            }
         }
      }
   }

   @Override
   public void enable() {
      this.state = true;
   }

   @Override
   public void disable() {
      this.state = true;
   }

   @Override
   public boolean isOn() {
      return true;
   }
}
