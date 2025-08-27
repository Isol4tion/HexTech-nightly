package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.DeathEvent;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.TotemEvent;
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
        if (HexTech.POP.popContainer.containsKey(player.method_5477().getString())) {
            int l_Count = HexTech.POP.popContainer.get(player.method_5477().getString());
            if (l_Count == 1) {
                if (player.equals((Object)PopCounter.mc.player)) {
                    this.sendMessage("\u00a7f\u4f60\u00a7r\u5df2\u7ecf\u5931\u53bb\u4e86\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
                } else {
                    this.sendMessage("\u00a7f" + player.method_5477().getString() + "\u00a7r\u51fb\u7834\u654c\u4eba\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
                }
            } else if (player.equals((Object)PopCounter.mc.player)) {
                this.sendMessage("\u00a7f\u4f60\u00a7r\u5df2\u7ecf\u5931\u53bb\u4e86 \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
            } else {
                this.sendMessage("\u00a7f" + player.method_5477().getString() + "\u00a7r\u51fb\u7834\u654c\u4eba\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
            }
        } else if (this.unPop.getValue()) {
            if (player.equals((Object)PopCounter.mc.player)) {
                this.sendMessage("\u00a7f\u4f60\u00a7r[\u786e\u8ba4\u6b7b\u4ea1]", player.method_5628());
            } else {
                this.sendMessage("\u00a7f" + player.method_5477().getString() + "\u00a7r[\u786e\u8ba4\u6b7b\u4ea1]", player.method_5628());
            }
        }
    }

    @EventHandler
    public void onTotem(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        int l_Count = 1;
        if (HexTech.POP.popContainer.containsKey(player.method_5477().getString())) {
            l_Count = HexTech.POP.popContainer.get(player.method_5477().getString());
        }
        if (l_Count == 1) {
            if (player.equals((Object)PopCounter.mc.player)) {
                this.sendMessage("\u00a7f\u4f60\u00a7r\u6b63\u5728\u4e22\u5931 \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
            } else {
                this.sendMessage("\u00a7f" + player.method_5477().getString() + " \u00a7r\u51fb\u7834\u654c\u4eba \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
            }
        } else if (player.equals((Object)PopCounter.mc.player)) {
            this.sendMessage("\u00a7f\u4f60\u00a7r\u5931\u53bb\u4e86\u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
        } else {
            this.sendMessage("\u00a7f" + player.method_5477().getString() + " \u00a7r\u51fb\u7834\u654c\u4eba \u00a7f" + l_Count + "\u00a7r \u56fe\u817e", player.method_5628());
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

    public static final class _jJlsegqwuwGfJNTYElis
    extends Enum<_jJlsegqwuwGfJNTYElis> {
        public static final /* enum */ _jJlsegqwuwGfJNTYElis Notify;
        public static final /* enum */ _jJlsegqwuwGfJNTYElis Chat;
        public static final /* enum */ _jJlsegqwuwGfJNTYElis Both;

        public static _jJlsegqwuwGfJNTYElis[] values() {
            return null;
        }

        public static _jJlsegqwuwGfJNTYElis valueOf(String string) {
            return null;
        }
    }
}
