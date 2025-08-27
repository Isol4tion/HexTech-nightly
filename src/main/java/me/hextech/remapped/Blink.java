package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.UUID;
import me.hextech.remapped.AutoEXP;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.Surround_BjIoVRziuWIfEWTJHPVz;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

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
        super("Blink", "Fake lag", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.packetsList.clear();
        if (Blink.nullCheck()) {
            this.disable();
            return;
        }
        fakePlayer = new OtherClientPlayerEntity(Blink.mc.world, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666601"), Blink.mc.player.method_5477().getString()));
        fakePlayer.method_5719((Entity)Blink.mc.player);
        fakePlayer.method_31548().method_7377(Blink.mc.player.method_31548());
        Blink.mc.world.method_53875((Entity)fakePlayer);
        if (this.exp.getValue()) {
            AutoEXP.INSTANCE.enable();
        }
    }

    @Override
    public void onUpdate() {
        if (Blink.mc.player.method_29504()) {
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
    public void onSendPacket(PacketEvent event) {
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
            this.packetsList.add((Packet)event.getPacket());
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
            fakePlayer.method_5768();
            fakePlayer.method_31745(Entity.RemovalReason.field_26998);
            fakePlayer.method_36209();
            fakePlayer = null;
        }
        for (Packet packet : this.packetsList) {
            Blink.mc.player.field_3944.method_52787(packet);
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
