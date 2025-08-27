package me.hextech.remapped;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import me.hextech.asm.accessors.IEntity;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Notify;
import me.hextech.remapped.Notify_EXlgYplaRzfgofOPOkyB;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.Render3DUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.util.math.Vec3d;

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
        super("LogoutSpots", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        Object object = event.getPacket();
        if (object instanceof PlayerListS2CPacket) {
            PlayerListS2CPacket packet = (PlayerListS2CPacket)object;
            if (packet.method_46327().contains(PlayerListS2CPacket.Action.field_29136)) {
                for (PlayerListS2CPacket.Entry addedPlayer : packet.method_46330()) {
                    for (UUID uuid : this.logoutCache.keySet()) {
                        if (!uuid.equals(addedPlayer.comp_1107().getId())) continue;
                        PlayerEntity player = this.logoutCache.get(uuid);
                        if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Both) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7alogged back at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                            }
                            if (this.message.getValue()) {
                                CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7alogged back at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Notify) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7alogged back at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Chat && this.message.getValue()) {
                            CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7alogged back at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                        }
                        this.logoutCache.remove(uuid);
                    }
                }
            }
            this.playerCache.clear();
        } else {
            object = event.getPacket();
            if (object instanceof PlayerRemoveS2CPacket) {
                PlayerRemoveS2CPacket packet = (PlayerRemoveS2CPacket)object;
                for (UUID uuid2 : packet.comp_1105()) {
                    for (UUID uuid : this.playerCache.keySet()) {
                        if (!uuid.equals(uuid2)) continue;
                        PlayerEntity player = this.playerCache.get(uuid);
                        if (this.logoutCache.containsKey(uuid)) continue;
                        if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Both) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7alogged back at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                            }
                            if (this.message.getValue()) {
                                CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7clogged out at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Notify) {
                            if (this.notify.getValue()) {
                                LogoutSpots.sendNotify("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7alogged back at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
                            }
                        } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Chat && this.message.getValue()) {
                            CommandManager.sendChatMessage("\u00a7e[!] \u00a7b" + player.method_5477().getString() + " \u00a7clogged out at X: " + (int)player.method_23317() + " Y: " + (int)player.method_23318() + " Z: " + (int)player.method_23321());
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
        for (PlayerEntity player : LogoutSpots.mc.field_1687.method_18456()) {
            if (player == null || player.equals((Object)LogoutSpots.mc.field_1724)) continue;
            this.playerCache.put(player.method_7334().getId(), player);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        for (UUID uuid : this.logoutCache.keySet()) {
            PlayerEntity data = this.logoutCache.get(uuid);
            if (data == null) continue;
            Render3DUtil.draw3DBox(matrixStack, ((IEntity)data).getDimensions().method_30757(data.method_19538()), this.color.getValue(), this.outline.getValue(), this.box.getValue());
            if (!this.text.getValue()) continue;
            Render3DUtil.drawText3D(data.method_5477().getString(), new Vec3d(data.method_23317(), ((IEntity)data).getDimensions().method_30757((Vec3d)data.method_19538()).field_1325 + 0.5, data.method_23321()), ColorUtil.injectAlpha(this.color.getValue(), 255));
        }
    }
}
