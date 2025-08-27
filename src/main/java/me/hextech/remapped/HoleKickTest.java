package me.hextech.remapped;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HoleKickTest extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HoleKickTest INSTANCE;
   public static boolean dopush;
   public final EnumSetting<Enum_EeQOXZQmWkBIGBYWBifQ> page = this.add(new EnumSetting("Page", Enum_EeQOXZQmWkBIGBYWBifQ.General));
   public final BooleanSetting syncCrystal = this.add(new BooleanSetting("SyncCrystal", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.Sync));
   public final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   public final BooleanSetting allowWeb = this.add(new BooleanSetting("AllowWeb", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   public final SliderSetting updateDelay = this.add(
      new SliderSetting("UpdateDelay", 100, 0, 500, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   public final SliderSetting surroundCheck = this.add(
      new SliderSetting("SurroundCheck", 2, 0, 4, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final Timer timer = new Timer();
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting preferAnchor = this.add(
      new BooleanSetting("PreferAnchor", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final BooleanSetting waitBurrow = this.add(new BooleanSetting("WaitBurrow", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting cancelBurrow = this.add(
      new BooleanSetting("CancelBurrow", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final BooleanSetting cancelBlink = this.add(new BooleanSetting("CancelBlink", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting nomove = this.add(new BooleanSetting("MovePause", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.Sync));
   private final BooleanSetting syncweb = this.add(new BooleanSetting("SyncWeb[!Test]", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.Sync));
   private final BooleanSetting pistonPacket = this.add(
      new BooleanSetting("PistonPacket", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final BooleanSetting redStonePacket = this.add(
      new BooleanSetting("RedStonePacket", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final BooleanSetting noEating = this.add(new BooleanSetting("NoEating", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting attackCrystal = this.add(
      new BooleanSetting("BreakCrystal", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final BooleanSetting mine = this.add(new BooleanSetting("Mine", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting checkPiston = this.add(new BooleanSetting("CheckPiston", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final BooleanSetting pullBack = this.add(
      new BooleanSetting("PullBack", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General).setParent()
   );
   private final BooleanSetting onlyBurrow = this.add(new BooleanSetting("OnlyBurrow", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   private final SliderSetting placeRange = this.add(
      new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General)
   );
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
   public PlayerEntity displayTarget = null;
   public Vec3d directionVec = null;
   private Enum_EeQOXZQmWkBIGBYWBifQ Page;

   public HoleKickTest() {
      super("HoleKickTest", Module_JlagirAibYQgkHtbRnhw.Combat);
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

   public static boolean isTargetHere(BlockPos pos, Entity target) {
      return new Box(pos).method_994(target.method_5829());
   }

   public static boolean isInWeb(PlayerEntity player) {
      Vec3d playerPos = player.method_19538();

      for (float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for (float z : new float[]{0.0F, 0.3F, -0.3F}) {
            for (float y : new float[]{0.0F, 1.0F, -1.0F}) {
               BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214() + (double)y, playerPos.method_10215() + (double)z);
               if (isTargetHere(pos, player) && mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10343) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public void onEnable() {
      AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastBreakTimer.reset();
   }

   public boolean check(boolean onlyStatic) {
      return MovementUtil.isMoving() && onlyStatic;
   }

   @Override
   public void onUpdate() {
      if (this.timer.passedMs(this.updateDelay.getValue())) {
         if (mc.field_1724 != null && this.selfGround.getValue() && !mc.field_1724.method_24828()) {
            if (this.autoDisable.getValue()) {
               this.disable();
            }
         } else if (mc.field_1724 == null || !this.check(this.nomove.getValue())) {
            if (!this.preferAnchor.getValue() || AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos == null) {
               if (!this.waitBurrow.getValue() || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.placePos != null) {
                  if (!this.cancelBurrow.getValue() || !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                     if (!this.cancelBlink.getValue() || !Blink.INSTANCE.isOn()) {
                        if (!this.syncCrystal.getValue() || AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null) {
                           if (!this.syncweb.getValue() || WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.pos != null) {
                              if (this.findBlock(Blocks.field_10002) != -1 && this.findClass(PistonBlock.class) != -1) {
                                 if (!this.noEating.getValue() || !mc.field_1724.method_6115()) {
                                    this.timer.reset();

                                    for (PlayerEntity target : CombatUtil.getEnemies(this.range.getValue())) {
                                       if (this.canPush(target) && (target.method_24828() || !this.onlyGround.getValue())) {
                                          this.displayTarget = target;
                                          if (this.doPush(EntityUtil.getEntityPos(target), target)) {
                                             return;
                                          }
                                       }
                                    }

                                    if (this.autoDisable.getValue()) {
                                       this.disable();
                                    }

                                    this.displayTarget = null;
                                 }
                              } else {
                                 if (this.autoDisable.getValue()) {
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

   private boolean checkPiston(BlockPos targetPos) {
      for (Direction i : Direction.values()) {
         if (i != Direction.field_11033 && i != Direction.field_11036) {
            BlockPos pos = targetPos.method_10084();
            if (this.getBlock(pos.method_10093(i)) instanceof PistonBlock
               && ((Direction)this.getBlockState(pos.method_10093(i)).method_11654(FacingBlock.field_10927)).method_10153() == i) {
               for (Direction i2 : Direction.values()) {
                  if (this.getBlock(pos.method_10093(i).method_10093(i2)) == Blocks.field_10002 && this.mine.getValue()) {
                     this.mine(pos.method_10093(i).method_10093(i2));
                     if (this.autoDisable.getValue()) {
                        this.disable();
                     }

                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public boolean doPush(BlockPos targetPos, PlayerEntity target) {
      if (this.checkPiston.getValue() && this.checkPiston(targetPos)) {
         return true;
      } else {
         if (!mc.field_1687.method_8320(targetPos.method_10086(2)).method_51366()) {
            for (Direction i : Direction.values()) {
               if (i != Direction.field_11033 && i != Direction.field_11036) {
                  BlockPos pos = targetPos.method_10093(i).method_10084();
                  if (this.getBlock(pos) instanceof PistonBlock
                     && !this.getBlockState(pos.method_10079(i, -2)).method_51366()
                     && (
                        this.getBlock(pos.method_10079(i, -2).method_10084()) == Blocks.field_10124
                           || this.getBlock(pos.method_10079(i, -2).method_10084()) == Blocks.field_10002
                     )
                     && ((Direction)this.getBlockState(pos).method_11654(FacingBlock.field_10927)).method_10153() == i) {
                     for (Direction i2 : Direction.values()) {
                        if (this.getBlock(pos.method_10093(i2)) == Blocks.field_10002) {
                           if (this.mine.getValue()) {
                              this.mine(pos.method_10093(i2));
                           }

                           if (this.autoDisable.getValue()) {
                              this.disable();
                           }

                           return true;
                        }
                     }
                  }
               }
            }

            for (Direction ix : Direction.values()) {
               if (ix != Direction.field_11033 && ix != Direction.field_11036) {
                  BlockPos pos = targetPos.method_10093(ix).method_10084();
                  if (this.getBlock(pos) instanceof PistonBlock
                     && !this.getBlockState(pos.method_10079(ix, -2)).method_51366()
                     && (
                        this.getBlock(pos.method_10079(ix, -2).method_10084()) == Blocks.field_10124
                           || this.getBlock(pos.method_10079(ix, -2).method_10084()) == Blocks.field_10002
                     )
                     && ((Direction)this.getBlockState(pos).method_11654(FacingBlock.field_10927)).method_10153() == ix
                     && this.doPower(pos)) {
                     return true;
                  }
               }
            }

            for (Direction ixx : Direction.values()) {
               if (ixx != Direction.field_11033 && ixx != Direction.field_11036) {
                  BlockPos pos = targetPos.method_10093(ixx).method_10084();
                  if (!(mc.field_1724.method_23318() - target.method_23318() <= -1.0) && !(mc.field_1724.method_23318() - target.method_23318() >= 2.0)
                     || !(BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6)) {
                     this.attackCrystal(pos);
                     if (this.isTrueFacing(pos, ixx)
                        && BlockUtil.clientCanPlace(pos, false)
                        && !this.getBlockState(pos.method_10079(ixx, -2)).method_51366()
                        && !this.getBlockState(pos.method_10079(ixx, -2).method_10084()).method_51366()) {
                        if (BlockUtil.getPlaceSide(pos) != null || !this.downPower(pos)) {
                           this.doPiston(ixx, pos, this.mine.getValue());
                           return true;
                        }
                        break;
                     }
                  }
               }
            }

            if ((this.getBlock(targetPos) != Blocks.field_10124 || !this.onlyBurrow.getValue()) && this.pullBack.getValue()) {
               for (Direction ixxx : Direction.values()) {
                  if (ixxx != Direction.field_11033 && ixxx != Direction.field_11036) {
                     BlockPos pos = targetPos.method_10093(ixxx).method_10084();

                     for (Direction i2x : Direction.values()) {
                        if (this.getBlock(pos) instanceof PistonBlock
                           && this.getBlock(pos.method_10093(i2x)) == Blocks.field_10002
                           && ((Direction)this.getBlockState(pos).method_11654(FacingBlock.field_10927)).method_10153() == ixxx) {
                           this.mine(pos.method_10093(i2x));
                           if (this.autoDisable.getValue()) {
                              this.disable();
                           }

                           return true;
                        }
                     }
                  }
               }

               for (Direction ixxxx : Direction.values()) {
                  if (ixxxx != Direction.field_11033 && ixxxx != Direction.field_11036) {
                     BlockPos pos = targetPos.method_10093(ixxxx).method_10084();

                     for (Direction i2xx : Direction.values()) {
                        if (this.getBlock(pos) instanceof PistonBlock
                           && this.getBlock(pos.method_10093(i2xx)) == Blocks.field_10124
                           && ((Direction)this.getBlockState(pos).method_11654(FacingBlock.field_10927)).method_10153() == ixxxx) {
                           this.attackCrystal(pos.method_10093(i2xx));
                           if (!this.doPower(pos, i2xx)) {
                              this.mine(pos.method_10093(i2xx));
                              return true;
                           }
                        }
                     }
                  }
               }

               for (Direction ixxxxx : Direction.values()) {
                  if (ixxxxx != Direction.field_11033 && ixxxxx != Direction.field_11036) {
                     BlockPos pos = targetPos.method_10093(ixxxxx).method_10084();
                     if (mc.field_1724 == null
                        || !(mc.field_1724.method_23318() - target.method_23318() <= -1.0) && !(mc.field_1724.method_23318() - target.method_23318() >= 2.0)
                        || !(BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6)) {
                        this.attackCrystal(pos);
                        if (this.isTrueFacing(pos, ixxxxx) && BlockUtil.clientCanPlace(pos, false) && !this.downPower(pos)) {
                           this.doPiston(ixxxxx, pos, true);
                           return true;
                        }
                     }
                  }
               }

               return false;
            }

            if (this.autoDisable.getValue()) {
               this.disable();
            }

            return true;
         } else {
            for (Direction ixxxxxx : Direction.values()) {
               if (ixxxxxx != Direction.field_11033 && ixxxxxx != Direction.field_11036) {
                  BlockPos pos = targetPos.method_10093(ixxxxxx).method_10084();
                  if (this.getBlock(pos) instanceof PistonBlock
                     && (
                        mc.field_1687.method_22347(pos.method_10079(ixxxxxx, -2)) && mc.field_1687.method_22347(pos.method_10079(ixxxxxx, -2).method_10074())
                           || isTargetHere(pos.method_10079(ixxxxxx, 2), target)
                     )
                     && ((Direction)this.getBlockState(pos).method_11654(FacingBlock.field_10927)).method_10153() == ixxxxxx) {
                     for (Direction i2xxx : Direction.values()) {
                        if (this.getBlock(pos.method_10093(i2xxx)) == Blocks.field_10002) {
                           if (this.mine.getValue()) {
                              this.mine(pos.method_10093(i2xxx));
                           }

                           if (this.autoDisable.getValue()) {
                              this.disable();
                           }

                           return true;
                        }
                     }
                  }
               }
            }

            for (Direction ixxxxxxx : Direction.values()) {
               if (ixxxxxxx != Direction.field_11033 && ixxxxxxx != Direction.field_11036) {
                  BlockPos pos = targetPos.method_10093(ixxxxxxx).method_10084();
                  if (this.getBlock(pos) instanceof PistonBlock
                     && (
                        mc.field_1687.method_22347(pos.method_10079(ixxxxxxx, -2)) && mc.field_1687.method_22347(pos.method_10079(ixxxxxxx, -2).method_10074())
                           || isTargetHere(pos.method_10079(ixxxxxxx, 2), target)
                     )
                     && ((Direction)this.getBlockState(pos).method_11654(FacingBlock.field_10927)).method_10153() == ixxxxxxx
                     && this.doPower(pos)) {
                     return true;
                  }
               }
            }

            for (Direction ixxxxxxxx : Direction.values()) {
               if (ixxxxxxxx != Direction.field_11033 && ixxxxxxxx != Direction.field_11036) {
                  BlockPos pos = targetPos.method_10093(ixxxxxxxx).method_10084();
                  if (mc.field_1724 == null
                     || !(mc.field_1724.method_23318() - target.method_23318() <= -1.0) && !(mc.field_1724.method_23318() - target.method_23318() >= 2.0)
                     || !(BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6)) {
                     this.attackCrystal(pos);
                     if (this.isTrueFacing(pos, ixxxxxxxx)
                        && BlockUtil.clientCanPlace(pos, false)
                        && (
                           mc.field_1687.method_22347(pos.method_10079(ixxxxxxxx, -2))
                                 && mc.field_1687.method_22347(pos.method_10079(ixxxxxxxx, -2).method_10074())
                              || isTargetHere(pos.method_10079(ixxxxxxxx, 2), target)
                        )
                        && !this.getBlockState(pos.method_10079(ixxxxxxxx, -2).method_10084()).method_51366()) {
                        if (BlockUtil.getPlaceSide(pos) != null || !this.downPower(pos)) {
                           dopush = true;
                           this.doPiston(ixxxxxxxx, pos, this.mine.getValue());
                           return true;
                        }
                        break;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   private boolean isTrueFacing(BlockPos pos, Direction facing) {
      if (this.yawDeceive.getValue()) {
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

   private boolean doPower(BlockPos pos, Direction i2) {
      if (!BlockUtil.canPlace(pos.method_10093(i2), this.placeRange.getValue())) {
         return true;
      } else {
         int old = 0;
         if (mc.field_1724 != null) {
            old = mc.field_1724.method_31548().field_7545;
         }

         int power = this.findBlock(Blocks.field_10002);
         this.doSwap(power);
         BlockUtil.placeBlock(pos.method_10093(i2), this.rotate.getValue(), this.redStonePacket.getValue());
         if (this.inventory.getValue()) {
            this.doSwap(power);
            EntityUtil.syncInventory();
         } else {
            this.doSwap(old);
         }

         return false;
      }
   }

   private boolean doPower(BlockPos pos) {
      Direction facing = BlockUtil.getBestNeighboring(pos, null);
      if (facing != null) {
         this.attackCrystal(pos.method_10093(facing));
         if (!this.doPower(pos, facing)) {
            return true;
         }
      }

      for (Direction i2 : Direction.values()) {
         this.attackCrystal(pos.method_10093(i2));
         if (!this.doPower(pos, i2)) {
            return true;
         }
      }

      return false;
   }

   private boolean downPower(BlockPos pos) {
      if (BlockUtil.getPlaceSide(pos) == null) {
         boolean noPower = true;

         for (Direction i2 : Direction.values()) {
            if (this.getBlock(pos.method_10093(i2)) == Blocks.field_10002) {
               noPower = false;
               break;
            }
         }

         if (noPower) {
            if (!BlockUtil.canPlace(pos.method_10069(0, -1, 0), this.placeRange.getValue())) {
               return true;
            }

            int old = 0;
            if (mc.field_1724 != null) {
               old = mc.field_1724.method_31548().field_7545;
            }

            int power = this.findBlock(Blocks.field_10002);
            this.doSwap(power);
            BlockUtil.placeBlock(pos.method_10069(0, -1, 0), this.rotate.getValue(), this.redStonePacket.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(power);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }
         }
      }

      return false;
   }

   private void doPiston(Direction i, BlockPos pos, boolean mine) {
      if (BlockUtil.canPlace(pos, this.placeRange.getValue())) {
         int piston = this.findClass(PistonBlock.class);
         Direction side = BlockUtil.getPlaceSide(pos);
         if (this.rotate.getValue()) {
            EntityUtil.facePosSide(pos.method_10093(side), side.method_10153());
         }

         if (this.yawDeceive.getValue()) {
            pistonFacing(i);
         }

         int old = 0;
         if (mc.field_1724 != null) {
            old = mc.field_1724.method_31548().field_7545;
         }

         this.doSwap(piston);
         BlockUtil.placeBlock(pos, false, this.pistonPacket.getValue());
         if (this.inventory.getValue()) {
            this.doSwap(piston);
            EntityUtil.syncInventory();
         } else {
            this.doSwap(old);
         }

         if (this.rotate.getValue()) {
            EntityUtil.facePosSide(pos.method_10093(side), side.method_10153());
         }

         for (Direction i2 : Direction.values()) {
            if (this.getBlock(pos.method_10093(i2)) == Blocks.field_10002) {
               if (mine) {
                  this.mine(pos.method_10093(i2));
               }

               if (this.autoDisable.getValue()) {
                  this.disable();
               }

               return;
            }
         }

         this.doPower(pos);
      }
   }

   @Override
   public String getInfo() {
      return this.displayTarget != null ? this.displayTarget.method_5477().getString() : null;
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

   public int findBlock(Block blockIn) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
   }

   public int findClass(Class clazz) {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(clazz) : InventoryUtil.findClass(clazz);
   }

   private void attackCrystal(BlockPos pos) {
      if (this.attackCrystal.getValue()) {
         if (mc.field_1687 != null) {
            for (Entity crystal : mc.field_1687.method_18112()) {
               if (crystal instanceof EndCrystalEntity
                  && !(
                     (double)MathHelper.method_15355(
                           (float)crystal.method_5649((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5)
                        )
                        > 2.0
                  )) {
                  CombatUtil.attackCrystal(crystal, this.rotate.getValue(), false);
                  return;
               }
            }
         }
      }
   }

   private void mine(BlockPos pos) {
      SpeedMine.INSTANCE.mine(pos);
   }

   private Block getBlock(BlockPos pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }

   private BlockState getBlockState(BlockPos pos) {
      return mc.field_1687 != null ? mc.field_1687.method_8320(pos) : null;
   }

   private Boolean canPush(PlayerEntity player) {
      if (this.onlyGround.getValue() && !player.method_24828()) {
         return false;
      } else if (!this.allowWeb.getValue() && isInWeb(player)) {
         return false;
      } else {
         int progress = 0;
         if (!mc.field_1687.method_22347(new BlockPosX(player.method_23317() + 1.0, player.method_23318() + 0.5, player.method_23321()))) {
            progress++;
         }

         if (!mc.field_1687.method_22347(new BlockPosX(player.method_23317() - 1.0, player.method_23318() + 0.5, player.method_23321()))) {
            progress++;
         }

         if (!mc.field_1687.method_22347(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321() + 1.0))) {
            progress++;
         }

         if (!mc.field_1687.method_22347(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321() - 1.0))) {
            progress++;
         }

         if (!mc.field_1687.method_22347(new BlockPosX(player.method_23317(), player.method_23318() + 2.5, player.method_23321()))) {
            for (Direction i : Direction.values()) {
               if (i != Direction.field_11036 && i != Direction.field_11033) {
                  BlockPos pos = EntityUtil.getEntityPos(player).method_10093(i);
                  if (mc.field_1687.method_22347(pos) && mc.field_1687.method_22347(pos.method_10084()) || isTargetHere(pos, player)) {
                     return !mc.field_1687.method_22347(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321()))
                        ? true
                        : (double)progress > this.surroundCheck.getValue() - 1.0;
                  }
               }
            }

            return false;
         } else {
            if (!mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 2.5, player.method_23321())))) {
               for (Direction ix : Direction.values()) {
                  if (ix != Direction.field_11036 && ix != Direction.field_11033) {
                     BlockPos pos = EntityUtil.getEntityPos(player).method_10093(ix);
                     Box box = player.method_5829().method_997(new Vec3d((double)ix.method_10148(), (double)ix.method_10164(), (double)ix.method_10165()));
                     if (this.getBlock(pos.method_10084()) != Blocks.field_10379
                        && !mc.field_1687.method_39454(player, box.method_989(0.0, 1.0, 0.0))
                        && !isTargetHere(pos, player)
                        && mc.field_1687
                           .method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321())))) {
                        return true;
                     }
                  }
               }
            }

            return (double)progress > this.surroundCheck.getValue() - 1.0
               || CombatUtil.isHard(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321()));
         }
      }
   }

   public void placeBlock(BlockPos pos, boolean rotate, boolean bypass) {
      if (BlockUtil.airPlace()) {
         for (Direction i : Direction.values()) {
            if (mc.field_1687 != null && mc.field_1687.method_22347(pos.method_10093(i))) {
               BlockUtil.clickBlock(pos, i, rotate);
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
         new BlockHitResult(directionVec, side, pos, false);
         BlockUtil.placedPos.add(pos);
         boolean sprint = false;
         if (mc.field_1724 != null) {
            sprint = mc.field_1724.method_5624();
         }

         boolean sneak = false;
         if (sprint) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12985));
         }

         if (sneak) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12979));
         }

         BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), rotate);
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
}
