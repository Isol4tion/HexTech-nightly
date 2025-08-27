package me.hextech.remapped;

import io.netty.buffer.Unpooled;
import me.hextech.remapped.Aura;
import me.hextech.remapped.Criticals_QenzavIULhSqCVPmsILH;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.TPAura_LycLkxHLQeGfgqfryvmV;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
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
        packet.method_11052(packetBuf);
        return Criticals.mc.world == null ? null : Criticals.mc.world.method_8469(packetBuf.readVarInt());
    }

    public static Criticals_QenzavIULhSqCVPmsILH getInteractType(PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.method_11052(packetBuf);
        packetBuf.readVarInt();
        return (Criticals_QenzavIULhSqCVPmsILH)packetBuf.readEnumConstant(Criticals_QenzavIULhSqCVPmsILH.class);
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
        if (!Aura.INSTANCE.sweeping && !TPAura_LycLkxHLQeGfgqfryvmV.attacking && (t = event.getPacket()) instanceof PlayerInteractEntityC2SPacket && Criticals.getInteractType(packet = (PlayerInteractEntityC2SPacket)t) == Criticals_QenzavIULhSqCVPmsILH.ATTACK && !((entity = Criticals.getEntity(packet)) instanceof EndCrystalEntity)) {
            Criticals.mc.player.method_7277(entity);
            this.doCrit();
        }
    }

    public void doCrit() {
        if (Aura.INSTANCE.isOn() && (Criticals.mc.player.method_24828() || Criticals.mc.player.method_31549().flying) && !Criticals.mc.player.method_5771() && !Criticals.mc.player.method_5869()) {
            if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.Strict && Criticals.mc.world.method_8320(Criticals.mc.player.method_24515()).method_26204() != Blocks.COBWEB) {
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 0.062600301692775, Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 0.07260029960661, Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318(), Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318(), Criticals.mc.player.method_23321(), false));
            } else if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.NCP) {
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 0.0625, Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318(), Criticals.mc.player.method_23321(), false));
            } else if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.Packet) {
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 1.058293536E-5, Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 9.16580235E-6, Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 1.0371854E-7, Criticals.mc.player.method_23321(), false));
            } else if (this.mode.getValue() == _llXqHCnomcmaIkSSIBHS.LowPacket) {
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318() + 2.71875E-7, Criticals.mc.player.method_23321(), false));
                Criticals.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Criticals.mc.player.method_23317(), Criticals.mc.player.method_23318(), Criticals.mc.player.method_23321(), false));
            }
        }
    }

    private static enum _llXqHCnomcmaIkSSIBHS {
        NCP,
        Strict,
        Packet,
        LowPacket;

    }
}
