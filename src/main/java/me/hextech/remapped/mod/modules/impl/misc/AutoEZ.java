package me.hextech.remapped.mod.modules.impl.misc;

import java.util.List;
import java.util.Random;
import me.hextech.HexTech;
import me.hextech.remapped.DeathEvent;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.StringSetting;
import net.minecraft.entity.player.PlayerEntity;

public class AutoEZ
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final EnumSetting<_TjwHGYKUAEgXsMpgUbfm> type = this.add(new EnumSetting<_TjwHGYKUAEgXsMpgUbfm>("Type", _TjwHGYKUAEgXsMpgUbfm.HEXTECH));
    private final SliderSetting range = this.add(new SliderSetting("Range", 10.0, 0.0, 20.0, 1.0));
    private final StringSetting msg = this.add(new StringSetting("Custom", "EZ %player%-BY Hextech-nightly", v -> this.type.getValue() == _TjwHGYKUAEgXsMpgUbfm.Custom));
    public List<String> ALEXJONNY = List.of("SO EASY!", "BEST CLIENT BUY+2353761389", "Power By Hex_Tech -Nightly", "YOU ARE AI?");
    public List<String> HEXTECH = List.of("Power By Hex_Tech -Nightly", "YOU ARE AI?", "SO EASY!", "BEST CLIENT BUY+2353761389");
    public List<String> GUAZIGEGE = List.of("Killed by HexTech-Nightly", "Only HexTech-Nightly can do", "Hex-Tech You need it");
    Random random = new Random();

    public AutoEZ() {
        super("AutoEZ", Module_JlagirAibYQgkHtbRnhw.Misc);
    }

    @EventHandler
    public void onDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (AutoEZ.mc.player != null && player != AutoEZ.mc.player && !HexTech.FRIEND.isFriend(player)) {
            if (this.range.getValue() > 0.0 && (double)AutoEZ.mc.player.distanceTo(player) > this.range.getValue()) {
                return;
            }
            switch (this.type.getValue().ordinal()) {
                case 1: {
                    AutoEZ.mc.player.networkHandler.sendChatMessage(this.ALEXJONNY.get(this.random.nextInt(this.ALEXJONNY.size() - 1)) + " " + player.getName());
                    break;
                }
                case 0: {
                    AutoEZ.mc.player.networkHandler.sendChatMessage(this.HEXTECH.get(this.random.nextInt(this.HEXTECH.size() - 1)) + " " + player.getName().getString());
                    break;
                }
                case 2: {
                    AutoEZ.mc.player.networkHandler.sendChatMessage(player.getName().getString() + " " + this.GUAZIGEGE.get(this.random.nextInt(this.GUAZIGEGE.size() - 1)));
                    break;
                }
                case 3: {
                    AutoEZ.mc.player.networkHandler.sendChatMessage(this.msg.getValue().replaceAll("%player%", player.getName().getString()));
                }
            }
        }
    }

    private String generateRandomString(int LENGTH) {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; ++i) {
            int index = this.random.nextInt("qwertyugjndshgshighewsnflksanffafskanx".length());
            sb.append("qwertyugjndshgshighewsnflksanffafskanx".charAt(index));
        }
        return sb.toString();
    }

    public enum _TjwHGYKUAEgXsMpgUbfm {
        HEXTECH,
        ALEXJONNY,
        GUAZIGEGE,
        Custom

    }
}
