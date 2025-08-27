package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AutoTrap extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoTrap INSTANCE;
   public final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
   public final BooleanSetting render = this.add(new BooleanSetting("Render", true));
   public final BooleanSetting box = this.add(new BooleanSetting("Box", true, v -> this.render.getValue()));
   public final BooleanSetting outline = this.add(new BooleanSetting("Outline", false, v -> this.render.getValue()));
   public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100), v -> this.render.getValue()));
   public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 5000, v -> this.render.getValue()).setSuffix("ms"));
   public final BooleanSetting pre = this.add(new BooleanSetting("Pre", false, v -> this.render.getValue()));
   public final BooleanSetting sync = this.add(new BooleanSetting("Sync", true, v -> this.render.getValue()));
   final Timer timer = new Timer();
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
   private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 1.0, 8.0).setSuffix("m"));
   private final EnumSetting<AutoTrap_YlnJzIMwjFLWxhoVZoJp> targetMod = this.add(new EnumSetting("TargetMode", AutoTrap_YlnJzIMwjFLWxhoVZoJp.Single));
   private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", false));
   private final BooleanSetting helper = this.add(new BooleanSetting("Helper", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting extend = this.add(new BooleanSetting("Extend", true));
   private final BooleanSetting antiStep = this.add(new BooleanSetting("AntiStep", false));
   private final BooleanSetting onlyBreak = this.add(new BooleanSetting("OnlyBreak", false, v -> this.antiStep.getValue()));
   private final BooleanSetting head = this.add(new BooleanSetting("Head", true));
   private final BooleanSetting headExtend = this.add(new BooleanSetting("HeadExtend", true));
   private final BooleanSetting headAnchor = this.add(new BooleanSetting("HeadAnchor", true));
   private final BooleanSetting chestUp = this.add(new BooleanSetting("ChestUp", true));
   private final BooleanSetting onlyBreaking = this.add(new BooleanSetting("OnlyBreaking", false, v -> this.chestUp.getValue()));
   private final BooleanSetting chest = this.add(new BooleanSetting("Chest", true));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, v -> this.chest.getValue()));
   private final BooleanSetting legs = this.add(new BooleanSetting("Legs", false));
   private final BooleanSetting legAnchor = this.add(new BooleanSetting("LegAnchor", true));
   private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
   private final BooleanSetting onlyHole = this.add(new BooleanSetting("OnlyHole", false));
   private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.0, 1.0, 6.0).setSuffix("m"));
   private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false));
   private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
   private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true));
   private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0));
   private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100));
   private final ArrayList<BlockPos> trapList = new ArrayList();
   private final ArrayList<BlockPos> placeList = new ArrayList();
   public PlayerEntity target;
   public Vec3d directionVec = null;
   int progress = 0;

   public AutoTrap() {
      super("AutoTrap", "Automatically trap the enemy", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
      me.hextech.HexTech.EVENT_BUS.subscribe(new AutoTrap_WyicUWrboAjyTEvkkOOH(this));
   }

   @EventHandler
   public void onRotate(OffTrackEvent event) {
      if (this.directionVec != null && this.rotate.getValue() && this.yawStep.getValue()) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         this.trapList.clear();
         this.directionVec = null;
         this.placeList.clear();
         this.progress = 0;
         if (this.selfGround.getValue() && !mc.field_1724.method_24828()) {
            this.target = null;
         } else if (this.usingPause.getValue() && EntityUtil.isUsing()) {
            this.target = null;
         } else if (this.timer.passedMs((long)this.delay.getValue())) {
            if (this.targetMod.getValue() == AutoTrap_YlnJzIMwjFLWxhoVZoJp.Single) {
               this.target = CombatUtil.getClosestEnemy(this.range.getValue());
               if (this.target == null) {
                  if (this.autoDisable.getValue()) {
                     this.disable();
                  }

                  return;
               }

               this.trapTarget(this.target);
            } else if (this.targetMod.getValue() == AutoTrap_YlnJzIMwjFLWxhoVZoJp.Multi) {
               boolean found = false;

               for (PlayerEntity player : CombatUtil.getEnemies(this.range.getValue())) {
                  found = true;
                  this.target = player;
                  this.trapTarget(this.target);
               }

               if (!found) {
                  if (this.autoDisable.getValue()) {
                     this.disable();
                  }

                  this.target = null;
               }
            }
         }
      }
   }

   private void trapTarget(PlayerEntity target) {
      if (!this.onlyHole.getValue() || BlockUtil.isHole(EntityUtil.getEntityPos(target))) {
         this.doTrap(EntityUtil.getEntityPos(target, true));
      }
   }

   private void doTrap(BlockPos pos) {
      if (!this.trapList.contains(pos)) {
         this.trapList.add(pos);
         if (this.legs.getValue()) {
            for (Direction i : Direction.values()) {
               if (i != Direction.field_11033 && i != Direction.field_11036) {
                  BlockPos offsetPos = pos.method_10093(i);
                  this.tryPlaceBlock(offsetPos, this.legAnchor.getValue());
                  if (BlockUtil.getPlaceSide(offsetPos) == null
                     && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())
                     && this.getHelper(offsetPos) != null) {
                     this.tryPlaceObsidian(this.getHelper(offsetPos));
                  }
               }
            }
         }

         if (this.headExtend.getValue()) {
            for (int x : new int[]{1, 0, -1}) {
               for (int z : new int[]{1, 0, -1}) {
                  BlockPos offsetPos = pos.method_10069(z, 0, x);
                  if (this.checkEntity(new BlockPos(offsetPos))) {
                     this.tryPlaceBlock(offsetPos.method_10086(2), this.headAnchor.getValue());
                  }
               }
            }
         }

         if (this.head.getValue() && BlockUtil.clientCanPlace(pos.method_10086(2), this.breakCrystal.getValue())) {
            if (BlockUtil.getPlaceSide(pos.method_10086(2)) == null) {
               boolean trapChest = this.helper.getValue();
               if (this.getHelper(pos.method_10086(2)) != null) {
                  this.tryPlaceObsidian(this.getHelper(pos.method_10086(2)));
                  trapChest = false;
               }

               if (trapChest) {
                  for (Direction ix : Direction.values()) {
                     if (ix != Direction.field_11033 && ix != Direction.field_11036) {
                        BlockPos offsetPos = pos.method_10093(ix).method_10084();
                        if (BlockUtil.clientCanPlace(offsetPos.method_10084(), this.breakCrystal.getValue())
                           && BlockUtil.canPlace(offsetPos, this.placeRange.getValue(), this.breakCrystal.getValue())) {
                           this.tryPlaceObsidian(offsetPos);
                           trapChest = false;
                           break;
                        }
                     }
                  }

                  if (trapChest) {
                     for (Direction ixx : Direction.values()) {
                        if (ixx != Direction.field_11033 && ixx != Direction.field_11036) {
                           BlockPos offsetPos = pos.method_10093(ixx).method_10084();
                           if (BlockUtil.clientCanPlace(offsetPos.method_10084(), this.breakCrystal.getValue())
                              && BlockUtil.getPlaceSide(offsetPos) == null
                              && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())
                              && this.getHelper(offsetPos) != null) {
                              this.tryPlaceObsidian(this.getHelper(offsetPos));
                              trapChest = false;
                              break;
                           }
                        }
                     }

                     if (trapChest) {
                        for (Direction ixxx : Direction.values()) {
                           if (ixxx != Direction.field_11033 && ixxx != Direction.field_11036) {
                              BlockPos offsetPos = pos.method_10093(ixxx).method_10084();
                              if (BlockUtil.clientCanPlace(offsetPos.method_10084(), this.breakCrystal.getValue())
                                 && BlockUtil.getPlaceSide(offsetPos) == null
                                 && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())
                                 && this.getHelper(offsetPos) != null
                                 && BlockUtil.getPlaceSide(offsetPos.method_10074()) == null
                                 && BlockUtil.clientCanPlace(offsetPos.method_10074(), this.breakCrystal.getValue())
                                 && this.getHelper(offsetPos.method_10074()) != null) {
                                 this.tryPlaceObsidian(this.getHelper(offsetPos.method_10074()));
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.tryPlaceBlock(pos.method_10086(2), this.headAnchor.getValue());
         }

         if (this.antiStep.getValue() && (BlockUtil.isMining(pos.method_10086(2)) || !this.onlyBreak.getValue())) {
            if (BlockUtil.getPlaceSide(pos.method_10086(3)) == null
               && BlockUtil.clientCanPlace(pos.method_10086(3), this.breakCrystal.getValue())
               && this.getHelper(pos.method_10086(3), Direction.field_11033) != null) {
               this.tryPlaceObsidian(this.getHelper(pos.method_10086(3)));
            }

            this.tryPlaceObsidian(pos.method_10086(3));
         }

         if (this.down.getValue()) {
            BlockPos offsetPos = pos.method_10074();
            this.tryPlaceObsidian(offsetPos);
            if (BlockUtil.getPlaceSide(offsetPos) == null
               && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())
               && this.getHelper(offsetPos) != null) {
               this.tryPlaceObsidian(this.getHelper(offsetPos));
            }
         }

         if (this.chestUp.getValue()) {
            for (Direction ixxxx : Direction.values()) {
               if (ixxxx != Direction.field_11033 && ixxxx != Direction.field_11036) {
                  BlockPos offsetPos = pos.method_10093(ixxxx).method_10086(2);
                  if (!this.onlyBreaking.getValue() || BlockUtil.isMining(pos.method_10086(2))) {
                     this.tryPlaceObsidian(offsetPos);
                     if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())) {
                        if (this.getHelper(offsetPos) != null) {
                           this.tryPlaceObsidian(this.getHelper(offsetPos));
                        } else if (BlockUtil.getPlaceSide(offsetPos.method_10074()) == null
                           && BlockUtil.clientCanPlace(offsetPos.method_10074(), this.breakCrystal.getValue())
                           && this.getHelper(offsetPos.method_10074()) != null) {
                           this.tryPlaceObsidian(this.getHelper(offsetPos.method_10074()));
                        }
                     }
                  }
               }
            }
         }

         if (this.chest.getValue() && (!this.onlyGround.getValue() || this.target.method_24828())) {
            for (Direction ixxxxx : Direction.values()) {
               if (ixxxxx != Direction.field_11033 && ixxxxx != Direction.field_11036) {
                  BlockPos offsetPos = pos.method_10093(ixxxxx).method_10084();
                  this.tryPlaceObsidian(offsetPos);
                  if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())) {
                     if (this.getHelper(offsetPos) != null) {
                        this.tryPlaceObsidian(this.getHelper(offsetPos));
                     } else if (BlockUtil.getPlaceSide(offsetPos.method_10074()) == null
                        && BlockUtil.clientCanPlace(offsetPos.method_10074(), this.breakCrystal.getValue())
                        && this.getHelper(offsetPos.method_10074()) != null) {
                        this.tryPlaceObsidian(this.getHelper(offsetPos.method_10074()));
                     }
                  }
               }
            }
         }

         if (this.extend.getValue()) {
            for (int x : new int[]{1, 0, -1}) {
               for (int zx : new int[]{1, 0, -1}) {
                  BlockPos offsetPos = pos.method_10069(x, 0, zx);
                  if (this.checkEntity(new BlockPos(offsetPos))) {
                     this.doTrap(offsetPos);
                  }
               }
            }
         }
      }
   }

   @Override
   public String getInfo() {
      return this.target != null ? this.target.method_5477().getString() : null;
   }

   public BlockPos getHelper(BlockPos pos) {
      if (!this.helper.getValue()) {
         return null;
      } else {
         for (Direction i : Direction.values()) {
            if ((!this.checkMine.getValue() || !BlockUtil.isMining(pos.method_10093(i)))
               && (
                  CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() != Placement.Strict
                     || BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153(), true)
               )
               && BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue(), this.breakCrystal.getValue())) {
               return pos.method_10093(i);
            }
         }

         return null;
      }
   }

   public BlockPos getHelper(BlockPos pos, Direction ignore) {
      if (!this.helper.getValue()) {
         return null;
      } else {
         for (Direction i : Direction.values()) {
            if (i != ignore
               && (!this.checkMine.getValue() || !BlockUtil.isMining(pos.method_10093(i)))
               && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153(), true)
               && BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue(), this.breakCrystal.getValue())) {
               return pos.method_10093(i);
            }
         }

         return null;
      }
   }

   private boolean checkEntity(BlockPos pos) {
      if (mc.field_1724.method_5829().method_994(new Box(pos))) {
         return false;
      } else {
         for (Entity entity : mc.field_1687.method_18467(PlayerEntity.class, new Box(pos))) {
            if (entity.method_5805()) {
               return true;
            }
         }

         return false;
      }
   }

   private void tryPlaceBlock(BlockPos pos, boolean anchor) {
      if (this.pre.getValue()) {
         AutoTrap_WyicUWrboAjyTEvkkOOH.addBlock(pos);
      }

      if (!this.placeList.contains(pos)) {
         if (!BlockUtil.isMining(pos)) {
            if (BlockUtil.canPlace(pos, 6.0, this.breakCrystal.getValue())) {
               if (!this.rotate.getValue() || this.faceVector(this.directionVec)) {
                  if ((double)this.progress < this.blocksPer.getValue()) {
                     if (!((double)MathHelper.method_15355((float)EntityUtil.getEyesPos().method_1025(pos.method_46558())) > this.placeRange.getValue())) {
                        int old = mc.field_1724.method_31548().field_7545;
                        int block = anchor && this.getAnchor() != -1 ? this.getAnchor() : this.getBlock();
                        if (block != -1) {
                           if (!this.pre.getValue()) {
                              AutoTrap_WyicUWrboAjyTEvkkOOH.addBlock(pos);
                           }

                           this.placeList.add(pos);
                           CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPause.getValue());
                           this.doSwap(block);
                           BlockUtil.placeBlock(pos, this.rotate.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(old);
                           }

                           this.timer.reset();
                           this.progress++;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void tryPlaceObsidian(BlockPos pos) {
      if (this.pre.getValue()) {
         AutoTrap_WyicUWrboAjyTEvkkOOH.addBlock(pos);
      }

      if (!this.placeList.contains(pos)) {
         if (!BlockUtil.isMining(pos)) {
            if (BlockUtil.canPlace(pos, 6.0, this.breakCrystal.getValue())) {
               if (!this.rotate.getValue() || this.faceVector(this.directionVec)) {
                  if ((double)this.progress < this.blocksPer.getValue()) {
                     if (!((double)MathHelper.method_15355((float)EntityUtil.getEyesPos().method_1025(pos.method_46558())) > this.placeRange.getValue())) {
                        int old = mc.field_1724.method_31548().field_7545;
                        int block = this.getBlock();
                        if (block != -1) {
                           if (!this.pre.getValue()) {
                              AutoTrap_WyicUWrboAjyTEvkkOOH.addBlock(pos);
                           }

                           this.placeList.add(pos);
                           CombatUtil.attackCrystal(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), this.usingPause.getValue());
                           this.doSwap(block);
                           BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(old);
                           }

                           this.timer.reset();
                           this.progress++;
                        }
                     }
                  }
               }
            }
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

   private boolean faceVector(Vec3d directionVec) {
      if (!this.yawStep.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         return me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
      }
   }

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_10540) : InventoryUtil.findBlock(Blocks.field_10540);
   }

   private int getAnchor() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.field_23152) : InventoryUtil.findBlock(Blocks.field_23152);
   }
}
