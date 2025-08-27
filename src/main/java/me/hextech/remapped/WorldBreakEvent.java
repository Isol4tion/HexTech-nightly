package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.entity.player.BlockBreakingInfo;
import net.minecraft.util.math.BlockPos;

public class WorldBreakEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final BlockBreakingInfo blockBreakingInfo;

    public WorldBreakEvent(BlockBreakingInfo pos) {
        super(Event.Pre);
        this.blockBreakingInfo = pos;
    }

    public BlockPos getPos() {
        return this.blockBreakingInfo.getPos();
    }

    public int getId() {
        return this.blockBreakingInfo.getActorId();
    }
}
