package me.hextech.remapped;


import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.render.BlockBreakingInfo;
import net.minecraft.util.math.BlockPos;

public class WorldBreakEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final BlockBreakingInfo blockBreakingInfo;

    public WorldBreakEvent(BlockBreakingInfo pos) {
        super(Stage.Pre);
        this.blockBreakingInfo = pos;
    }

    public BlockPos getPos() {
        return this.blockBreakingInfo.getPos();
    }

    public int getId() {
        return this.blockBreakingInfo.getActorId();
    }
}
