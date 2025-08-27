package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class MoveUp
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static MoveUp INSTANCE;
    public final SliderSetting rubberbandPackets = this.add(new SliderSetting("Packet", 1.0, 0.0, 9.0, 1.0));
    public final SliderSetting setPosition = this.add(new SliderSetting("SetPosition", 1.0, -5.0, 5.0, 0.1));
    public final SliderSetting rubberbandOffset = this.add(new SliderSetting("Offset", 1.0, 0.0, 9.0, 1.0));
    final EnumSetting<_FPLgygTQIdxRiYXqovqv> mode = this.add(new EnumSetting<_FPLgygTQIdxRiYXqovqv>("Mode", _FPLgygTQIdxRiYXqovqv.Jump));
    private final BooleanSetting noWeb = this.add(new BooleanSetting("PauseWeb", true));
    private final BooleanSetting onlyburrow = this.add(new BooleanSetting("OnlyBurrow", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("GroundCheck", true));
    private final BooleanSetting pEndChest = this.add(new BooleanSetting("EndChest", true));
    private final BooleanSetting doburrow = this.add(new BooleanSetting("StartBurrow", true));
    private final BooleanSetting toggle = this.add(new BooleanSetting("Toggle", false));

    public MoveUp() {
        super("MoveUp", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (MoveUp.mc.player != null && this.noWeb.getValue() && HoleKickTest.isInWeb((PlayerEntity)MoveUp.mc.player)) {
            return;
        }
        if (MoveUp.mc.player != null && this.onlyGround.getValue() && !MoveUp.mc.player.method_24828()) {
            return;
        }
        if (MoveUp.mc.player != null && (!this.onlyburrow.getValue() || Util.isBurrowed((PlayerEntity)MoveUp.mc.player, !this.pEndChest.getValue()))) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.method_23317(), MoveUp.mc.player.method_23318() + 0.4199999868869781, MoveUp.mc.player.method_23321(), false));
                    mc.getNetworkHandler().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.method_23317(), MoveUp.mc.player.method_23318() + 0.7531999805212017, MoveUp.mc.player.method_23321(), false));
                    MoveUp.mc.player.method_5814(MoveUp.mc.player.method_23317(), MoveUp.mc.player.method_23318() + (double)this.setPosition.getValueFloat(), MoveUp.mc.player.method_23321());
                    mc.getNetworkHandler().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.method_23317(), MoveUp.mc.player.method_23318(), MoveUp.mc.player.method_23321(), true));
                    break;
                }
                case 0: {
                    if (MoveUp.mc.player == null || this.onlyburrow.getValue() && !Util.isBurrowed((PlayerEntity)MoveUp.mc.player, !this.pEndChest.getValue())) break;
                    double y = 0.0;
                    double velocity = 0.42;
                    while (y < 1.1) {
                        velocity = (velocity - 0.08) * 0.98;
                        this.sendPacket((Packet<?>)new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.method_23317(), MoveUp.mc.player.method_23318() + (y += velocity), MoveUp.mc.player.method_23321(), false));
                    }
                    for (int i = 0; i < this.rubberbandPackets.getValueInt(); ++i) {
                        this.sendPacket((Packet<?>)new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.method_23317(), MoveUp.mc.player.method_23318() + y + (double)this.rubberbandOffset.getValueInt(), MoveUp.mc.player.method_23321(), false));
                    }
                    break;
                }
            }
        }
        if (this.doburrow.getValue() && !MovementUtil.isMoving() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
        }
        if (this.toggle.getValue()) {
            this.disable();
        }
    }

    public static enum _FPLgygTQIdxRiYXqovqv {
        Packet,
        Jump;

    }
}
