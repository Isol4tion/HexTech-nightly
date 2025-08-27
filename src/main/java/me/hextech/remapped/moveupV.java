package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class moveupV
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    final EnumSetting<_rlgNzzROxlnlLAUMICmS> mode = this.add(new EnumSetting<_rlgNzzROxlnlLAUMICmS>("Mode", _rlgNzzROxlnlLAUMICmS.Teleport));
    private final BooleanSetting noWeb = this.add(new BooleanSetting("PauseWeb", true));
    private final BooleanSetting onlyburrow = this.add(new BooleanSetting("OnlyBurrow", true));
    private final BooleanSetting pEndChest = this.add(new BooleanSetting("EndChest", true));
    private final BooleanSetting doburrow = this.add(new BooleanSetting("StarBurrow", true));
    private final BooleanSetting movecheck = this.add(new BooleanSetting("MoveCheck", true));
    private final BooleanSetting toggle = this.add(new BooleanSetting("Toggle", false));

    public moveupV() {
        super("MoveClip", Module_JlagirAibYQgkHtbRnhw.Movement);
    }

    @Override
    public void onUpdate() {
        if (this.movecheck.getValue() && moveupV.mc.player != null && MovementUtil.isMoving()) {
            return;
        }
        if (moveupV.mc.player != null && this.noWeb.getValue() && HoleKickTest.isInWeb((PlayerEntity)moveupV.mc.player)) {
            return;
        }
        if (moveupV.mc.player != null && (!this.onlyburrow.getValue() || Util.isBurrowed((PlayerEntity)moveupV.mc.player, !this.pEndChest.getValue()))) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    moveupV.mc.player.setPosition(moveupV.mc.player.getX(), moveupV.mc.player.getY() + 3.0, moveupV.mc.player.getZ());
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(moveupV.mc.player.getX(), moveupV.mc.player.getY(), moveupV.mc.player.getZ(), true));
                    break;
                }
                case 0: {
                    double posX = moveupV.mc.player.getX();
                    double posY = Math.round(moveupV.mc.player.getY());
                    double posZ = moveupV.mc.player.getZ();
                    boolean onGround = moveupV.mc.player.isOnGround();
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY, posZ, onGround));
                    double halfY = 0.005;
                    moveupV.mc.player.setPosition(posX, posY -= halfY, posZ);
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY, posZ, onGround));
                    moveupV.mc.player.setPosition(posX, posY -= halfY * 300.0, posZ);
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY, posZ, onGround));
                }
            }
        }
        if (this.toggle.getValue()) {
            this.disable();
        }
        if (this.doburrow.getValue() && !MovementUtil.isMoving() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
        }
    }

    public static enum _rlgNzzROxlnlLAUMICmS {
        Glitch,
        Teleport;

    }
}
