package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class XinBurrow extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static XinBurrow INSTANCE;
   private final Timer timer = new Timer();
   private final Timer webTimer = new Timer();
   private final BooleanSetting disable = this.add(new BooleanSetting("Disable", true));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 500, 0, 1000, v -> !this.disable.getValue()));
   private final SliderSetting webTime = this.add(new SliderSetting("WebTime", 0, 0, 500));
   private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true));
   private final BooleanSetting antiLag = this.add(new BooleanSetting("AntiLag", false));
   private final BooleanSetting detectMine = this.add(new BooleanSetting("DetectMining", false));
   private final BooleanSetting headFill = this.add(new BooleanSetting("HeadFill", false));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
   private final BooleanSetting down = this.add(new BooleanSetting("Down", true));
   private final BooleanSetting noSelfPos = this.add(new BooleanSetting("NoSelfPos", false));
   private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
   private final BooleanSetting sound = this.add(new BooleanSetting("Sound", true));
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 4.0, 1.0, 4.0, 1.0));
   private final EnumSetting<XinBurrow_uPHLOgEUPRaLZqLkrbQU> rotate = this.add(new EnumSetting("RotateMode", XinBurrow_uPHLOgEUPRaLZqLkrbQU.Bypass));
   private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true));
   private final BooleanSetting wait = this.add(new BooleanSetting("Wait", true, v -> !this.disable.getValue()));
   private final BooleanSetting fakeMove = this.add(new BooleanSetting("FakeMove", true).setParent());
   private final BooleanSetting center = this.add(new BooleanSetting("AllowCenter", false, v -> this.fakeMove.isOpen()));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final EnumSetting<XinBurrow_nDnthhFJjiqtqyXrSjjG> lagMode = this.add(new EnumSetting("LagMode", XinBurrow_nDnthhFJjiqtqyXrSjjG.TrollHack));
   private final EnumSetting<XinBurrow_nDnthhFJjiqtqyXrSjjG> aboveLagMode = this.add(new EnumSetting("MoveLagMode", XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart));
   private final SliderSetting smartX = this.add(
      new SliderSetting(
         "SmartXZ",
         3.0,
         0.0,
         10.0,
         0.1,
         v -> this.lagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart
      )
   );
   private final SliderSetting smartUp = this.add(
      new SliderSetting(
         "SmartUp",
         3.0,
         0.0,
         10.0,
         0.1,
         v -> this.lagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart
      )
   );
   private final SliderSetting smartDown = this.add(
      new SliderSetting(
         "SmartDown",
         3.0,
         0.0,
         10.0,
         0.1,
         v -> this.lagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart
      )
   );
   private final SliderSetting smartDistance = this.add(
      new SliderSetting(
         "SmartDistance",
         2.0,
         0.0,
         10.0,
         0.1,
         v -> this.lagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == XinBurrow_nDnthhFJjiqtqyXrSjjG.Smart
      )
   );
   private final List<BlockPos> placePos = new ArrayList();
   private int progress = 0;

   public XinBurrow() {
      super("XinBurrow", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (me.hextech.HexTech.PLAYER.isInWeb(mc.field_1724)) {
         this.webTimer.reset();
      } else if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
         if (this.webTimer.passed(this.webTime.getValue())) {
            if (this.disable.getValue() || this.timer.passed(this.delay.getValue())) {
               if (mc.field_1724.method_24828()) {
                  if (!this.antiLag.getValue() || mc.field_1687.method_8320(EntityUtil.getPlayerPos(true).method_10074()).method_51366()) {
                     if (!Blink.INSTANCE.isOn()) {
                        int oldSlot = mc.field_1724.method_31548().field_7545;
                        int block;
                        if ((block = this.getBlock()) == -1) {
                           this.disable();
                        } else {
                           this.progress = 0;
                           this.placePos.clear();
                           double offset = CombatSetting_kxXrLvbWbduSuFoeBUsC.getOffset();
                           BlockPos pos1 = new BlockPosX(
                              mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + offset
                           );
                           BlockPos pos2 = new BlockPosX(
                              mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + offset
                           );
                           BlockPos pos3 = new BlockPosX(
                              mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - offset
                           );
                           BlockPos pos4 = new BlockPosX(
                              mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - offset
                           );
                           BlockPos pos5 = new BlockPosX(
                              mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + 1.5, mc.field_1724.method_23321() + offset
                           );
                           BlockPos pos6 = new BlockPosX(
                              mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + 1.5, mc.field_1724.method_23321() + offset
                           );
                           BlockPos pos7 = new BlockPosX(
                              mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + 1.5, mc.field_1724.method_23321() - offset
                           );
                           BlockPos pos8 = new BlockPosX(
                              mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + 1.5, mc.field_1724.method_23321() - offset
                           );
                           BlockPos pos9 = new BlockPosX(
                              mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() - 1.0, mc.field_1724.method_23321() + offset
                           );
                           BlockPos pos10 = new BlockPosX(
                              mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() - 1.0, mc.field_1724.method_23321() + offset
                           );
                           BlockPos pos11 = new BlockPosX(
                              mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() - 1.0, mc.field_1724.method_23321() - offset
                           );
                           BlockPos pos12 = new BlockPosX(
                              mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() - 1.0, mc.field_1724.method_23321() - offset
                           );
                           BlockPos playerPos = EntityUtil.getPlayerPos(true);
                           boolean headFill = false;
                           if (!this.canPlace(pos1) && !this.canPlace(pos2) && !this.canPlace(pos3) && !this.canPlace(pos4)) {
                              boolean cantHeadFill = !this.headFill.getValue()
                                 || !this.canPlace(pos5) && !this.canPlace(pos6) && !this.canPlace(pos7) && !this.canPlace(pos8);
                              boolean cantDown = !this.down.getValue()
                                 || !this.canPlace(pos9) && !this.canPlace(pos10) && !this.canPlace(pos11) && !this.canPlace(pos12);
                              if (cantHeadFill) {
                                 if (cantDown) {
                                    if (!this.wait.getValue() && this.disable.getValue()) {
                                       this.disable();
                                    }

                                    return;
                                 }
                              } else {
                                 headFill = true;
                              }
                           }

                           boolean above = false;
                           BlockPos headPos = EntityUtil.getPlayerPos(true).method_10086(2);
                           boolean rotate = this.rotate.getValue() == XinBurrow_uPHLOgEUPRaLZqLkrbQU.Normal;
                           CombatUtil.attackCrystal(pos1, rotate, false);
                           CombatUtil.attackCrystal(pos2, rotate, false);
                           CombatUtil.attackCrystal(pos3, rotate, false);
                           CombatUtil.attackCrystal(pos4, rotate, false);
                           if (!headFill
                              && !mc.field_1724.method_20448()
                              && !this.trapped(headPos)
                              && !this.trapped(headPos.method_10069(1, 0, 0))
                              && !this.trapped(headPos.method_10069(-1, 0, 0))
                              && !this.trapped(headPos.method_10069(0, 0, 1))
                              && !this.trapped(headPos.method_10069(0, 0, -1))
                              && !this.trapped(headPos.method_10069(1, 0, -1))
                              && !this.trapped(headPos.method_10069(-1, 0, -1))
                              && !this.trapped(headPos.method_10069(1, 0, 1))
                              && !this.trapped(headPos.method_10069(-1, 0, 1))) {
                              mc.method_1562()
                                 .method_52787(
                                    new PositionAndOnGround(
                                       mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.4199999868869781, mc.field_1724.method_23321(), false
                                    )
                                 );
                              mc.method_1562()
                                 .method_52787(
                                    new PositionAndOnGround(
                                       mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.7531999805212017, mc.field_1724.method_23321(), false
                                    )
                                 );
                              mc.method_1562()
                                 .method_52787(
                                    new PositionAndOnGround(
                                       mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.9999957640154541, mc.field_1724.method_23321(), false
                                    )
                                 );
                              mc.method_1562()
                                 .method_52787(
                                    new PositionAndOnGround(
                                       mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.1661092609382138, mc.field_1724.method_23321(), false
                                    )
                                 );
                           } else {
                              above = true;
                              if (!this.fakeMove.getValue()) {
                                 if (!this.wait.getValue() && this.disable.getValue()) {
                                    this.disable();
                                 }

                                 return;
                              }

                              boolean moved = false;
                              if (!this.checkSelf(playerPos)
                                 || BlockUtil.canReplace(playerPos)
                                 || this.headFill.getValue() && BlockUtil.canReplace(playerPos.method_10084())) {
                                 for (Direction facing : Direction.values()) {
                                    if (facing != Direction.field_11036 && facing != Direction.field_11033) {
                                       BlockPos offPos = playerPos.method_10093(facing);
                                       if (this.checkSelf(offPos)
                                          && !BlockUtil.canReplace(offPos)
                                          && (!this.headFill.getValue() || !BlockUtil.canReplace(offPos.method_10084()))) {
                                          this.gotoPos(offPos);
                                          moved = true;
                                          break;
                                       }
                                    }
                                 }

                                 if (!moved) {
                                    for (Direction facingx : Direction.values()) {
                                       if (facingx != Direction.field_11036 && facingx != Direction.field_11033) {
                                          BlockPos var38 = playerPos.method_10093(facingx);
                                          if (this.checkSelf(var38)) {
                                             this.gotoPos(var38);
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
                                             BlockPos var39 = playerPos.method_10093(facingxx);
                                             if (this.canMove(var39)) {
                                                this.gotoPos(var39);
                                                moved = true;
                                                break;
                                             }
                                          }
                                       }

                                       if (!moved) {
                                          if (!this.wait.getValue() && this.disable.getValue()) {
                                             this.disable();
                                          }

                                          return;
                                       }
                                    }
                                 }
                              } else {
                                 this.gotoPos(playerPos);
                              }
                           }

                           this.timer.reset();
                           this.doSwap(block);
                           if (this.rotate.getValue() == XinBurrow_uPHLOgEUPRaLZqLkrbQU.Bypass) {
                              EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), 90.0F);
                           }

                           this.placeBlock(playerPos, rotate);
                           this.placeBlock(pos1, rotate);
                           this.placeBlock(pos2, rotate);
                           this.placeBlock(pos3, rotate);
                           this.placeBlock(pos4, rotate);
                           if (this.down.getValue()) {
                              this.placeBlock(pos9, rotate);
                              this.placeBlock(pos10, rotate);
                              this.placeBlock(pos11, rotate);
                              this.placeBlock(pos12, rotate);
                           }

                           if (this.headFill.getValue() && above) {
                              this.placeBlock(pos5, rotate);
                              this.placeBlock(pos6, rotate);
                              this.placeBlock(pos7, rotate);
                              this.placeBlock(pos8, rotate);
                           }

                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldSlot);
                           }

                           switch (above
                              ? (XinBurrow_nDnthhFJjiqtqyXrSjjG)this.aboveLagMode.getValue()
                              : (XinBurrow_nDnthhFJjiqtqyXrSjjG)this.lagMode.getValue()) {
                              case Smart:
                                 ArrayList<BlockPos> list = new ArrayList();

                                 for (double x = mc.field_1724.method_19538().method_10216() - this.smartX.getValue();
                                    x < mc.field_1724.method_19538().method_10216() + this.smartX.getValue();
                                    x++
                                 ) {
                                    for (double z = mc.field_1724.method_19538().method_10215() - this.smartX.getValue();
                                       z < mc.field_1724.method_19538().method_10215() + this.smartX.getValue();
                                       z++
                                    ) {
                                       for (double y = mc.field_1724.method_19538().method_10214() - this.smartDown.getValue();
                                          y < mc.field_1724.method_19538().method_10214() + this.smartUp.getValue();
                                          y++
                                       ) {
                                          list.add(new BlockPosX(x, y, z));
                                       }
                                    }
                                 }

                                 double distance = 0.0;
                                 BlockPos bestPos = null;

                                 for (BlockPos pos : list) {
                                    if (this.canMove(pos)
                                       && !(
                                          (double)MathHelper.method_15355((float)mc.field_1724.method_5707(pos.method_46558().method_1031(0.0, -0.5, 0.0)))
                                             < this.smartDistance.getValue()
                                       )
                                       && (bestPos == null || mc.field_1724.method_5707(pos.method_46558()) < distance)) {
                                       bestPos = pos;
                                       distance = mc.field_1724.method_5707(pos.method_46558());
                                    }
                                 }

                                 if (bestPos != null) {
                                    mc.method_1562()
                                       .method_52787(
                                          new PositionAndOnGround(
                                             (double)bestPos.method_10263() + 0.5, (double)bestPos.method_10264(), (double)bestPos.method_10260() + 0.5, false
                                          )
                                       );
                                 }
                                 break;
                              case Invalid:
                                 for (int i = 0; i < 20; i++) {
                                    mc.method_1562()
                                       .method_52787(
                                          new PositionAndOnGround(
                                             mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1337.0, mc.field_1724.method_23321(), false
                                          )
                                       );
                                 }
                                 break;
                              case TrollHack:
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.3400880035762786, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 break;
                              case ToVoid:
                                 mc.method_1562()
                                    .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), -70.0, mc.field_1724.method_23321(), false));
                                 break;
                              case ToVoid2:
                                 mc.method_1562()
                                    .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), -7.0, mc.field_1724.method_23321(), false));
                                 break;
                              case Normal:
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.9, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 break;
                              case Rotation:
                                 mc.method_1562().method_52787(new LookAndOnGround(-180.0F, -90.0F, false));
                                 mc.method_1562().method_52787(new LookAndOnGround(180.0F, 90.0F, false));
                                 break;
                              case Fly:
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.16610926093821, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.170005801788139, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.2426308013947485, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.3400880035762786, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.640088003576279, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 break;
                              case Glide:
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0001, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0405, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0802, mc.field_1724.method_23321(), false
                                       )
                                    );
                                 mc.method_1562()
                                    .method_52787(
                                       new PositionAndOnGround(
                                          mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.1027, mc.field_1724.method_23321(), false
                                       )
                                    );
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

   private void placeBlock(BlockPos pos, boolean rotate) {
      if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.blocksPer.getValueInt()) {
         this.placePos.add(pos);
         if (BlockUtil.airPlace()) {
            this.progress++;
            BlockUtil.placedPos.add(pos);
            if (this.sound.getValue()) {
               mc.field_1687.method_8396(mc.field_1724, pos, SoundEvents.field_14574, SoundCategory.field_15245, 1.0F, 0.8F);
            }

            BlockUtil.clickBlock(pos, Direction.field_11033, rotate, this.packetPlace.getValue());
         }

         Direction side;
         if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
         }

         this.progress++;
         BlockUtil.placedPos.add(pos);
         if (this.sound.getValue()) {
            mc.field_1687.method_8396(mc.field_1724, pos, SoundEvents.field_14574, SoundCategory.field_15245, 1.0F, 0.8F);
         }

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
      if (this.rotate.getValue() == XinBurrow_uPHLOgEUPRaLZqLkrbQU.None) {
         mc.method_1562()
            .method_52787(
               new PositionAndOnGround((double)offPos.method_10263() + 0.5, mc.field_1724.method_23318() + 0.1, (double)offPos.method_10260() + 0.5, false)
            );
      } else {
         mc.method_1562()
            .method_52787(
               new Full(
                  (double)offPos.method_10263() + 0.5,
                  mc.field_1724.method_23318() + 0.1,
                  (double)offPos.method_10260() + 0.5,
                  me.hextech.HexTech.ROTATE.rotateYaw,
                  90.0F,
                  false
               )
            );
      }
   }

   private boolean canMove(BlockPos pos) {
      return mc.field_1687.method_22347(pos) && mc.field_1687.method_22347(pos.method_10084());
   }

   private boolean canPlace(BlockPos pos) {
      if (this.noSelfPos.getValue() && pos.equals(EntityUtil.getPlayerPos(true))) {
         return false;
      } else if (!BlockUtil.airPlace() && BlockUtil.getPlaceSide(pos) == null) {
         return false;
      } else if (!BlockUtil.canReplace(pos)) {
         return false;
      } else {
         return this.detectMine.getValue() && me.hextech.HexTech.BREAK.isMining(pos) ? false : !this.hasEntity(pos);
      }
   }

   private boolean hasEntity(BlockPos pos) {
      for (Entity entity : BlockUtil.getEntities(new Box(pos))) {
         if (entity != mc.field_1724
            && entity.method_5805()
            && !(entity instanceof ItemEntity)
            && !(entity instanceof ExperienceOrbEntity)
            && !(entity instanceof ExperienceBottleEntity)
            && !(entity instanceof ArrowEntity)
            && (!(entity instanceof EndCrystalEntity) || !this.breakCrystal.getValue())) {
            return true;
         }
      }

      return false;
   }

   private boolean checkSelf(BlockPos pos) {
      return mc.field_1724.method_5829().method_994(new Box(pos));
   }

   private boolean trapped(BlockPos pos) {
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
