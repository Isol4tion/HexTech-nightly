package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class Cleaner_iFwqnooxsJEmHoVteFeQ extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Cleaner_iFwqnooxsJEmHoVteFeQ INSTANCE;
   public final Timer lastBreakTimer = new Timer();
   private final Timer placeTimer = new Timer();
   private final Timer noPosTimer = new Timer();
   private final Timer switchTimer = new Timer();
   private final Timer delayTimer = new Timer();
   private final List<BlockPos> checkPos = new ArrayList();
   public Vec3d directionVec = null;
   public BlockPos syncPos;
   public float state = 2.0F;
   EnumSetting<Cleaner_zDJWekpHpTWvapSemLWN> page = this.add(new EnumSetting("Page", Cleaner_zDJWekpHpTWvapSemLWN.General));
   private final BooleanSetting rotate = this.add(
      new BooleanSetting("Rotate", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation).setParent()
   );
   private final BooleanSetting onBreak = this.add(
      new BooleanSetting("OnBreak", false, v -> this.rotate.isOpen() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation)
   );
   private final SliderSetting yOffset = this.add(
      new SliderSetting(
         "YOffset", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.onBreak.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation
      )
   );
   private final BooleanSetting yawStep = this.add(
      new BooleanSetting("YawStep", false, v -> this.rotate.isOpen() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation)
   );
   private final SliderSetting steps = this.add(
      new SliderSetting(
         "Steps", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation
      )
   );
   private final BooleanSetting checkFov = this.add(
      new BooleanSetting(
         "OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation
      )
   );
   private final SliderSetting fov = this.add(
      new SliderSetting(
         "Fov",
         30,
         0,
         50,
         v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation
      )
   );
   private final SliderSetting priority = this.add(
      new SliderSetting(
         "Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation
      )
   );
   private final SliderSetting noPosTimerSet = this.add(
      new SliderSetting("NoPosTimer", 50, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation)
   );
   private final BooleanSetting eatingPause = this.add(
      new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General)
   );
   private final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
   private final BooleanSetting attack = this.add(new BooleanSetting("Attack", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
   private final BooleanSetting forceWeb = this.add(new BooleanSetting("ForceWeb", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
   private final BooleanSetting afterBreak = this.add(
      new BooleanSetting("PlaceAfterBreak", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General)
   );
   private final SliderSetting range = this.add(
      new SliderSetting("Range", 5.0, 1.0, 10.0, 0.1, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("m")
   );
   private final SliderSetting wallRange = this.add(
      new SliderSetting("WallRange", 5.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General)
   );
   private final SliderSetting maxSelf = this.add(
      new SliderSetting("MaxSelf", 3.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General)
   );
   private final SliderSetting placeDelay = this.add(
      new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("ms")
   );
   private final SliderSetting breakDelay = this.add(
      new SliderSetting("BreakDelay", 0, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("ms")
   );
   private final SliderSetting calcDelay = this.add(
      new SliderSetting("CalcDelay", 250, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("ms")
   );
   private final BooleanSetting checkBurrow = this.add(new BooleanSetting("CheckBurrow", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Check));
   private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Check));
   private final BooleanSetting fireFix = this.add(new BooleanSetting("FireFix", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Check));
   EnumSetting<Enum_rNhWITNdkrqkhKfDZgGo> autoSwap = this.add(
      new EnumSetting("AutoSwap", Enum_rNhWITNdkrqkhKfDZgGo.Off, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General)
   );
   EnumSetting<SwingSide> swingMode = this.add(new EnumSetting("Swing", SwingSide.Server, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
   private final float lastYaw = 0.0F;
   private final float lastPitch = 0.0F;

   public Cleaner_iFwqnooxsJEmHoVteFeQ() {
      super("WebCleaner", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static Block getBlock(BlockPos pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }

   @Override
   public void onDisable() {
      this.directionVec = null;
   }

   @EventHandler
   public void onRotate(OffTrackEvent event) {
      if (this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null && !this.noPosTimer.passed(this.noPosTimerSet.getValue())) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }
   }

   @EventHandler(
      priority = -199
   )
   public void onPacketSend(PacketEvent event) {
      if (!event.isCancelled()) {
         if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
            this.switchTimer.reset();
         }
      }
   }

   public List<BlockPos> getWebPos(PlayerEntity player) {
      Vec3d playerPos = player.method_19538();
      ArrayList<BlockPos> qzw = new ArrayList();

      for (float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for (float z : new float[]{0.0F, 0.3F, -0.3F}) {
            for (int y : new int[]{0, 1, 2}) {
               BlockPos pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214(), playerPos.method_10215() + (double)z)
                  .method_10086(y);
               if (mc.field_1687 == null || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10343) {
                  qzw.add(pos);
               }
            }
         }
      }

      return qzw;
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         if (this.delayTimer.passedMs((long)this.calcDelay.getValue())) {
            if (!Blink.INSTANCE.isOn()) {
               this.state = 2.0F;
               if (!this.checkBurrow.getValue() || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                  if (!this.selfGround.getValue() || mc.field_1724.method_24828() || me.hextech.HexTech.PLAYER.insideBlock) {
                     if (this.state == 2.0F) {
                        if (mc.field_1724 == null || !this.eatingPause.getValue() || !mc.field_1724.method_6115()) {
                           this.delayTimer.reset();
                           List<BlockPos> webPos = this.getWebPos(mc.field_1724);
                           if (webPos.size() >= 1) {
                              for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
                                 if (!this.behindWall(pos)
                                    && this.canExplodeReach(pos, (BlockPos)webPos.get(0), 6.1F)
                                    && (double)this.calculateDamage(pos.method_46558(), mc.field_1724, mc.field_1724) <= this.maxSelf.getValue()) {
                                    if (this.attack.getValue()) {
                                       this.doBreak(pos);
                                    }

                                    if (this.fireFix.getValue()) {
                                       CombatUtil.terrainIgnore = false;
                                    }

                                    if ((double)pos.method_10264() - mc.field_1724.method_23318() <= 1.0
                                       && this.canTouch(pos.method_10074())
                                       && this.canPlaceCrystal(pos, false, false)
                                       && mc.field_1687.method_22347(pos)
                                       && mc.field_1687.method_8320(pos).method_26204() != Blocks.field_10036
                                       && this.place.getValue()) {
                                       this.doPlace(pos);
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

   public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
      float damage = OyveyExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
      CombatUtil.modifyPos = null;
      return damage;
   }

   private void doPlace(BlockPos pos) {
      this.noPosTimer.reset();
      if (mc.field_1724.method_6047().method_7909().equals(Items.field_8301)
         || mc.field_1724.method_6079().method_7909().equals(Items.field_8301)
         || this.findCrystal()) {
         if (this.canTouch(pos.method_10074())) {
            BlockPos obsPos = pos.method_10074();
            Direction facing = BlockUtil.getClickSide(obsPos);
            Vec3d vec = obsPos.method_46558()
               .method_1031(
                  (double)facing.method_10163().method_10263() * 0.5,
                  (double)facing.method_10163().method_10264() * 0.5,
                  (double)facing.method_10163().method_10260() * 0.5
               );
            if (facing != Direction.field_11036 && facing != Direction.field_11033) {
               vec = vec.method_1031(0.0, 0.45, 0.0);
            }

            if (!this.rotate.getValue() || this.faceVector(vec)) {
               if (this.placeTimer.passedMs((long)this.placeDelay.getValue())) {
                  if (!mc.field_1724.method_6047().method_7909().equals(Items.field_8301)
                     && !mc.field_1724.method_6079().method_7909().equals(Items.field_8301)) {
                     this.placeTimer.reset();
                     this.syncPos = pos;
                     int old = mc.field_1724.method_31548().field_7545;
                     int crystal = this.getCrystal();
                     if (crystal == -1) {
                        return;
                     }

                     this.doSwap(crystal);
                     this.placeCrystal(pos);
                     if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent) {
                        this.doSwap(old);
                     } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
                        this.doSwap(crystal);
                        EntityUtil.syncInventory();
                     }
                  } else {
                     this.placeTimer.reset();
                     this.syncPos = pos;
                     this.placeCrystal(pos);
                  }
               }
            }
         }
      }
   }

   public boolean findCrystal() {
      return this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Off ? false : this.getCrystal() != -1;
   }

   private int getCrystal() {
      if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
         return InventoryUtil.findItem(Items.field_8301);
      } else {
         return this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory ? InventoryUtil.findItemInventorySlot(Items.field_8301) : -1;
      }
   }

   private void doSwap(int slot) {
      if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
         InventoryUtil.switchToSlot(slot);
      } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      }
   }

   private void placeCrystal(BlockPos pos) {
      boolean offhand = mc.field_1724.method_6079().method_7909() == Items.field_8301;
      BlockPos obsPos = pos.method_10074();
      Direction facing = BlockUtil.getClickSide(obsPos);
      BlockUtil.clickBlock(obsPos, facing, false, offhand ? Hand.field_5810 : Hand.field_5808, (SwingSide)this.swingMode.getValue());
   }

   private void doBreak(BlockPos pos) {
      this.noPosTimer.reset();
      this.lastBreakTimer.reset();
      Iterator var2 = BlockUtil.getEndCrystals(
            new Box(
               (double)pos.method_10263(),
               (double)pos.method_10264(),
               (double)pos.method_10260(),
               (double)(pos.method_10263() + 1),
               (double)(pos.method_10264() + 2),
               (double)(pos.method_10260() + 1)
            )
         )
         .iterator();
      if (var2.hasNext()) {
         EndCrystalEntity entity = (EndCrystalEntity)var2.next();
         if (!this.rotate.getValue() || !this.onBreak.getValue() || this.faceVector(entity.method_19538().method_1031(0.0, this.yOffset.getValue(), 0.0))) {
            if (CombatUtil.breakTimer.passedMs((long)this.breakDelay.getValue())) {
               CombatUtil.breakTimer.reset();
               this.syncPos = pos;
               mc.method_1562().method_52787(PlayerInteractEntityC2SPacket.method_34206(entity, mc.field_1724.method_5715()));
               mc.field_1724.method_7350();
               EntityUtil.swingHand(Hand.field_5808, (SwingSide)this.swingMode.getValue());
               if (pos != null
                  && mc.field_1724 != null
                  && this.afterBreak.getValue()
                  && (!this.yawStep.getValue() || !this.checkFov.getValue() || me.hextech.HexTech.ROTATE.inFov(entity.method_19538(), this.fov.getValueFloat()))
                  )
                {
                  this.doPlace(pos);
               }

               if (this.forceWeb.getValue() && WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.isOn()) {
                  WebAuraTick_gaIdrzDzsbegzNTtPQoV.force = true;
               }
            }
         }
      }
   }

   public boolean behindWall(BlockPos pos) {
      Vec3d testVec = new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
      HitResult result = mc.field_1687
         .method_17742(new RaycastContext(EntityUtil.getEyesPos(), testVec, ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724));
      return result != null && result.method_17783() != Type.field_1333
         ? mc.field_1724.method_33571().method_1022(pos.method_46558().method_1031(0.0, -0.5, 0.0)) > this.wallRange.getValue()
         : false;
   }

   private boolean canTouch(BlockPos pos) {
      Direction side = BlockUtil.getClickSideStrict(pos);
      return side != null
         && pos.method_46558()
               .method_1019(
                  new Vec3d(
                     (double)side.method_10163().method_10263() * 0.5,
                     (double)side.method_10163().method_10264() * 0.5,
                     (double)side.method_10163().method_10260() * 0.5
                  )
               )
               .method_1022(mc.field_1724.method_33571())
            <= this.range.getValue();
   }

   public boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      BlockPos obsPos = pos.method_10074();
      BlockPos boost = obsPos.method_10084();
      BlockPos boost2 = boost.method_10084();
      return (getBlock(obsPos) == Blocks.field_9987 || getBlock(obsPos) == Blocks.field_10540)
         && BlockUtil.getClickSideStrict(obsPos) != null
         && this.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem)
         && this.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem)
         && (mc.field_1687.method_22347(boost) || BlockUtil.hasCrystal(boost) && getBlock(boost) == Blocks.field_10036);
   }

   private boolean noEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
      Iterator var4 = BlockUtil.getEntities(new Box(pos)).iterator();

      while (true) {
         if (!var4.hasNext()) {
            return true;
         }

         Entity entity = (Entity)var4.next();
         if (entity.method_5805() && (!ignoreItem || !(entity instanceof ItemEntity))) {
            if (!(entity instanceof EndCrystalEntity)) {
               break;
            }

            if (!ignoreCrystal) {
               return false;
            }

            if (!mc.field_1724.method_6057(entity) && !(mc.field_1724.method_33571().method_1022(entity.method_19538()) <= this.wallRange.getValue())) {
               break;
            }
         }
      }

      return false;
   }

   public boolean canExplodeReach(BlockPos explosionCenter, BlockPos targetBlock, float power) {
      if (explosionCenter.method_10264() > targetBlock.method_10264()) {
         return false;
      } else {
         Vec3d start = new Vec3d(
            (double)explosionCenter.method_10263() + 0.5, (double)explosionCenter.method_10264() + 0.5, (double)explosionCenter.method_10260() + 0.5
         );
         Vec3d end = new Vec3d((double)targetBlock.method_10263() + 0.5, (double)targetBlock.method_10264() + 0.5, (double)targetBlock.method_10260() + 0.5);
         Vec3d direction = end.method_1020(start).method_1029();
         double distance = start.method_1022(end);
         double step = 0.5;

         for (double d = 0.0; d < distance; d += step) {
            Vec3d currentPos = start.method_1019(direction.method_1021(d));
            BlockPosX currentBlockPos = new BlockPosX(currentPos);
            BlockState blockState = null;
            if (mc.field_1687 != null) {
               blockState = mc.field_1687.method_8320(currentBlockPos);
            }

            if (blockState == null || !blockState.method_26215() && blockState.method_26204().method_9520() > power) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean willBlockBeDestroyedByExplosion(World world, BlockPos explosionPos, BlockPos pos2, float power) {
      Explosion explosion = new Explosion(
         world,
         null,
         (double)explosionPos.method_10263(),
         (double)explosionPos.method_10264(),
         (double)explosionPos.method_10260(),
         power,
         false,
         DestructionType.field_18687
      );
      BlockState blockState = world.method_8320(pos2);
      float doubleExplosionSize = 2.0F * explosion.method_55107();
      double distancedsize = (double)MathHelper.method_15355((float)explosionPos.method_10262(pos2)) / (double)doubleExplosionSize;
      float damage = (float)((1.0 - distancedsize) * (double)this.getExposure(explosionPos.method_46558(), pos2));
      float blastResistance = blockState.method_26204().method_9520();
      return (double)damage > (double)blastResistance * 3.5;
   }

   public float getExposure(Vec3d source, BlockPos pos2) {
      Box box = new Box(pos2);
      double d = 1.0 / ((box.field_1320 - box.field_1323) * 2.0 + 1.0);
      double e = 1.0 / ((box.field_1325 - box.field_1322) * 2.0 + 1.0);
      double f = 1.0 / ((box.field_1324 - box.field_1321) * 2.0 + 1.0);
      double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
      double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
      if (!(d < 0.0) && !(e < 0.0) && !(f < 0.0)) {
         int i = 0;
         int j = 0;

         for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
               for (double m = 0.0; m <= 1.0; m += f) {
                  double n = MathHelper.method_16436(k, box.field_1323, box.field_1320);
                  Vec3d vec3d = new Vec3d(
                     n + g, MathHelper.method_16436(l, box.field_1322, box.field_1325), MathHelper.method_16436(m, box.field_1321, box.field_1324) + h
                  );
                  if (mc.field_1687 != null
                     && mc.field_1687
                           .method_17742(
                              new RaycastContext(
                                 vec3d, source, ShapeType.field_17558, FluidHandling.field_1348, (new Cleaner_KppopygwsrtGITfPgSgG(this, this, pos2)).predict
                              )
                           )
                           .method_17783()
                        == Type.field_1333) {
                     i++;
                  }

                  j++;
               }
            }
         }

         return (float)i / (float)j;
      } else {
         return 0.0F;
      }
   }

   @Override
   public void onEnable() {
      this.lastBreakTimer.reset();
   }

   private boolean faceVector(Vec3d directionVec) {
      if (!this.yawStep.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         return me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat());
      }
   }
}
