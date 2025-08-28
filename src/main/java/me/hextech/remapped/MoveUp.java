package me.hextech.remapped;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
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
        if (MoveUp.mc.player != null && this.noWeb.getValue() && HoleKickTest.isInWeb(MoveUp.mc.player)) {
            return;
        }
        if (MoveUp.mc.player != null && this.onlyGround.getValue() && !MoveUp.mc.player.isOnGround()) {
            return;
        }
        if (MoveUp.mc.player != null && (!this.onlyburrow.getValue() || Util.isBurrowed(MoveUp.mc.player, !this.pEndChest.getValue()))) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.getX(), MoveUp.mc.player.getY() + 0.4199999868869781, MoveUp.mc.player.getZ(), false));
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.getX(), MoveUp.mc.player.getY() + 0.7531999805212017, MoveUp.mc.player.getZ(), false));
                    MoveUp.mc.player.setPosition(MoveUp.mc.player.getX(), MoveUp.mc.player.getY() + (double)this.setPosition.getValueFloat(), MoveUp.mc.player.getZ());
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.getX(), MoveUp.mc.player.getY(), MoveUp.mc.player.getZ(), true));
                    break;
                }
                case 0: {
                    if (MoveUp.mc.player == null || this.onlyburrow.getValue() && !Util.isBurrowed(MoveUp.mc.player, !this.pEndChest.getValue())) break;
                    double y = 0.0;
                    double velocity = 0.42;
                    while (y < 1.1) {
                        velocity = (velocity - 0.08) * 0.98;
                        this.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.getX(), MoveUp.mc.player.getY() + (y += velocity), MoveUp.mc.player.getZ(), false));
                    }
                    for (int i = 0; i < this.rubberbandPackets.getValueInt(); ++i) {
                        this.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(MoveUp.mc.player.getX(), MoveUp.mc.player.getY() + y + (double)this.rubberbandOffset.getValueInt(), MoveUp.mc.player.getZ(), false));
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

    public enum _FPLgygTQIdxRiYXqovqv {
        Packet,
        Jump

    }
}
