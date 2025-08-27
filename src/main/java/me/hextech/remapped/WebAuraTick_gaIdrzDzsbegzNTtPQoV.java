package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class WebAuraTick_gaIdrzDzsbegzNTtPQoV extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static WebAuraTick_gaIdrzDzsbegzNTtPQoV INSTANCE;
   public static float lastYaw;
   public static float lastPitch;
   public static boolean force;
   public static boolean ignore;
   public final EnumSetting<WebAuraTick> page = this.add(new EnumSetting("Page", WebAuraTick.General));
   public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 2, 1, 10, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 2.0, 0.0, 50.0, 1.0, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting maxWebs = this.add(new SliderSetting("MaxWebs", 2.0, 1.0, 8.0, 1.0, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting burrowMaxWebs = this.add(new SliderSetting("BurrowMaxWebs", 2.0, 1.0, 8.0, 1.0, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 8.0, 0.0, 8.0, 0.1, v -> this.page.getValue() == WebAuraTick.General));
   public final SliderSetting offset = this.add(new SliderSetting("Offset", 0.25, 0.0, 0.3, 0.01, v -> this.page.getValue() == WebAuraTick.General));
   public final ArrayList<BlockPos> pos = new ArrayList();
   private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting noMine = this.add(new BooleanSetting("NoMine", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting interact = this.add(new BooleanSetting("InteractPacket", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting seqpack = this.add(new BooleanSetting("SequencedPacket", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting onlyTick = this.add(new BooleanSetting("OnlyTick", false, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting face = this.add(new BooleanSetting("Face", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting noPushFaceHT = this.add(new BooleanSetting("NoFacePush[HT]", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting noPushFaceFK = this.add(new BooleanSetting("NoFacePush[FK]", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting preferAnchor = this.add(new BooleanSetting("WaitAnchor", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting waitTrap = this.add(new BooleanSetting("WaitTrap", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting down = this.add(new BooleanSetting("Down", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, v -> this.page.getValue() == WebAuraTick.General));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == WebAuraTick.Rotate).setParent());
   private final BooleanSetting yawStep = this.add(
      new BooleanSetting("YawStep", false, v -> this.rotate.isOpen() && this.page.getValue() == WebAuraTick.Rotate)
   );
   private final SliderSetting steps = this.add(
      new SliderSetting("Steps", 0.3, 0.1, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == WebAuraTick.Rotate)
   );
   private final BooleanSetting checkFov = this.add(
      new BooleanSetting("OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == WebAuraTick.Rotate)
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov", 30, 0, 50, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == WebAuraTick.Rotate
      )
   );
   private final SliderSetting priority = this.add(
      new SliderSetting("Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == WebAuraTick.Rotate)
   );
   private final Timer timer = new Timer();
   public Vec3d directionVec = null;
   int progress = 0;
   int tempMaxWebs = 1;

   public WebAuraTick_gaIdrzDzsbegzNTtPQoV() {
      super("WebAuraTick", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public String getInfo() {
      return this.pos.isEmpty() ? null : "Working";
   }

   @EventHandler
   public void onRotate(OffTrackEvent event) {
      if (this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      if (!this.onlyTick.getValue()) {
         this.onUpdate();
      }
   }

   @Override
   public void onDisable() {
      force = false;
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (!this.onlyTick.getValue()) {
         this.onUpdate();
      }
   }

   @Override
   public void onUpdate() {
      if (force) {
         ignore = true;
      }

      if (this.noPushFaceHT.getValue() && HoleKickTest.INSTANCE.isOn()) {
         this.face.setValue(false);
         this.noPushFaceFK.setValue(false);
      }

      if (this.noPushFaceHT.getValue() && HoleKickTest.INSTANCE.isOff()) {
         this.face.setValue(true);
      }

      if (this.noPushFaceFK.getValue() && FinalHoleKick.INSTANCE.isOn()) {
         this.face.setValue(false);
         this.noPushFaceHT.setValue(false);
      }

      if (this.noPushFaceFK.getValue() && FinalHoleKick.INSTANCE.isOff()) {
         this.face.setValue(true);
      }

      this.update();
   }

   private void update() {
      if (this.timer.passedMs((long)this.placeDelay.getValueInt())) {
         if (!this.cancelBurrow.getValue() || !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            if (!this.waitTrap.getValue() || !AutoTrap.INSTANCE.isOn()) {
               this.pos.clear();
               this.progress = 0;
               this.directionVec = null;
               if (!this.preferAnchor.getValue() || AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos == null) {
                  if (this.getWebSlot() != -1) {
                     if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
                        label166:
                        for (PlayerEntity player : CombatUtil.getEnemies(this.targetRange.getValue())) {
                           this.tempMaxWebs = (int)this.maxWebs.getValue();
                           if (this.isInBurrow(player)) {
                              this.tempMaxWebs = (int)this.burrowMaxWebs.getValue();
                           }

                           Vec3d playerPos = this.predictTicks.getValue() > 0.0
                              ? CombatUtil.getEntityPosVec(player, this.predictTicks.getValueInt())
                              : player.method_19538();
                           int webs = 0;
                           if (this.down.getValue()) {
                              this.placeWeb(new BlockPosX(playerPos.method_10216(), playerPos.method_10214() - 0.8, playerPos.method_10215()));
                           }

                           List<BlockPos> list = new ArrayList();

                           for (float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                              for (float z : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                 for (float y : new float[]{0.0F, 1.0F, -1.0F}) {
                                    BlockPosX pos = new BlockPosX(
                                       playerPos.method_10216() + (double)x, playerPos.method_10214() + (double)y, playerPos.method_10215() + (double)z
                                    );
                                    if (!list.contains(pos)) {
                                       list.add(pos);
                                       if (this.isTargetHere(pos, player)
                                          && mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10343
                                          && !me.hextech.HexTech.BREAK.isMining(pos)) {
                                          webs++;
                                       }
                                    }
                                 }
                              }
                           }

                           if (webs < this.tempMaxWebs || ignore) {
                              boolean skip = false;
                              if (this.feet.getValue()) {
                                 label140:
                                 for (float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                    for (float z : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                       BlockPosX pos = new BlockPosX(
                                          playerPos.method_10216() + (double)x, playerPos.method_10214(), playerPos.method_10215() + (double)z
                                       );
                                       if (this.isTargetHere(pos, player) && this.placeWeb(pos)) {
                                          if (++webs >= this.tempMaxWebs) {
                                             skip = true;
                                             break label140;
                                          }
                                       }
                                    }
                                 }
                              }

                              if (!skip && this.face.getValue()) {
                                 for (float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                    for (float zx : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                       BlockPosX pos = new BlockPosX(
                                          playerPos.method_10216() + (double)x, playerPos.method_10214() + 1.1, playerPos.method_10215() + (double)zx
                                       );
                                       if (this.isTargetHere(pos, player) && this.placeWeb(pos)) {
                                          if (++webs >= this.tempMaxWebs) {
                                             continue label166;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isTargetHere(BlockPos pos, PlayerEntity target) {
      return new Box(pos).method_994(target.method_5829());
   }

   private boolean placeWeb(BlockPos pos) {
      if (this.pos.contains(pos)) {
         return false;
      } else {
         this.pos.add(pos);
         if (this.progress >= this.blocksPer.getValueInt()) {
            return false;
         } else if (this.getWebSlot() == -1) {
            return false;
         } else if (!this.detectMining.getValue() || !me.hextech.HexTech.BREAK.isMining(pos) && (this.noMine.getValue() || !pos.equals(SpeedMine.breakPos))) {
            if (BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) != null
               && (mc.field_1687.method_22347(pos) || ignore && BlockUtil.getBlock(pos) == Blocks.field_10343)
               && pos.method_10264() < 320) {
               int oldSlot = mc.field_1724.method_31548().field_7545;
               int webSlot = this.getWebSlot();
               if (!this.placeBlock(pos, this.rotate.getValue(), webSlot)) {
                  return false;
               } else {
                  if (this.noMine.getValue() && pos.equals(SpeedMine.breakPos)) {
                     SpeedMine.breakPos = null;
                  }

                  BlockUtil.placedPos.add(pos);
                  this.progress++;
                  if (this.inventorySwap.getValue()) {
                     this.doSwap(webSlot);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlot);
                  }

                  force = false;
                  ignore = false;
                  this.timer.reset();
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean isInBurrow(PlayerEntity player) {
      for (float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
         for (float z : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
            BlockPosX pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 0.15F, player.method_23321() + (double)z);
            if (mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10540
               || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_9987
               || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10443
               || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_23152) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean placeBlock(BlockPos pos, boolean rotate, int slot) {
      Direction side = BlockUtil.getPlaceSide(pos);
      if (side == null) {
         return BlockUtil.airPlace() ? this.clickBlock(pos, Direction.field_11033, rotate, slot) : false;
      } else {
         return this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, slot);
      }
   }

   public boolean clickBlock(BlockPos pos, Direction side, boolean rotate, int slot) {
      Vec3d directionVec = new Vec3d(
         (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
         (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
         (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
      );
      if (rotate && !this.faceVector(directionVec)) {
         return false;
      } else {
         this.doSwap(slot);
         EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
         if (this.interact.getValue()) {
            mc.field_1724.field_3944.method_52787(new PlayerInteractBlockC2SPacket(Hand.field_5808, result, BlockUtil.getWorldActionId(mc.field_1687)));
         }

         if (this.seqpack.getValue()) {
            Module_eSdgMXWuzcxgQVaJFmKZ.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(Hand.field_5808, result, id));
         }

         return true;
      }
   }

   private boolean faceVector(Vec3d directionVec) {
      if (!this.yawStep.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         return me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
      }
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private int getWebSlot() {
      return this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10343) : InventoryUtil.findBlock(Blocks.field_10343);
   }
}
