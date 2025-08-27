package me.hextech.remapped;

import java.awt.Color;
import java.util.HashMap;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ChatSetting;
import me.hextech.remapped.ChatSetting_FjbVfiTpsZbQdeUlhhhi;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.FadeUtils;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.StringSetting;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;

public class ChatSetting_qVnAbgCzNciNTevKRovy
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final HashMap<OrderedText, StringVisitable> chatMessage = new HashMap();
    public static ChatSetting_qVnAbgCzNciNTevKRovy INSTANCE;
    public final StringSetting hackName = this.add(new StringSetting("Name", "HexTech-nightly Cracked By NoWhisper"));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(230, 230, 230)));
    public final ColorSetting pulse = this.add(new ColorSetting("Pulse", new Color(167, 167, 167)).injectBoolean(true));
    public final SliderSetting pulseSpeed = this.add(new SliderSetting("Speed", 1.0, 0.0, 5.0, 0.1, v -> this.pulse.booleanValue));
    public final SliderSetting pulseCounter = this.add(new SliderSetting("Counter", 10, 1, 50, v -> this.pulse.booleanValue));
    public final SliderSetting animateTime = this.add(new SliderSetting("AnimTime", 300, 0, 1000));
    public final SliderSetting animateOffset = this.add(new SliderSetting("AnimOffset", -40, -200, 100));
    public final EnumSetting<FadeUtils> animQuad = this.add(new EnumSetting<FadeUtils>("Quad", FadeUtils.In));
    public final BooleanSetting keepHistory = this.add(new BooleanSetting("KeepHistory", true));
    public final BooleanSetting infiniteChat = this.add(new BooleanSetting("InfiniteChat", true));
    public final EnumSetting<ChatSetting> messageStyle = this.add(new EnumSetting<ChatSetting>("MessageStyle", ChatSetting.HexTech));
    public final EnumSetting<ChatSetting_FjbVfiTpsZbQdeUlhhhi> messageCode = this.add(new EnumSetting<ChatSetting_FjbVfiTpsZbQdeUlhhhi>("MessageCode", ChatSetting_FjbVfiTpsZbQdeUlhhhi.HexTech));
    public final BooleanSetting onlyOne = this.add(new BooleanSetting("Remove", false));
    public final StringSetting start = this.add(new StringSetting("StartCode", "[", v -> this.messageCode.getValue() == ChatSetting_FjbVfiTpsZbQdeUlhhhi.Custom));
    public final StringSetting end = this.add(new StringSetting("EndCode", "]", v -> this.messageCode.getValue() == ChatSetting_FjbVfiTpsZbQdeUlhhhi.Custom));

    public ChatSetting_qVnAbgCzNciNTevKRovy() {
        super("ChatSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    @Override
    public void enable() {
        this.state = true;
    }

    @Override
    public void disable() {
        this.state = true;
    }

    @Override
    public boolean isOn() {
        return true;
    }
}
