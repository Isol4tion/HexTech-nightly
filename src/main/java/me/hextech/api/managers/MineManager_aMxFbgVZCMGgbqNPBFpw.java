package me.hextech.api.managers;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.mod.modules.impl.render.BreakESP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.concurrent.ConcurrentHashMap;

public class MineManager_aMxFbgVZCMGgbqNPBFpw
implements Wrapper {
    public final ConcurrentHashMap<Integer, MineInfo> breakMap = new ConcurrentHashMap();

    public MineManager_aMxFbgVZCMGgbqNPBFpw() {
        HexTech.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    public void onPacket(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        Object t = event.getPacket();
        if (t instanceof BlockBreakingProgressS2CPacket packet) {
            if (packet.getPos() == null) {
                return;
            }
            MineInfo breakData = new MineInfo(packet.getPos(), packet.getEntityId());
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
        for (MineInfo breakData : this.breakMap.values()) {
            if (breakData.getEntity() == null || breakData.getEntity().getEyePos().distanceTo(pos.toCenterPos()) > 7.0 || !breakData.pos.equals(pos)) continue;
            return true;
        }
        return false;
    }

    public static class MineInfo {
        public final BlockPos pos;
        public final int entityId;
        public final FadeUtils_DPfHthPqEJdfXfNYhDbG fade;
        public final Timer timer;

        public MineInfo(BlockPos pos, int entityId) {
            this.pos = pos;
            this.entityId = entityId;
            this.fade = new FadeUtils_DPfHthPqEJdfXfNYhDbG((long) BreakESP.INSTANCE.animationTime.getValue());
            this.timer = new Timer();
        }

        public Entity getEntity() {
            if (mc.world == null) {
                return null;
            }
            Entity entity = mc.world.getEntityById(this.entityId);
            return entity instanceof PlayerEntity ? entity : null;
        }
    }
}
