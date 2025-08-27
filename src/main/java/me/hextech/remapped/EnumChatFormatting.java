package me.hextech.remapped;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormatting {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
    OBFUSCATED,
    BOLD,
    STRIKETHROUGH,
    UNDERLINE,
    ITALIC,
    RESET;

    private static final Map<String, EnumChatFormatting> nameMapping;
    private static final Pattern formattingCodePattern;
    private final String name;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int colorIndex;

    private EnumChatFormatting(String formattingName, char formattingCodeIn, int colorIndex) {
        this(formattingName, formattingCodeIn, false, colorIndex);
    }

    private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn) {
        this(formattingName, formattingCodeIn, fancyStylingIn, -1);
    }

    private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex) {
        this.name = formattingName;
        this.formattingCode = formattingCodeIn;
        this.fancyStyling = fancyStylingIn;
        this.colorIndex = colorIndex;
        this.controlString = "\u00a7" + formattingCodeIn;
    }

    private static String func_175745_c(String p_175745_0_) {
        return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
    }

    public static String getTextWithoutFormattingCodes(String text) {
        return text == null ? null : formattingCodePattern.matcher(text).replaceAll("");
    }

    public static EnumChatFormatting getValueByName(String friendlyName) {
        return friendlyName == null ? null : nameMapping.get(EnumChatFormatting.func_175745_c(friendlyName));
    }

    public static EnumChatFormatting func_175744_a(int p_175744_0_) {
        if (p_175744_0_ < 0) {
            return RESET;
        }
        for (EnumChatFormatting enumchatformatting : EnumChatFormatting.values()) {
            if (enumchatformatting.getColorIndex() != p_175744_0_) continue;
            return enumchatformatting;
        }
        return null;
    }

    public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_) {
        ArrayList<String> list = Lists.newArrayList();
        for (EnumChatFormatting enumchatformatting : EnumChatFormatting.values()) {
            if (enumchatformatting.isColor() && !p_96296_0_ || enumchatformatting.isFancyStyling() && !p_96296_1_) continue;
            list.add(enumchatformatting.getFriendlyName());
        }
        return list;
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    public boolean isFancyStyling() {
        return this.fancyStyling;
    }

    public boolean isColor() {
        return !this.fancyStyling && this != RESET;
    }

    public String getFriendlyName() {
        return this.name().toLowerCase();
    }

    public String toString() {
        return this.controlString;
    }
}
