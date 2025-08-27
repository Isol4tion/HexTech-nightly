package me.hextech.remapped;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HolePush extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HolePush INSTANCE;
   private final EnumSetting<HolePush_yzHmKDmPzgLjnTXAWdrq> page = this.add(new EnumSetting("Page", HolePush_yzHmKDmPzgLjnTXAWdrq.General));
   private final BooleanSetting torch = this.add(new BooleanSetting("Torch", false, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General));
   private final BooleanSetting rotate = this.add(new BooleanSetting("ROTATE", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Rotate));
   private final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Rotate));
   private final BooleanSetting allowWeb = this.add(new BooleanSetting("AllowWeb", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check));
   public final BindSetting allowKey = this.add(
      new BindSetting("AllowKey", -1, v -> !this.allowWeb.getValue() && this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check)
   );
   private final BooleanSetting noEating = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check));
   private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check));
   private final BooleanSetting onlyNoInside = this.add(
      new BooleanSetting("OnlyNoInside", false, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check)
   );
   private final BooleanSetting onlyInside = this.add(new BooleanSetting("OnlyInside", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check));
   private final SliderSetting surroundCheck = this.add(
      new SliderSetting("SurroundCheck", 0, 0, 4, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check)
   );
   private final BooleanSetting syncCrystal = this.add(
      new BooleanSetting("SyncCrystal", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.Check)
   );
   private final BooleanSetting pistonPacket = this.add(
      new BooleanSetting("PistonPacket", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final BooleanSetting powerPacket = this.add(
      new BooleanSetting("PowerPacket", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General));
   private final BooleanSetting pauseCity = this.add(
      new BooleanSetting("PauseCity", true, v -> this.mine.isOpen() && this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final SliderSetting updateDelay = this.add(
      new SliderSetting("Delay", 200, 0, 1000, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final BooleanSetting autoDisable = this.add(
      new BooleanSetting("AutoDisable", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final SliderSetting range = this.add(new SliderSetting("Range", 4.7, 0.0, 6.0, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General));
   private final SliderSetting placeRange = this.add(
      new SliderSetting("PlaceRange", 4.7, 0.0, 6.0, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final BooleanSetting inventory = this.add(
      new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == HolePush_yzHmKDmPzgLjnTXAWdrq.General)
   );
   private final Timer timer = new Timer();
   Vec3d directionVec = null;

   public HolePush() {
      super("HolePush", Module_JlagirAibYQgkHtbRnhw.Combat);
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

   @Override
   public void onEnable() {
      AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastBreakTimer.reset();
   }

   private boolean faceVector(Vec3d directionVec) {
      EntityUtil.faceVector(directionVec);
      return true;
   }

   boolean isTargetHere(BlockPos pos, Entity target) {
      return new Box(pos).method_994(target.method_5829());
   }

   @Override
   public void onUpdate() {
      if (this.timer.passedMs(this.updateDelay.getValue())) {
         if (!this.selfGround.getValue() || mc.field_1724.method_24828()) {
            if (this.findBlock(this.getBlockType()) != -1 && this.findClass(PistonBlock.class) != -1) {
               if (AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null || !this.syncCrystal.getValue()) {
                  if (!this.noEating.getValue() || !mc.field_1724.method_6115()) {
                     if (!Blink.INSTANCE.isOn()) {
                        for (PlayerEntity player : CombatUtil.getEnemies(this.range.getValue())) {
                           if (this.canPush(player)) {
                              for (Direction i : Direction.values()) {
                                 if (i != Direction.field_11036 && i != Direction.field_11033) {
                                    BlockPos pos = EntityUtil.getEntityPos(player).method_10093(i);
                                    if (this.isTargetHere(pos, player) && mc.field_1687.method_39454(player, new Box(pos))) {
                                       if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(i.method_10153()), i)) {
                                          this.timer.reset();
                                          return;
                                       }

                                       if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(i.method_10153()).method_10084(), i)) {
                                          this.timer.reset();
                                          return;
                                       }
                                    }
                                 }
                              }

                              float[] offset = new float[]{-0.25F, 0.0F, 0.25F};

                              for (float x : offset) {
                                 for (float z : offset) {
                                    BlockPosX playerPos = new BlockPosX(
                                       player.method_23317() + (double)x, player.method_23318() + 0.5, player.method_23321() + (double)z
                                    );

                                    for (Direction ix : Direction.values()) {
                                       if (ix != Direction.field_11036 && ix != Direction.field_11033) {
                                          BlockPos pos = playerPos.method_10093(ix);
                                          if (this.isTargetHere(pos, player) && mc.field_1687.method_39454(player, new Box(pos))) {
                                             if (this.tryPush(playerPos.method_10093(ix.method_10153()), ix)) {
                                                this.timer.reset();
                                             }

                                             if (this.tryPush(playerPos.method_10093(ix.method_10153()).method_10084(), ix)) {
                                                this.timer.reset();
                                                return;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }

                              if (!mc.field_1687
                                 .method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 2.5, player.method_23321())))) {
                                 for (Direction ixx : Direction.values()) {
                                    if (ixx != Direction.field_11036 && ixx != Direction.field_11033) {
                                       BlockPos pos = EntityUtil.getEntityPos(player).method_10093(ixx);
                                       Box box = player.method_5829()
                                          .method_997(new Vec3d((double)ixx.method_10148(), (double)ixx.method_10164(), (double)ixx.method_10165()));
                                       if (this.getBlock(pos.method_10084()) != Blocks.field_10379
                                          && !mc.field_1687.method_39454(player, box.method_989(0.0, 1.0, 0.0))
                                          && !this.isTargetHere(pos, player)) {
                                          if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(ixx.method_10153()).method_10084(), ixx)) {
                                             this.timer.reset();
                                             return;
                                          }

                                          if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(ixx.method_10153()), ixx)) {
                                             this.timer.reset();
                                             return;
                                          }
                                       }
                                    }
                                 }
                              }

                              for (float x : offset) {
                                 for (float z : offset) {
                                    BlockPosX playerPos = new BlockPosX(
                                       player.method_23317() + (double)x, player.method_23318() + 0.5, player.method_23321() + (double)z
                                    );

                                    for (Direction ixxx : Direction.values()) {
                                       if (ixxx != Direction.field_11036 && ixxx != Direction.field_11033) {
                                          BlockPos pos = playerPos.method_10093(ixxx);
                                          if (this.isTargetHere(pos, player)) {
                                             if (this.tryPush(playerPos.method_10093(ixxx.method_10153()).method_10084(), ixxx)) {
                                                this.timer.reset();
                                                return;
                                             }

                                             if (this.tryPush(playerPos.method_10093(ixxx.method_10153()), ixxx)) {
                                                this.timer.reset();
                                                return;
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
            } else {
               if (this.autoDisable.getValue()) {
                  this.disable();
               }
            }
         }
      }
   }

   private boolean tryPush(BlockPos piston, Direction direction) {
      if (!mc.field_1687.method_22347(piston.method_10093(direction))) {
         return false;
      } else {
         if (this.isTrueFacing(piston, direction) && this.facingCheck(piston) && BlockUtil.clientCanPlace(piston)) {
            boolean canPower = false;
            if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
               CombatUtil.modifyPos = piston;
               CombatUtil.modifyBlockState = Blocks.field_10560.method_9564();

               for (Direction i : Direction.values()) {
                  if (this.getBlock(piston.method_10093(i)) == this.getBlockType()) {
                     canPower = true;
                     break;
                  }
               }

               for (Direction ix : Direction.values()) {
                  if (canPower) {
                     break;
                  }

                  if (BlockUtil.canPlace(piston.method_10093(ix), this.placeRange.getValue())) {
                     canPower = true;
                  }
               }

               CombatUtil.modifyPos = null;
               if (canPower) {
                  int pistonSlot = this.findClass(PistonBlock.class);
                  Direction side = BlockUtil.getPlaceSide(piston);
                  if (side != null) {
                     if (this.rotate.getValue()) {
                        Vec3d directionVec = new Vec3d(
                           (double)piston.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
                           (double)piston.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
                           (double)piston.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
                        );
                        if (!this.faceVector(directionVec)) {
                           return true;
                        }
                     }

                     if (this.yawDeceive.getValue()) {
                        pistonFacing(direction.method_10153());
                     }

                     int old = mc.field_1724.method_31548().field_7545;
                     this.doSwap(pistonSlot);
                     BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(pistonSlot);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }

                     if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                        EntityUtil.facePosSide(piston.method_10093(side), side.method_10153());
                     }

                     for (Direction ix : Direction.values()) {
                        if (this.getBlock(piston.method_10093(ix)) == this.getBlockType()) {
                           if (this.mine.getValue() && !this.pauseCity.getValue() || this.mine.getValue() && SpeedMine.breakPos == SpeedMine.secondPos) {
                              SpeedMine.INSTANCE.mine(piston.method_10093(ix));
                           }

                           if (this.autoDisable.getValue()) {
                              this.disable();
                           }

                           return true;
                        }
                     }

                     for (Direction ixx : Direction.values()) {
                        if ((ixx != Direction.field_11036 || !this.torch.getValue())
                           && BlockUtil.canPlace(piston.method_10093(ixx), this.placeRange.getValue())) {
                           int oldSlot = mc.field_1724.method_31548().field_7545;
                           int powerSlot = this.findBlock(this.getBlockType());
                           this.doSwap(powerSlot);
                           BlockUtil.placeBlock(piston.method_10093(ixx), this.rotate.getValue(), this.powerPacket.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(powerSlot);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldSlot);
                           }

                           if (this.mine.getValue() && !this.pauseCity.getValue() || this.mine.getValue() && SpeedMine.breakPos == SpeedMine.secondPos) {
                              SpeedMine.INSTANCE.mine(piston.method_10093(ixx));
                           }

                           return true;
                        }
                     }

                     return true;
                  }
               }
            } else {
               Direction powerFacing = null;

               for (Direction ixxx : Direction.values()) {
                  if (ixxx != Direction.field_11036 || !this.torch.getValue()) {
                     if (powerFacing != null) {
                        break;
                     }

                     CombatUtil.modifyPos = piston.method_10093(ixxx);
                     CombatUtil.modifyBlockState = this.getBlockType().method_9564();
                     if (BlockUtil.getPlaceSide(piston) != null) {
                        powerFacing = ixxx;
                     }

                     CombatUtil.modifyPos = null;
                     if (powerFacing != null && !BlockUtil.canPlace(piston.method_10093(powerFacing))) {
                        powerFacing = null;
                     }
                  }
               }

               if (powerFacing != null) {
                  int oldSlotx = mc.field_1724.method_31548().field_7545;
                  int powerSlotx = this.findBlock(this.getBlockType());
                  this.doSwap(powerSlotx);
                  BlockUtil.placeBlock(piston.method_10093(powerFacing), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlotx);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlotx);
                  }

                  CombatUtil.modifyPos = piston.method_10093(powerFacing);
                  CombatUtil.modifyBlockState = this.getBlockType().method_9564();
                  int pistonSlot = this.findClass(PistonBlock.class);
                  Direction side = BlockUtil.getPlaceSide(piston);
                  if (side != null) {
                     if (this.rotate.getValue()) {
                        Vec3d directionVec = new Vec3d(
                           (double)piston.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
                           (double)piston.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
                           (double)piston.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
                        );
                        if (!this.faceVector(directionVec)) {
                           return true;
                        }
                     }

                     if (this.yawDeceive.getValue()) {
                        pistonFacing(direction.method_10153());
                     }

                     int oldx = mc.field_1724.method_31548().field_7545;
                     this.doSwap(pistonSlot);
                     BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(pistonSlot);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(oldx);
                     }

                     if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                        EntityUtil.facePosSide(piston.method_10093(side), side.method_10153());
                     }
                  }

                  CombatUtil.modifyPos = null;
                  return true;
               }
            }
         }

         BlockState state = mc.field_1687.method_8320(piston);
         if (state.method_26204() instanceof PistonBlock && this.getBlockState(piston).method_11654(FacingBlock.field_10927) == direction) {
            for (Direction ixxxx : Direction.values()) {
               if (this.getBlock(piston.method_10093(ixxxx)) == this.getBlockType()) {
                  if (this.autoDisable.getValue()) {
                     this.disable();
                     return true;
                  }

                  return false;
               }
            }

            for (Direction ixxxxx : Direction.values()) {
               if ((ixxxxx != Direction.field_11036 || !this.torch.getValue()) && BlockUtil.canPlace(piston.method_10093(ixxxxx), this.placeRange.getValue())) {
                  int oldSlotxx = mc.field_1724.method_31548().field_7545;
                  int powerSlotxx = this.findBlock(this.getBlockType());
                  this.doSwap(powerSlotxx);
                  BlockUtil.placeBlock(piston.method_10093(ixxxxx), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlotxx);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlotxx);
                  }

                  return true;
               }
            }
         }

         this.directionVec = null;
         return false;
      }
   }

   @Override
   public String getInfo() {
      if (AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null && this.syncCrystal.getValue()) {
         return "WaitSync";
      } else if (this.allowWeb.getValue()) {
         return "CrazyDog";
      } else {
         return this.allowKey.isPressed() ? "CrazyDog" : "SmartPush";
      }
   }

   private boolean facingCheck(BlockPos pos) {
      return true;
   }

   private boolean isTrueFacing(BlockPos pos, Direction facing) {
      if (this.yawDeceive.getValue()) {
         return true;
      } else {
         Direction side = BlockUtil.getPlaceSide(pos);
         if (side == null) {
            return false;
         } else {
            Vec3d directionVec = new Vec3d(
               (double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5,
               (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5,
               (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5
            );
            float[] ROTATE = me.hextech.HexTech.ROTATE.getRotation(directionVec);
            return MathUtil.getFacingOrder(ROTATE[0], ROTATE[1]).method_10153() == facing;
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
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

   private boolean burrowUpdate(PlayerEntity player) {
      for (float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for (float z : new float[]{0.0F, 0.3F, -0.3F}) {
            BlockPos pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 1.5, player.method_23321() + (double)z);
            if (new Box(pos).method_994(player.method_5829())
               && (
                  mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10540
                     || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_23152
                     || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_9987
                     || mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10443
               )) {
               return true;
            }
         }
      }

      return false;
   }

   private Boolean canPush(PlayerEntity player) {
      if (this.onlyGround.getValue() && !player.method_24828()) {
         return false;
      } else if (!this.allowWeb.getValue() && me.hextech.HexTech.PLAYER.isInWeb(player) && !this.allowKey.isPressed()) {
         return false;
      } else if (this.onlyNoInside.getValue() && this.burrowUpdate(player)) {
         return false;
      } else if (this.onlyInside.getValue() && !this.burrowUpdate(player)) {
         return false;
      } else {
         float[] offset = new float[]{-0.25F, 0.0F, 0.25F};
         int progress = 0;
         if (mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317() + 1.0, player.method_23318() + 0.5, player.method_23321())))) {
            progress++;
         }

         if (mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317() - 1.0, player.method_23318() + 0.5, player.method_23321())))) {
            progress++;
         }

         if (mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321() + 1.0)))) {
            progress++;
         }

         if (mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321() - 1.0)))) {
            progress++;
         }

         for (float x : offset) {
            for (float z : offset) {
               BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 0.5, player.method_23321() + (double)z);

               for (Direction i : Direction.values()) {
                  if (i != Direction.field_11036 && i != Direction.field_11033) {
                     BlockPos pos = playerPos.method_10093(i);
                     if (this.isTargetHere(pos, player)) {
                        if (mc.field_1687.method_39454(player, new Box(pos))) {
                           return true;
                        }

                        if ((double)progress > this.surroundCheck.getValue() - 1.0) {
                           return true;
                        }
                     }
                  }
               }
            }
         }

         if (!mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 2.5, player.method_23321())))) {
            for (Direction ix : Direction.values()) {
               if (ix != Direction.field_11036 && ix != Direction.field_11033) {
                  BlockPos pos = EntityUtil.getEntityPos(player).method_10093(ix);
                  Box box = player.method_5829().method_997(new Vec3d((double)ix.method_10148(), (double)ix.method_10164(), (double)ix.method_10165()));
                  if (this.getBlock(pos.method_10084()) != Blocks.field_10379
                     && !mc.field_1687.method_39454(player, box.method_989(0.0, 1.0, 0.0))
                     && !this.isTargetHere(pos, player)
                     && mc.field_1687.method_39454(player, new Box(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321())))) {
                     return true;
                  }
               }
            }
         }

         return (double)progress > this.surroundCheck.getValue() - 1.0
            || CombatUtil.isHard(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321()));
      }
   }

   private Block getBlock(BlockPos pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }

   private Block getBlockType() {
      return this.torch.getValue() ? Blocks.field_10523 : Blocks.field_10002;
   }

   private BlockState getBlockState(BlockPos pos) {
      return mc.field_1687.method_8320(pos);
   }
}
