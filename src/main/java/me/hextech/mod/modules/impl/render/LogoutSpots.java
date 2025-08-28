package me.hextech.mod.modules.impl.render;

import com.google.common.collect.Maps;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.managers.CommandManager;
import me.hextech.api.utils.render.ColorUtil;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.asm.accessors.IEntity;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.client.Notify_EXlgYplaRzfgofOPOkyB;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class LogoutSpots
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true));
    private final BooleanSetting text = this.add(new BooleanSetting("Text", true));
    private final BooleanSetting message = this.add(new BooleanSetting("Message", true));
    private final BooleanSetting notify = this.add(new BooleanSetting("Notify", true));
    private final Map<UUID, PlayerEntity> playerCache = Maps.newConcurrentMap();
    private final Map<UUID, PlayerEntity> logoutCache = Maps.newConcurrentMap();

    public LogoutSpots() {
        super("LogoutSpots", Category.Render);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        Object object = event.getPacket();
        if (object instanceof PlayerListS2CPacket packet) {
            if (packet.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER)) {
                for (PlayerListS2CPacket.Entry addedPlayer : packet.getPlayerAdditionEntries()) {
                    for (UUID uuid : this.logoutCache.keySet()) {
                        if (!uuid.equals(addedPlayer.profile().getId())) continue;
                        PlayerEntity player = this.logoutCache.get(uuid);
                        if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_EXlgYplaRzfgofOPOkyB.NotifyInfo.Mode.Both) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7alogged back at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                            }
                            if (this.message.getValue()) {
                                CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7alogged back at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_EXlgYplaRzfgofOPOkyB.NotifyInfo.Mode.Notify) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7alogged back at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_EXlgYplaRzfgofOPOkyB.NotifyInfo.Mode.Chat && this.message.getValue()) {
                            CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7alogged back at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                        }
                        this.logoutCache.remove(uuid);
                    }
                }
            }
            this.playerCache.clear();
        } else {
            object = event.getPacket();
            if (object instanceof PlayerRemoveS2CPacket packet) {
                for (UUID uuid2 : packet.profileIds()) {
                    for (UUID uuid : this.playerCache.keySet()) {
                        if (!uuid.equals(uuid2)) continue;
                        PlayerEntity player = this.playerCache.get(uuid);
                        if (this.logoutCache.containsKey(uuid)) continue;
                        if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_EXlgYplaRzfgofOPOkyB.NotifyInfo.Mode.Both) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7alogged back at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                            }
                            if (this.message.getValue()) {
                                CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7clogged out at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_EXlgYplaRzfgofOPOkyB.NotifyInfo.Mode.Notify) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7alogged back at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_EXlgYplaRzfgofOPOkyB.NotifyInfo.Mode.Chat && this.message.getValue()) {
                            CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.getName().getString() + " \u00a7clogged out at X: " + (int)player.getX() + " Y: " + (int)player.getY() + " Z: " + (int)player.getZ());
                        }
                        this.logoutCache.put(uuid, player);
                    }
                }
                this.playerCache.clear();
            }
        }
    }

    @Override
    public void onEnable() {
        this.playerCache.clear();
        this.logoutCache.clear();
    }

    @Override
    public void onUpdate() {
        for (PlayerEntity player : LogoutSpots.mc.world.getPlayers()) {
            if (player == null || player.equals(LogoutSpots.mc.player)) continue;
            this.playerCache.put(player.getGameProfile().getId(), player);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        for (UUID uuid : this.logoutCache.keySet()) {
            PlayerEntity data = this.logoutCache.get(uuid);
            if (data == null) continue;
            Render3DUtil.draw3DBox(matrixStack, ((IEntity)data).getDimensions().getBoxAt(data.getPos()), this.color.getValue(), this.outline.getValue(), this.box.getValue());
            if (!this.text.getValue()) continue;
            Render3DUtil.drawText3D(data.getName().getString(), new Vec3d(data.getX(), ((IEntity)data).getDimensions().getBoxAt(data.getPos()).maxY + 0.5, data.getZ()), ColorUtil.injectAlpha(this.color.getValue(), 255));
        }
    }
}
