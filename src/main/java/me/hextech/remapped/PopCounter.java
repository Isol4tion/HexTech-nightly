package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.events.impl.DeathEvent;
import me.hextech.remapped.api.managers.CommandManager;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.settings.impl.EnumSetting;
import net.minecraft.entity.player.PlayerEntity;

public class PopCounter
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PopCounter INSTANCE;
    public final BooleanSetting unPop = this.add(new BooleanSetting("Dead", true));
    private final EnumSetting<_jJlsegqwuwGfJNTYElis> notitype = this.add(new EnumSetting<_jJlsegqwuwGfJNTYElis>("Type", _jJlsegqwuwGfJNTYElis.Notify));

    public PopCounter() {
        super("PopCounter", "Counts players totem pops", Module_JlagirAibYQgkHtbRnhw.Misc);
        INSTANCE = this;
    }

    @EventHandler
    public void onPlayerDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (HexTech.POP.popContainer.containsKey(player.getName().getString())) {
            int l_Count = HexTech.POP.popContainer.get(player.getName().getString());
            if (l_Count == 1) {
                if (player.equals(PopCounter.mc.player)) {
                    this.sendMessage("\u00a7f\u4f60\u00a7r\u5df2\u7ecf\u5931\u53bb\u4e86\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
                } else {
                    this.sendMessage("\u00a7f" + player.getName().getString() + "\u00a7r\u51fb\u7834\u654c\u4eba\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
                }
            } else if (player.equals(PopCounter.mc.player)) {
                this.sendMessage("\u00a7f\u4f60\u00a7r\u5df2\u7ecf\u5931\u53bb\u4e86 \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
            } else {
                this.sendMessage("\u00a7f" + player.getName().getString() + "\u00a7r\u51fb\u7834\u654c\u4eba\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
            }
        } else if (this.unPop.getValue()) {
            if (player.equals(PopCounter.mc.player)) {
                this.sendMessage("\u00a7f\u4f60\u00a7r[\u786e\u8ba4\u6b7b\u4ea1]", player.getId());
            } else {
                this.sendMessage("\u00a7f" + player.getName().getString() + "\u00a7r[\u786e\u8ba4\u6b7b\u4ea1]", player.getId());
            }
        }
    }

    @EventHandler
    public void onTotem(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        int l_Count = 1;
        if (HexTech.POP.popContainer.containsKey(player.getName().getString())) {
            l_Count = HexTech.POP.popContainer.get(player.getName().getString());
        }
        if (l_Count == 1) {
            if (player.equals(PopCounter.mc.player)) {
                this.sendMessage("\u00a7f\u4f60\u00a7r\u6b63\u5728\u4e22\u5931 \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
            } else {
                this.sendMessage("\u00a7f" + player.getName().getString() + " \u00a7r\u51fb\u7834\u654c\u4eba \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
            }
        } else if (player.equals(PopCounter.mc.player)) {
            this.sendMessage("\u00a7f\u4f60\u00a7r\u5931\u53bb\u4e86\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
        } else {
            this.sendMessage("\u00a7f" + player.getName().getString() + " \u00a7r\u51fb\u7834\u654c\u4eba \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.getId());
        }
    }

    public void sendMessage(String message, int id) {
        if (!PopCounter.nullCheck()) {
            if (this.notitype.getValue() == _jJlsegqwuwGfJNTYElis.Notify) {
                PopCounter.sendNotify("\u00a76[!] " + message);
            } else if (this.notitype.getValue() == _jJlsegqwuwGfJNTYElis.Both) {
                CommandManager.sendChatMessageWidthId("\u00a76[!] " + message, id);
                PopCounter.sendNotify("\u00a76[!] " + message);
            } else if (this.notitype.getValue() == _jJlsegqwuwGfJNTYElis.Chat) {
                CommandManager.sendChatMessageWidthId("\u00a76[!] " + message, id);
            }
        }
    }

    public enum _jJlsegqwuwGfJNTYElis {
        Notify,
        Chat,
        Both

    }
}
