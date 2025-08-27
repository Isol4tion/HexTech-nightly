package me.hextech.remapped;

import java.util.concurrent.ConcurrentHashMap;
import me.hextech.HexTech;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.MineManager;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.Wrapper;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MineManager_aMxFbgVZCMGgbqNPBFpw
implements Wrapper {
    public final ConcurrentHashMap<Integer, MineManager> breakMap = new ConcurrentHashMap();

    public MineManager_aMxFbgVZCMGgbqNPBFpw() {
        HexTech.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    public void onPacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        Object t = event.getPacket();
        if (t instanceof BlockBreakingProgressS2CPacket) {
            BlockBreakingProgressS2CPacket packet = (BlockBreakingProgressS2CPacket)t;
            if (packet.method_11277() == null) {
                return;
            }
            MineManager breakData = new MineManager(packet.method_11277(), packet.method_11280());
            if (this.breakMap.containsKey(packet.method_11280()) && this.breakMap.get((Object)Integer.valueOf((int)packet.method_11280())).pos.equals((Object)packet.method_11277())) {
                return;
            }
            if (breakData.getEntity() == null) {
                return;
            }
            if (MathHelper.method_15355((float)((float)breakData.getEntity().getEyePos().squaredDistanceTo(packet.method_11277().toCenterPos()))) > 8.0f) {
                return;
            }
            this.breakMap.put(packet.method_11280(), breakData);
        }
    }

    public boolean isMining(BlockPos pos) {
        for (MineManager breakData : this.breakMap.values()) {
            if (breakData.getEntity() == null || breakData.getEntity().getEyePos().method_1022(pos.toCenterPos()) > 7.0 || !breakData.pos.equals((Object)pos)) continue;
            return true;
        }
        return false;
    }
}
