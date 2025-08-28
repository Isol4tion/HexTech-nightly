package me.hextech.mod.modules.impl.setting;

import me.hextech.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.*;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;

import java.awt.*;
import java.util.HashMap;

public class ChatSetting_qVnAbgCzNciNTevKRovy
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final HashMap<OrderedText, StringVisitable> chatMessage = new HashMap();
    public static ChatSetting_qVnAbgCzNciNTevKRovy INSTANCE;
    public final StringSetting hackName = this.add(new StringSetting("Name", "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c"));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(230, 230, 230)));
    public final ColorSetting pulse = this.add(new ColorSetting("Pulse", new Color(167, 167, 167)).injectBoolean(true));
    public final SliderSetting pulseSpeed = this.add(new SliderSetting("Speed", 1.0, 0.0, 5.0, 0.1, v -> this.pulse.booleanValue));
    public final SliderSetting pulseCounter = this.add(new SliderSetting("Counter", 10, 1, 50, v -> this.pulse.booleanValue));
    public final SliderSetting animateTime = this.add(new SliderSetting("AnimTime", 300, 0, 1000));
    public final SliderSetting animateOffset = this.add(new SliderSetting("AnimOffset", -40, -200, 100));
    public final EnumSetting<FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad> animQuad = this.add(new EnumSetting<FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad>("Quad", FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad.In));
    public final BooleanSetting keepHistory = this.add(new BooleanSetting("KeepHistory", true));
    public final BooleanSetting infiniteChat = this.add(new BooleanSetting("InfiniteChat", true));
    public final EnumSetting<Style> messageStyle = this.add(new EnumSetting<Style>("MessageStyle", Style.HexTech));
    public final EnumSetting<Code> messageCode = this.add(new EnumSetting<Code>("MessageCode", Code.HexTech));
    public final BooleanSetting onlyOne = this.add(new BooleanSetting("Remove", false));
    public final StringSetting start = this.add(new StringSetting("StartCode", "[", v -> this.messageCode.getValue() == Code.Custom));
    public final StringSetting end = this.add(new StringSetting("EndCode", "]", v -> this.messageCode.getValue() == Code.Custom));

    public ChatSetting_qVnAbgCzNciNTevKRovy() {
        super("ChatSetting", Category.Setting);
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

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Style {
        HexTech,
        Earth,
        None

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Code {
        HexTech,
        Earth,
        Custom,
        None

    }
}
