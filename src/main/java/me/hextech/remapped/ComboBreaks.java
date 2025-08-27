package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;

public class ComboBreaks
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ComboBreaks INSTANCE;
    public final EnumSetting<_PNwHzpQsMnnDslSAEFgc> page = this.add(new EnumSetting<_PNwHzpQsMnnDslSAEFgc>("Page", _PNwHzpQsMnnDslSAEFgc.Attack));
    public final ColorSetting Acolor = this.add(new ColorSetting("AttackColor ", new Color(255, 255, 255, 150), v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)).injectBoolean(true));
    public final ColorSetting Aonline = this.add(new ColorSetting("AttackOnline", new Color(255, 255, 255, 202), v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack))).injectBoolean(true);
    public final SliderSetting attackSync = this.add(new SliderSetting("Attack-\u00a74(SyncTime)", 10, 0, 10, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting AlastSync = this.add(new SliderSetting("Attack-\u00a74(LastSync)", 10, 0, 10, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting AonlySync = this.add(new SliderSetting("Attack-\u00a74(OnlySync)", 10, 0, 10, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting AspamTime = this.add(new SliderSetting("Attack-\u00a74(SpamTime)", 0, 0, 1000, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting Acyrstal = this.add(new SliderSetting("Attack-\u00a74(Place)", 70, 0, 150, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting Abreak = this.add(new SliderSetting("Attack-\u00a74(Break)", 70, 0, 150, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting Aweb = this.add(new SliderSetting("A-AutoWeb-\u00a74(Delay)", 50, 0, 150, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting Arender = this.add(new SliderSetting("Attack-\u00a74(Slider)", 0.2, 0.01, 1.0, 0.01, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final SliderSetting Arotate = this.add(new SliderSetting("Attack-\u00a74(LagTime)", 70.0, 0.0, 100.0, 0.1, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Attack)));
    public final ColorSetting Dcolor = this.add(new ColorSetting("DefendColor ", new Color(255, 255, 255, 150), v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)).injectBoolean(true));
    public final ColorSetting Donline = this.add(new ColorSetting("DefendOnline", new Color(255, 255, 255, 202), v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend))).injectBoolean(true);
    public final SliderSetting DattackSync = this.add(new SliderSetting("Defend-\u00a7b(SyncTime)", 10, 0, 10, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting DlastSync = this.add(new SliderSetting("Defend-\u00a7b(LastSync)", 10, 0, 10, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting DonlySync = this.add(new SliderSetting("Defend-\u00a7b(OnlySync)", 10, 0, 10, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting DspamTime = this.add(new SliderSetting("Defend-\u00a7b(SpamTime)", 0, 0, 1000, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting Dcrystal = this.add(new SliderSetting("Defend-\u00a7b(Place)", 50, 0, 150, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting Dbreak = this.add(new SliderSetting("Defend-\u00a7b(Break)", 50, 0, 150, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting Dweb = this.add(new SliderSetting("D-AutoWeb-\u00a7b(Delay)", 50, 0, 150, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting Drender = this.add(new SliderSetting("D-Crystal-\u00a7b(Slider)", 0.2, 0.01, 1.0, 0.01, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final SliderSetting Drotate = this.add(new SliderSetting("D-Rotate-\u00a7b(LagTime)", 50, 0, 100, v -> this.page.is(_PNwHzpQsMnnDslSAEFgc.Defend)));
    public final BooleanSetting staticmove = this.add(new BooleanSetting("StaticMove", false));

    public ComboBreaks() {
        super("ComboBreaks", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (ComboBreaks.nullCheck()) {
            // empty if block
        }
    }

    public static final class _PNwHzpQsMnnDslSAEFgc
    extends Enum<_PNwHzpQsMnnDslSAEFgc> {
        public static final /* enum */ _PNwHzpQsMnnDslSAEFgc Attack;
        public static final /* enum */ _PNwHzpQsMnnDslSAEFgc Defend;

        public static _PNwHzpQsMnnDslSAEFgc[] values() {
            return null;
        }

        public static _PNwHzpQsMnnDslSAEFgc valueOf(String string) {
            return null;
        }
    }
}
