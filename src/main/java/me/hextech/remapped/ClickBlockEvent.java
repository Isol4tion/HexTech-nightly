package me.hextech.remapped;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ClickBlockEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private final BlockPos pos;
   private final Direction direction;

   public ClickBlockEvent(BlockPos pos, Direction direction) {
      super(Event.Pre);
      this.pos = pos;
      this.direction = direction;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public BlockPos getPos() {
      return this.pos;
   }
}
