package me.hextech.remapped;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HoleFiller extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HoleFiller INSTANCE;
   public final EnumSetting<HoleFiller_sZEAolNLTvldDiHjSnlT> page = this.add(new EnumSetting("Page", HoleFiller_sZEAolNLTvldDiHjSnlT.General));
   public final SliderSetting delay = this.add(new SliderSetting("Delay", 50, 0, 500, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General));
   public final SliderSetting blocksPer = this.add(
      new SliderSetting("BlocksPer", 2, 1, 10, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   public final SliderSetting placeRange = this.add(
      new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   public final BooleanSetting any = this.add(new BooleanSetting("AnyHole", true, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General));
   public final BooleanSetting doubleHole = this.add(
      new BooleanSetting("DoubleHole", true, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final BooleanSetting detectMining = this.add(
      new BooleanSetting("DetectMining", true, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final BooleanSetting usingPause = this.add(
      new BooleanSetting("UsingPause", true, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final BooleanSetting inventory = this.add(
      new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final BooleanSetting webs = this.add(new BooleanSetting("Webs", false, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General));
   private final SliderSetting range = this.add(
      new SliderSetting("Radius", 1.9, 0.0, 6.0, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final SliderSetting saferange = this.add(
      new SliderSetting("SafeRange", 1.4, 0.0, 6.0, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final SliderSetting targetRange = this.add(
      new SliderSetting("TargetRange", 12.0, 0.0, 20.0, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final SliderSetting predictTicks = this.add(
      new SliderSetting("PredictTicks", 4, 0, 10, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.General)
   );
   private final BooleanSetting rotate = this.add(
      new BooleanSetting("Rotation", true, v -> this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.Rotate).setParent()
   );
   private final BooleanSetting newRotate = this.add(
      new BooleanSetting("NewRotate", false, v -> this.rotate.isOpen() && this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.Rotate)
   );
   private final SliderSetting yawStep = this.add(
      new SliderSetting(
         "YawStep",
         0.3F,
         0.1F,
         1.0,
         0.01F,
         v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.Rotate
      )
   );
   private final BooleanSetting checkLook = this.add(
      new BooleanSetting(
         "CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.Rotate
      )
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov",
         5.0,
         0.0,
         30.0,
         v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == HoleFiller_sZEAolNLTvldDiHjSnlT.Rotate
      )
   );
   private final Timer timer = new Timer();
   public Vec3d directionVec = null;
   int progress = 0;
   private PlayerEntity closestTarget;
   private float lastYaw = 0.0F;
   private float lastPitch = 0.0F;

   public HoleFiller() {
      super("HoleFiller", "Fills all safe spots in radius", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static boolean needSneak(Block in) {
      return BlockUtil.shiftBlocks.contains(in);
   }

   @Override
   public void onDisable() {
      this.closestTarget = null;
   }

   @Override
   public String getInfo() {
      return "";
   }

   @EventHandler(
      priority = 98
   )
   public void onRotate(RotateEvent event) {
      if (this.newRotate.getValue() && this.directionVec != null) {
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

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         if (this.timer.passedMs((long)this.delay.getValueInt())) {
            if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
               this.progress = 0;
               this.directionVec = null;
               this.timer.reset();
               int obbySlot = this.findBlock(Blocks.field_10540);
               int eChestSlot = this.findBlock(Blocks.field_10443);
               int webSlot = this.findBlock(Blocks.field_10343);
               int block = this.webs.getValue()
                  ? (webSlot == -1 ? (obbySlot == -1 ? eChestSlot : obbySlot) : webSlot)
                  : (obbySlot == -1 ? eChestSlot : obbySlot);
               if (this.webs.getValue() || obbySlot != -1 || eChestSlot != -1) {
                  if (!this.webs.getValue() || webSlot != -1 || obbySlot != -1 || eChestSlot != -1) {
                     ArrayList<PredictionSetting_XBpBEveLWEKUGQPHCCIS> list = new ArrayList();

                     for (PlayerEntity target : CombatUtil.getEnemies(this.targetRange.getRange())) {
                        list.add(new PredictionSetting_XBpBEveLWEKUGQPHCCIS(target));
                     }

                     new PredictionSetting_XBpBEveLWEKUGQPHCCIS(mc.field_1724);
                     if (!list.isEmpty()) {
                        for (PredictionSetting_XBpBEveLWEKUGQPHCCIS pap : list) {
                           for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat(), pap.player.method_19538())) {
                              if ((BlockUtil.isHole(pos, true, true, this.any.getValue()) || this.doubleHole.getValue() && CombatUtil.isDoubleHole(pos))
                                 && !(mc.field_1724.method_5707(pos.method_46558()) < this.saferange.getValue())
                                 && (!this.detectMining.getValue() || !me.hextech.HexTech.BREAK.isMining(pos) && !pos.equals(SpeedMine.breakPos))
                                 && this.progress < this.blocksPer.getValueInt()
                                 && BlockUtil.canPlace(pos, this.placeRange.getValue())
                                 && BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) != null
                                 && mc.field_1687.method_22347(pos)) {
                                 int oldSlot = mc.field_1724.method_31548().field_7545;
                                 this.doSwap(block);
                                 this.placeBlock(pos, this.rotate.getValue());
                                 this.progress++;
                                 if (this.inventory.getValue()) {
                                    this.doSwap(block);
                                    EntityUtil.syncInventory();
                                 } else {
                                    this.doSwap(oldSlot);
                                 }

                                 this.timer.reset();
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

   public boolean placeBlock(BlockPos pos, boolean rotate) {
      if (BlockUtil.airPlace()) {
         for (Direction i : Direction.values()) {
            if (mc.field_1687.method_22347(pos.method_10093(i))) {
               return this.clickBlock(pos, i, rotate);
            }
         }
      }

      Direction side = BlockUtil.getPlaceSide(pos);
      if (side == null) {
         return false;
      } else {
         Vec3d directionVec = new Vec3d(
            (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
            (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
            (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
         );
         BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
         BlockUtil.placedPos.add(pos);
         boolean sprint = false;
         if (mc.field_1724 != null) {
            sprint = mc.field_1724.method_5624();
         }

         boolean sneak = false;
         if (mc.field_1687 != null) {
            sneak = needSneak(mc.field_1687.method_8320(result.method_17777()).method_26204()) && !mc.field_1724.method_5715();
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

         EntityUtil.swingHand(Hand.field_5808, (SwingSide)CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
         return true;
      }
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
         mc.field_1724.field_3944.method_52787(new PlayerInteractBlockC2SPacket(Hand.field_5808, result, BlockUtil.getWorldActionId(mc.field_1687)));
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
            EntityUtil.sendYawAndPitch(angle[0], angle[1]);
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

   public int findBlock(Block blockIn) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }
}
