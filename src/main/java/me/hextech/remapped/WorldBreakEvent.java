package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.render.BlockBreakingInfo;
import net.minecraft.util.math.BlockPos;

public class WorldBreakEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final BlockBreakingInfo blockBreakingInfo;

    public WorldBreakEvent(BlockBreakingInfo pos) {
        super(Event.Pre);
        this.blockBreakingInfo = pos;
    }

    public BlockPos getPos() {
        return this.blockBreakingInfo.method_13991();
    }

    public int getId() {
        return this.blockBreakingInfo.method_34868();
    }
}
