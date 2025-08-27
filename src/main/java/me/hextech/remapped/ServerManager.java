package me.hextech.remapped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import me.hextech.HexTech;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.HUD_ssNtBhEveKlCmIccBvAN;
import me.hextech.remapped.JelloUtil;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.Timer;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class ServerManager
implements Wrapper {
    private final Timer timeDelay = new Timer();
    private final ArrayDeque<Float> tpsResult = new ArrayDeque(20);
    boolean worldNull = true;
    private long time;
    private long tickTime;
    private float tps;

    public ServerManager() {
        HexTech.EVENT_BUS.subscribe(this);
    }

    public static float round2(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public float getTPS() {
        return ServerManager.round2(this.tps);
    }

    public float getCurrentTPS() {
        return ServerManager.round2(20.0f * ((float)this.tickTime / 1000.0f));
    }

    public float getTPSFactor() {
        return (float)this.tickTime / 1000.0f;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            if (this.time != 0L) {
                this.tickTime = System.currentTimeMillis() - this.time;
                if (this.tpsResult.size() > 20) {
                    this.tpsResult.poll();
                }
                this.tpsResult.add(Float.valueOf(20.0f * (1000.0f / (float)this.tickTime)));
                float average = 0.0f;
                for (Float value : this.tpsResult) {
                    average += MathUtil.clamp(value.floatValue(), 0.0f, 20.0f);
                }
                this.tps = average / (float)this.tpsResult.size();
            }
            this.time = System.currentTimeMillis();
        }
    }

    public long serverRespondingTime() {
        return this.timeDelay.getPassedTimeMs();
    }

    public boolean isServerNotResponding() {
        return this.timeDelay.passedMs(HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.lagTime.getValue());
    }

    public int getPing() {
        PlayerListEntry playerListEntry = mc.method_1562().method_2871(ServerManager.mc.player.method_5667());
        int ping = playerListEntry == null ? 0 : playerListEntry.method_2959();
        return ping;
    }

    public void onUpdate() {
        JelloUtil.updateJello();
        if (this.worldNull && ServerManager.mc.world != null) {
            HexTech.MODULE.onLogin();
            this.worldNull = false;
        } else if (!this.worldNull && ServerManager.mc.world == null) {
            HexTech.save();
            HexTech.MODULE.onLogout();
            this.worldNull = true;
        }
    }

    public void run() {
        JelloUtil.updateJello();
        if (this.worldNull && ServerManager.mc.world != null) {
            HexTech.MODULE.onLogin();
            this.worldNull = false;
        } else if (!this.worldNull && ServerManager.mc.world == null) {
            HexTech.MODULE.onLogout();
            this.worldNull = true;
        }
    }
}
