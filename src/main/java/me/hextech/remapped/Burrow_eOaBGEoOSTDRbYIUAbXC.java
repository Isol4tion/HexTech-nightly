package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Burrow_eOaBGEoOSTDRbYIUAbXC extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Burrow_eOaBGEoOSTDRbYIUAbXC INSTANCE;
   public final EnumSetting<Enum_MOJxjPVaItysgPVNqBDX> page = this.add(new EnumSetting("Page", Enum_MOJxjPVaItysgPVNqBDX.General));
   public final SliderSetting placeDelay = this.add(new SliderSetting("Delay", 50, 0, 500, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting webcheck = this.add(new BooleanSetting("WebCheck", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting antiLag = this.add(new BooleanSetting("AntiLag", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final SliderSetting multiPlace = this.add(
      new SliderSetting("BlocksPer", 4.0, 1.0, 4.0, 1.0, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General)
   );
   public final SliderSetting offest = this.add(
      new SliderSetting("BoxOffset", 0.3, 0.0, 1.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General).setSuffix("/int x2")
   );
   public final BooleanSetting syncCrystal = this.add(new BooleanSetting("SyncCrsytal", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting helper = this.add(new BooleanSetting("Helper", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting ground = this.add(new BooleanSetting("Ground", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting disable = this.add(new BooleanSetting("Disable", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting SmartActive = this.add(new BooleanSetting("SmartActive", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public final BooleanSetting onlyStatic = this.add(new BooleanSetting("OnlyStatic", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final BooleanSetting autocenter = this.add(new BooleanSetting("AutoCenter", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final BooleanSetting cancelblink = this.add(new BooleanSetting("CancelBlink", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final EnumSetting<Enum_HAljcKfxHffzpuFTAOaQ> rotate = this.add(
      new EnumSetting("RotateMode", Enum_HAljcKfxHffzpuFTAOaQ.Bypass, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.Rotate)
   );
   private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final BooleanSetting fakeMove = this.add(new BooleanSetting("FakeMove", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.Above));
   private final BooleanSetting center = this.add(new BooleanSetting("AllowCenter", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.Above));
   private final BooleanSetting Air = this.add(new BooleanSetting("SejiaLag/Air", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.intersic));
   private final BooleanSetting Wait = this.add(new BooleanSetting("Wait/SeJiaAutoLag", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.intersic));
   private final BooleanSetting nomove = this.add(new BooleanSetting("MovingPause", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.intersic));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final BooleanSetting noeat = this.add(new BooleanSetting("NoUsingPlace", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   private final EnumSetting<Enum_YBWtbEXllPkRSdEiULQW> lagMode = this.add(
      new EnumSetting("LagMode", Enum_YBWtbEXllPkRSdEiULQW.Auto, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final EnumSetting<Enum_YBWtbEXllPkRSdEiULQW> aboveLagMode = this.add(
      new EnumSetting("Above", Enum_YBWtbEXllPkRSdEiULQW.Auto, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final SliderSetting AutoXZ = this.add(
      new SliderSetting("AutoXZ", 3.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final SliderSetting AutoUp = this.add(
      new SliderSetting("AutoUp", 3.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final SliderSetting AutoDown = this.add(
      new SliderSetting("AutoDown", 3.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final SliderSetting Distance = this.add(
      new SliderSetting("Distance", 2.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final SliderSetting cuicanHeight = this.add(
      new SliderSetting("cuicanHeightLag", -7.0, -10.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode)
   );
   private final Timer inWebTimer = new Timer();
   private final Timer timer = new Timer();
   public BooleanSetting checkselfpos = this.add(new BooleanSetting("CheckSelfPos", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
   public List<BlockPos> placePos = new ArrayList();
   int progress = 0;
   BlockPos movedPos = null;
   private boolean shouldCenter = true;

   public Burrow_eOaBGEoOSTDRbYIUAbXC() {
      super("Burrow", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
      Vec3d vec3d = posTo.method_1020(posFrom);
      return SelfTrap.getRotationFromVec(vec3d);
   }

   @EventHandler
   @Override
   public void onUpdate() {
      if (!this.cancelblink.getValue() || !Blink.INSTANCE.isOn()) {
         if (!this.syncCrystal.getValue() || AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null) {
            if (this.timer.passedMs((long)this.placeDelay.getValue())) {
               if (mc.field_1724 == null || !this.check(this.nomove.getValue(), this.ground.getValue())) {
                  this.movedPos = null;
                  if (!this.ground.getValue() || mc.field_1724.method_24828()) {
                     if (this.Air.getValue() && mc.field_1724.method_3149()) {
                        this.toggle();
                     }

                     if (!this.noeat.getValue() || !EntityUtil.isUsing()) {
                        if (this.webcheck.getValue() && HoleKickTest.isInWeb(mc.field_1724)) {
                           this.inWebTimer.reset();
                        } else {
                           if (this.antiLag.getValue()) {
                              BlockUtil.getState(EntityUtil.getPlayerPos(true).method_10074()).method_51366();
                           }

                           this.timer.reset();
                           int oldSlot = mc.field_1724.method_31548().field_7545;
                           int block;
                           if ((block = this.getBlock()) == -1) {
                              CommandManager.sendChatMessage("§e[?] §c§oObsidian" + (this.enderChest.getValue() ? "/EnderChest" : "") + "?");
                              this.disable();
                           } else {
                              BlockPos pos1 = new BlockPosX(
                                 mc.field_1724.method_23317() + this.offest.getValue(),
                                 mc.field_1724.method_23318() + 0.5,
                                 mc.field_1724.method_23321() + this.offest.getValue()
                              );
                              BlockPos pos2 = new BlockPosX(
                                 mc.field_1724.method_23317() - this.offest.getValue(),
                                 mc.field_1724.method_23318() + 0.5,
                                 mc.field_1724.method_23321() + this.offest.getValue()
                              );
                              BlockPos pos3 = new BlockPosX(
                                 mc.field_1724.method_23317() + this.offest.getValue(),
                                 mc.field_1724.method_23318() + 0.5,
                                 mc.field_1724.method_23321() - this.offest.getValue()
                              );
                              BlockPos pos4 = new BlockPosX(
                                 mc.field_1724.method_23317() - this.offest.getValue(),
                                 mc.field_1724.method_23318() + 0.5,
                                 mc.field_1724.method_23321() - this.offest.getValue()
                              );
                              BlockPos playerPos = EntityUtil.getPlayerPos(true);
                              if (!this.canPlace(pos1) && !this.canPlace(pos2) && !this.canPlace(pos3) && !this.canPlace(pos4)) {
                                 if (!this.Wait.getValue()) {
                                    this.disable();
                                 }
                              } else {
                                 boolean above = false;
                                 BlockPos headPos = EntityUtil.getPlayerPos().method_10086(2);
                                 boolean rotate = this.rotate.getValue() == Enum_HAljcKfxHffzpuFTAOaQ.Normal;
                                 CombatUtil.attackCrystal(pos1, rotate, false);
                                 CombatUtil.attackCrystal(pos2, rotate, false);
                                 CombatUtil.attackCrystal(pos3, rotate, false);
                                 CombatUtil.attackCrystal(pos4, rotate, false);
                                 if (!mc.field_1724.method_18276()
                                    && !this.Trapped(headPos)
                                    && !this.Trapped(headPos.method_10069(1, 0, 0))
                                    && !this.Trapped(headPos.method_10069(-1, 0, 0))
                                    && !this.Trapped(headPos.method_10069(0, 0, 1))
                                    && !this.Trapped(headPos.method_10069(0, 0, -1))
                                    && !this.Trapped(headPos.method_10069(1, 0, -1))
                                    && !this.Trapped(headPos.method_10069(-1, 0, -1))
                                    && !this.Trapped(headPos.method_10069(1, 0, 1))
                                    && !this.Trapped(headPos.method_10069(-1, 0, 1))) {
                                    mc.field_1724
                                       .field_3944
                                       .method_52787(
                                          new PositionAndOnGround(
                                             mc.field_1724.method_23317(),
                                             mc.field_1724.method_23318() + 0.4199999868869781,
                                             mc.field_1724.method_23321(),
                                             false
                                          )
                                       );
                                    mc.field_1724
                                       .field_3944
                                       .method_52787(
                                          new PositionAndOnGround(
                                             mc.field_1724.method_23317(),
                                             mc.field_1724.method_23318() + 0.7531999805212017,
                                             mc.field_1724.method_23321(),
                                             false
                                          )
                                       );
                                    mc.field_1724
                                       .field_3944
                                       .method_52787(
                                          new PositionAndOnGround(
                                             mc.field_1724.method_23317(),
                                             mc.field_1724.method_23318() + 0.9999957640154541,
                                             mc.field_1724.method_23321(),
                                             false
                                          )
                                       );
                                    mc.field_1724
                                       .field_3944
                                       .method_52787(
                                          new PositionAndOnGround(
                                             mc.field_1724.method_23317(),
                                             mc.field_1724.method_23318() + 1.1661092609382138,
                                             mc.field_1724.method_23321(),
                                             false
                                          )
                                       );
                                 } else {
                                    above = true;
                                    if (!this.fakeMove.getValue()) {
                                       if (!this.Wait.getValue()) {
                                          this.disable();
                                       }

                                       return;
                                    }

                                    boolean moved = false;
                                    if (this.checkSelf(playerPos) && !BlockUtil.canReplace(playerPos)) {
                                       this.gotoPos(playerPos);
                                    } else {
                                       for (Direction facing : Direction.values()) {
                                          if (facing != Direction.field_11036 && facing != Direction.field_11033) {
                                             BlockPos pos = playerPos.method_10093(facing);
                                             if (this.checkSelf(pos) && !BlockUtil.canReplace(pos)) {
                                                this.gotoPos(pos);
                                                moved = true;
                                                break;
                                             }
                                          }
                                       }

                                       if (!moved) {
                                          for (Direction facingx : Direction.values()) {
                                             if (facingx != Direction.field_11036 && facingx != Direction.field_11033) {
                                                BlockPos var28 = playerPos.method_10093(facingx);
                                                if (this.checkSelf(var28)) {
                                                   this.gotoPos(var28);
                                                   moved = true;
                                                   break;
                                                }
                                             }
                                          }

                                          if (!moved) {
                                             if (!this.center.getValue()) {
                                                return;
                                             }

                                             for (Direction facingxx : Direction.values()) {
                                                if (facingxx != Direction.field_11036 && facingxx != Direction.field_11033) {
                                                   BlockPos var29 = playerPos.method_10093(facingxx);
                                                   if (this.canAbove(var29)) {
                                                      this.gotoPos(var29);
                                                      moved = true;
                                                      break;
                                                   }
                                                }
                                             }

                                             if (!moved) {
                                                if (!this.Wait.getValue()) {
                                                   this.disable();
                                                }

                                                return;
                                             }
                                          }
                                       }
                                    }
                                 }

                                 this.doSwap(block);
                                 this.progress = 0;
                                 this.placePos.clear();
                                 if (this.rotate.getValue() == Enum_HAljcKfxHffzpuFTAOaQ.Bypass) {
                                    EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), 90.0F);
                                 }

                                 this.placeBlock(playerPos, rotate);
                                 this.placeBlock(pos1, rotate);
                                 this.placeBlock(pos2, rotate);
                                 this.placeBlock(pos3, rotate);
                                 this.placeBlock(pos4, rotate);
                                 if (this.helper.getValue()) {
                                    this.placeBlock(playerPos.method_10074(), rotate);
                                 }

                                 this.placeBlock(playerPos, rotate);
                                 if (this.helper.getValue()) {
                                    this.placeBlock(pos1.method_10074(), rotate);
                                 }

                                 this.placeBlock(pos1, rotate);
                                 if (this.helper.getValue()) {
                                    this.placeBlock(pos2.method_10074(), rotate);
                                 }

                                 this.placeBlock(pos2, rotate);
                                 if (this.helper.getValue()) {
                                    this.placeBlock(pos3.method_10074(), rotate);
                                 }

                                 this.placeBlock(pos3, rotate);
                                 if (this.helper.getValue()) {
                                    this.placeBlock(pos4.method_10074(), rotate);
                                 }

                                 this.placeBlock(pos4, rotate);
                                 if (this.inventory.getValue()) {
                                    this.doSwap(block);
                                    EntityUtil.syncInventory();
                                 } else {
                                    this.doSwap(oldSlot);
                                 }

                                 switch (above ? (Enum_YBWtbEXllPkRSdEiULQW)this.aboveLagMode.getValue() : (Enum_YBWtbEXllPkRSdEiULQW)this.lagMode.getValue()) {
                                    case Auto:
                                       double distance = 0.0;
                                       BlockPos bestPos = null;

                                       for (int i = 0; i < 10; i++) {
                                          BlockPos pos = EntityUtil.getPlayerPos().method_10086(i);
                                          if (this.canGoto(pos)
                                             && !(MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < 2.0F)
                                             && (bestPos == null || mc.field_1724.method_5707(pos.method_46558()) < distance)) {
                                             bestPos = pos;
                                             distance = mc.field_1724.method_5707(pos.method_46558());
                                          }
                                       }

                                       if (bestPos != null) {
                                          mc.field_1724
                                             .field_3944
                                             .method_52787(
                                                new PositionAndOnGround(
                                                   (double)bestPos.method_10263() + 0.5,
                                                   (double)bestPos.method_10264(),
                                                   (double)bestPos.method_10260() + 0.5,
                                                   false
                                                )
                                             );
                                       }
                                       break;
                                    case TrollHack:
                                       for (int ix = 0; ix < 20; ix++) {
                                          mc.field_1724
                                             .field_3944
                                             .method_52787(
                                                new PositionAndOnGround(
                                                   mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1337.0, mc.field_1724.method_23321(), false
                                                )
                                             );
                                       }
                                       break;
                                    case Strict:
                                       mc.field_1724
                                          .method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 3.0, mc.field_1724.method_23321());
                                       mc.method_1562()
                                          .method_52787(
                                             new PositionAndOnGround(
                                                mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true
                                             )
                                          );
                                       break;
                                    case CuiCan:
                                       mc.field_1724
                                          .field_3944
                                          .method_52787(
                                             new PositionAndOnGround(
                                                mc.field_1724.method_23317(),
                                                mc.field_1724.method_23318() + 2.3400880035762786,
                                                mc.field_1724.method_23321(),
                                                false
                                             )
                                          );
                                       break;
                                    case Sejia:
                                       mc.field_1724
                                          .field_3944
                                          .method_52787(
                                             new PositionAndOnGround(
                                                mc.field_1724.method_23317(),
                                                mc.field_1724.method_23318() + this.cuicanHeight.getValue(),
                                                mc.field_1724.method_23321(),
                                                false
                                             )
                                          );
                                       break;
                                    case TP:
                                       ArrayList<BlockPos> list = new ArrayList();

                                       for (double x = mc.field_1724.method_19538().method_10216() - this.AutoXZ.getValue();
                                          x < mc.field_1724.method_19538().method_10216() + this.AutoXZ.getValue();
                                          x++
                                       ) {
                                          for (double z = mc.field_1724.method_19538().method_10215() - this.AutoXZ.getValue();
                                             z < mc.field_1724.method_19538().method_10215() + this.AutoXZ.getValue();
                                             z++
                                          ) {
                                             for (double y = mc.field_1724.method_19538().method_10214() - this.AutoDown.getValue();
                                                y < mc.field_1724.method_19538().method_10214() + this.AutoUp.getValue();
                                                y++
                                             ) {
                                                list.add(new BlockPosX(x, y, z));
                                             }
                                          }
                                       }

                                       double distance = 0.0;
                                       BlockPos bestPos = null;

                                       for (BlockPos pos : list) {
                                          if (this.canAbove(pos)
                                             && this.canGoto(pos)
                                             && !(
                                                (double)MathHelper.method_15355(
                                                      (float)mc.field_1724.method_5707(pos.method_46558().method_1031(0.0, -0.5, 0.0))
                                                   )
                                                   < this.Distance.getValue()
                                             )
                                             && (bestPos == null || mc.field_1724.method_5707(pos.method_46558()) < distance)) {
                                             bestPos = pos;
                                             distance = mc.field_1724.method_5707(pos.method_46558());
                                          }
                                       }

                                       if (bestPos != null) {
                                          mc.field_1724
                                             .field_3944
                                             .method_52787(
                                                new PositionAndOnGround(
                                                   (double)bestPos.method_10263() + 0.5,
                                                   (double)bestPos.method_10264(),
                                                   (double)bestPos.method_10260() + 0.5,
                                                   false
                                                )
                                             );
                                       }
                                       break;
                                    case Invalid:
                                       for (int ix = -10; ix < 10; ix++) {
                                          if (ix == -1) {
                                             ix = 4;
                                          }

                                          if (mc.field_1687
                                                .method_8320(BlockPos.method_49638(mc.field_1724.method_19538()).method_10069(0, ix, 0))
                                                .method_26204()
                                                .equals(Blocks.field_10124)
                                             && mc.field_1687
                                                .method_8320(BlockPos.method_49638(mc.field_1724.method_19538()).method_10069(0, ix + 1, 0))
                                                .method_26204()
                                                .equals(Blocks.field_10124)) {
                                             BlockPos posx = BlockPos.method_49638(mc.field_1724.method_19538()).method_10069(0, ix, 0);
                                             mc.field_1724
                                                .field_3944
                                                .method_52787(
                                                   new PositionAndOnGround(
                                                      (double)posx.method_10263() + 0.3, (double)posx.method_10264(), (double)posx.method_10260() + 0.3, false
                                                   )
                                                );
                                             return;
                                          }
                                       }
                                 }

                                 if (this.disable.getValue()) {
                                    this.disable();
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

   @EventHandler(
      priority = -1
   )
   public void onMove(MoveEvent event) {
      if (!nullCheck() && this.autocenter.getValue() && !mc.field_1724.method_6128()) {
         BlockPos blockPos = EntityUtil.getPlayerPos(true);
         if (mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 <= 0.2
            && mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 >= -0.2
            && mc.field_1724.method_23321() - (double)blockPos.method_10260() - 0.5 <= 0.2
            && mc.field_1724.method_23321() - 0.5 - (double)blockPos.method_10260() >= -0.2) {
            if (this.shouldCenter && (mc.field_1724.method_24828() || MovementUtil.isMoving())) {
               event.setX(0.0);
               event.setZ(0.0);
               this.shouldCenter = false;
            }
         } else if (this.shouldCenter) {
            Vec3d centerPos = EntityUtil.getPlayerPos(true).method_46558();
            float rotation = getRotationTo(mc.field_1724.method_19538(), centerPos).field_1343;
            float yawRad = rotation / 180.0F * (float) Math.PI;
            double dist = mc.field_1724.method_19538().method_1022(new Vec3d(centerPos.field_1352, mc.field_1724.method_23318(), centerPos.field_1350));
            double cappedSpeed = Math.min(0.2873, dist);
            double x = (double)(-((float)Math.sin((double)yawRad))) * cappedSpeed;
            double z = (double)((float)Math.cos((double)yawRad)) * cappedSpeed;
            event.setX(x);
            event.setZ(z);
         }
      }
   }

   public boolean check(boolean onlyStatic, boolean value) {
      return MovementUtil.isMoving() && onlyStatic;
   }

   private void placeBlock(BlockPos pos, boolean rotate) {
      if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.multiPlace.getValueInt()) {
         this.placePos.add(pos);
         if (BlockUtil.airPlace()) {
            this.progress++;
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos, Direction.field_11036, rotate, this.packetPlace.getValue());
         }

         Direction side;
         if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
         }

         this.progress++;
         BlockUtil.placedPos.add(pos);
         BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), rotate, this.packetPlace.getValue());
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }
   }

   private void gotoPos(BlockPos offPos) {
      this.movedPos = offPos;
      if (Math.abs((double)offPos.method_10263() + 0.5 - mc.field_1724.method_23317())
         < Math.abs((double)offPos.method_10260() + 0.5 - mc.field_1724.method_23321())) {
         mc.field_1724
            .field_3944
            .method_52787(
               new PositionAndOnGround(
                  mc.field_1724.method_23317(),
                  mc.field_1724.method_23318() + 0.2,
                  mc.field_1724.method_23321() + ((double)offPos.method_10260() + 0.5 - mc.field_1724.method_23321()),
                  true
               )
            );
      } else {
         mc.field_1724
            .field_3944
            .method_52787(
               new PositionAndOnGround(
                  mc.field_1724.method_23317() + ((double)offPos.method_10263() + 0.2 - mc.field_1724.method_23317()),
                  mc.field_1724.method_23318() + 0.2,
                  mc.field_1724.method_23321(),
                  true
               )
            );
      }
   }

   private boolean canGoto(BlockPos pos) {
      return !BlockUtil.getState(pos).method_51366() && !BlockUtil.getState(pos.method_10084()).method_51366();
   }

   private boolean canAbove(BlockPos pos) {
      return mc.field_1687.method_22347(pos) && mc.field_1687.method_22347(pos.method_10084());
   }

   public boolean canPlace(BlockPos pos) {
      if (BlockUtil.getPlaceSide(pos) == null) {
         return false;
      } else if (!BlockUtil.canReplace(pos)) {
         return false;
      } else {
         if (this.checkselfpos.getValue() && pos.equals(SpeedMine.breakPos)) {
            SpeedMine.breakPos = null;
         }

         if (BurrowAssist.INSTANCE.isOn() && BurrowAssist.INSTANCE.checkPos.getValue()) {
            for (MineManager breakData : new HashMap(me.hextech.HexTech.BREAK.breakMap).values()) {
               if (breakData != null && breakData.getEntity() != null && pos.equals(breakData.pos) && breakData.getEntity() != mc.field_1724) {
                  return false;
               }
            }
         }

         return !this.hasEntity(pos);
      }
   }

   private boolean hasEntity(BlockPos pos) {
      for (Entity entity : mc.field_1687.method_18467(Entity.class, new Box(pos))) {
         if (entity != mc.field_1724
            && entity.method_5805()
            && !(entity instanceof ItemEntity)
            && !(entity instanceof ExperienceOrbEntity)
            && !(entity instanceof ExperienceBottleEntity)
            && !(entity instanceof ArrowEntity)
            && (!(entity instanceof EndCrystalEntity) || !this.breakCrystal.getValue())
            && (!(entity instanceof ArmorStandEntity) || !CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue())) {
            return true;
         }
      }

      return false;
   }

   private boolean checkSelf(BlockPos pos) {
      return mc.field_1724.method_5829().method_994(new Box(pos));
   }

   private boolean Trapped(BlockPos pos) {
      return (mc.field_1687.method_39454(mc.field_1724, new Box(pos)) || BlockUtil.getBlock(pos) == Blocks.field_10343) && this.checkSelf(pos.method_10087(2));
   }

   private int getBlock() {
      if (this.inventory.getValue()) {
         return InventoryUtil.findBlockInventorySlot(Blocks.field_10540) == -1 && this.enderChest.getValue()
            ? InventoryUtil.findBlockInventorySlot(Blocks.field_10443)
            : InventoryUtil.findBlockInventorySlot(Blocks.field_10540);
      } else {
         return InventoryUtil.findBlock(Blocks.field_10540) == -1 && this.enderChest.getValue()
            ? InventoryUtil.findBlock(Blocks.field_10443)
            : InventoryUtil.findBlock(Blocks.field_10540);
      }
   }
}
