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
        if (t instanceof BlockBreakingProgressS2CPacket packet) {
            if (packet.getPos() == null) {
                return;
            }
            MineManager breakData = new MineManager(packet.getPos(), packet.getEntityId());
            if (this.breakMap.containsKey(packet.getEntityId()) && this.breakMap.get(Integer.valueOf(packet.getEntityId())).pos.equals(packet.getPos())) {
                return;
            }
            if (breakData.getEntity() == null) {
                return;
            }
            if (MathHelper.sqrt((float)breakData.getEntity().getEyePos().squaredDistanceTo(packet.getPos().toCenterPos())) > 8.0f) {
                return;
            }
            this.breakMap.put(packet.getEntityId(), breakData);
        }
    }

    public boolean isMining(BlockPos pos) {
        for (MineManager breakData : this.breakMap.values()) {
            if (breakData.getEntity() == null || breakData.getEntity().getEyePos().distanceTo(pos.toCenterPos()) > 7.0 || !breakData.pos.equals(pos)) continue;
            return true;
        }
        return false;
    }
}
