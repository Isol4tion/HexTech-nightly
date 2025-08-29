package me.hextech.api.managers;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.DeathEvent;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.events.impl.TotemEvent;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.ArrayList;
import java.util.HashMap;

public class PopManager
        implements Wrapper {
    public final HashMap<String, Integer> popContainer = new HashMap();
    public final ArrayList<PlayerEntity> deadPlayer = new ArrayList();

    public PopManager() {
        HexTech.EVENT_BUS.subscribe(this);
    }

    public Integer getPop(String s) {
        return this.popContainer.getOrDefault(s, 0);
    }

    public void update() {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        for (PlayerEntity player : PopManager.mc.world.getPlayers()) {
            if (player == null || player.isAlive()) {
                this.deadPlayer.remove(player);
                continue;
            }
            if (this.deadPlayer.contains(player)) continue;
            HexTech.EVENT_BUS.post(new DeathEvent(player));
            this.onDeath(player);
            this.deadPlayer.add(player);
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        Entity entity;
        EntityStatusS2CPacket packet;
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        Object t = event.getPacket();
        if (t instanceof EntityStatusS2CPacket && (packet = (EntityStatusS2CPacket) t).getStatus() == 35 && (entity = packet.getEntity(PopManager.mc.world)) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            this.onTotemPop(player);
        }
    }

    public void onDeath(PlayerEntity player) {
        this.popContainer.remove(player.getName().getString());
    }

    public void onTotemPop(PlayerEntity player) {
        int l_Count = 1;
        if (this.popContainer.containsKey(player.getName().getString())) {
            l_Count = this.popContainer.get(player.getName().getString());
            this.popContainer.put(player.getName().getString(), ++l_Count);
        } else {
            this.popContainer.put(player.getName().getString(), l_Count);
        }
        HexTech.EVENT_BUS.post(new TotemEvent(player));
    }
}
