package me.hextech.remapped.mod.modules.impl.combat;

import io.netty.buffer.Unpooled;
import me.hextech.remapped.*;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.mod.modules.settings.impl.EnumSetting;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Criticals INSTANCE;
    public final EnumSetting<_llXqHCnomcmaIkSSIBHS> mode = this.add(new EnumSetting<_llXqHCnomcmaIkSSIBHS>("Mode", _llXqHCnomcmaIkSSIBHS.Packet));

    public Criticals() {
        super("Criticals", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static Entity getEntity(PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        return Criticals.mc.world == null ? null : Criticals.mc.world.getEntityById(packetBuf.readVarInt());
    }

    public static _QenzavIULhSqCVPmsILH getInteractType(PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        packetBuf.readVarInt();
        return packetBuf.readEnumConstant(_QenzavIULhSqCVPmsILH.class);
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventHandler
    public void onPacketSend(PacketEvent event) {
        Entity entity;
        PlayerInteractEntityC2SPacket packet;
        Object t;
        if (!Aura.INSTANCE.sweeping && !TPAura_LycLkxHLQeGfgqfryvmV.attacking && (t = event.getPacket()) instanceof PlayerInteractEntityC2SPacket && Criticals.getInteractType(packet = (PlayerInteractEntityC2SPacket)t) == _QenzavIULhSqCVPmsILH.ATTACK && !((entity = Criticals.getEntity(packet)) instanceof EndCrystalEntity)) {
            Criticals.mc.player.addCritParticles(entity);
            this.doCrit();
        }
    }

    public void doCrit() {
        if (Aura.INSTANCE.isOn() && (Criticals.mc.player.isOnGround() || Criticals.mc.player.getAbilities().flying) && !Criticals.mc.player.isInLava() && !Criticals.mc.player.isSubmergedInWater()) {
            if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.Strict && Criticals.mc.world.getBlockState(Criticals.mc.player.getBlockPos()).getBlock() != Blocks.COBWEB) {
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 0.062600301692775, Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 0.07260029960661, Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY(), Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY(), Criticals.mc.player.getZ(), false));
            } else if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.NCP) {
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 0.0625, Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY(), Criticals.mc.player.getZ(), false));
            } else if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.Packet) {
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 1.058293536E-5, Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 9.16580235E-6, Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 1.0371854E-7, Criticals.mc.player.getZ(), false));
            } else if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.LowPacket) {
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY() + 2.71875E-7, Criticals.mc.player.getZ(), false));
                Criticals.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.getX(), Criticals.mc.player.getY(), Criticals.mc.player.getZ(), false));
            }
        }
    }

    public enum _llXqHCnomcmaIkSSIBHS {
        NCP,
        Strict,
        Packet,
        LowPacket

    }
    public enum _QenzavIULhSqCVPmsILH {
        INTERACT,
        ATTACK,
        INTERACT_AT

    }
}
