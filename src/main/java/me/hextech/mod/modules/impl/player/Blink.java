package me.hextech.mod.modules.impl.player;

import com.mojang.authlib.GameProfile;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.AutoEXP;
import me.hextech.mod.modules.impl.combat.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.mod.modules.impl.combat.Surround_BjIoVRziuWIfEWTJHPVz;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.*;

import java.util.ArrayList;
import java.util.UUID;

public class Blink
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Blink INSTANCE;
    public static OtherClientPlayerEntity fakePlayer;
    private final ArrayList<Packet> packetsList = new ArrayList();
    private final BooleanSetting allPacket = this.add(new BooleanSetting("AllPacket", true));
    public BooleanSetting bur = this.add(new BooleanSetting("DoneBurrow", false));
    public BooleanSetting exp = this.add(new BooleanSetting("Exp", false));
    public BooleanSetting sur = this.add(new BooleanSetting("DoneSurround", false));

    public Blink() {
        super("Blink", "Fake lag", Category.Player);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.packetsList.clear();
        if (Blink.nullCheck()) {
            this.disable();
            return;
        }
        fakePlayer = new OtherClientPlayerEntity(Blink.mc.world, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666601"), Blink.mc.player.getName().getString()));
        fakePlayer.copyPositionAndRotation(Blink.mc.player);
        fakePlayer.getInventory().clone(Blink.mc.player.getInventory());
        Blink.mc.world.addEntity(fakePlayer);
        if (this.exp.getValue()) {
            AutoEXP.INSTANCE.enable();
        }
    }

    @Override
    public void onUpdate() {
        if (Blink.mc.player.isDead()) {
            this.packetsList.clear();
            this.disable();
        }
    }

    @Override
    public void onLogin() {
        if (this.isOn()) {
            this.packetsList.clear();
            this.disable();
        }
    }

    @EventHandler
    public void onSendPacket(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        Object t = event.getPacket();
        if (t instanceof ChatMessageC2SPacket) {
            return;
        }
        if (t instanceof RequestCommandCompletionsC2SPacket) {
            return;
        }
        if (t instanceof CommandExecutionC2SPacket) {
            return;
        }
        if (t instanceof TeleportConfirmC2SPacket) {
            return;
        }
        if (t instanceof KeepAliveC2SPacket) {
            return;
        }
        if (t instanceof AdvancementTabC2SPacket) {
            return;
        }
        if (t instanceof ClientStatusC2SPacket) {
            return;
        }
        if (t instanceof ClickSlotC2SPacket) {
            return;
        }
        if (t instanceof PlayerMoveC2SPacket || this.allPacket.getValue()) {
            this.packetsList.add(event.getPacket());
            event.cancel();
        }
    }

    @Override
    public void onDisable() {
        if (Blink.nullCheck()) {
            this.packetsList.clear();
            this.disable();
            return;
        }
        if (fakePlayer != null) {
            fakePlayer.kill();
            fakePlayer.setRemoved(Entity.RemovalReason.KILLED);
            fakePlayer.onRemoved();
            fakePlayer = null;
        }
        for (Packet packet : this.packetsList) {
            Blink.mc.player.networkHandler.sendPacket(packet);
        }
        if (this.bur.getValue() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
        }
        if (this.sur.getValue() && !Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn()) {
            Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.enable();
        }
    }

    @Override
    public String getInfo() {
        return String.valueOf(this.packetsList.size());
    }
}
